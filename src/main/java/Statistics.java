/**
 * This class represents the statistics of a spelling trainer.
 * @author Leonhard Stransky
 * @version 2024-09-28
 */
public class Statistics {
    private int correctGuesses;
    private int incorrectGuesses;
    private int totalGuesses;

    // Constructors

    /**
     * Constructor for a Statistics object.
     */
    public Statistics() {
        this.correctGuesses = 0;
        this.incorrectGuesses = 0;
        this.totalGuesses = 0;
    }

    // Getters

    public int getCorrectGuesses() {
        return this.correctGuesses;
    }

    public int getIncorrectGuesses() {
        return this.incorrectGuesses;
    }

    public int getTotalGuesses() {
        return this.totalGuesses;
    }

    // Methods

    /**
     * Increments the correct guesses count and updates the total guesses.
     */
    public void incrementCorrectGuesses() {
        this.correctGuesses++;
        this.totalGuesses++;
    }

    /**
     * Increments the incorrect guesses count and updates the total guesses.
     */
    public void incrementIncorrectGuesses() {
        this.incorrectGuesses++;
        this.totalGuesses++;
    }

    /**
     * Resets all the statistics (correct, incorrect, and total guesses).
     */
    public void reset() {
        this.correctGuesses = 0;
        this.incorrectGuesses = 0;
        this.totalGuesses = 0;
    }

    /**
     * Calculates the accuracy as a percentage of correct guesses.
     * @return The accuracy as a percentage, or 0 if no guesses have been made.
     */
    public double getAccuracy() {
        // Avoid division by zero
        if (this.totalGuesses == 0) {
            return 0;
        }
        return (double) this.correctGuesses / this.totalGuesses * 100;
    }

    /**
     * Returns a string representation of the statistics.
     * @return The string representation.
     */
    @Override
    public String toString() {
        return "Correct Guesses: " + this.correctGuesses + "\n" +
                "Incorrect Guesses: " + this.incorrectGuesses + "\n" +
                "Total Guesses: " + this.totalGuesses + "\n" +
                "Accuracy: " + String.format("%.2f", getAccuracy()) + "%";
    }
}
