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
package org.machinelearning4j.pipeline;

import java.util.Iterator;
import java.util.List;

import org.machinelearning4j.supervisedlearning.NumericLabeledData;
import org.machinelearning4j.supervisedlearning.NumericLabeledDataAdapter;
/**
 * Caching Iterator, caching a specified number of elements in memory before passing through the pipeline
 * 
 * @author Michael Lavelle
 */
public class CachingNumericLabeledDataPipe<S> extends DataPipe<NumericLabeledData, NumericLabeledData> {

	public enum CachingStrategy { FIRST_ELEMENTS}
	
	private double[][] cachedElementFeatures;
	private List<Double> cachedLabels;
	private int cacheWriteIndex;
	private int cacheReadIndex;

	
	public CachingNumericLabeledDataPipe(
			Iterator<NumericLabeledData> sourceIterator,double[][] featuresCache,List<Double> labelsCache,CachingStrategy cachingStrategy) {
		super(sourceIterator);
		this.cachedElementFeatures = featuresCache;
		this.cachedLabels = labelsCache;
		cacheData(featuresCache.length);
	}
	
	protected void cacheData(int elementsToCache)
	{
		while (sourceIterator.hasNext())
		{
			NumericLabeledData labeledDataFeatures = sourceIterator.next();
			if (cacheWriteIndex < elementsToCache)
			{
				cachedElementFeatures[cacheWriteIndex]= labeledDataFeatures.getElement();
				cachedLabels.add(labeledDataFeatures.getLabel());
				cacheWriteIndex++;
			}
		}
		if (cacheWriteIndex < elementsToCache)
		{
			throw new RuntimeException("Not enough data to cache " + elementsToCache + " elements");
		}
	}
	

	public double[][] getCachedElementFeatures() {
		return cachedElementFeatures;
	}

	public List<Double> getCachedLabels() {
		return cachedLabels;
	}

	@Override
	public boolean hasNext() {
		return (cacheReadIndex < cacheWriteIndex) || sourceIterator.hasNext();
	}

	@Override
	public NumericLabeledData next() {
		if (cacheReadIndex < cacheWriteIndex)
		{
			NumericLabeledData labeledData =  new NumericLabeledDataAdapter(cachedElementFeatures[cacheReadIndex],cachedLabels.get(cacheReadIndex));
			cacheReadIndex++;
			return labeledData;
		}
		else
		{
			return sourceIterator.next();
		}
	}

	
	
}
