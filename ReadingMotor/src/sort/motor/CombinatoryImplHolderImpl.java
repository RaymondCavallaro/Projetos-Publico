package sort.motor;

@Holder
public class CombinatoryImplHolderImpl extends ImplFactory {

//	@inject
	ImplFactoryService factoryService = new ImplFactoryServiceImpl();

	@SuppressWarnings("unchecked")
	public <Y> Y getHolded(Class<Y> clazz) {
		if (holdeds.containsKey(clazz)) {
			return (Y) holdeds.get(clazz);
		}
		return (Y) factoryService.getFactory().getHolded(clazz);
	}

	@SuppressWarnings("rawtypes")
	public boolean hasImpl(Class clazz) {
		return holdeds.get(clazz) == null;
	}
}
