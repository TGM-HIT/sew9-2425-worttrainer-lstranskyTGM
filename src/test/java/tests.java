import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.util.Arrays;
import java.util.List;

/**
 * This is a test class for the project.
 * @author Leonhard Stransky
 * @version 2024-09-28
 */
public class tests {
    private WordPicturePair validPair1;
    private WordPicturePair validPair2;
    private WordPicturePair invalidPair;

    private SpellingTrainer trainer;
    private List<WordPicturePair> wordPairs;

    private Statistics stats;
    private JSONPersistence jsonPersistence;

    // Test setup before each test method
    @BeforeEach
    public void setUp() {
        validPair1 = new WordPicturePair("Cat", "http://example.com/cat.jpg");
        validPair2 = new WordPicturePair("Dog", "http://example.com/dog.jpg");
        wordPairs = Arrays.asList(validPair1, validPair2);

        jsonPersistence = new JSONPersistence();
        trainer = new SpellingTrainer(wordPairs, jsonPersistence);
        stats = new Statistics();
    }

    // WordPicturePair Tests

    @Test
    public void testWordPicturePairConstructor_Valid() {
        assertEquals("Cat", validPair1.getWord());
        assertEquals("http://example.com/cat.jpg", validPair1.getImageUrl());
    }

    @Test
    public void testWordPicturePairConstructor_InvalidUrl() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new WordPicturePair("Bird", "invalid-url");
        });
        assertEquals("The URL provided is invalid.", exception.getMessage());
    }

    @Test
    public void testWordPicturePairSetWord_Valid() {
        validPair1.setWord("Tiger");
        assertEquals("Tiger", validPair1.getWord());
    }

    @Test
    public void testWordPicturePairSetWord_Invalid() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            validPair1.setWord("");
        });
        assertEquals("Word cannot be null or empty.", exception.getMessage());
    }

    @Test
    public void testWordPicturePairSetImageUrl_Valid() {
        validPair1.setImageUrl("https://example.com/newcat.jpg");
        assertEquals("https://example.com/newcat.jpg", validPair1.getImageUrl());
    }

    @Test
    public void testWordPicturePairSetImageUrl_Invalid() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            validPair1.setImageUrl("invalid-url");
        });
        assertEquals("The URL provided is invalid.", exception.getMessage());
    }

    // SpellingTrainer Tests

    @Test
    public void testSpellingTrainerConstructor_Valid() {
        assertNotNull(trainer.getCurrentWordPair());
        assertNotNull(trainer.getStatistics());
    }

    @Test
    public void testSpellingTrainerConstructor_Invalid() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new SpellingTrainer(null, jsonPersistence);
        });
        assertEquals("No word pairs available.", exception.getMessage());
    }

    @Test
    public void testSelectRandomWordPair() {
        WordPicturePair currentPair = trainer.getCurrentWordPair();
        trainer.selectRandomWordPair();
        WordPicturePair newPair = trainer.getCurrentWordPair();
        // There’s a chance they might be the same due to randomness, but the test should ensure it doesn't crash
        assertNotNull(newPair);
    }

    @Test
    public void testGuessWord_Correct() {
        trainer.selectRandomWordPair(); // Reset the current word pair to a random one
        WordPicturePair currentPair = trainer.getCurrentWordPair();
        assertTrue(trainer.guessWord(currentPair.getWord())); // Should be correct
        assertEquals(Boolean.TRUE, trainer.getLastResult());
    }

    @Test
    public void testGuessWord_Incorrect() {
        trainer.selectRandomWordPair(); // Reset the current word pair to a random one
        assertFalse(trainer.guessWord("IncorrectGuess")); // Should be incorrect
        assertEquals(Boolean.FALSE, trainer.getLastResult());
    }

    @Test
    public void testGuessWord_Invalid() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            trainer.guessWord(null);
        });
        assertEquals("Guess cannot be null or empty.", exception.getMessage());
    }

    // Statistics Tests

    @Test
    public void testStatisticsCorrectGuesses() {
        stats.incrementCorrectGuesses();
        assertEquals(1, stats.getCorrectGuesses());
        assertEquals(1, stats.getTotalGuesses());
    }

    @Test
    public void testStatisticsIncorrectGuesses() {
        stats.incrementIncorrectGuesses();
        assertEquals(1, stats.getIncorrectGuesses());
        assertEquals(1, stats.getTotalGuesses());
    }

    @Test
    public void testStatisticsAccuracy_Correct() {
        stats.incrementCorrectGuesses();
        assertEquals(100.0, stats.getAccuracy());
    }

    @Test
    public void testStatisticsAccuracy_Mixed() {
        stats.incrementCorrectGuesses();
        stats.incrementIncorrectGuesses();
        assertEquals(50.0, stats.getAccuracy());
    }

    @Test
    public void testStatisticsAccuracy_NoGuesses() {
        assertEquals(0.0, stats.getAccuracy());
    }

    @Test
    public void testStatisticsReset() {
        stats.incrementCorrectGuesses();
        stats.incrementIncorrectGuesses();
        stats.reset();
        assertEquals(0, stats.getCorrectGuesses());
        assertEquals(0, stats.getIncorrectGuesses());
        assertEquals(0, stats.getTotalGuesses());
    }

    // JSONPersistence Tests

    @Test
    public void testJSONPersistenceSaveData() {
        JSONPersistence jsonPersistence = new JSONPersistence();
        trainer = new SpellingTrainer(wordPairs, jsonPersistence);

        jsonPersistence.saveData(trainer);

        // Check that the file exists and is not empty
        // File operations are environment dependent, so this test should be more thorough with integration tests
        File file = new File("spelling_trainer_data.json");
        assertTrue(file.exists());
        assertTrue(file.length() > 0);
    }

    @Test
    public void testJSONPersistenceLoadData() {
        JSONPersistence jsonPersistence = new JSONPersistence();
        trainer = new SpellingTrainer(wordPairs, jsonPersistence);

        // Save the trainer
        jsonPersistence.saveData(trainer);

        // Now load the trainer from the file
        SpellingTrainer loadedTrainer = jsonPersistence.loadData();
        assertNotNull(loadedTrainer);
        assertEquals(trainer.getCurrentWordPair().getWord(), loadedTrainer.getCurrentWordPair().getWord());
    }
}
