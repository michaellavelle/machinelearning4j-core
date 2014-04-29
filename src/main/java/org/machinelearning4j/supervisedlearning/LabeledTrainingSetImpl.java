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

import org.machinelearning4j.core.FeatureScalingStrategy;
import org.machinelearning4j.core.NumericFeatureMapper;
import org.machinelearning4j.core.TrainingSetImpl;

/**
 * Default implementation of a LabeledTrainingSet
 * 
 * @author Michael Lavelle
 */
public class LabeledTrainingSetImpl<T,L>  extends TrainingSetImpl<T> implements LabeledTrainingSet<T,L> {

	private List<L> labels;
	private LabelDefinition<T,L> labelDefinition;
	
	public LabeledTrainingSetImpl(NumericFeatureMapper<T> numericFeatureMapper,LabelDefinition<T,L> labelDefinition,int size)
	{
		super(numericFeatureMapper,size);
		this.labels = new ArrayList<L>();
		this.labelDefinition = labelDefinition;
	}
	
	public LabeledTrainingSetImpl(NumericFeatureMapper<T> numericFeatureMapper,FeatureScalingStrategy featureScalingStrategy,LabelDefinition<T,L> labelDefinition,int size)
	{
		super(numericFeatureMapper,featureScalingStrategy,size);
		this.labels = new ArrayList<L>();
		this.labelDefinition = labelDefinition;
	}
	
	protected void addLabelForElement(T element)
	{
		labels.add(labelDefinition.getLabel(element));
	}
	
	

	@Override
	protected void processElement(T element, int index) {
		super.processElement(element, index);
		addLabelForElement(element);

	}

	@Override
	protected void ensureDataSize(int length) {
		labels = labels.subList(0, length );
		super.ensureDataSize(length);
	}

	@Override
	public List<L> getLabels() {
		return labels;
	}

	@Override
	public LabelDefinition<T, L> getLabelDefinition() {
		return labelDefinition;
	}

}
