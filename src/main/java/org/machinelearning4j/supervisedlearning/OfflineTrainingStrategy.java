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
package org.machinelearning4j.supervisedlearning;

import java.util.ArrayList;
import java.util.List;

import org.machinelearning4j.algorithms.supervisedlearning.NumericHypothesisFunction;
import org.machinelearning4j.algorithms.supervisedlearning.RegressionAlgorithm;
import org.machinelearning4j.core.FeatureScaler;
import org.machinelearning4j.core.FeatureStatisticsSource;
import org.machinelearning4j.core.InMemoryFeatureStatisticsSource;
import org.machinelearning4j.pipeline.CachingNumericLabeledDataPipe;
import org.machinelearning4j.pipeline.CachingNumericLabeledDataPipe.CachingStrategy;
import org.machinelearning4j.pipeline.DataMapper;
import org.machinelearning4j.pipeline.DataPipe;
import org.machinelearning4j.pipeline.FeatureScalingLabeledDataMapper;
import org.machinelearning4j.pipeline.MappingDataPipe;
import org.machinelearning4j.pipeline.NumericFeatureAndLabelExtractingDataMapper;
/**
 *  Encapsulates a strategy for training an offline Regression algorithm to return a NumericHypothesisFunction on training completion.
 *  
 *  The strategy is offline because the source data is loaded in full into memory before training starts and passed 
 *  to the regression algorithm as a feature matrix and label vector
 *  
 *  Given a labeled training set, an implementation will run within a specified training context, and will extract source elements from the training set into numeric values
 *  and use a LabelMapper to map labels into numerics.
 * 
 * @author Michael Lavelle
 */
public class OfflineTrainingStrategy<C> implements TrainingStrategy<C>{
	
	private RegressionAlgorithm<C> regressionAlgorithm;
	
	public OfflineTrainingStrategy(RegressionAlgorithm<C> regressionAlgorithm)
	{
		this.regressionAlgorithm = regressionAlgorithm;
	}

	public <T,L> NumericHypothesisFunction train(LabeledTrainingSet<T,L> labeledTrainingSet,NumericLabelMapper<L> labelMapper,C trainingContext)
	{
		// Create a data mapper for this pipeline, useing the feature mapper, and specifying label definition and mapper
		DataMapper<T, NumericLabeledData> dataMapper
		 = new NumericFeatureAndLabelExtractingDataMapper<T,L>(labeledTrainingSet.getFeatureMapper() , labeledTrainingSet.getLabelDefinition(), labelMapper);
		
		// Create a mapping data pipe line
		MappingDataPipe<T,NumericLabeledData> labelingPipe
		= new MappingDataPipe<T,NumericLabeledData>(labeledTrainingSet.getSourceElementsIterator(),dataMapper);
		
		double[][] inMemoryNumericFeatures= new double[labeledTrainingSet.getSize()][];
		List<Double> inMemoryLabels = new ArrayList<Double>();
		
		CachingNumericLabeledDataPipe<NumericLabeledData> cachingPipe
		= new CachingNumericLabeledDataPipe<NumericLabeledData>(labelingPipe,inMemoryNumericFeatures,inMemoryLabels,CachingStrategy.FIRST_ELEMENTS);
		
		labeledTrainingSet.setBenchmarkFeatureMatrix(inMemoryNumericFeatures);
		labeledTrainingSet.setBenchmarkLabels(inMemoryLabels);
		
		DataPipe<?,NumericLabeledData> targetDataPipe = cachingPipe;
		
		if (labeledTrainingSet.isFeatureScalingConfigured())
		{
			FeatureStatisticsSource statisticsSource = new InMemoryFeatureStatisticsSource(inMemoryNumericFeatures,labeledTrainingSet.getFeatureMapper().isHasInterceptFeature());

			FeatureScaler featureScaler = labeledTrainingSet.getFeatureScalingStrategy().getFeatureScaler(statisticsSource);
			
			labeledTrainingSet.setFeatureScaler(featureScaler);
			
			DataMapper<NumericLabeledData,NumericLabeledData> featureScalingDataMapper
				= new FeatureScalingLabeledDataMapper(labeledTrainingSet.getFeatureScaler(), labeledTrainingSet.getFeatureMapper().isHasInterceptFeature());
		
			MappingDataPipe<NumericLabeledData,NumericLabeledData> featureScalingPipe
			= new MappingDataPipe<NumericLabeledData,NumericLabeledData>(cachingPipe,featureScalingDataMapper);
		
			targetDataPipe = featureScalingPipe;
			labeledTrainingSet.setDataIsFeatureScaled(true);
			
		}

		double[] numericLabels = new double[labeledTrainingSet.getSize()];
		int labelIndex = 0;
		while (targetDataPipe.hasNext())
		{
			NumericLabeledData labeledData = targetDataPipe.next();
			numericLabels[labelIndex++] = labeledData.getLabel();
		}
		
		
		return regressionAlgorithm.train(inMemoryNumericFeatures, numericLabels,trainingContext);
	}

	
}
