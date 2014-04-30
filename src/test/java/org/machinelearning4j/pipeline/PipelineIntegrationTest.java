/*
 * Copyright 2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.machinelearning4j.pipeline;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.machinelearning4j.core.DefaultFeatureScalingStrategy;
import org.machinelearning4j.core.FeatureScaler;
import org.machinelearning4j.core.FeatureScalingStrategy;
import org.machinelearning4j.core.FeatureStatisticsSource;
import org.machinelearning4j.core.InMemoryFeatureStatisticsSource;
import org.machinelearning4j.core.NumericFeatureMapper;
import org.machinelearning4j.pipeline.CachingNumericLabeledDataPipe.CachingStrategy;
import org.machinelearning4j.supervisedlearning.AdmissionStatus;
import org.machinelearning4j.supervisedlearning.AdmissionStatusLabelDefinition;
import org.machinelearning4j.supervisedlearning.AdmissionStatusLabelMapper;
import org.machinelearning4j.supervisedlearning.Application;
import org.machinelearning4j.supervisedlearning.ExamScores;
import org.machinelearning4j.supervisedlearning.ExamScoresFeatureDefinition;
import org.machinelearning4j.supervisedlearning.NumericLabeledData;
import org.machinelearning4j.supervisedlearning.PreviousApplicationCsvDataExtractor;
import org.machinelearning4j.util.CsvFileClassloaderDataSource;
import org.machinelearning4j.util.TrainingSetDataSource;

public class PipelineIntegrationTest {

	private Iterable<Application> previousApplications;
	private int trainingSetSize;
	
	@Test
	public void testPipeline_WhenUsingInterceptFeatures()
	{
		boolean hasInterceptFeature = true;

		// Create a feature mapper for our data
		NumericFeatureMapper<Application> featureMapper = new NumericFeatureMapper<Application>(hasInterceptFeature);
		featureMapper.addFeatureDefinition(new ExamScoresFeatureDefinition());
		
		// Create a data mapper for this pipeline, useing the feature mapper, and specifying label definition and mapper
		DataMapper<Application, NumericLabeledData> dataMapper
		 = new NumericFeatureAndLabelExtractingDataMapper<Application,AdmissionStatus>(featureMapper , new AdmissionStatusLabelDefinition(), new AdmissionStatusLabelMapper());
		
		// Create a mapping data pipe line
		MappingDataPipe<Application,NumericLabeledData> labelingPipe
		= new MappingDataPipe<Application,NumericLabeledData>(previousApplications.iterator(),dataMapper);
		
		// Create a caching data pipe line
		int numberOfDataElementsToReadInMemory = trainingSetSize;
		
		double[][] inMemoryNumericFeatures= new double[numberOfDataElementsToReadInMemory][];
		List<Double> inMemoryLabels = new ArrayList<Double>();
		
		CachingNumericLabeledDataPipe<NumericLabeledData> cachingPipe
		= new CachingNumericLabeledDataPipe<NumericLabeledData>(labelingPipe,inMemoryNumericFeatures,inMemoryLabels,CachingStrategy.FIRST_ELEMENTS);
		
		FeatureScalingStrategy featureScalingStrategy = new DefaultFeatureScalingStrategy();
		
		FeatureStatisticsSource statisticsSource = new InMemoryFeatureStatisticsSource(inMemoryNumericFeatures,hasInterceptFeature);
		
		FeatureScaler featureScaler = featureScalingStrategy.getFeatureScaler(statisticsSource);
		
		DataMapper<NumericLabeledData,NumericLabeledData> featureScalingDataMapper
		 = new FeatureScalingLabeledDataMapper(featureScaler, hasInterceptFeature);
		
		MappingDataPipe<NumericLabeledData,NumericLabeledData> featureScalingPipe
		= new MappingDataPipe<NumericLabeledData,NumericLabeledData>(cachingPipe,featureScalingDataMapper);

		List<double[]> featureScaledData = new ArrayList<double[]>();
		List<Double> numericLabels = new ArrayList<Double>();
		while (featureScalingPipe.hasNext())
		{
			NumericLabeledData labeledData = featureScalingPipe.next();
			featureScaledData.add(labeledData.getElement());
			numericLabels.add(labeledData.getLabel());
		}
		
		// Assert that we have the same number of output element arrays and labels as the training set
		Assert.assertEquals(trainingSetSize,featureScaledData.size());
		Assert.assertEquals(trainingSetSize,numericLabels.size());

		// Get the first element feature array and label
		double[] firstFeatureScaledDataElement = featureScaledData.get(0);
		Double firstLabel = numericLabels.get(0);
		
		// Asser that the element feature array has 3 features
		Assert.assertEquals(3,firstFeatureScaledDataElement.length);
		
		// Assert that the first feature has an intercept term and it is 1
		Assert.assertEquals(1.0d, firstFeatureScaledDataElement[0]);

		// Assert that the other 2 features are scaled versions of the input features
		Assert.assertEquals(-1.6022476316822152d, firstFeatureScaledDataElement[1]);
		Assert.assertEquals(0.6383411191827778d, firstFeatureScaledDataElement[2]);

		// Asser the first label is not null and is a 0 ( for NOT_ACCCEPTED s)
		Assert.assertNotNull(firstLabel);
		Assert.assertEquals(0.0, firstLabel.doubleValue());
		
	}
	
	@Test
	public void testPipeline_WhenNotUsingInterceptFeatures()
	{
		boolean hasInterceptFeature = false;

		// Create a feature mapper for our data
		NumericFeatureMapper<Application> featureMapper = new NumericFeatureMapper<Application>(hasInterceptFeature);
		featureMapper.addFeatureDefinition(new ExamScoresFeatureDefinition());
		
		// Create a data mapper for this pipeline, useing the feature mapper, and specifying label definition and mapper
		DataMapper<Application, NumericLabeledData> dataMapper
		 = new NumericFeatureAndLabelExtractingDataMapper<Application,AdmissionStatus>(featureMapper , new AdmissionStatusLabelDefinition(), new AdmissionStatusLabelMapper());
		
		// Create a mapping data pipe line
		MappingDataPipe<Application,NumericLabeledData> labelingPipe
		= new MappingDataPipe<Application,NumericLabeledData>(previousApplications.iterator(),dataMapper);
		
		// Create a caching data pipe line
		int numberOfDataElementsToReadInMemory = trainingSetSize;
		
		double[][] inMemoryNumericFeatures= new double[numberOfDataElementsToReadInMemory][];
		List<Double> inMemoryLabels = new ArrayList<Double>();
		
		CachingNumericLabeledDataPipe<NumericLabeledData> cachingPipe
		= new CachingNumericLabeledDataPipe<NumericLabeledData>(labelingPipe,inMemoryNumericFeatures,inMemoryLabels,CachingStrategy.FIRST_ELEMENTS);
		
		FeatureScalingStrategy featureScalingStrategy = new DefaultFeatureScalingStrategy();
		
		FeatureStatisticsSource statisticsSource = new InMemoryFeatureStatisticsSource(inMemoryNumericFeatures,hasInterceptFeature);
		
		FeatureScaler featureScaler = featureScalingStrategy.getFeatureScaler(statisticsSource);
		
		DataMapper<NumericLabeledData,NumericLabeledData> featureScalingDataMapper
		 = new FeatureScalingLabeledDataMapper(featureScaler, hasInterceptFeature);
		
		MappingDataPipe<NumericLabeledData,NumericLabeledData> featureScalingPipe
		= new MappingDataPipe<NumericLabeledData,NumericLabeledData>(cachingPipe,featureScalingDataMapper);

		List<double[]> featureScaledData = new ArrayList<double[]>();
		List<Double> numericLabels = new ArrayList<Double>();
		while (featureScalingPipe.hasNext())
		{
			NumericLabeledData labeledData = featureScalingPipe.next();
			featureScaledData.add(labeledData.getElement());
			numericLabels.add(labeledData.getLabel());
		}
		
		// Assert that we have the same number of output element arrays and labels as the training set
		Assert.assertEquals(trainingSetSize,featureScaledData.size());
		Assert.assertEquals(trainingSetSize,numericLabels.size());

		// Get the first element feature array and label
		double[] firstFeatureScaledDataElement = featureScaledData.get(0);
		Double firstLabel = numericLabels.get(0);
		
		// Asser that the element feature array has 2 features
		Assert.assertEquals(2,firstFeatureScaledDataElement.length);
		

		// Assert that the 2 features are scaled versions of the input features
		Assert.assertEquals(-1.6022476316822152d, firstFeatureScaledDataElement[0]);
		Assert.assertEquals(0.6383411191827778d, firstFeatureScaledDataElement[1]);

		// Asser the first label is not null and is a 0 ( for NOT_ACCCEPTED s)
		Assert.assertNotNull(firstLabel);
		Assert.assertEquals(0.0, firstLabel.doubleValue());
		
	}
	
	
	@Before
	public void setUp() throws Exception
	{
		// Read exam scores and admission status data used as part of a programming assignment on
		// Stanford's Machine Learning course
		Collection<Application> previousApplicationsCollection = getPreviousApplicationDataFromFile("ex2data1.txt");
		
		// Assert that we have read the training data correctly
		Assert.assertNotNull(previousApplicationsCollection);
		Assert.assertEquals(100,previousApplicationsCollection.size());
		Application application1 = previousApplicationsCollection.iterator().next();
		ExamScores examScores1 = application1.getExamScores();
		Assert.assertEquals(34.62365962451697, examScores1.getExamScore1());
		Assert.assertEquals(78.0246928153624, examScores1.getExamScore2());
		Assert.assertEquals(AdmissionStatus.NOT_ACCEPTED, application1.getAdmissionStatus());
	
		
		this.previousApplications = previousApplicationsCollection;
		this.trainingSetSize = previousApplicationsCollection.size();
	}
	
	private Collection<Application> getPreviousApplicationDataFromFile(String fileName)
	{
		TrainingSetDataSource<Application,Collection<Application>> houses = new CsvFileClassloaderDataSource<Application>(fileName,getClass().getClassLoader(),new PreviousApplicationCsvDataExtractor());
		Collection<Application> housesCollection = houses.getData();
		return housesCollection;
	}
}
