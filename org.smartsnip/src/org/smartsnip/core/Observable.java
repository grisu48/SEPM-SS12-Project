package org.smartsnip.core;

import java.util.ArrayList;
import java.util.List;

public class Observable<E extends IObserver> {
	/** List of observers */
	protected List<E> observers = new ArrayList<E>();

	/**
	 * Adds an observer to this observable. If the observer is null, nothing is
	 * done
	 * 
	 * @param observer
	 *            to be added
	 */
	public synchronized void addObserver(E observer) {
		if (observer == null) return;
		if (observers.contains(observer)) return;
		observers.add(observer);
	}

	/**
	 * Removes an observer from the observable. If the observer is null, nothing
	 * is done
	 * 
	 * @param observer
	 *            to be removed
	 */
	public synchronized void removeObserver(E observer) {
		if (observer == null) return;
		while (observers.contains(observer))
			observers.remove(observer);
	}

	/**
	 * @return the number of observers currently in the observable
	 */
	public synchronized int getObserverCount() {
		return observers.size();
	}

}
