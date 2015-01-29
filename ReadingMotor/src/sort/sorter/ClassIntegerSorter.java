package sort.sorter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import sort.classes.SimpleDeviationClassInteger;
import sort.motor.ClassIntegerSortedList;

public class ClassIntegerSorter {

	@SuppressWarnings("unused")
	private static boolean isImediatellyOredered(ClassIntegerSortedList lista) {
		return true;
	}

	private static boolean isImediatellyOredered(List<Integer> lista) {
		if (lista.size() == 0 || lista.size() == 1) {
			return true;
		} else if (lista.size() == 2 && lista.get(0) < lista.get(1)) {
			return true;
		}
		return false;
	}

	public static ClassIntegerSortedList sort(List<Integer> lista) {
		ClassIntegerSortedList listaOrdenada;
		if (isImediatellyOredered(lista)) {
			listaOrdenada = new ClassIntegerSortedList(lista);
		} else {
			listaOrdenada = new ClassIntegerSortedList();
			if (lista.size() == 2) {
				listaOrdenada.add(lista.get(1));
				listaOrdenada.add(lista.get(0));
//			} else if (lista.size() == 3) {
//				int v0 = lista.get(0);
//				int v1 = lista.get(1);
//				int v2 = lista.get(2);
//				if (v0 > v1) {
//					v0 = lista.get(1);
//					v1 = lista.get(0);
//					if (v2 < v0) {
//						v2 = v1;
//						v1 = v0;
//						v0 = lista.get(2);
//					} else if (v2 < v1) {
//						v2 = v1;
//						v1 = lista.get(2);
//					}
//				} else {
//					if (v0 > v2) {
//						v0 = lista.get(2);
//						v2 = lista.get(0);
//					}
//					if (v1 > v2) {
//						v1 = v2;
//						v2 = lista.get(1);
//					}
//				}
//				SimpleDeviationClassInteger deviation = (SimpleDeviationClassInteger) SimpleDeviationClassInteger
//						.createValues(v0, v1, v2);

			}
		}
		return listaOrdenada;
	}

//	public static void main(String[] args) {
//		Random rand = new Random();
//		int minimum = 0;
//		int maximum = 1000;
//		int qtd = 100;
//		List<Integer> lista = new ArrayList<Integer>();
//		for (int i = 0; i < qtd; i++) {
//			lista.add(rand.nextInt(maximum - minimum));
//		}
//		List<Integer> listaOrdenada = ClassIntegerSorter.sort(lista);
//		for (Integer integer : listaOrdenada) {
//			System.out.println(integer);
//		}
//	}
}
