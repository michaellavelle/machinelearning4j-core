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

import java.util.List;

import org.apache.log4j.Logger;
import org.machinelearning4j.algorithms.ConvergenceCriteria;
/**
 * Defines convergence criteria for the cost function of a gradient descent algorithm which is decreasing on
 * average with each snapshot and then "converges" to a value which oscillates around the minimum where the oscillations
 * are bounded by a small range.
 * 
 * @author Michael Lavelle
 */
public class DecreasingOnAverageThenOscillatingCostFunctionSnapshotConvergenceCriteria implements ConvergenceCriteria<GradientDescentAlgorithmTrainingContext> {

	static Logger LOG = Logger.getLogger(DecreasingOnAverageThenOscillatingCostFunctionSnapshotConvergenceCriteria.class);

	
	private double minimumInitialCostSnapshotDecreasePercentage;
	private double declareConvergenceIfCostSnapshotMagnitudeChangesByLessThanPercentage;
	
	public DecreasingOnAverageThenOscillatingCostFunctionSnapshotConvergenceCriteria(double minimumInitialCostSnapshotDecreasePercentage,double declareConvergenceIfCostSnapshotMagnitudeChangesByLessThanPercentage) {
		this.declareConvergenceIfCostSnapshotMagnitudeChangesByLessThanPercentage = declareConvergenceIfCostSnapshotMagnitudeChangesByLessThanPercentage;
		this.minimumInitialCostSnapshotDecreasePercentage = minimumInitialCostSnapshotDecreasePercentage;
	}

	@Override
	public boolean isPrerequisiteConditionViolated(GradientDescentAlgorithmTrainingContext trainingContext) {
		List<Double> costFunctionSnapshots = trainingContext.getCostFunctionSnapshotHistory();
	
		if (costFunctionSnapshots.size() > 1)
		{
			// Check initial cost decrease
			double initialCostSnapshot = costFunctionSnapshots.get(0).doubleValue();
			if (initialCostSnapshot == 0) return true;
			double nextCostSnapshot = costFunctionSnapshots.get(1).doubleValue();
			if (nextCostSnapshot != 0)
			{
				double initialCostDecreasePercentage = (1d - nextCostSnapshot/initialCostSnapshot) * 100d;
				if (initialCostDecreasePercentage < minimumInitialCostSnapshotDecreasePercentage) return true; 
			}
			
		}
		return false;
	}

	@Override
	public boolean isConvergenceCompleteConditionSatisfied(GradientDescentAlgorithmTrainingContext trainingContext) {
		List<Double> costFunctionSnapshots = trainingContext.getCostFunctionSnapshotHistory();
		
		if (costFunctionSnapshots.size() == 0)
		{
			throw new RuntimeException("No cost function snapshots being captured.");
		}
			
		
		if (costFunctionSnapshots.size() > 2)
		{
			double lastCostSnapshot = costFunctionSnapshots.get(costFunctionSnapshots.size() - 1);
			double penultimateCostSnapshot = costFunctionSnapshots.get(costFunctionSnapshots.size() - 2);
			double lastCostDecreasePercentage = (1d - lastCostSnapshot/penultimateCostSnapshot) * 100d;			
			return Math.abs(lastCostDecreasePercentage) < declareConvergenceIfCostSnapshotMagnitudeChangesByLessThanPercentage;
		}
		else
		{
			return false;
		}
	}

	

}