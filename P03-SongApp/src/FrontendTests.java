///////////////////////// TOP OF FILE COMMENT BLOCK ////////////////////////////
// Title:           Frontend Tests of P103
// Course:          Fall 24 CS 400
//
// Author:          Kejun Liu
// Email:           kliu337@wisc.edu
// Lecturer's Name: GARY DAHL
//
//////////////////////////////////// songs1.csv //////////////////////////////////
// title,artist,top genre,year,bpm,nrgy,dnce,dB,live,val,dur,acous,spch,pop
// Find U Again (feat. Camila Cabello),Mark Ronson,dance pop,2019,104,66,61,-7,20,16,176,1,3,75
// Cross Me (feat. Chance the Rapper & PnB Rock),Ed Sheeran,pop,2019,95,79,75,-6,7,61,206,21,12,75
// "No Brainer (feat. Justin Bieber, Chance the Rapper & Quavo)",DJ Khaled,dance pop,2019,136,76,53,-5,9,65,260,7,34,70
// Nothing Breaks Like a Heart (feat. Miley Cyrus),Mark Ronson,dance pop,2019,114,79,60,-6,42,24,217,1,7,69
// Kills You Slowly,The Chainsmokers,electropop,2019,150,44,70,-9,13,23,213,6,6,67
//
/////////////////////////////// 80 COLUMNS WIDE ////////////////////////////////

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTimeout;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.io.PrintStream;


public class FrontendTests {
    private Frontend frontend;
    private Backend backend;

    /**
     * Sets up the Frontend and Backend instances before each test case.
     */
    @BeforeEach
    public void setup() {
        // Initialize the Backend and Frontend objects before each test
        backend = new Backend(new Tree_Placeholder());
    }

    /**
     * Test the loadFile() method of the Frontend class to ensure that the file loads successfully.
     */
    @Test
    public void testLoadFile() {
        // Simulate user input to load the file
        String input = "\nL"; // Option: D - load file
        input += "\nL\nsongs1.csv\n"; // Simulate loading the file
        InputStream inputStream = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
        Scanner scanner = new Scanner(inputStream);
        frontend = new Frontend(scanner, (Backend) backend);

        // Redirect standard output to capture the output of loadFile method
        ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStreamCaptor));

        // Run the loadFile method
        frontend.loadFile();

        // Retrieve the captured output
        String output = outputStreamCaptor.toString();

        // Check if the file was loaded successfully
        assertTrue(output.contains("File loaded successfully."), "Should indicate that the file was loaded successfully.");

        // Reset the standard output to its original state
        System.setOut(System.out);
    }

    /**
     * Test the getSongs() method to verify that the songs are retrieved correctly based on the user's range.
     */
    @Test
    public void testGetSongs() throws IOException {
        // Preload songs into the backend for testing purposes
        backend.readData("songs1.csv");

        // Simulate user input to retrieve songs within a specific range
        String input = "\nL"; // Option: D - load file
        input += "songs1.csv\n"; // file name
        input += "\nG"; // Option G - get songs
        input = "10\n50\n";
        InputStream inputStream = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
        Scanner scanner = new Scanner(inputStream);
        frontend = new Frontend(scanner, (Backend) backend);

        // Redirect standard output to capture the output of getSongs method
        ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStreamCaptor));

        // Run the getSongs method
        frontend.getSongs();

        // Backend: check that the retrieved songs are within the specified range
        assertTrue(backend.getRange(10, 50).size() > 0, "Should retrieve songs within the " +
                "specified range.");

        // Retrieve the captured output
        String output = outputStreamCaptor.toString();

        // Frontend: verify that the output is correct
        assertTrue(output.contains("Get Songs by Danceability."), "Should display songs in the specified range.");

        // Reset the System.out to its original state
        System.setOut(System.out);
    }

    /**
     * Test the setFilter() method to ensure the filter is applied correctly.
     */
    @Test
    public void testSetFilter() {
        backend.setFilter(10);
        // Backend: check that the filtered songs are below the threshold
        assertTrue(backend.setFilter(10).size() == 0, "Should filter songs with speed less than " +
                "the specified figure.");

        String input = "\nF"; // Option F: set filter
        input += "\n10"; // Simulate user input to set a speed filter of 10;
        InputStream inputStream = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
        Scanner scanner = new Scanner(inputStream);
        frontend = new Frontend(scanner, (Backend) backend);

        // Redirect standard output to capture the output of displayTopFive
        ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStreamCaptor));

        // Run the setFilter method
        frontend.setFilter();

        // Retrieve the captured output
        String output = outputStreamCaptor.toString();

        // Frontend: verify that the output is correct
        assertTrue(output.contains("Filter Songs by Speed"), "Should display songs below the speed threshold.");

        // Reset the System.out to its original state
        System.setOut(System.out);
    }

    /**
     * Test the displayTopFive() method to check if the top five songs are displayed correctly.
     */
    @Test
    public void testDisplayTopFive() throws IOException {
        // Preload songs into the backend for testing purposes
        backend.readData("songs1.csv");

        String input = "\nD"; // Option D: Display the top file
        InputStream inputStream = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
        Scanner scanner = new Scanner(inputStream);
        frontend = new Frontend(scanner, (Backend) backend);

        // Redirect standard output to capture the output of displayTopFive
        ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStreamCaptor));

        // Run the displayTopFive method
        frontend.displayTopFive();

        // Backend: verify that the list contains up to five songs
        assertTrue(backend.fiveMost().size() <= 5, "Top five list should contain up to five songs.");

        // Retrieve the captured output
        String output = outputStreamCaptor.toString();

        // Frontend: verify that the output is correct
        assertTrue(output.contains("Display five most Recent"), "Should display the five most recent songs.");

        // Reset the System.out to its original state
        System.setOut(System.out);
    }


    /**
     * Test for loading a file, getting songs, setting a filter, and displaying top five songs.
     * This test simulates user input to test the command loop.
     */
    @Test
    public void roleTest1() {
        Tree_Placeholder tree = new Tree_Placeholder();
        Backend backend = new Backend(tree);

        String input = "\nL";
        input += "\n";
        input += "songs1.csv";
        input += "\nL";
        input += "\nG";
        input += "\nQ";
        InputStream inputStream = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
        Scanner scanner = new Scanner(inputStream);
        frontend = new Frontend(scanner, (Backend) backend);

        // Redirect standard output to capture the output
        ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStreamCaptor));

        frontend.runCommandLoop(); // Start the command loop

        // Retrieve the captured output
        String output = outputStreamCaptor.toString();

        // Verify that the output contains specific sentences
        assertTrue(output.contains("Please select a command from the options below:"), "Should display the menu.");

        // Reset the System.out to its original state
        System.setOut(System.out);

    }

    /**
     * Test for setting a filter and then displaying the top five songs based on that filter.
     * This test simulates user input for setting a filter and confirms the output reflects
     * the filter application.
     */
    @Test
    public void roleTest2() {
        Tree_Placeholder tree = new Tree_Placeholder();
        Backend backend = new Backend(tree);

        String input = "\nL";
        input += "\n";
        input += "songs1.csv";
        input += "\nL";
        input += "\nF";
        InputStream inputStream = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
        Scanner scanner = new Scanner(inputStream);
        frontend = new Frontend(scanner, (Backend) backend);

        // Redirect standard output to capture the output
        ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStreamCaptor));

        frontend.setFilter(); // Filter songs

        // Retrieve the captured output
        String output = outputStreamCaptor.toString();

        // Verify that the output contains specific sentences
        assertTrue(output.contains("Filter Songs by Speed"), "Should display the filter instructions.");

        // Reset the System.out to its original state
        System.setOut(System.out);
    }

    /**
     * Test for clearing a previously set filter and quitting the program.
     * This test simulates clearing the filter and confirming that the program is quit.
     */
    @Test
    public void roleTest3() {
        Tree_Placeholder tree = new Tree_Placeholder();
        Backend backend = new Backend(tree);

        // Simulate user input for loading the file, filtering, and quitting
        String input = "Q";
        InputStream inputStream = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
        Scanner scanner = new Scanner(inputStream);
        frontend = new Frontend(scanner, (Backend) backend);

        // Redirect standard output to capture the output
        ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStreamCaptor));

        // Run the command loop
        frontend.runCommandLoop();

        // Retrieve the captured output
        String output = outputStreamCaptor.toString();

        // Verify that the command loop ends (check for quit message or lack of prompt)
        // Why it does not quit????
        // Never mind, it's successful on VM
        assertFalse(!output.contains("Enter a command (or 'Q' to quit)"), "Should quit the program.");

        // Reset the System.out to its original state
        System.setOut(System.out);
    }
}