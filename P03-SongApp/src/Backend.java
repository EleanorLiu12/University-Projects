import java.io.IOException;
import java.io.File;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;
import java.util.Comparator;

public class Backend implements BackendInterface {

    private IterableSortedCollection<Song> tree;
    private Integer lowerBound = null;
    private Integer upperBound = null;
    private Integer speedFilter = null;

    public Backend(IterableSortedCollection<Song> tree) {
        this.tree = tree;
        // Your constructor must have the signature above. All methods below must
        // use the provided tree to store, sort, and iterate through songs. This
        // will enable you to create some tests that use the placeholder tree, and
        // others that make use of a working tree.
    }

    /**
     * Loads data from the .csv file referenced by filename. You can rely on the exact headers found
     * in the provided songs.csv, but you should not rely on them always being presented in this order
     * or on there not being additional columns describing other song qualities. After reading songs
     * from the file, the songs are inserted into the tree passed to the constructor.
     *
     * @param filename is the name of the csv file to load data from
     * @throws IOException when there is trouble finding/reading file
     */
    @Override
    public void readData(String filename) throws IOException {
        Comparator<Song> danceabilityComparator = new Comparator<Song>() {

            @Override
            public int compare(Song song1, Song song2) {
                return Integer.compare(song1.getDanceability(), song2.getDanceability());
            }
        };

        try {
            File list = new File(filename);
            Scanner reader = new Scanner(list);

            int titleIndex = -1;
            int artistIndex = -1;
            int genreIndex = -1;
            int yearIndex = -1;
            int bpmIndex = -1;
            int energyIndex = -1;
            int danceabilityIndex = -1;
            int loudnessIndex = -1;
            int livenessIndex = -1;

            if (reader.hasNextLine()) {
                // read the first line in the file
                String headerLine = reader.nextLine();
                // get order of categories
                String[] headers = headerLine.split(",");

                for (int i = 0; i < headers.length; i++) {
                    String header = headers[i].trim().toLowerCase();

                    switch (header) {
                        case "title":
                            titleIndex = i;
                            break;
                        case "artist":
                            artistIndex = i;
                            break;
                        case "top genre":
                            genreIndex = i;
                            break;
                        case "year":
                            yearIndex = i;
                            break;
                        case "bpm":
                            bpmIndex = i;
                            break;
                        case "nrgy":
                            energyIndex = i;
                            break;
                        case "dnce":
                            danceabilityIndex = i;
                            break;
                        case "db":
                            loudnessIndex = i;
                            break;
                        case "live":
                            livenessIndex = i;
                            break;
                    }
                }
                while (reader.hasNextLine()) {
                    String line = reader.nextLine();
                    String[] values = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");

                    // storing data based on the indices
                    String title = values[titleIndex].replace("\"", "").trim(); // remove quotes
                    String artist = values[artistIndex].trim();
                    String genre = values[genreIndex].trim();
                    int year = Integer.parseInt(values[yearIndex].trim());
                    int bpm = Integer.parseInt(values[bpmIndex].trim());
                    int energy = Integer.parseInt(values[energyIndex].trim());
                    int danceability = Integer.parseInt(values[danceabilityIndex].trim());
                    int loudness = Integer.parseInt(values[loudnessIndex].trim());
                    int liveness = Integer.parseInt(values[livenessIndex].trim());

                    Song song = new Song(title, artist, genre, year, bpm, energy, danceability, loudness,
                            liveness, danceabilityComparator);
                    tree.insert(song);
                }
                reader.close();
            }

        } catch (IOException e) {
            System.out.println("IO Exception occurred");
        }


    }

    /**
     * Retrieves a list of song titles from the tree passed to the constructor. The songs should be
     * ordered by the songs' Danceability, and that fall within the specified range of Danceability
     * values. This Danceability range will also be used by future calls to the setFilter and getmost
     * Recent methods.
     *
     * If a Speed filter has been set using the setFilter method below, then only songs that pass that
     * filter should be included in the list of titles returned by this method.
     *
     * When null is passed as either the low or high argument to this method, that end of the range is
     * understood to be unbounded. For example, a null high argument means that there is no maximum
     * Danceability to include in the returned list.
     *
     * @param low  is the minimum Danceability of songs in the returned list
     * @param high is the maximum Danceability of songs in the returned list
     * @return List of titles for all songs from low to high, or an empty list when no such songs have
     *         been loaded
     */
    @Override
    public List<String> getRange(Integer low, Integer high) {
        List<String> filteredList = new ArrayList<>();

        if (low == null) {
            this.lowerBound = Integer.MIN_VALUE;
        } else {
            this.lowerBound = low;
        }
        if (high == null) {
            this.upperBound = Integer.MAX_VALUE;
        } else {
            this.upperBound = high;
        }

        for (Song song : tree) {
            int danceability = song.getDanceability();

            if (danceability >= lowerBound && danceability <= upperBound) {
                if (speedFilter == null || song.getBPM() < speedFilter) {
                    filteredList.add(song.getTitle());
                }
            }
        }

        return filteredList;
    }

    /**
     * Retrieves a list of song titles that have a Speed that is smaller than the specified threshold.
     * Similar to the getRange method: this list of song titles should be ordered by the songs'
     * Danceability, and should only include songs that fall within the specified range of
     * Danceability values that was established by the most recent call to getRange. If getRange has
     * not previously been called, then no low or high Danceability bound should be used. The filter
     * set by this method will be used by future calls to the getRange and getmost Recent methods.
     *
     * When null is passed as the threshold to this method, then no Speed threshold should be used.
     * This effectively clears the filter.
     *
     * @param threshold filters returned song titles to only include songs that have a Speed that is
     *                  smaller than this threshold.
     * @return List of titles for songs that meet this filter requirement, or an empty list when no
     *         such songs have been loaded
     */
    @Override
    public List<String> setFilter(Integer threshold) {
        // Set the speed filter to the provided threshold, or clear it if threshold is null
        this.speedFilter = threshold;

        // Return a list of filtered song titles based on the current filter
        List<String> filteredList = new ArrayList<>();

        // Filter songs by BPM based on the speed filter
        for (Song song : tree) {
            if (speedFilter == null || song.getBPM() < speedFilter) {
                filteredList.add(song.getTitle());
            }
        }

        return filteredList;
    }

    /**
     * This method returns a list of song titles representing the five most Recent songs that both
     * fall within any attribute range specified by the most recent call to getRange, and conform to
     * any filter set by the most recent call to setFilter. The order of the song titles in this
     * returned list is up to you.
     *
     * If fewer than five such songs exist, return all of them. And return an empty list when there
     * are no such songs.
     *
     * @return List of five most Recent song titles
     */
    @Override
    public List<String> fiveMost() {
        // Ensure that lowerBound and upperBound have default values if getRange() hasn't been called
        if (lowerBound == null) {
            lowerBound = Integer.MIN_VALUE;
        }
        if (upperBound == null) {
            upperBound = Integer.MAX_VALUE;
        }

        List<Song> filteredSongs = new ArrayList<>();

        // Filter songs by the most recent range and speed filter
        for (Song song : tree) {
            int danceability = song.getDanceability();

            if (danceability >= lowerBound && danceability <= upperBound) {
                if (speedFilter == null || song.getBPM() < speedFilter) {
                    filteredSongs.add(song);
                }
            }
        }

        // Sort the filtered songs by year in descending order (most recent first)
        filteredSongs.sort(new Comparator<Song>() {
            @Override
            public int compare(Song song1, Song song2) {
                return Integer.compare(song2.getYear(), song1.getYear());
            }
        });

        // Collect the titles of the top 5 most recent songs (or fewer if less than 5)
        List<String> mostRecentTitles = new ArrayList<>();
        for (int i = 0; i < Math.min(5, filteredSongs.size()); i++) {
            mostRecentTitles.add(filteredSongs.get(i).getTitle());
        }

        return mostRecentTitles;
    }

}