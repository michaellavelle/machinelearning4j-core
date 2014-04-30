package org.machinelearning4j.supervisedlearning;

public interface LabeledData<T,L> {
	
	public T getElement();
	public L getLabel();

}
