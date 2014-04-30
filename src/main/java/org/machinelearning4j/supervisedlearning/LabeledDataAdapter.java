package org.machinelearning4j.supervisedlearning;

public class LabeledDataAdapter<T,L> implements LabeledData<T,L> {

	private T element;
	private L label;
	
	public LabeledDataAdapter(T element,L label)
	{
		this.element = element;
		this.label = label;
	}
	
	@Override
	public T getElement() {
		return element;
	}

	@Override
	public L getLabel() {
		return label;
	}

	
}
