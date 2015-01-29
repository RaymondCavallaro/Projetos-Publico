package sort.motor;

import java.util.HashMap;
import java.util.Map;

public class ImplFactory {

	@SuppressWarnings("rawtypes")
	Map<Class, Object> holdeds = new HashMap<Class, Object>();

	@SuppressWarnings("unchecked")
	public <Y> Y getHolded(Class<Y> clazz) {
		return (Y) holdeds.get(clazz);
	}

	public <Y> void setHolded(Class<Y> clazz, Y holded) {
		holdeds.put(clazz, holded);
	}

	@SuppressWarnings("rawtypes")
	public boolean hasImpl(Class clazz) {
		return holdeds.containsKey(clazz);
	}

}
