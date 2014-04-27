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

/**
 *  A NumericLabelMapper is used to convert labels of original type L to numerics.
 *  
 *  This is an implementation of NumericLabelMapper which can be used when the original labels
 *  are Numbers.
 *  
 * @author Michael Lavelle
 */
public class SingleNumericValueLabelMapper implements NumericLabelMapper<Number> {

	@Override
	public double[] getLabelValues(List<Number> labels) {
		double[] labelValues = new double[labels.size()];
		int labelValueIndex = 0;
		for (Number label : labels)
		{
			labelValues[labelValueIndex++] = label.doubleValue();
		}
		return labelValues;
	}

}
