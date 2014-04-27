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

import org.machinelearning4j.algorithms.supervisedlearning.NumericHypothesisFunction;
/**
 *  Encapsulates a strategy for training an algorithm to return a NumericHypothesisFunction on training completion.
 *  
 *  Given a labeled training set, an implementation will run within a specified training context, and will extract source elements from the training set into numeric values
 *  and use a LabelMapper to map labels into numerics.
 * 
 * @author Michael Lavelle
 */
public interface TrainingStrategy<C> {

	public <T,L> NumericHypothesisFunction train(LabeledTrainingSet<T,L> labeledTrainingSet,NumericLabelMapper<L> labelMapper,C trainingContext);

}

