package org.smartsnip.shared;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * A pair of objects
 * 
 * @param <E>
 *            First object
 * @param <V>
 *            Second object
 */
public class Pair<E, V> implements Serializable, IsSerializable {
	/** Serialisation ID */
	private static final long serialVersionUID = -2215994228066675515L;

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

	@Override
	public int hashCode() {
		return first.hashCode() ^ second.hashCode();
	}

	@Override
	@SuppressWarnings("rawtypes")
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (!(obj instanceof Pair))
			return false;
		Pair pair = (Pair) obj;
		if (!pair.first.equals(first))
			return false;
		if (!pair.second.equals(second))
			return false;

		return true;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Pair [first=").append(this.first).append(", second=")
				.append(this.second).append("]");
		return builder.toString();
	}
}
