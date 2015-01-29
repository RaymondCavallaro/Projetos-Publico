package sort.classes;

import java.util.List;

public class SimpleDeviationClassInteger extends ExponentialClassInteger {

	private Double grow = null;

	public Double getGrow() {
		return grow;
	}

	public void setGrow(Double grow) {
		this.grow = grow;
	}

	public boolean fit(List<Integer> ordered) {
		return false;
	}
}
