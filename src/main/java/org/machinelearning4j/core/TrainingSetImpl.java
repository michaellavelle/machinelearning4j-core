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
package org.machinelearning4j.core;

import java.util.Iterator;

import org.apache.log4j.Logger;

/**
 * Default implementation of a TrainingSet
 * 
 * @author Michael Lavelle
 */
public class TrainingSetImpl<T> implements TrainingSet<T> {

	private NumericFeatureMapper<T> numericFeatureMapper;
	protected int size;
	protected boolean dataIsFeatureScaled;
	protected FeatureScalingStrategy featureScalingStrategy;
	protected Statistics[] featureStatistics;
	protected Iterable<T> elements;
	protected double[][] featureMatrix;
	protected Iterator<T> elementsIterator;
	protected FeatureScaler featureScaler;
	private double[][] benchmarkFeatureMatrix;
	
	
	public double[][] getBenchmarkFeatureMatrix() {
		return benchmarkFeatureMatrix;
	}

	public void setBenchmarkFeatureMatrix(double[][] benchmarkFeatureMatrix) {
		this.benchmarkFeatureMatrix = benchmarkFeatureMatrix;
	}

	private static Logger LOG = Logger.getLogger(TrainingSetImpl.class);

	
	public int getSize() {
		return size;
	}
	
	public Iterator<T> getSourceElementsIterator()
	{
		if (elementsIterator == null)
		{
			LOG.debug("Accessing training elements source Iterable" );
			return elements.iterator();
		}
		else
		{
			LOG.debug("Accessing training elements source Iterator" );

			return elementsIterator;
		}
	}

	public TrainingSetImpl(NumericFeatureMapper<T> numericFeatureMapper,int size)
	{
		this.numericFeatureMapper = numericFeatureMapper;
		this.size = size;
	}
	
	public TrainingSetImpl(NumericFeatureMapper<T> numericFeatureMapper,FeatureScalingStrategy featureScalingStrategy,int size)
	{
		this.numericFeatureMapper = numericFeatureMapper;
		this.featureScalingStrategy = featureScalingStrategy;
		this.size = size;
	}
	

	/**
	 * @param elements Data elements to add to training set
	 */
	@Override
	public void setTrainingElementsSource(Iterable<T> elements) {
		this.elements = elements;
		if (dataIsFeatureScaled)
		{
			throw new IllegalStateException("Cannot add any more elements to this training set as it has been feature scaled");
		}
		if (featureStatistics != null)
		{
			throw new IllegalStateException("Cannot add any more elements to this training set as feature statistics have been calculated");
		}
	}
	
	/**
	 * @param elements Data elements to add to training set
	 */
	@Override
	public void setTrainingElementsSource(Iterator<T> elements) {
		this.elementsIterator = elements;
		if (dataIsFeatureScaled)
		{
			throw new IllegalStateException("Cannot add any more elements to this training set as it has been feature scaled");
		}
		if (featureStatistics != null)
		{
			throw new IllegalStateException("Cannot add any more elements to this training set as feature statistics have been calculated");
		}
		
	}
	
	@Override
	public NumericFeatureMapper<T> getFeatureMapper() {
		return numericFeatureMapper;
	}



	@Override
	public boolean isFeatureScalingConfigured() {
		return featureScalingStrategy != null;
	}



	@Override
	public boolean isDataFeatureScaled() {
		return dataIsFeatureScaled;
	}

	@Override
	public FeatureScalingStrategy getFeatureScalingStrategy() {
		return featureScalingStrategy;
	}
	
	@Override
	public FeatureScaler getFeatureScaler() {
		return featureScaler;
	}
	
	
	public boolean isDataIsFeatureScaled() {
		return dataIsFeatureScaled;
	}

	public void setDataIsFeatureScaled(boolean dataIsFeatureScaled) {
		this.dataIsFeatureScaled = dataIsFeatureScaled;
	}

	@Override
	public void setFeatureScaler(FeatureScaler featureScaler) {
		this.featureScaler = featureScaler;
	}


	

}
