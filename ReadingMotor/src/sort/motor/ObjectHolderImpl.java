package sort.motor;

import java.util.HashMap;
import java.util.Map;

@Holder
public class ObjectHolderImpl {

	Map<String, Object> holdeds = new HashMap<String, Object>();

	@SuppressWarnings("unchecked")
	public <Y> Y getHolded(String name) {
		return (Y) holdeds.get(name);
	}

	public void setHolded(String name, Object holded) {
		holdeds.put(name, holded);
	}

	public boolean hasHolded(String name) {
		return holdeds.containsKey(name);
	}

}
