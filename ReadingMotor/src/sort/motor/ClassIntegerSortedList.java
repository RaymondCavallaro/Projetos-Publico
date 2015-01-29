package sort.motor;

import java.util.ArrayList;
import java.util.List;

import sort.classes.ClassInteger;
import sort.classes.LinearClassInteger;
import sort.classes.SimpleDeviationClassInteger;
import sort.classes.funtion.AxisRelativeClass;

public class ClassIntegerSortedList extends ArrayList<Integer> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1794909730785331791L;

	private ClassInteger holderClasse = null;
	private List<Integer> ordered = new ArrayList<Integer>();

	public ClassIntegerSortedList(List<Integer> lista) {
		super(lista);
		ordered = new ArrayList<Integer>(lista);
	}

	public ClassIntegerSortedList() {
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

	public static ClassIntegerSortedList createValues(int v1, int v2, int v3) {
		ClassIntegerSortedList lista = new ClassIntegerSortedList();
		if (v1 < v2) {
			lista.setHolderClasse(LinearClassInteger.create(v1, v2));
		} else {
			lista.setHolderClasse(LinearClassInteger.create(v2, v1));
		}
		lista.fit(v3);
		return lista;
	}

	public boolean fit(int newValue) {
		ClassInteger classe = getClasse();
		if (classe instanceof LinearClassInteger) {

		} else if (classe instanceof SimpleDeviationClassInteger) {
			SimpleDeviationClassInteger sdc = (SimpleDeviationClassInteger) classe;
			if (sdc.getGrow() == null) {
				AxisRelativeClass axis = sdc.getAxis();
				int position = 0;
				if (newValue > axis.getX2()) {
					axis.setX2(newValue);
					position = ordered.size();
					ordered.add(newValue);
				} else {
					if (newValue < axis.getX1()) {
						axis.setX1(newValue);
					} else {
						position = (int) (newValue - axis.getX1())
								* axis.getDy() / (axis.getX2() - axis.getX1());
					}
					ordered.add(position, newValue);
				}
				axis.setDy(axis.getDy() + 1);
				return sdc.fit(ordered);
			}
		}
		return false;
	}
}
