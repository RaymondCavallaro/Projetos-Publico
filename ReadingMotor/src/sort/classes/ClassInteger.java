package sort.classes;

import sort.motor.Holder;
import sort.motor.ObjectHolderImpl;

@Holder(interfaces = { ClassInteger.class })
public interface ClassInteger {

	public ObjectHolderImpl getHolder();

	public void setHolder(ObjectHolderImpl holder);
}
