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


/**
 *  Configures a training set container for source data
 * 
 * @author Michael Lavelle
 */
public interface TrainingSet<T>  {

	/**
	 * @param elements Sets the source of the data for algorithms that require data to be accessed and looped
	 * through multiple times (eg. batch algorithms)
	 * 
	 * ie. Data set can be traversed multiple times ( for batch algorithms )  via an Iterable
	 */
	void setTrainingElementsSource(Iterable<T> elements);


	/**
	 * @param elements Sets the source of the data for algorithms that enable data be acceessed in a continuous stream
	 * and do not enable a mechanism to loop through the data multiple times, but only a single time
	 * 
	 * ie. Data set can be traversed only once via an Iterator
	 */
	void setTrainingElementsSource(Iterator<T> elements);
	
	
	public Iterator<T> getSourceElementsIterator();

	double[][] getBenchmarkFeatureMatrix();
	void setBenchmarkFeatureMatrix(double[][] benchmarkFeatures);
	
	public int getSize();


	/**
	 * @return An adapter which maps the features of an element of type T to an array of double values
	 */
	NumericFeatureMapper<T> getFeatureMapper();
	
	boolean isFeatureScalingConfigured();
	
	boolean isDataFeatureScaled();
	
	void setDataIsFeatureScaled(boolean featureScaled);

	
	public FeatureScalingStrategy getFeatureScalingStrategy();
	public FeatureScaler getFeatureScaler();
	public void setFeatureScaler(FeatureScaler featureScaler);
			
	

}
