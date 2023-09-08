// creates the interface for the HashMap
public interface KWHashMap {
    V get(Object key);

    V put(K key, V value);

    V remove(Object key);

    int size();

    boolean isEmpty();
}
