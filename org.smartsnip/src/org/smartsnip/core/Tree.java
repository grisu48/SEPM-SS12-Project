package org.smartsnip.core;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Implementation of a tree with an arbitary number of children per node. The
 * indexing of the tree happens with the hashCode of the generic type E
 * 
 * @param <E>
 *            Generic type of the children
 */
public class Tree<E> {

	/** Root item */
	private final TreeItem<E> root = new TreeItem<E>(null, null);

	/**
	 * An internal tree with an arbitary number of children
	 * 
	 * @param <E>
	 */
	public class TreeItem<E> {
		private TreeItem<E> parent = null;
		private E value;
		private final List<TreeItem<E>> subTree = new ArrayList<TreeItem<E>>();

		/** Internal tree hash used to invalidate possible iterators */
		private volatile int treehash = 0;

		public TreeItem(E value, TreeItem<E> parent) {
			this.value = value;
			this.parent = parent;
		}

		/**
		 * Checks the own children and then the subtree if a given objects is in
		 * the tree or subtree Returns false if the given argument is null
		 * 
		 * @param e
		 *            item to be searched for
		 * @return true if existing, false if not existing or if e is null
		 */
		public synchronized boolean contains(E e) {
			if (e == null)
				return false;

			if (e.equals(value))
				return true;
			for (TreeItem<E> item : subTree) {
				if (item.contains(e))
					return true;
			}

			return false;
		}

		/**
		 * @return The number of concrete children of this tree item and of the
		 *         subtree items
		 */
		public synchronized int size() {
			int size = (value == null ? 0 : 1);
			for (TreeItem<E> current : subTree) {
				size += current.size();
			}
			return size;
		}

		/**
		 * Puts an item into this children set. If the item is null, nothing
		 * happens and null is been returned
		 * 
		 * @param item
		 *            to be put
		 * @return the tree item that has been added or null, if something went
		 *         wrong
		 */
		public synchronized TreeItem<E> put(E item) {
			if (item == null)
				return null;
			TreeItem<E> treeItem = get(item);
			if (treeItem != null) {
				treeItem.value = item;
			} else {
				treeItem = new TreeItem<E>(item, this);
				this.subTree.add(treeItem);
				treehash++;
			}
			return treeItem;
		}

		/**
		 * Gets the tree item of a given item, or null, if it has not been found
		 * 
		 * @param item
		 *            to be search for. If null, immediately null is returned
		 * @return the tree item of the given item, or null if it has not been
		 *         found
		 */
		public synchronized TreeItem<E> get(E item) {
			if (item == null)
				return null;

			if (item.equals(this.value))
				return this;
			for (TreeItem<E> e : subTree) {
				TreeItem<E> result = e.get(item);
				if (result != null)
					return result;
			}
			return null;
		}

		/**
		 * Searches the tree for an item by it's hash code
		 * 
		 * @param hash
		 * @return
		 */
		public TreeItem<E> getbyhash(int hash) {
			if (hash == hashCode())
				return this;
			for (TreeItem<E> e : subTree) {
				TreeItem<E> result = e.getbyhash(hash);
				if (result != null)
					return result;
			}
			return null;
		}

		@Override
		public boolean equals(Object obj) {
			if (obj == null)
				return false;
			if (obj instanceof TreeItem) {
				@SuppressWarnings("rawtypes")
				TreeItem item = (TreeItem) obj;
				if (item.value == null)
					return false;
				return item.value.equals(obj);
			}
			return obj.equals(value);
		}

		@Override
		public int hashCode() {
			if (value == null)
				return 0;
			return value.hashCode();
		}

		/**
		 * Creates an in-order iterator for the current tree-item and those
		 * subtree.
		 * 
		 * The iterator is invalidated, if the tree or it's subtree changes.
		 * 
		 * @return the newly created iterator
		 */
		public Iterator<TreeItem<E>> iterator() {
			return new Iterator<TreeItem<E>>() {

				/**
				 * Used to check tree state. If the hash changes, the iterator
				 * is invalidated
				 */
				private final int iteratorTreeHash = treehash;

				/** List of all tree items to be iterated */
				private final List<TreeItem<E>> iteratorList = TreeItem.this.flattenTree();

				@Override
				public synchronized boolean hasNext() {
					if (iteratorTreeHash != TreeItem.this.treehash)
						throw new IllegalStateException("Iterator has been invalidated");
					return iteratorList.size() > 0;
				}

				@Override
				public synchronized TreeItem<E> next() {
					if (!hasNext())
						return null;

					TreeItem<E> result = iteratorList.get(0);
					iteratorList.remove(0);
					return result;
				}

				@Override
				public void remove() {
					throw new UnsupportedOperationException();
				}

			};
		}

		/**
		 * Creates an in-order list of the current tree
		 * 
		 * @return in-order list of the current tree
		 */
		public synchronized List<TreeItem<E>> flattenTree() {
			List<TreeItem<E>> result = new ArrayList<TreeItem<E>>();

			result.add(this);
			for (TreeItem<E> child : subTree) {
				result.addAll(child.flattenTree());
			}

			return result;
		}
	}

	/**
	 * Puts a new item into the root. If the given item is null, nothing happens
	 * 
	 * @param item
	 *            to be put to
	 */
	public synchronized TreeItem<E> put(E item) {
		if (item == null)
			return null;
		return root.put(item);
	}

	/**
	 * Puts an item into the tree as a child of parent. If the parent does not
	 * apper in the tree, the item is added as a root item
	 * 
	 * @param item
	 *            To be added
	 * @param parent
	 *            Parent of the new item, or null, if it should be added as root
	 *            item
	 */
	public synchronized TreeItem<E> put(E item, E parent) {
		if (item == null)
			return null;
		TreeItem<E> root = this.root.get(parent);

		if (root == null)
			return put(item);
		else
			return root.put(item);
	}

	/**
	 * Gets an item given by it's hash code. Returns null if no such item was
	 * found
	 * 
	 * @param hash
	 *            hash code of the item to be searched for
	 * @return the found item, or null if not found
	 */
	public synchronized E get(int hash) {
		return root.getbyhash(hash).value;
	}

	/**
	 * Gets the parent tree item of a given value entry. If the given item is
	 * null, null is returned. If the item could not be found, null is returned
	 * 
	 * @param item
	 *            the parent of this value item is to be searched for
	 * @return The parent tree item or null, if the item is not found or null
	 */
	public synchronized TreeItem<E> getParent(E item) {
		if (item == null)
			return null;

		TreeItem<E> treeitem = root.get(item);
		if (treeitem == null)
			return null;
		return treeitem.parent;
	}

	/**
	 * @return the number of items in the tree
	 */
	public synchronized int size() {
		return root.size();
	}
}
