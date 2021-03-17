
import java.util.*;

@SuppressWarnings("rawtypes")
public class HashTable<K, D> {
    private final int minimumthreshold = 10;
    private final int getterresultsize = 2;
    private final int doublesize = 2;
    private final double lfactormax = 0.66667;

    protected class TableEntry<K, D> {
        private K key;
        private D data;

        public TableEntry(K key, D data) {
            this.key = key;
            this.data = data;
        }

        @Override
        public boolean equals(Object obj) {
            if ((obj == null) || !(obj instanceof TableEntry))
                return false;
            return key.equals(((TableEntry<?, ?>) obj).key);
        }

        @Override
        public int hashCode() {
            return key.hashCode();
        }
    }

    private LinkedList<TableEntry<K, D>>[] table;
    private int nElems;


    /**
     * constructor of HashTable
     *
     * @param capacity
     * @throws IllegalArgumentException when capacity less then minimumthreshold
     */
    @SuppressWarnings("unchecked")
    public HashTable(int capacity) {
        if (capacity < minimumthreshold) {
            // checks for the minimum of capacity
            throw new IllegalArgumentException();
        }
        this.table = new LinkedList[capacity];
    }

    /**
     * inserts items into HashTable, it only inserts if key doesnt already exists.
     * (in hashtable multiple keys can be hashed to one list.
     *
     * @param key used to get the hash code and the right table
     * @param data the data to be inserted with that hashcode
     * @return true if key is new and inserted to hashtable, false if key already exists
     * @throws NullPointerException throws exception when value is null
     */
    public boolean insert(K key, D data) {
        if (key == null || data == null) {
            //throws exception when value is null
            throw new NullPointerException();
        }
        if (loadfactor() > lfactormax) {
            //checks the load factor then rehash
            rehash();
        }

        int[] result = getter(key, data);
        TableEntry temp = new TableEntry<>(key, data);

        if (!(table[result[0]] instanceof LinkedList)) {
            // result[1] is -1 when LinkedList doesnt exists or its empty or key doesnt exists
            //its only empty when initialed before adding
            // if there is no linkedlist initialized
            this.table[result[0]] = new LinkedList<>();
        }

        if (lookup(key) == null) {
            // lookup returns null if key is not found
            table[result[0]].add(temp); //add temp to the linkedlist
            this.nElems++; // implements number of elements
            return true;
        }
        return false;

    }
    /**
     * updates the data of key into newdata
     *
     * @param key used to get the hash code and the right table
     * @param newData the data to be inserted with that hashcode
     * @throws NullPointerException throws exception when value is null
     */
    public boolean update(K key, D newData) {
        if (key == null || newData == null) {
            //throws exception when value is null
            throw new NullPointerException();
        }
        int[] result = getter(key, newData);
        if (result[1] == -1) {
            //if key is not found
            return false;
        }
        TableEntry temp = new TableEntry<>(key, newData);
        table[result[0]].set(result[1], temp);
        return true;
    }

    private double loadfactor() {
        return (double) size() / capacity();
    }

    /**
     * updates the data of key into newdata
     *
     * @param key used to get the hash code and the right table
     * @param newData the data to be inserted with that hashcode
     * @throws NullPointerException throws exception when value is null
     */
    private int[] getter(K key, D newData) {
        int hkey = hashValue(key); // hash key
        int[] result = new int[getterresultsize]; // creates a result with hashkey and index
        result[0] = hkey; //sets hash key as index 0 of result
        TableEntry temp = new TableEntry<>(key, newData); // temp node to find
        result[1] = -1; //index is -1 if index is empty or doesnt exists
        //its empty when in insert i initials it before adding to it
        if (table[result[0]] instanceof LinkedList) {
            // loops through the linked if exists
            for (int i = 0; i < table[hkey].size(); i++) {
                //loops through the getter to find the temp node
                if (table[hkey].get(i).equals(temp)) {
                    result[1] = i;
                    break;
                }
            }
        }
        // if linkedlist doesn't exists
        return result;
    }
    /**
     * deletes the node or the given key
     *
     * @param key the key to delete
     * @return boolean false if was found, and true if deleted
     * @throws NullPointerException when value is null
     */
    public boolean delete(K key) {
        if (key == null) {
            //throws exception when value is null
            throw new NullPointerException();
        }
        int[] result = getter(key, null);
        if (result[1] == -1) {
            //if key is not found
            return false;
        }
        table[result[0]].remove(result[1]);
        this.nElems--;
        return true;
    }

    /**
     * looks for the given key and returns the data
     *
     * @param key to look for
     * @return D the data
     * @return null if data not found
     * @throws NullPointerException when value is null
     */
    public D lookup(K key) {
        if (key == null) {
            //throws exception when value is null
            throw new NullPointerException();
        }
        int[] result = getter(key, null); //get the hashnum and index of key
        if (result[1] == -1) {
            //if index doesnt exists return null
            return null;
        }
        try {
            return (D) table[result[0]].get(result[1]).data;
            // return the data at that index and hash num
        } catch (NullPointerException e) {
            // return null if no data found
            return null;
        }
    }
    /**
     * returns the size
     * @return int size nelems instance
     *
     */
    public int size() {
        return nElems;
    }

    /**
     * returns the capacity
     *
     * @return int capacity (table.length)
     */
    public int capacity() {
        return table.length;
    }

    /**
     * calculates the hashvalue
     *
     * @param key
     */
    private int hashValue(K key) {
        return Math.abs(key.hashCode() % capacity());
    }
    /**
     * helper function to rehash
     */
    @SuppressWarnings("unchecked")
    private void rehash() {
        HashTable<K, D> temp = new HashTable(capacity() * doublesize);
        for (int i = 0; i < capacity(); i++) {
            // for every list in table
            if (table[i] instanceof LinkedList) { //checks if linkedlist exists
                for (int j = 0; j < table[i].size(); j++) {
                    // for every element in linkedlist
                    TableEntry tempn = table[i].get(j);
                    temp.insert((K) tempn.key, (D) tempn.data);
                }
            }

        }
        this.table = temp.table;
    }

}