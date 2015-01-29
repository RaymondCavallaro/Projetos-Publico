package sort.classes;

import java.util.List;

import sort.classes.funtion.AxisRelativeClass;
import sort.classes.funtion.AxisRelativeClassImpl;

public class LinearClassInteger extends SimpleDeviationClassInteger {

	public static LinearClassInteger create(int minimum, int maximum) {
		LinearClassInteger classe = new LinearClassInteger()
				.getHolder().getHolded(ClassInteger.class.getName());
		AxisRelativeClass axis = new AxisRelativeClassImpl();
		classe.getHolder().setHolded(AxisRelativeClass.class.getName(), axis);
		axis.setX1(minimum);
		axis.setX2(maximum);
		return classe;
	}

	public boolean fit(List<Integer> ordered) {
		return false;
	}
}
