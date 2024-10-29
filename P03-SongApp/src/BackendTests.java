import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

public class BackendTests {

    // testing getRange()
    @Test
    public void roleTest1() {
        Tree_Placeholder tree = new Tree_Placeholder();
        Backend backend = new Backend(tree);

        // adding a new song to the given tree
        Song song = new Song("New Song", "Test Artist", "Test Genre", 2020, 100, 50, 60, -5, 10, null);
        tree.insert(song);

        // setting a danceability range to filter songs (danceability between 50 and 80)
        List<String> result = backend.getRange(50, 80);

        // checking if the result contains 2 songs that fit the filter
        assertTrue(result.contains("New Song"));
        assertTrue(result.contains("Cake By The Ocean"));
        assertEquals(2, result.size());
    }

    // testing setFilter()
    @Test
    public void roleTest2() {
        Tree_Placeholder tree = new Tree_Placeholder();
        Backend backend = new Backend(tree);

        Song newSong =
                new Song("New Song", "Test Artist", "Test Genre", 2020, 100, 50, 60, -5, 10, null);
        tree.insert(newSong);

        // setting a filter for songs with a BPM lower than 50
        List<String> result = backend.setFilter(50);

        assertTrue(result.isEmpty());
    }

    // testing fiveMost()
    @Test
    public void roleTest3() {
        Tree_Placeholder tree = new Tree_Placeholder();
        Backend backend = new Backend(tree);

        Song newSong =
                new Song("New Song", "Test Artist", "Test Genre", 2020, 100, 50, 60, -5, 10, null);
        tree.insert(newSong);

        // calling fiveMost() and check if the most recent song is returned
        List<String> result = backend.fiveMost();

        // checking if "New Song" is in the list and if the list contains 5 or fewer songs
        assertTrue(result.contains("New Song"));
        assertTrue(result.size() <= 5);
    }

}