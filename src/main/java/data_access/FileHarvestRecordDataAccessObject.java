package data_access;

import entity.HarvestRecord;
import use_case.leaderboard.LeaderboardRecordDataAccessInterface;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Keeps each player's HarvestRecord in a local csv file so it survives restarts.
 * Usernames are URL-encoded in the file, so commas or spaces in a name are safe.
 */
public class FileHarvestRecordDataAccessObject implements LeaderboardRecordDataAccessInterface {

    private static final String HEADER = "username,harvestCount,visibility";

    private final File csvFile;
    private final Map<String, HarvestRecord> records = new LinkedHashMap<>();

    /**
     * Opens (or creates) the file.
     * @param csvPath where the file is, for example "harvest_records.csv"
     * @throws RuntimeException if the file cannot be read or has a wrong format
     */
    public FileHarvestRecordDataAccessObject(String csvPath) {
        csvFile = new File(csvPath);
        if (csvFile.length() == 0) {
            writeFile();
        }
        else {
            readFile();
        }
    }

    @Override
    public HarvestRecord get(String username) {
        return records.get(username);
    }

    @Override
    public void save(HarvestRecord record) {
        records.put(record.getUsername(), record);
        writeFile();
    }

    private void readFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(csvFile))) {
            final String header = reader.readLine();
            if (!HEADER.equals(header)) {
                throw new RuntimeException(String.format(
                        "header should be%n: %s%n but was:%n%s", HEADER, header));
            }

            String row;
            while ((row = reader.readLine()) != null) {
                if (row.isEmpty()) {
                    continue;
                }
                final String[] col = row.split(",", -1);
                if (col.length != 3) {
                    throw new RuntimeException("Bad row in " + csvFile + ": " + row);
                }
                final String username = URLDecoder.decode(col[0], StandardCharsets.UTF_8);
                final int count = Integer.parseInt(col[1]);
                records.put(username, new HarvestRecord(username, count, col[2]));
            }
        }
        catch (IOException | IllegalArgumentException ex) {
            throw new RuntimeException(ex);
        }
    }

    private void writeFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(csvFile))) {
            writer.write(HEADER);
            writer.newLine();
            for (HarvestRecord record : records.values()) {
                writer.write(String.format("%s,%d,%s",
                        URLEncoder.encode(record.getUsername(), StandardCharsets.UTF_8),
                        record.getHarvestCount(),
                        record.getVisibility()));
                writer.newLine();
            }
        }
        catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}