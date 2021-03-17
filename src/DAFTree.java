
import java.util.*;

@SuppressWarnings("rawtypes")
public class DAFTree<K extends Comparable<? super K>, D> implements Iterable {

    private DAFNode<K, D> root;
    private int nElems;
    private LinkedList<K> unielems = new LinkedList<>(); //list to keep track of uniqe keys

    protected class DAFNode<K extends Comparable<? super K>, D> {
        K key;
        D data;
        int count; // duplicate counter
        DAFNode<K, D> left, right;

        /**
         * constructor of DAFNode
         *
         * @param key key to insert
         * @param data to insert in that key
         * @throws NullPointerException if data or key are null
         */
        public DAFNode(K key, D data) {
            if (key == null || data == null) {
                throw new NullPointerException();
            }
            this.key = key;
            this.data = data;
            this.count = 1;
            this.left = null;
            this.right = null;
        }

        /**
         * constructor of DAFNode
         *
         * @param key key to insert
         * @param data to insert in that key
         * @throws NullPointerException if data or key are null
         * @throws IllegalArgumentException ncopy is less than 1
         */
        public DAFNode(K key, D data, int nCopy) {
            if (key == null || data == null) {
                throw new NullPointerException();
            }
            if (nCopy < 1) {
                throw new IllegalArgumentException();
            }
            this.key = key;
            this.data = data;
            this.count = nCopy;
            this.left = null;
            this.right = null;
        }
    }

    /**
     * constructor of DAFTree
     *
     */
    public DAFTree() {
        this.root = null;
        this.nElems = 0;
    }

    /**
     * helper method to get the node from the key
     * return the node of the keys are the same
     * or returns the last node the parent
     * @param key to look for
     * @return the node
     *
     */
    private DAFNode getnode(K key) {
        DAFNode temp = root; //sets the root
        while (temp != null) {
            //goes down the tree from the root
            if (temp.key.compareTo(key) == 0) {
                //when finds the node
                return temp;
            } else if (temp.key.compareTo(key) < 0) {
                // to go down the right child
                if (temp.right == null) {
                    return temp;
                }
                temp = temp.right;
            } else if (temp.key.compareTo(key) > 0) {
                // to go down the left child
                if (temp.left == null) {
                    return temp;
                }
                temp = temp.left;
            }
        }
        return temp;
    }

    /**
     * returns the number of keys
     * @return nElems
     */
    public int size() {
        return nElems;
    }

    /**
     * returns the total number of unique keys stored in the FADAF.
     *
     * @return int
     */
    public int nUniqueKeys() {
        return unielems.size();
    }

    /**
     * inserts key to the tree
     *
     * @param key the key to insert
     * @param data the data for the key
     * @param nCopy the amount of times to count
     * @return DAFNode<K, D> the node that was inserted or if already exists
     * @throws NullPointerException key or data are null
     * @throws IllegalArgumentException ncopy is less than 1
     */
    public DAFNode<K, D> insert(K key, D data, int nCopy) {
        if (key == null || data == null) {
            throw new NullPointerException();
        }
        if (nCopy < 1) {
            throw new IllegalArgumentException();
        }
        DAFNode temp = getnode(key); // returns the node or null if it doesnt exists
        DAFNode newnode = new DAFNode(key, data, nCopy); //creates a new node
        if (root == null) { // if tree is empty sets it as root
            // temp is only null when the root is null
            this.root = newnode;
        } else if (temp.key.compareTo(key) == 0) {
            unielems.remove(key);
            //if the key already exsits it implements the count
            temp.count += nCopy;
            this.nElems += nCopy;
            return temp;
        } else if (temp.key.compareTo(key) < 0) {
            //parent key is smaller than key
            temp.right = newnode; // set to right child
        } else if (temp.key.compareTo(key) > 0) {
            //parent key is bigger than key
            temp.left = newnode; // set to left child
        }
        if (nCopy == 1) { // if ncpy is 1 adds to unique key list
            unielems.add(key);
        }
        this.nElems += nCopy;
        return newnode;
    }
    /**
     * inserts the given key to the tree nCopy times
     *
     * @param key the key to insert
     * @param nCopy the amount of times to count
     * @return DAFNode<K, D> the node that already exists
     * @return null if key is not found
     * @throws NullPointerException key or data are null
     * @throws IllegalArgumentException ncopy is less than 1
     */
    public DAFNode<K, D> insertDuplicate(K key, int nCopy) {
        if (key == null) {
            throw new NullPointerException();
        }
        if (nCopy < 1) {
            throw new IllegalArgumentException();
        }
        DAFNode temp = getnode(key);
        if (temp.key.compareTo(key) == 0) {
            //if the key already exsits it implements the count
            temp.count += nCopy;
            this.nElems += nCopy;
            return temp;
        }
        //returns null if data is not the same or temp is null
        return null;
    }

    /**
     * A method that checks if a node with the given key is stored in the tree
     *
     * @param key the key to insert
     * @return DAFNode<K, D> the node that already exists
     * @return null if key is not found
     * @throws NullPointerException key  are null
     */
    public DAFNode<K, D> lookup(K key) {
        if (key == null) {
            throw new NullPointerException();
        }
        DAFNode temp = getnode(key); // find the noe
        if (temp == null) {
            return null;
        } else if (temp.key.compareTo(key) == 0) {
            // checks if keys are the same
            return temp;
        }
        return null;
    }

    /**
     * updates the data associated with the given key to newData.
     * This method returns the node instance if the update is successful,
     * or null if the key is not found.
     *
     * @param key the key to update
     * @param newData the data for the key
     * @return DAFNode<K, D> the node that already exists
     * @return null if key is not found
     * @throws NullPointerException key or data are null
     */
    public DAFNode<K, D> updateData(K key, D newData) {
        if (key == null || newData == null) {
            throw new NullPointerException();
        }
        DAFNode temp = getnode(key); // finds the noe
        if (temp == null) {
            return null;
        } else if (temp.key.compareTo(key) == 0) {
            //checks if key are the same
           temp.data = newData; // updates the data
           return temp;
        }
        return null;
    }

    /**
     * A method that removes the node with the given key from the tree nCopy times.
     *
     * @param key the key to remove
     * @param nCopy the amount of times to remove
     * @return DAFNode<K, D> the node that already exists
     * @return null if key is not found
     * @throws IllegalArgumentException when ncopy is less then one
     * @throws NullPointerException key or data are null
     */
    public DAFNode<K, D> remove(K key, int nCopy) {
        if (key == null) {
            throw new NullPointerException();
        }
        if (nCopy < 1) {
            throw new IllegalArgumentException();
        }
        DAFNode temp = getnode(key); // finds the node
        if (temp == null) {
            return null;
        } else if (temp.key.compareTo(key) == 0) {
            // checks if the keys are the same
            temp.count -= nCopy;
            this.nElems -= nCopy;
            if (temp.count == 1) { //if count is 1 add to uniqe keys list
                unielems.add(key);
            } else if (temp.count <= 0) { // if ncopy is 0 remove the whole node
                this.nElems -= temp.count;
                unielems.remove(key);
                removenode((K) temp.key);
            }
            return temp;
        }
        // returns null when data not found
        return null;
    }

    /**
     * removes the whole node from the tree
     *
     * @param key the key to remove
     * @return true if it was removed false otherwise
     * @throws NullPointerException key or data are null
     */
    public boolean removenode(K key) {
        DAFNode<K, D> par = null;
        DAFNode<K, D> cur = root;
        while (cur != null) { // Search for node
            if (cur.key.compareTo(key) == 0) { // Node found
                if (cur.left == null && cur.right == null) { // Remove leaf
                    if (par == null) {// Node is root
                        this.root = null; // empties the tree
                        return true;
                    } else if (par.left != null && par.left.key.compareTo(cur.key) == 0) {
                        // if cur is left child of par node
                        par.left = null; // par node left is null
                        return true;
                    } else { // if cur is right child of par node
                        par.right = null; // par node left is null
                        return true;
                    }
                } else if (cur.right == null) { // Remove node with only left child
                    if (par == null){
                        this.root = cur.left; // Node is root
                        return true;
                    } else if (par.left != null && par.left.key.compareTo(cur.key) == 0) {
                        // if cur is left child of par node
                        par.left = cur.left; // par node left is null
                        return true;
                    } else{ // if cur is right child of par node
                        par.right = cur.left; // par node left is null
                        return true;
                    }
                } else if (cur.left == null) {    // Remove node with only right child
                    if (par == null){// Node is root
                        this.root = cur.right;
                        return true;
                    } else if (par.left != null && par.left.key.compareTo(cur.key) == 0){
                        // if node is the left child
                        par.left = cur.right;
                        return true;
                    } else { // if cur is right child of par node
                        par.right = cur.right; // par node left is null
                        return true;
                    }
                } else {  // Remove node with two children
                    // Find successor (leftmost child of right subtree)
                    DAFNode<K, D> suc = cur.right;
                    DAFNode<K, D> sucpar = cur;
                    while (suc.left != null) {
                        // loops through the leftmost child of right subtree
                        sucpar = suc;
                        suc = suc.left;
                    }
                    if (sucpar.left != null && sucpar.left.key.compareTo(suc.key) == 0) {
                        sucpar.left = suc.right;
                        suc.right = cur.right;
                    } else {
                        sucpar.right = null;
                    }
                    suc.left = cur.left;
                    if (par == null) { // Node is root
                        this.root = suc;
                        return true;
                    } else if (par.left != null && par.left.key.compareTo(cur.key) == 0) {
                        // if node is the left child
                        par.left = suc;
                        return true;
                    } else { // if cur is right child of par node
                        par.right = suc; // par node left is null
                        return true;
                    }

                }
            } else if (cur.key.compareTo(key) < 0) { // Search right
                par = cur;
                cur = cur.right;
            } else {        // Search left
                par = cur;
                cur = cur.left;
            }
        }
        return false;// Node not found
    }

    /**
     * A method that finds the node with the most extreme key. If the tree is empty, return null.
     *
     * @param isMax If isMax is true return the max node; otherwise,return the min.
     * @return min or max node
     * @return null if tree is empty
     */
    public DAFNode<K, D> findExtreme(boolean isMax) {
        return getmaxmin(root, isMax);
    }

    /**
     * helper method to return max or min of that node also used in removenode
     * @param ismax If isMax is true return the max node; otherwise,return the min.
     * @return min or max node
     * @return null if tree is empty
     */
    private DAFNode<K, D> getmaxmin(DAFNode node, boolean ismax) {
        if (node == null) {
            return null;
        }
        DAFNode temp = node;
        if (ismax) { // if looking for max
            while (temp.right != null) {
                // loops throug the right most node
                temp = temp.right;
            }
            return temp;
        } else {
            while (temp.left != null) {
                // loops through the left most node
                temp = temp.left;
            }
            return temp;
        }
    }
    /**
     * DAFTreeIterator
     */
    public class DAFTreeIterator implements Iterator<K> {
        DAFNode temp = root;
        Stack<DAFNode<K, D>> stack;
        int parr = 0;
        K multi;

        /**
         * A constructor that initializes a DAFTree iterator that iterates
         * through all keys in the tree (including duplicates).
         *
         */
        public DAFTreeIterator() {
            this.stack = new Stack<>();
            if (temp == null) {
                return;
            }

            this.stack.push(temp);
            while (temp.left != null) {
                temp = temp.left;
                this.stack.push(temp);
            }
        }
        /**
         * A method that checks if the iterator has more elements to return.
         * This method returns true if there are more elements, and false otherwise.
         *
         * @return  bolean true if there is more element to return
         */
        public boolean hasNext() {
            return (!stack.isEmpty() || parr > 0);
        }
        /**
         * returns the next element in stack
         *
         * @return the next key on the stack
         */
        public K next() {

            if (parr > 0) {
                this.parr--;
              //  //this.temp = stack.peek();
                return multi;
            }

            this.temp = this.stack.pop();
            if (temp.count > 1) {
                this.parr = temp.count - 1;
                this.multi = (K) temp.key;
            }
            if (temp == null) {
                throw new NoSuchElementException();
            }

            K key = (K) temp.key;
            if (!(temp.right == null)) {
                this.stack.push(temp.right);
                temp = temp.right;
                while (!(temp.left == null)) {
                    temp = temp.left;
                    this.stack.push(temp);
                }
            }
            return key;
        }
    }
    /**
     * A method that returns a new DAFTree iterator instance.
     *
     */
    public Iterator<K> iterator() {
        return new DAFTreeIterator();
    }

}
