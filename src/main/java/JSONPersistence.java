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

    private static final String FILE_PATH = "spelling_trainer_data.json";
    private Gson gson;

    /**
     * Constructor for the JSONPersistence class.
     */
    public JSONPersistence() {
        this.gson = new Gson();
    }

    /**
     * Saves the SpellingTrainer object to a JSON file.
     * @param trainer The SpellingTrainer object to be saved.
     */
    @Override
    public void saveData(SpellingTrainer trainer) {
        try (FileWriter writer = new FileWriter(FILE_PATH)) {
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
        try (FileReader reader = new FileReader(FILE_PATH)) {
            return gson.fromJson(reader, SpellingTrainer.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
