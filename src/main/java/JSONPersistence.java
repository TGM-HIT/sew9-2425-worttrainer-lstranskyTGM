import com.google.gson.Gson;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Class responsible for saving and loading SpellingTrainer objects to/from a JSON file.
 * @author Leonhard Stransky
 * @version 2024-09-29
 */
public class JSONPersistence implements PersistenceStrategy<SpellingTrainer> {
    private String filePath;
    private Gson gson;

    /**
     * Constructor for the JSONPersistence class.
     */
    public JSONPersistence(String filePath) {
        this.gson = new Gson();
        this.filePath = filePath;
    }

    // Getters and Setters

    /**
     * Sets a new file path dynamically.
     * @param filePath The new file path for saving/loading.
     */
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    // Methods

    /**
     * Saves the SpellingTrainer object to a JSON file.
     * @param trainer The SpellingTrainer object to be saved.
     */
    @Override
    public void saveData(SpellingTrainer trainer) {
        try (FileWriter writer = new FileWriter(this.filePath)) {
            gson.toJson(trainer, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads the SpellingTrainer object from a JSON file.
     * @return The loaded SpellingTrainer object.
     */
    @Override
    public SpellingTrainer loadData() {
        try (FileReader reader = new FileReader(this.filePath)) {
            SpellingTrainer trainer = gson.fromJson(reader, SpellingTrainer.class);
            if (trainer != null) {
                // Automatically inject the persistence strategy after loading
                trainer.setPersistenceStrategy(this);
            }
            return trainer;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
