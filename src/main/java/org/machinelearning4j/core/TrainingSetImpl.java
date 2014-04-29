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
	protected FeatureScaler featureScaler;
	protected Statistics[] featureStatistics;
	protected Iterable<T> elements;
	protected double[][] featureMatrix;
	protected Iterator<T> elementsIterator;
	
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
		this.featureScaler = featureScalingStrategy == null ? null : featureScalingStrategy.getFeatureScaler(this);
		this.size = size;
	}
	
	protected void processElement(T element,int index)
	{
		featureMatrix[index] = numericFeatureMapper.getFeatureValues(element);
	}
	
	protected void ensureDataSize(int length)
	{
		if (length < size)
		{
		LOG.debug("Resizing to:" + length);
		double[][] resizedFeatureMatrix = new double[length][];
		for (int i = 0; i < length; i++)
		{
			resizedFeatureMatrix[i] = featureMatrix[i];
		}
		featureMatrix = null;
		featureMatrix = resizedFeatureMatrix;
		}
	}
	
	private double[][] getElementFeatures()
	{
		if (featureMatrix == null)
		{
			LOG.debug("About to convert all available elements(up to max of " + size + ") into numeric element features matrix");
			featureMatrix =  new double[size][];
			int index = 0;
			Iterator<T> elementsIterator = getSourceElementsIterator();
			while (elementsIterator.hasNext())
			{
				T element = elementsIterator.next();
				if (index >= size) 
				{ 
					break;
				}
				processElement(element,index++);
			}	
			LOG.debug("Read " + index + " elements from source and converted into numeric element features matrix");
			ensureDataSize(index);
		}
		return featureMatrix;
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
	public double[][] getFeatureMatrix() {
		
		// TODO. Build up feature matrix as elements are added instead of on feature matrix access
		if (featureScaler == null || dataIsFeatureScaled)
		{
			return getElementFeatures();
		}
		else
		{

			for (double[] elementFeatureArray : getElementFeatures())
			{
				featureScaler.scaleFeatures(elementFeatureArray,true);
			}
			LOG.debug("Scaling feature matrix");

			dataIsFeatureScaled = true;
		}
		
		return getElementFeatures();
	}
	
	
	public Statistics[] getFeatureStatistics()
	{
		// Lazy evaulate feature statistics
		if (featureStatistics != null)
		{
			return featureStatistics;
		}
		else
		{
			LOG.debug("Calculating feature statistics on " + getElementFeatures().length + " elements");
			int startIndex = numericFeatureMapper.isHasInterceptFeature() ? 1 : 0;
	
			featureStatistics = new Statistics[getElementFeatures()[0].length - startIndex];
			for (int featureIndex = startIndex;  featureIndex < (getElementFeatures()[0].length); featureIndex++)
			{
				double[] allFeatureValues = new double[getElementFeatures().length];
				int elementIndex = 0;
				for (double[] elementFeaturesArray : getElementFeatures())
				{
					allFeatureValues[elementIndex++] = elementFeaturesArray[featureIndex];
				}
				int featInd = numericFeatureMapper.isHasInterceptFeature()  ? (featureIndex - 1) : featureIndex;
				featureStatistics[featInd] = new Statistics(allFeatureValues);
			}
			return featureStatistics;
		}
 	}

	@Override
	public NumericFeatureMapper<T> getFeatureMapper() {
		return numericFeatureMapper;
	}



	@Override
	public boolean isFeatureScalingConfigured() {
		return featureScaler != null;
	}



	@Override
	public boolean isDataFeatureScaled() {
		return dataIsFeatureScaled;
	}

	@Override
	public FeatureScaler getFeatureScaler() {
		return featureScaler;
	}

	

}
