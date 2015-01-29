package sort.motor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sort.classes.ClassInteger;

public class MultiClassIntegerSortedList extends ArrayList<Integer> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1794909730785331791L;

	private Map<ClassInteger, List<Integer>> classesHolder = new HashMap<ClassInteger, List<Integer>>();
	private List<Integer> ordered = new ArrayList<Integer>();

	public MultiClassIntegerSortedList(List<Integer> lista) {
		super(lista);
		ordered = new ArrayList<Integer>(lista);
	}

	public MultiClassIntegerSortedList() {
	}

	public Map<ClassInteger, List<Integer>> getClasses() {
		return classesHolder;
	}

	public void setClasses(Map<ClassInteger, List<Integer>> classes) {
		this.classesHolder = classes;
	}

	public List<Integer> getOrdered() {
		return ordered;
	}

	public void setOrdered(List<Integer> ordered) {
		this.ordered = ordered;
	}
}
