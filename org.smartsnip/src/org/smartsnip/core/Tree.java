package org.smartsnip.core;

import java.util.ArrayList;
import java.util.List;


/**
 * Implementation of a tree with an arbitary number of children per node.
 * The indexing of the tree happens with the hashCode of the generic type
 * E
 *
 * @param <E> Generic type of the children
 */
public class Tree<E> {
	
	/** Root item */
	private final TreeItem<E> root = new TreeItem<E>(null,null);
	
	/**
	 * An internal tree with an arbitary number of children
	 *
	 * @param <E>
	 */
	public class TreeItem<E> {
		private TreeItem<E> parent = null;
		private E value;
		private final List<TreeItem<E>> subTree = new ArrayList<TreeItem<E>>();
		
		public TreeItem(E value, TreeItem<E> parent) {
			this.value = value;
			this.parent = parent;
		}
		
		/**
		 * Checks the own children and then the subtree if a given objects is in the tree or subtree
		 * Returns false if the given argument is null
		 * 
		 * @param e item to be searched for
		 * @return true if existing, false if not existing or if e is null
		 */
		public synchronized  boolean contains(E e) {
			if (e == null)
				return false;

			
			if (e.equals(value)) return true;
			for (TreeItem<E> item : subTree) {
				if (item.contains(e))
					return true;
			}

			return false;
		}

		/**
		 * @return The number of concrete children of this tree item and of the subtree items
		 */
		public synchronized int size() {
			int size = (value==null?0:1);
			for (TreeItem<E> current : subTree) {
				size += current.size();
			}
			return size;
		}
		
		/**
		 * Puts an item into this children set. If the item is null, nothing happens and null is been returned
		 * @param item to be put
		 * @return the tree item that has been added or null, if something went wrong
		 */
		public synchronized TreeItem<E> put(E item) {
			if(item == null) return null;
			TreeItem<E> treeItem = get(item);
			if (treeItem != null) {
				treeItem.value = item;
			} else {
				treeItem = new TreeItem<E>(item, this);
				this.subTree.add(treeItem);
			}
			return treeItem;
		}
		
		/**
		 * Gets the tree item of a given item, or null, if it has not been found 
		 * @param item to be search for. If null, immediately null is returned
		 * @return the tree item of the given item, or null if it has not been found
		 */
		public synchronized TreeItem<E> get(E item) {
			if (item == null) return null;
			
			if (item.equals(this.value)) return this;
			for (TreeItem<E> e : subTree) {
				TreeItem<E> result = e.get(item);
				if (result != null) return result;
			}
			return null;
		}

		/**
		 * Searches the tree for an item by it's hash code
		 * @param hash
		 * @return
		 */
		public TreeItem<E> getbyhash(int hash) {
			if (hash == hashCode()) return this;
			for (TreeItem<E> e : subTree) {
				TreeItem<E> result = e.getbyhash(hash);
				if (result != null) return result;
			}
			return null;
		}
		
		@Override
		public boolean equals(Object obj) {
			if (obj == null) return false;
			if (obj instanceof TreeItem) {
				@SuppressWarnings("rawtypes")
				TreeItem item = (TreeItem) obj;
				if (item.value == null) return false;
				return item.value.equals(obj);
			}
			return obj.equals(value);
		}
		
		@Override
		public int hashCode() {
			if (value == null) return 0;
			return value.hashCode();
		}
	}
	
	/**
	 * Puts a new item into the root. If the given item is null, nothing happens
	 * @param item to be put to
	 */
	public synchronized TreeItem<E> put(E item) {
		if (item == null) return null;
		return root.put(item);
	}
	
	/**
	 * Puts an item into the tree as a child of parent.
	 * If the parent does not apper in the tree, the item is added as a root item
	 * 
	 * @param item To be added
	 * @param parent Parent of the new item, or null, if it should be added as root item
	 */
	public synchronized TreeItem<E> put(E item, E parent) {
		if (item == null) return null;
		TreeItem<E> root = this.root.get(parent);
		
		if (root == null) {
			return put(item);
		} else {
			return root.put(item);
		}
	}

	/**
	 * Gets an item given by it's hash code. Returns null if no such item was found
	 * @param hash hash code of the item to be searched for
	 * @return the found item, or null if not found
	 */
	public synchronized E get(int hash) {
		return root.getbyhash(hash).value;
	}
	
	/**
	 * @return the number of items in the tree
	 */
	public synchronized int size() {
		return root.size();
	}
}
