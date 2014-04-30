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
import java.util.List;

import org.machinelearning4j.core.NumericFeatureMapper;
import org.machinelearning4j.supervisedlearning.LabelDefinition;
import org.machinelearning4j.supervisedlearning.NumericLabelMapper;
import org.machinelearning4j.supervisedlearning.NumericLabeledData;
import org.machinelearning4j.supervisedlearning.NumericLabeledDataAdapter;

/**
 * Data mapper to be used with MappingDataPipe to extract numeric features and numeric labels
 * from elements of type T with domain labels of type L
 * 
 * @author Michael Lavelle
 */
public class NumericFeatureAndLabelExtractingDataMapper<T,L> implements DataMapper<T,NumericLabeledData>{

	private NumericLabelMapper<L> labelMapper;
	private LabelDefinition<T,L> labelDefinition;
	private NumericFeatureMapper<T> featureMapper;
	
	public NumericFeatureAndLabelExtractingDataMapper(NumericFeatureMapper<T> featureMapper,LabelDefinition<T,L> labelDefinition,NumericLabelMapper<L> labelMapper)
	{
		this.featureMapper = featureMapper;
		this.labelDefinition = labelDefinition;
		this.labelMapper = labelMapper;
	}
	
	@Override
	public NumericLabeledData getMappedData(T element) {
		L label = labelDefinition.getLabel(element);
		List<L> labels = new ArrayList<L>();
		labels.add(label);
		double labelValue = labelMapper.getLabelValues(labels)[0];
		return new NumericLabeledDataAdapter(featureMapper.getFeatureValues(element),labelValue);
	}

}
