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

import java.util.Arrays;

/**
 * 
 * @author Michael Lavelle
 * 
 * A NumericHypothesisFunction encapsulates a function h : double[] -> Double so that predict(double[] numericFeatures) is a "good" predictor
 * given an numeric features array for the corresponding value of a numeric label.
 * 
 * This LinearRegressionHypothesisFunction uses a set of theta weights corresponding
 * to the feature values to predict a value from a linear combination of the weighted values.
 */
public class LinearRegressionHypothesisFunction implements NumericHypothesisFunction {

	private double[] thetas;

	public LinearRegressionHypothesisFunction(double[] thetas)
	{
		this.thetas = thetas;
	}
	
	@Override
	public Double predict(double[] numericFeatures) {
		double y = 0d;
		for (int index = 0; index < numericFeatures.length; index++)
		{
			y = y +  thetas[index] * numericFeatures[index];
		}
		return y;
	}
	
	public String toString()
	{
		return Arrays.asList(thetas).toString();
	}

}
