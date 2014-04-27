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
import org.machinelearning4j.algorithms.supervisedlearning.NumericHypothesisFunction;

/**
 *  Trainable component which learns to predict numeric labels from elements of type T,
 *  using a linear regression algorithm
 * 
 * @author Michael Lavelle
 */
public class NumericLabelPredictor<T,L,C> implements
		LabelPredictor<T, L,Number,C> {

	private LinearRegressionAlgorithm<C> linearRegressionAlgorithm;
	
	private NumericHypothesisFunction hypothesisFunction;
	
	private LabeledTrainingSet<T, L> labeledTrainingSet;
	
	private NumericLabelMapper<L> labelMapper;
	
	private TrainingStrategy<C> trainingStrategy;

	
	
	public NumericLabelPredictor(NumericLabelMapper<L> labelMapper,
			LinearRegressionAlgorithm<C> linearRegressionAlgorithm) {
		this.linearRegressionAlgorithm = linearRegressionAlgorithm;
		this.labelMapper = labelMapper;
		trainingStrategy = new OfflineTrainingStrategy<C>(linearRegressionAlgorithm);
		
		
	}

	/**
	 * Train the label predictor, using the linearRegressionAlgorithm and 
	 * the data within the labeledTrainingSet
	 */
	@Override
	public void train(LabeledTrainingSet<T, L> labeledTrainingSet,C trainingContext) {
		
		this.labeledTrainingSet = labeledTrainingSet;
		if (!labeledTrainingSet.isDataFeatureScaled() && linearRegressionAlgorithm.isFeatureScaledDataRequired())
		{
			throw new IllegalStateException("This regression algorithm requires " +
					"that the data in the training set has been feature scaled");
		}	
		
		NumericHypothesisFunction updatedHypothesisFunction = trainingStrategy.train(labeledTrainingSet, labelMapper, trainingContext);
		if (updatedHypothesisFunction == null)
		{
			throw new RuntimeException("Training has completed without returning a hypothesis function");
		}
		else
		{
			hypothesisFunction = updatedHypothesisFunction;	
		}
		//hypothesisFunction = linearRegressionAlgorithm.train(labeledTrainingSet.getFeatureMatrix(), labelMapper.getLabelValues(labeledTrainingSet.getLabels()),trainingContext);
	}

	/**
	 * @param The element we wish to predict a label for
	 * @return  The predicted numeric label
	 */
	@Override
	public Number predictLabel(T element) {
		
		if (hypothesisFunction == null)
		{
			throw new IllegalStateException("No hypothesis function available to use to make predictions - has training been run?");
		}
		
		double[] featureValues = labeledTrainingSet.getFeatureMapper().getFeatureValues(element);
		if (labeledTrainingSet.isFeatureScalingConfigured() && labeledTrainingSet.isDataFeatureScaled())
		{
			featureValues = labeledTrainingSet.getFeatureScaler().scaleFeatures(labeledTrainingSet, featureValues,true);
		}
		return linearRegressionAlgorithm.predictLabel(featureValues, hypothesisFunction);
	}

	

}
