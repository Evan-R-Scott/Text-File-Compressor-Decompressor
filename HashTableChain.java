import java.util.*;
public class HashTableChain<K,V> implements KWHashMap<K,V> {
    // creates constructor class that gets key and value and can set value to a key

    private static class Entry<K,V> {
        private final K key;
        private V value;
        public Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }
        public K getKey() {
            return key;
        }
        public V getValue() {
            return value;
        }
        public V setValue(V val) {
            V oldVal = value;
            value = val;
            return oldVal;
        }
        public String toString() {
            return key.toString() + " : " + value.toString();
        }
    }

    // intialize linkedlist aspect of hash table (makes it a chain)
    private LinkedList<Entry<K,V>>[] table;
    private int numKeys = 0;
    private int numRehashes = 0;

    // create load threshold and capacity variables
    private static final double LOAD_THRESHOLD = 0.75;
    private static final int Capacity = 233;

    // give linkedlist length from capacity variable
    public HashTableChain(int Capacity) {
        table = new LinkedList[Capacity];
    }

    // get key from hashcode % length of table, basically finds spots for the keys in the hash table that are empty (rehashes if not)
    public V get(Object key) {
        int ind = key.hashCode() % table.length;
        if (ind < 0) {
            ind += table.length;
            }
        if (table[ind] == null) {
            return null;
        }
        for (Entry<K,V> next : table[ind]) {
            if (next.getKey().equals(key)) {
                return next.getValue();
            }
        }
        return null;
    }

    // same as get but instead puts the values into the hash table where there is space following the guidelines provided in index
    public V put(K key, V value) {
        int index = key.hashCode() % table.length;
        double load_factor = 0;
        if (index < 0) {
            index += table.length;
        }
        if (table[index] == null) {
            table[index] = new LinkedList<>();
        }
        for (Entry<K,V> next : table[index]) {
            if (next.getKey().equals(key)) {
                V oldVal = next.getValue();
                next.setValue(value);
                return oldVal;
            }
        }
        table[index].addFirst(new Entry<>(key, value));
        numKeys++;
        load_factor = (double)numKeys / table.length;
        if (load_factor > LOAD_THRESHOLD) {
            rehash();
        }
        return null;
    }

    // removes keys when prompted by searching for hashcode and lowering numKeys and using iter.remove()
    public V remove(Object key) {
        int index = key.hashCode() % table.length;
        if (index < 0) {
            index += table.length;
        }
        if (table[index] == null) {
            table[index] = new LinkedList<>();
        }
        Iterator<Entry<K,V>> iter = table[index].iterator();
        while (iter.hasNext()) {
            Entry<K,V> current = iter.next();
            if (current.getKey().equals(key)) {
                iter.remove();
                numKeys--;
                if (table[index].isEmpty()) {
                    table[index] = null;
                }
                numKeys--;
                return current.getValue();
            }
        }
        return null;
    }

    // gets the amount of keys in hash table
    public int size() {
        return numKeys;
    }

    // checks if empty
    public boolean isEmpty() {
        return numKeys == 0;
    }

    // checks if table_length is prime
    public boolean isPrime(int table_length) {
        if (table_length <= 1) {
            return false;
        }
        for (int i = 2; i <= Math.sqrt(table_length); i++) {
            if (table_length % i == 0) {
                return false;
            }
        }
        return true;
    }

    // rehash method that will rehash the table if necessary (new table legnth is found by multiplying by two and adding one)
    private void rehash() {
        LinkedList<Entry<K,V>>[] oldTable = table;
        int table_length = 2 * oldTable.length + 1;
        while(!isPrime(table_length)) {
            table_length += 2;
        }
        table = new LinkedList[table_length];
        numKeys = 0;
        for(int i = 0; i < oldTable.length; i++) {
            if(oldTable[i] != null) {
                for(Entry<K,V> nextItem : oldTable[i]) {
                    put(nextItem.getKey(), nextItem.getValue());
                }
            }
        }
        numRehashes++;
    }

    // returns the amount of times rehash was called from counter variable numRehashes in rehash()
    public int getRehashes(){
        return numRehashes;
    }
}
