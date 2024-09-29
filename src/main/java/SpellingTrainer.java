import java.util.List;
import java.util.Random;

/**
 * Class representing a spelling trainer that uses word-picture pairs to train spelling.
 * @author Leonhard Stransky
 * @version 2024-09-28
 */
public class SpellingTrainer {
    private List<WordPicturePair> wordPairs;
    private WordPicturePair currentWordPair;
    private Statistics statistics;
    private Boolean lastResult; // Boolean to store true, false or null if no guess was made
    private transient PersistenceStrategy<SpellingTrainer> persistenceStrategy; // transient keyword to exclude from serialization

    // Constructors

    /**
     * Constructor for a SpellingTrainer.
     * @param wordPairs The list of word-picture pairs.
     * @param persistenceStrategy The strategy for saving/loading the SpellingTrainer object.
     * @throws IllegalArgumentException if the wordPairs list is null or empty.
     */
    public SpellingTrainer(List<WordPicturePair> wordPairs, PersistenceStrategy<SpellingTrainer> persistenceStrategy) {
        if (wordPairs == null || wordPairs.isEmpty()) {
            throw new IllegalArgumentException("No word pairs available.");
        }
        this.wordPairs = wordPairs;
        this.statistics = new Statistics();
        this.selectRandomWordPair(); // Select an initial word pair at startup
        this.lastResult = null;  // No guess made initially
        this.persistenceStrategy = persistenceStrategy; // Strategy injected
    }

    // Getters and Setters

    public WordPicturePair getCurrentWordPair() {
        return this.currentWordPair;
    }

    public Statistics getStatistics() {
        return this.statistics;
    }

    public Boolean getLastResult() {
        return this.lastResult;
    }

    public void setPersistenceStrategy(PersistenceStrategy<SpellingTrainer> persistenceStrategy) {
        this.persistenceStrategy = persistenceStrategy;
    }

    // Methods

    /**
     * Selects a random word-picture pair from the list of word pairs.
     */
    public void selectRandomWordPair() {
        Random random = new Random();
        this.currentWordPair = this.wordPairs.get(random.nextInt(this.wordPairs.size()));
        this.lastResult = null; // Reset last result since a new pair is selected
    }

    /**
     * Guesses the word of the current word-picture pair.
     * @param guess The word to guess.
     * @return true if the guess is correct, false otherwise.
     */
    public boolean guessWord(String guess) {
        if (guess == null || guess.trim().isEmpty()) {
            throw new IllegalArgumentException("Guess cannot be null or empty.");
        }

        this.lastResult = guess.equals(this.currentWordPair.getWord());
        if(this.lastResult) {
            this.statistics.incrementCorrectGuesses();
        } else {
            this.statistics.incrementIncorrectGuesses();
        }
        return this.lastResult;
    }

    /**
     * Saves the SpellingTrainer object using the current persistence strategy.
     */
    public void persistData() {
        this.persistenceStrategy.saveData(this);
    }

    /**
     * Loads the SpellingTrainer object using the current persistence strategy.
     * @return The loaded SpellingTrainer object.
     */
    public SpellingTrainer loadData(PersistenceStrategy<SpellingTrainer> persistenceStrategy) {
        return persistenceStrategy.loadData();
    }

    /**
     * Returns a string representation of the SpellingTrainer.
     * @return The string representation.
     */
    @Override
    public String toString() {
        return "Current Word Pair: " + this.currentWordPair + "\n" +
                "Statistics: " + this.statistics.toString() + "\n" +
                "Last Result: " + this.lastResult;
    }
}
