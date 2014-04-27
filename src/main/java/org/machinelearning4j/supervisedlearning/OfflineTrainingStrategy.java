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
import org.machinelearning4j.algorithms.supervisedlearning.RegressionAlgorithm;
/**
 *  Encapsulates a strategy for training an offline Regression algorithm to return a NumericHypothesisFunction on training completion.
 *  
 *  The strategy is offline because the source data is loaded in full into memory before training starts and passed 
 *  to the regression algorithm as a feature matrix and label vector
 *  
 *  Given a labeled training set, an implementation will run within a specified training context, and will extract source elements from the training set into numeric values
 *  and use a LabelMapper to map labels into numerics.
 * 
 * @author Michael Lavelle
 */
public class OfflineTrainingStrategy<C> implements TrainingStrategy<C>{
	
	private RegressionAlgorithm<C> regressionAlgorithm;
	
	public OfflineTrainingStrategy(RegressionAlgorithm<C> regressionAlgorithm)
	{
		this.regressionAlgorithm = regressionAlgorithm;
	}

	public <T,L> NumericHypothesisFunction train(LabeledTrainingSet<T,L> labeledTrainingSet,NumericLabelMapper<L> labelMapper,C trainingContext)
	{
		return regressionAlgorithm.train(labeledTrainingSet.getFeatureMatrix(), labelMapper.getLabelValues(labeledTrainingSet.getLabels()),trainingContext);
	}

	
}
