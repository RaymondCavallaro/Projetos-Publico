package sort.motor;

public class ImplFactoryServiceImpl implements ImplFactoryService {

	static ImplFactory implFactory = new ImplFactory();

	public ImplFactory getFactory() {
		return implFactory;
	}

}
