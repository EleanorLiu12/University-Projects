///////////////////////// TOP OF FILE COMMENT BLOCK ////////////////////////////
//
// Title:           Frontend of P103
// Course:          Fall 24 CS 400
//
// Author:          Kejun Liu
// Email:           kliu337@wisc.edu
// Lecturer's Name: GARY DAHL
//
/////////////////////////////// 80 COLUMNS WIDE ////////////////////////////////
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
public class Frontend implements FrontendInterface{
    private final Scanner scnr;
    private final Backend backend;

    // Constructor that takes a Scanner as an argument
    public Frontend(Scanner scnr, Backend backend) {
        this.scnr = scnr;
        this.backend = backend;
    }

    /**
     * Repeatedly gives the user an opportunity to issue new commands until
     * they select Q to quit. Uses the scanner passed to the constructor to
     * read user input.
     */
    @Override
    public void runCommandLoop() {
        // The runCommandLoop() method lacks detailed inline comments to explain the logic behind
        // each command option
        String input;
        do {
            displayMainMenu();
            System.out.println("Enter a command (or 'Q' to quit): ");
            input = scnr.nextLine().trim().toUpperCase();
        } while (!input.equals("Q"));
    }

    /**
     * Displays the menu of command options to the user.  Giving the user the
     * instructions of entering L, G, F, D, or Q (case insensitive) to load a
     * file, get songs, set filter, display the top five, or quit.
     */
    @Override
    public void displayMainMenu() {
        System.out.println("Please select a command from the options below:");
        System.out.println("L - Load a file");
        System.out.println("G - Get songs");
        System.out.println("F - Set filter");
        System.out.println("D - Display the top five");
        System.out.println("Q - Quit the program");
        System.out.println("Enter your choice (L, G, F, D, or Q): ");
    }

    /**
     * Provides text-based user interface for prompting the user to select
     * the csv file that they would like to load, provides feedback about
     * whether this is successful vs any errors are encountered.
     * [L]oad Song File
     * <p>
     * When the user enters a valid filename, the file with that name
     * should be loaded.
     * Uses the scanner passed to the constructor to read user input and
     * the backend passed to the constructor to load the file provided
     * by the user. If the backend indicates a problem with finding or
     * reading the file by throwing an IOException, a message is displayed
     * to the user, and they will be asked to enter a new filename.
     */
    @Override
    public void loadFile() {
        boolean success = false;

        while (!success) {
            System.out.println("Please enter the name of the CSV file to load:");
            String fileName = scnr.nextLine().trim();

            try {
                // Attempt to load the file using the backend
                backend.readData(fileName);
                System.out.println("File loaded successfully.");
                success = true; // Exit the loop on success
            } catch (FileNotFoundException e) {
                // More specific error handling for file not found
                System.out.println("Error: File not found. Please check that the file exists and try again.");
            } catch (IOException e) {
                // Generic error handling for other IOExceptions
                System.out.println("Error: Unable to load file. Please check the file name and try again.");
            } catch (Exception e) {
                // Catch any other unforeseen exceptions
                System.out.println("An unexpected error occurred: " + e.getMessage());
            }
        }
    }

    /**
     * Provides text-based user interface and error handling for retrieving a
     * list of song titles that are sorted by Danceability.  The user should be
     * given the opportunity to optionally specify a minimum and/or maximum
     * Danceability to limit the number of songs displayed to that range.
     * [G]et Songs by Danceability
     * <p>
     * When the user enters only two numbers (pressing enter after each), the
     * first of those numbers should be interpreted as the minimum, and the
     * second as the maximum Danceability.
     * Uses the scanner passed to the constructor to read user input and
     * the backend passed to the constructor to retrieve the list of sorted
     * songs.
     */
    @Override
    public void getSongs() {
        Integer minDanceability = null;
        Integer maxDanceability = null;

        System.out.println("Get Songs by Danceability.");
        System.out.println("You may enter a minimum and/or maximum Danceability value.");
        System.out.println("Press Enter to skip setting a minimum or maximum value.");


        try {
            // Prompt for minimum Danceability
            System.out.println("Enter minimum Danceability (or press Enter to skip): ");
            minDanceability = IntDanceability(scnr.nextLine().trim());

            // Prompt for maximum Danceability
            System.out.println("Enter maximum Danceability (or press Enter to skip): ");
            maxDanceability = IntDanceability(scnr.nextLine().trim());

            // Retrieve the sorted list of songs based on Danceability range
            List<String> songs = backend.getRange(minDanceability, maxDanceability);

            // If no songs are found
            if (songs.isEmpty()) {
                System.out.println("No songs found within the specified Danceability range.");
            } else {
                System.out.println("Songs sorted by Danceability:");
                for (String song : songs) {
                    System.out.println(song);
                }
            }

        } catch (Exception e) {
            // Handle any unexpected errors
            System.out.println("An error occurred while retrieving the songs. Please try again.");
        }
    }

    // Helper method to reduce redundancy
    private int IntDanceability(String prompt) {
        String input = scnr.nextLine().trim();
        int danceability = -1;

        if (!input.isEmpty()) {
            try {
                danceability = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number for maximum Danceability.");
                return -1;
            }
        }

        return danceability;
    }

    // Helper method to reduce redundancy


    /**
     * Provides text-based user interface and error handling for setting a
     * filter threshold.  This and future requests to retrieve songs will
     * will only return the titles of songs that are smaller than the
     * user specified Speed.  The user should also be able to clear
     * any previously specified filters.
     * [F]ilter Songs by Speed
     * <p>
     * When the user enters only a single number, that number should be used
     * as the new filter threshold.
     * Uses the scanner passed to the constructor to read user input and
     * the backend passed to the constructor to set the filters provided by
     * the user and retrieve songs that matches the filter criteria.
     */
    @Override
    public void setFilter() {
        System.out.println("Filter Songs by Speed");
        System.out.println("Enter a speed threshold (songs with speed smaller than this will be filtered):");
        System.out.println("Or, enter 'C' to clear any previously set filter.");

        String input = scnr.nextLine().trim();
        List<String> filter = new ArrayList<>();

        if (input.equalsIgnoreCase("C")) {
            // Clear the filter
            filter = backend.setFilter(null);
            System.out.println("Speed filter cleared.");
        } else {
            try {
                // Set the filter with the user-specified speed threshold
                Integer speedThreshold = Integer.parseInt(input);
                filter = backend.setFilter(speedThreshold);
                System.out.println("Speed filter set to " + speedThreshold + ". All songs with a speed less than this will be returned.");
            } catch (NumberFormatException e) {
                // Handle invalid input
                System.out.println("Invalid input. Please enter a valid number for the speed threshold or 'C' to clear the filter.");
            }
        }
    }

    /**
     * Displays the titles of up to five of the most Recent songs within the
     * previously set Danceability range and smaller than the specified
     * Speed.  If there are no such songs, then this method should
     * indicate that and recommend that the user change their current range or
     * filter settings.
     * [D]isplay five most Recent
     * <p>
     * The user should not need to enter any input when running this command.
     * Uses the backend passed to the constructor to retrieve the list of up
     * to five songs.
     */
    @Override
    public void displayTopFive() {
        System.out.println("Display five most Recent");

        // Retrieve the list of up to five songs from the backend
        List<String> recentSongs = backend.fiveMost();

        // Check if any songs were retrieved
        if (recentSongs.isEmpty()) {
            System.out.println("No songs found within the current Danceability range and Speed filter.");
            System.out.println("Please consider adjusting your filter settings.");
        } else {
            System.out.println("Top five most recent songs:");
            for (String songTitle : recentSongs) {
                System.out.println(songTitle);
            }
        }
    }
}
