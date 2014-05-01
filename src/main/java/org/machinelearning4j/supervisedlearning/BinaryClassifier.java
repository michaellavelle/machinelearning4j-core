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

import java.util.Arrays;
import java.util.List;

import org.machinelearning4j.algorithms.supervisedlearning.LogisticRegressionAlgorithm;
import org.machinelearning4j.algorithms.supervisedlearning.NumericHypothesisFunction;
import org.machinelearning4j.algorithms.supervisedlearning.RegressionAlgorithm;
/**
 * Classifying LabelPredictor - predicts a classification of type L (where there are exactly
 * two exclusive values the classification can take ) with an associated
 * prediction probability
 * 
 * @author Michael Lavelle
 */
public class BinaryClassifier<T,L,C> implements Classifier<T,L,C> {

	private LabeledTrainingSet<T, L> labeledTrainingSet;
	private NumericHypothesisFunction hypothesisFunction;
	private double decisionBoundaryProbabilityThreshold = 0.5d;
	private L negativeClass;
	private L positiveClass;
	private NumericLabelMapper<L> labelMapper;
	
	private TrainingStrategy<C,? extends RegressionAlgorithm<C>> trainingStrategy;

	
	public BinaryClassifier(
			TrainingStrategy<C,? extends LogisticRegressionAlgorithm<C>> trainingStrategy,NumericLabelMapper<L> labelMapper,L negativeClass,L positiveClass) {
			
			this.labelMapper = labelMapper;
			this.negativeClass = negativeClass;
			this.positiveClass = positiveClass;
			this.trainingStrategy = trainingStrategy;
	}
	
	

	@Override
	public void train(LabeledTrainingSet<T, L> labeledTrainingSet,C trainingContext) {
		
		if (!labeledTrainingSet.isFeatureScalingConfigured())
		{
			throw new IllegalStateException("Logistic regression algorithm requires " +
					"that feature scaling is configured for the training set");
		}
		
		this.labeledTrainingSet = labeledTrainingSet;
		
		
		NumericHypothesisFunction updatedHypothesisFunction = trainingStrategy.train(labeledTrainingSet, labelMapper, trainingContext);
		if (updatedHypothesisFunction == null)
		{
			throw new RuntimeException("Training has completed without returning a hypothesis function");
		}
		else
		{
			hypothesisFunction = updatedHypothesisFunction;	
		}
	}
	
	protected ClassificationProbability<L> predictLabel(double[] featureValues)
	{
		if (hypothesisFunction == null)
		{
			throw new IllegalStateException("No hypothesis function available to use to make predictions - has training been run?");
		}
		Double positiveClassProbability = trainingStrategy.getAlgorithm().predictLabel(featureValues , hypothesisFunction);
		if (positiveClassProbability == null)
		{
			return null;
		}
		else
		{
			if (positiveClassProbability.doubleValue() >= decisionBoundaryProbabilityThreshold)
			{
				return new ClassificationProbability<L>(positiveClass,positiveClassProbability.doubleValue());
			}
			else
			{
				return new ClassificationProbability<L>(negativeClass,1d - positiveClassProbability.doubleValue());
			}
		}
	}
	
	@Override
	public ClassificationProbability<L> predictLabel(T element) {

		double[] featureValues = labeledTrainingSet.getFeatureMapper().getFeatureValues(element);
		if (labeledTrainingSet.isFeatureScalingConfigured() && labeledTrainingSet.isDataFeatureScaled())
		{
			featureValues = labeledTrainingSet.getFeatureScaler().scaleFeatures(featureValues,true);
		}
		return predictLabel(featureValues);
		
	}

	public void setDecisionBoundaryProbabilityThreshold(
			double decisionBoundaryProbabilityThreshold) {
		this.decisionBoundaryProbabilityThreshold = decisionBoundaryProbabilityThreshold;
	}
	
	public double getTrainingSetPredictionAccuracyPercentage()
	{
		double predictedCorrect = 0;
		int trainingExampleIndex = 0;
		double[][] benchmarkFeatureMatrix = labeledTrainingSet.getBenchmarkFeatureMatrix();
		List<Double> benchmarkLabels = labeledTrainingSet.getBenchmarkLabels();
		if (benchmarkFeatureMatrix == null || benchmarkLabels == null ||  benchmarkFeatureMatrix.length == 0 || benchmarkLabels.size() == 0 || benchmarkFeatureMatrix.length != benchmarkLabels.size() )
		{
			throw new IllegalStateException("Benchmark data has not been correctly set on the training set");
		}
		for (double[] elementFeatures :benchmarkFeatureMatrix)
		{
			ClassificationProbability<L> prediction = predictLabel(elementFeatures);
			double actualLabelValue = benchmarkLabels.get(trainingExampleIndex++);
			@SuppressWarnings("unchecked")
			double predictedLabelValue = labelMapper.getLabelValues(Arrays.asList(prediction.getClassification()))[0];
			if (predictedLabelValue == actualLabelValue)
			{
				predictedCorrect++;
			}
		}
		return 100 * predictedCorrect/benchmarkFeatureMatrix.length;
	}

}
