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

import org.machinelearning4j.algorithms.supervisedlearning.LinearRegressionAlgorithm;

/**
 *  Implementation of NumericLabelPredictor which predicts a single numeric label from an
 *  element of type T with an original label of type L
 * 
 * @author Michael Lavelle
 */
public class SingleNumericValueLabelPredictor<T,C> extends
		NumericLabelPredictor<T, Number,C> {

	public SingleNumericValueLabelPredictor(
			LinearRegressionAlgorithm<C> linearRegressionAlgorithm) {
		super(new SingleNumericValueLabelMapper(), linearRegressionAlgorithm);
	}

}
