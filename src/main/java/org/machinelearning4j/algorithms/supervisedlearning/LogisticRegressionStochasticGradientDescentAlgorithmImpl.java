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
package org.machinelearning4j.algorithms.supervisedlearning;

import org.machinelearning4j.supervisedlearning.NumericLabeledData;

/**
 * A logistic regression algorithm implementation using batch gradient descent
 * 
 * @author Michael Lavelle
 */ 
public class LogisticRegressionStochasticGradientDescentAlgorithmImpl implements
		OnlineLogisticRegressionAlgorithm<GradientDescentAlgorithmTrainingContext>{

	@Override
	public NumericHypothesisFunction train(double[][] featureMatrix,
			double[] labelVector,
			GradientDescentAlgorithmTrainingContext trainingContext) {
		// TODO
		return null;
	}

	@Override
	public Double predictLabel(double[] featureVector,
			NumericHypothesisFunction hypothesisFunction) {
		// TODO
		return null;
	}

	@Override
	public boolean isFeatureScaledDataRequired() {
		// TODO
		return false;
	}

	@Override
	public NumericHypothesisFunction train(
			Iterable<NumericLabeledData> labeledElements,
			long maxElementsToProcess,
			GradientDescentAlgorithmTrainingContext trainingContext) {
		// TODO
		return null;
	}
}
