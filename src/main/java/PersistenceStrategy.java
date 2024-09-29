/**
 * Interface defining methods for saving and loading objects to/from a file or storage.
 * @param <T> The type of the object to persist.
 * @author Leonhard Stransky
 * @version 2024-09-29
 */
public interface PersistenceStrategy<T> {

    /**
     * Saves the given object to a file or storage.
     * @param object The object to be saved.
     */
    void saveData(T object);

    /**
     * Loads the object from a file or storage.
     * @return The loaded object.
     */
    T loadData();

}
