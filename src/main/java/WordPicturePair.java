import java.util.regex.Pattern;

/**
 * Class representing a pair consisting of a word and an image URL.
 * @author Leonhard Stransky
 * @version 2024-09-28
 */
public class WordPicturePair {
    private String word;
    private String imageUrl;

    // Regex pattern for URL validation
    private static final String URL_REGEX = "^(https?://)([\\w.-]+)(:[0-9]{1,5})?(/[\\w./-]*)?$";
    private static final Pattern URL_PATTERN = Pattern.compile(URL_REGEX);

    // Constructors

    /**
     * Constructor for a WordPicturePair.
     * Validates the word and imageUrl before setting them.
     * @param word The word.
     * @param imageUrl The URL of the image.
     * @throws IllegalArgumentException if the word is null/empty or the URL is invalid.
     */
    public WordPicturePair(String word, String imageUrl) {
        this.setWord(word);
        this.setImageUrl(imageUrl);
    }

    // Getters and Setters

    public String getWord() {
        return this.word;
    }

    public String getImageUrl() {
        return this.imageUrl;
    }

    /**
     * Sets the word.
     * Validates that the word is not null or empty.
     * @param word The word to set.
     * @throws IllegalArgumentException if the word is null or empty.
     */
    public void setWord(String word) {
        if (word == null || word.trim().isEmpty()) {
            throw new IllegalArgumentException("Word cannot be null or empty.");
        }
        this.word = word.trim();
    }

    /**
     * Sets the URL of the image.
     * Validates the URL format.
     * @param imageUrl The URL of the image to set.
     * @throws IllegalArgumentException if the URL is invalid.
     */
    public void setImageUrl(String imageUrl) {
        if (imageUrl == null || !isValidUrl(imageUrl)) {
            throw new IllegalArgumentException("The URL provided is invalid.");
        }
        this.imageUrl = imageUrl;
    }

    // Methods

    /**
     * Checks if a URL is valid.
     * Validates the URL format by matching it against a regex pattern.
     * @param url The URL to check.
     * @return True if the URL is valid, false otherwise.
     */
    private boolean isValidUrl(String url) {
        return URL_PATTERN.matcher(url).matches();
    }

    /**
     * Returns a string representation of the WordPicturePair.
     * @return The string representation.
     */
    @Override
    public String toString() {
        return this.word + " / " + this.imageUrl;
    }
}
