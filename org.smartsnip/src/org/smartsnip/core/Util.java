package org.smartsnip.core;

import java.util.ArrayList;
import java.util.List;

/**
 * This class provides different utility methods, that are not clearly
 * characterised by an object entity
 * 
 * 
 */
public class Util {

	/** Utility class is not instancable */
	private Util() {
		throw new RuntimeException("Not supported");
	}

	/**
	 * Crops a given list to a sublist, given by a start index and a element
	 * count
	 * 
	 * @param input
	 *            Input list
	 * @param start
	 *            Index where to start
	 * @param count
	 *            Maximum number of item
	 * @return the cropped list
	 */
	public static <E> List<E> cropList(List<E> input, int start, int count) {
		if (input == null)
			return null;
		if (start < 0 || count <= 0)
			return new ArrayList<E>();
		if (start >= input.size())
			return new ArrayList<E>();

		List<E> result = new ArrayList<E>(input);
		while (start > 0) {
			result.remove(0);
			start--;
		}
		while (result.size() > count)
			result.remove(count);

		return result;
	}
}
