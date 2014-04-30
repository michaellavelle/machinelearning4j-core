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

/**
 * Decorating Iterator, mapping source elements of type S to type T
 * 
 * @author Michael Lavelle
 */
public class MappingDataPipe<S,T> extends DataPipe<S, T> {

	private DataMapper<S,T> dataMapper;
	
	public MappingDataPipe(Iterator<S> sourceIterator,DataMapper<S,T> dataMapper) {
		super(sourceIterator);
		this.dataMapper = dataMapper;
	}

	@Override
	public boolean hasNext() {
		return sourceIterator.hasNext();
	}

	@Override
	public T next() {
		return dataMapper.getMappedData(sourceIterator.next());
	}

}
