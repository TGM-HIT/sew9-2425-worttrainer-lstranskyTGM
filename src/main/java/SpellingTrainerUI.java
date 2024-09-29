import javax.swing.*;
import java.awt.Image;
import java.util.List;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Class responsible for the graphical interface of the SpellingTrainer using JOptionPane.
 * @author Leonhard Stransky
 * @version 2024-09-29
 */
public class SpellingTrainerUI {
    private SpellingTrainer trainer;
    private boolean running;

    /**
     * Constructor for the SpellingTrainerUI class.
     * @param trainer The spelling trainer to be used in the UI.
     */
    public SpellingTrainerUI(SpellingTrainer trainer) {
        this.trainer = trainer;
        this.running = true;
    }

    /**
     * Starts the UI and allows interaction with the spelling trainer.
     */
    public void start() {
        while (running) {
            showCurrentWordPair();
            String guess = getUserGuess();

            if (guess == null || guess.trim().isEmpty()) {
                int confirm = JOptionPane.showConfirmDialog(null, "Do you want to exit the trainer?",
                        "Exit Confirmation", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    exitTrainer();
                    break;
                }
            } else {
                boolean correct = trainer.guessWord(guess.trim());
                showResult(correct);

                // If the guess is correct, select a new word-pair for the next round
                if (correct) {
                    trainer.selectRandomWordPair();
                }
            }

            showStatistics();
            trainer.persistData();  // Save progress after each interaction
        }
    }

    /**
     * Shows the current word-picture pair with an image (if available).
     */
    private void showCurrentWordPair() {
        WordPicturePair pair = trainer.getCurrentWordPair();
        String message = "Can you guess the word for the following picture?";
        String imageUrl = pair.getImageUrl();

        try {
            URL url = new URL(imageUrl);
            ImageIcon imageIcon = new ImageIcon(url);

            // Check if the image was loaded successfully
            if (imageIcon.getIconWidth() == -1 || imageIcon.getIconHeight() == -1) {
                // The image failed to load
                JOptionPane.showMessageDialog(null, "Failed to load image. URL might be invalid.", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                // Get original dimensions
                int originalWidth = imageIcon.getIconWidth();
                int originalHeight = imageIcon.getIconHeight();

                // Desired size while keeping aspect ratio
                int targetWidth = 300;
                int targetHeight = (int) (originalHeight * ((double) targetWidth / originalWidth)); // Maintain aspect ratio

                // Resize the image
                Image scaledImage = imageIcon.getImage().getScaledInstance(targetWidth, targetHeight, Image.SCALE_SMOOTH);
                imageIcon = new ImageIcon(scaledImage); // Back to ImageIcon

                // Set the message and icon in a JLabel
                JLabel label = new JLabel(message, imageIcon, JLabel.CENTER);
                label.setVerticalTextPosition(JLabel.TOP); // Place text above the image
                label.setHorizontalTextPosition(JLabel.CENTER); // Center the image and text
                label.setIconTextGap(10); // Set some gap between the text and the image

                // Display in a message dialog
                JOptionPane.showMessageDialog(null, label, "Guess the Word", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (MalformedURLException e) {
            JOptionPane.showMessageDialog(null, "Error: Invalid image URL!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Prompts the user to enter their guess for the word.
     * @return The user's input.
     */
    private String getUserGuess() {
        return JOptionPane.showInputDialog(null, "Enter your guess:");
    }

    /**
     * Displays whether the guess was correct or incorrect.
     * @param correct True if the guess was correct, false otherwise.
     */
    private void showResult(boolean correct) {
        if (correct) {
            JOptionPane.showMessageDialog(null, "Correct! Well done!", "Result", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "Incorrect. Try again!", "Result", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Displays the current statistics (total guesses, correct guesses, incorrect guesses, accuracy).
     */
    private void showStatistics() {
        Statistics stats = trainer.getStatistics();
        Boolean lastResult = trainer.getLastResult();
        String resultMessage = (lastResult == null) ? "No guess made yet" :
                (lastResult ? "Last guess was correct" : "Last guess was incorrect");

        String statisticsMessage = "Statistics:\n" +
                "Correct Guesses: " + stats.getCorrectGuesses() + "\n" +
                "Incorrect Guesses: " + stats.getIncorrectGuesses() + "\n" +
                "Total Guesses: " + stats.getTotalGuesses() + "\n" +
                "Accuracy: " + String.format("%.2f", stats.getAccuracy()) + "%\n" +
                resultMessage;

        JOptionPane.showMessageDialog(null, statisticsMessage, "Your Statistics", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Closes the trainer and stops the UI loop.
     */
    private void exitTrainer() {
        trainer.persistData();  // Save final state before exit
        JOptionPane.showMessageDialog(null, "Trainer exited. Goodbye!", "Exit", JOptionPane.INFORMATION_MESSAGE);
        running = false;
    }

    /**
     * Main method to start the spelling trainer UI.
     * @param args Command-line arguments (unused).
     */
    public static void main(String[] args) {
        // Example word-picture pairs
        List<WordPicturePair> wordPairs = List.of(
                new WordPicturePair("Bird", "https://example.com/bird.jpg"),
                new WordPicturePair("Cat", "https://example.com/cat.jpg"),
                new WordPicturePair("Car", "https://example.com/car.jpg")
        );

        // Use JSON persistence strategy
        String filePath = "spelling_trainer_data.json"; // Change manually or dynamically by user input or config
        JSONPersistence persistenceStrategy = new JSONPersistence(filePath);

        // Load existing trainer data or create a new one
        SpellingTrainer trainer = persistenceStrategy.loadData();
        if (trainer == null) {
            trainer = new SpellingTrainer(wordPairs, persistenceStrategy);
        } else {
            System.out.println("Loaded existing trainer data.");
        }

        // Start the UI
        SpellingTrainerUI ui = new SpellingTrainerUI(trainer);
        ui.start();
    }
}
