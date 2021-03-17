import java.util.*;

@SuppressWarnings("rawtypes")
public class FADAF<K extends Comparable<? super K>, D> {

    private HashTable<K, DAFTree.DAFNode> htable;
    private DAFTree<K, D> dtree;


    /**
     * A constructor that initializes a FADAF structure
     * and all instances of backed data structures
     *
     * @param capacity the initial capacity for the hashtable
     * @throws IllegalArgumentException when capacity less then minimumthreshold
     */
    public FADAF(int capacity) {
        this.htable = new HashTable<>(capacity);
        this.dtree = new DAFTree<>();
    }

    /**
     *  returns the total number of unique keys stored in the FADAF
     *
     * @return int total number of unique keys
     */
    public int size() {
        return dtree.size();
    }

    /**
     * returns the total number of unique keys stored in the FADAF.
     *
     * @return int dtree.nUniqueKeys()
     */
    public int nUniqueKeys() {
        return dtree.nUniqueKeys();
    }

    /**
     * inserts key into tree and inserts the node into hash table
     * hashtable stores node base on key and data is the node
     * @param key the key to insert
     * @param data the data for the key
     * @param nCopy the amount of times to count
     * @return true if key is new and inserted to hashtable, false if key already exists
     * @throws NullPointerException key or data are null
     * @throws IllegalArgumentException when capacity less then minimumthreshold
     */
    public boolean insert(K key, D data, int nCopy) {
        if (key == null || data == null) {
            throw new NullPointerException();
        }
        if (nCopy < 1) {
            throw new IllegalArgumentException();
        }
        return htable.insert(key, dtree.insert(key, data, nCopy));
    }

    /**
     * looks for the DAFnode in the hash table and returns the count of that node
     *
     * @param key to look for in hash table
     * @return int the count of key
     * @return int 0 if key is not present
     * @throws NullPointerException key is null
     */
    public int lookup(K key) {
        if (key == null) {
            throw new NullPointerException();
        }
        try{
            return htable.lookup(key).count;
        } catch (NullPointerException e) {
            return 0;
        }
    }

    /**
     * removes the key from table ncopy times
     *
     * @param key the key to remove
     * @param nCopy the amount of times to remove
     * @throws IllegalArgumentException when ncopy is less then one
     * @throws NullPointerException key is null
     */
    public boolean remove(K key, int nCopy) {
        if (key == null) {
            throw new NullPointerException();
        }
        if (nCopy < 1) {
            throw new IllegalArgumentException();
        }
        if (dtree.remove(key, nCopy) == null) {
            // removes the key ncopy times
            return false;
        }
        // when i remove from ncopy, the hashtable data is connected to the
        // memory address of that node, so when I change the count or data,
        // its connected to the hashtable.

        //htable.lookup(key).count -= nCopy;
        if (htable.lookup(key).count <= 0) {
            return htable.delete(key);
        }
        return true;//(htable.delete(key) && dtree.remove(key, nCopy).key.equals(key));
    }

    /**
     * rovmoves the given key from both the tree and hashtable
     *
     * @param key to be removed
     * @throws NullPointerException key is null
     */
    public boolean removeAll(K key) {
        if (key == null) {
            throw new NullPointerException();
        }
        return (htable.delete(key) && dtree.removenode(key));

    }

    /**
     * A method that updates the data associated with the key to newData.
     *
     * @param key the key to be updated
     * @throws NullPointerException key or data is null
     */
    public boolean update(K key, D newData) {
        if (key == null || newData == null) {
            throw new NullPointerException();
        }
        try{
            return htable.update(key, dtree.updateData(key, newData));
        } catch (NullPointerException e ) {
            return false;
        }
    }

    /**
     * uses itterator to go through and return  the keys in accending order
     *
     * @param allowDuplicate if Duplicates are allowed
     * @return a linkedlist of all the keys
     */
    public List<K> getAllKeys(boolean allowDuplicate) {
        // loops thrugh the itterator to get all the keys.
        LinkedList<K> result = new LinkedList<>();
        Iterator<K> treeit = dtree.iterator();
        while (treeit.hasNext()){
            K temp = treeit.next();
            if (!allowDuplicate) {
                if (lookup(temp) == 1) {
                    result.add(temp);
                }
            } else {
                result.add(temp);
            }
        }
        return result;
    }

    /**
     * A method that returns a LinkedList of all unique keys
     * between lower (exclusive) and upper (exclusive).
     *
     * @param lower the lower parameter for keys to be returned
     * @param upper the upper parameter for keys to be returned
     * @return a linkedlist of all the keys
     */
    public List<K> getUniqueKeysInRange(K lower, K upper) {
        LinkedList<K> result = new LinkedList<>();
        Iterator<K> treeit = dtree.iterator();
        while (treeit.hasNext()){
            K temp = treeit.next();
            if (lookup(temp) == 1 && temp.compareTo(lower) > 0 && temp.compareTo(upper) < 0) {
                result.add(temp);
            }
        }
        return result;
    }
    /**
     * returns the minimum key
     * @return the minimum key
     * @return null if empty
     */
    public K getMinKey() {
        try {
            //uses DAFtree to get the key
            return dtree.findExtreme(false).key;
        } catch (NullPointerException e ) {
            return null;
        }
    }
    /**
     * returns the maximum key
     * @return the maximum key
     * @return null if empty
     */
    public K getMaxKey() {
        try{
            //uses DAFtree to get the key
            return dtree.findExtreme(true).key;
        } catch (NullPointerException e ) {
            return null;
        }


    }

}
