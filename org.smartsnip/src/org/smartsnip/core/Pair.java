package org.smartsnip.core;

/**
 * A pair of objects
 * 
 * @param <E>
 *            First object
 * @param <V>
 *            Second object
 */
public class Pair<E, V> {
	/** The first item of the pair */
	public final E first;
	/** The second item of the pair */
	public final V second;

	/**
	 * Creates a new pair of items
	 * 
	 * @param first
	 *            the first item
	 * @param second
	 *            the second item
	 */
	public Pair(E first, V second) {
		this.first = first;
		this.second = second;
	}
}
