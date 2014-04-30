package org.machinelearning4j.core;

import org.apache.log4j.Logger;

public class InMemoryFeatureStatisticsSource implements FeatureStatisticsSource {

	private static Logger LOG = Logger.getLogger(InMemoryFeatureStatisticsSource.class);

	
	private double[][] elementFeatures;
	private Statistics[] featureStatistics;
	private boolean hasInterceptFeature;
	
	
	public InMemoryFeatureStatisticsSource(double[][] elementFeatures,boolean hasInterceptFeature)
	{
		this.elementFeatures = elementFeatures;
		this.hasInterceptFeature = hasInterceptFeature;
	}
	
	@Override
	public Statistics[] getFeatureStatistics()
	{
		// Lazy evaulate feature statistics
		if (featureStatistics != null)
		{
			return featureStatistics;
		}
		else
		{
			LOG.debug("Calculating feature statistics on " + elementFeatures.length + " elements");
			int startIndex = hasInterceptFeature ? 1 : 0;
	
			featureStatistics = new Statistics[elementFeatures[0].length - startIndex];
			for (int featureIndex = startIndex;  featureIndex < (elementFeatures[0].length); featureIndex++)
			{
				double[] allFeatureValues = new double[elementFeatures.length];
				int elementIndex = 0;
				for (double[] elementFeaturesArray : elementFeatures)
				{
					allFeatureValues[elementIndex++] = elementFeaturesArray[featureIndex];
				}
				int featInd = hasInterceptFeature  ? (featureIndex - 1) : featureIndex;
				featureStatistics[featInd] = new Statistics(allFeatureValues);
			}
			return featureStatistics;
		}
 	}

}
