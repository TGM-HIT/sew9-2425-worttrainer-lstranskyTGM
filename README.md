# sew9-2425-worttrainer-lstranskyTGM

## Overview

This project is a simple spelling trainer designed for children. 
It presents an image and asks the user to type the corresponding word. 
The application tracks the user's performance, keeping statistics on correct and incorrect guesses, and saves the session using JSON. 
This way, progress can be resumed later. The project uses Java with Gradle as its build system, and Gson for handling JSON data.

## Features

- **Interactive word-image guessing game:** Users are shown an image and asked to guess the word associated with it.
- **Performance tracking:** Tracks user performance, including total guesses, correct guesses, incorrect guesses, and accuracy.
- **Persistent game state:** The current state of the game (word pairs and statistics) is saved in a JSON file, allowing the user to resume their session later.
- **Random word selection:** After each correct guess, a new word-image pair is randomly selected for the next round.
- **Simple GUI:** The game uses `JOptionPane` for user input and feedback.

## Structure

### `WordPicturePair.java`

This class represents a pair of a word and its corresponding image URL. It ensures that the word is valid (i.e., not null or empty) and that the URL follows a proper format. 
If the word or URL is invalid, an exception is thrown. The class provides methods for setting and getting the word and image URL, and checks for the validity of the URL using a regular expression.

### `Statistics.java`

The `Statistics` class is responsible for recording the number of correct and incorrect guesses, along with the total number of guesses made by the user. 
It calculates the accuracy as a percentage of correct guesses. The statistics can be reset at any point. This class is essential for tracking the user's progress throughout the game.

### `SpellingTrainer.java`

`SpellingTrainer` is the core class that manages the gameplay logic. It holds a list of word-picture pairs and interacts with the `Statistics` class to update the user's progress. 
The trainer selects a random word pair for the user to guess, checks whether the user's guess is correct, and updates the statistics accordingly. 
Additionally, it integrates with the persistence layer to save and load game states, ensuring that the user's progress is maintained between sessions.

### `PersistenceStrategy.java`

This is an interface that defines the contract for saving and loading objects to and from storage. It abstracts the persistence mechanism so that the underlying storage system (such as JSON, XML, or a database) can be swapped easily. 
Any class implementing this interface must provide methods for saving and loading data.

### `JsonPersistence.java`

This class implements the `PersistenceStrategy` interface using Gson to serialize and deserialize the `SpellingTrainer` object into JSON format. It is responsible for saving the game state to a JSON file and restoring it when the user resumes the game. 
The `filePath` is configurable, allowing the developer or user to specify the file location dynamically. This class ensures that all relevant data—word pairs, statistics, and game progress—are properly saved and loaded.

### `SpellingTrainerUI.java`

The `SpellingTrainerUI` class provides the graphical user interface (GUI) for the spelling trainer. It uses `JOptionPane` to display images, accept user input, and provide feedback. The user is presented with an image and prompted to enter the corresponding word. 
The GUI informs the user whether their guess was correct or incorrect and displays their statistics. The game persists after each interaction, and the user can exit the game through the interface. It serves as the main entry point for interacting with the `SpellingTrainer`.

## External Libraries

- **Gson:** A Java library used to convert Java objects to JSON and back. It is used in the project for serializing and deserializing the `SpellingTrainer` object to persist the game state between sessions.