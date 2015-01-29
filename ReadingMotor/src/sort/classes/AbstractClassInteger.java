package sort.classes;

import sort.motor.Holder;
import sort.motor.ObjectHolderImpl;

@Holder(interfaces = { ClassInteger.class })
public class AbstractClassInteger implements ClassInteger {

	protected ObjectHolderImpl holder = new ObjectHolderImpl();

	public AbstractClassInteger() {
		holder.setHolded(ClassInteger.class.getName(), this);
	}

	public ObjectHolderImpl getHolder() {
		return holder;
	}

	public void setHolder(ObjectHolderImpl holder) {
		this.holder = holder;
	}
}
