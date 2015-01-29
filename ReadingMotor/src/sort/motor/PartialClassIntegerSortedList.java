package sort.motor;

import java.util.ArrayList;
import java.util.List;

import sort.classes.ClassInteger;
import sort.classes.LinearClassInteger;
import sort.classes.SimpleDeviationClassInteger;
import sort.classes.funtion.AxisRelativeClass;

public class PartialClassIntegerSortedList extends ArrayList<Integer> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1794909730785331791L;

	private ClassInteger holderClasse = null;
	private List<Integer> ordered = new ArrayList<Integer>();

	public PartialClassIntegerSortedList(List<Integer> lista) {
		super(lista);
		ordered = new ArrayList<Integer>(lista);
	}

	public PartialClassIntegerSortedList() {
	}

	public ClassInteger getClasse() {
		return holderClasse.getHolder().getHolded(ClassInteger.class.getName());
	}

	public void setHolderClasse(ClassInteger classe) {
		this.holderClasse = classe;
	}

	public List<Integer> getOrdered() {
		return ordered;
	}

	public void setOrdered(List<Integer> ordered) {
		this.ordered = ordered;
	}
}
