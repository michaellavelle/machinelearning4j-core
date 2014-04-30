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

import org.machinelearning4j.core.FeatureScaler;

/**
 * Data mapper to be used with MappingDataPipe to scale the features of elements that are represented
 * by double[]
 * 
 * @author Michael Lavelle
 */
public class FeatureScalingDataMapper implements DataMapper<double[], double[]> {

	private FeatureScaler featureScaler;
	private boolean firstFeatureIsIntercept;
	
	public FeatureScalingDataMapper(FeatureScaler featureScaler,boolean firstFeatureIsIntercept)
	{
		this.featureScaler = featureScaler;
		this.firstFeatureIsIntercept = firstFeatureIsIntercept;
	}
	
	@Override
	public double[] getMappedData(double[] source) {
		featureScaler.scaleFeatures(source, firstFeatureIsIntercept);
		return source;
	}

}
