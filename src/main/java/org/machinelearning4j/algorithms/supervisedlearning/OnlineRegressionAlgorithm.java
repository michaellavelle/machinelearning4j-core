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
 * 
 * @author Michael Lavelle
 * 
 * An online RegressionAlgorithm can be trained on Iterable training set - the iterators of which can obtain different windows on source data,
 * in order to accommodate online learning.  This means that the entire training set does not need to be
 * loaded into memory before training can begin.  Each element of the trainin sethas a list of
 * numeric features, and learns a NumericHypothesisFunction which can be used to predict the
 * value of numeric labels given the numeric features of a specific element.
 * 
 * The numeric features and labels for a specific element are encapsulated in a NumericLabeledData pojo, with
 * the features for the input collection of training elements.
 *  * 
 * 
 */
public interface OnlineRegressionAlgorithm<C> extends RegressionAlgorithm<C>{

	public NumericHypothesisFunction train(Iterable<NumericLabeledData> labeledElements,long maxElementsToProcess,C trainingContext);

}

