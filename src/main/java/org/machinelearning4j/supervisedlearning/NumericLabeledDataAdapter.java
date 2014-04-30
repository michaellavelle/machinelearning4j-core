package org.machinelearning4j.supervisedlearning;

public class NumericLabeledDataAdapter extends LabeledDataAdapter<double[], Double>
		implements NumericLabeledData {

	public NumericLabeledDataAdapter(double[] element, Double label) {
		super(element, label);
	}



}
