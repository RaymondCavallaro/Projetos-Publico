package sort.classes;

import java.util.List;

import sort.classes.funtion.AxisRelativeClass;
import sort.motor.Holder;

@Holder(interfaces = { AxisRelativeClass.class })
public class ExponentialClassInteger extends AbstractClassInteger {

	public boolean fit(List<Integer> ordered) {
		return false;
	}

	public AxisRelativeClass getAxis() {
		ClassInteger classe = this.getHolder().getHolded(
				ClassInteger.class.getName());
		return classe.getHolder().getHolded(AxisRelativeClass.class.getName());
	}

}
