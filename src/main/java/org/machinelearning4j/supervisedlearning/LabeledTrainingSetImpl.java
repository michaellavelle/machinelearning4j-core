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

import java.util.List;

import org.machinelearning4j.core.FeatureScalingStrategy;
import org.machinelearning4j.core.NumericFeatureMapper;
import org.machinelearning4j.core.TrainingSetImpl;

/**
 * Default implementation of a LabeledTrainingSet
 * 
 * @author Michael Lavelle
 */
public class LabeledTrainingSetImpl<T,L>  extends TrainingSetImpl<T> implements LabeledTrainingSet<T,L> {

	private List<Double> benchmarkLabels;
	public List<Double> getBenchmarkLabels() {
		return benchmarkLabels;
	}

	public void setBenchmarkLabels(List<Double> benchmarkLabels) {
		this.benchmarkLabels = benchmarkLabels;
	}

	private LabelDefinition<T,L> labelDefinition;
	
	public LabeledTrainingSetImpl(NumericFeatureMapper<T> numericFeatureMapper,LabelDefinition<T,L> labelDefinition,int size)
	{
		super(numericFeatureMapper,size);
		this.labelDefinition = labelDefinition;
	}
	
	public LabeledTrainingSetImpl(NumericFeatureMapper<T> numericFeatureMapper,FeatureScalingStrategy featureScalingStrategy,LabelDefinition<T,L> labelDefinition,int size)
	{
		super(numericFeatureMapper,featureScalingStrategy,size);
		this.labelDefinition = labelDefinition;
	}

	
	@Override
	public LabelDefinition<T, L> getLabelDefinition() {
		return labelDefinition;
	}

}
