package data_access;

import entity.HarvestRecord;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class FileHarvestRecordDataAccessObjectTest {

    @TempDir
    Path tempDir;

    private String path() {
        return tempDir.resolve("harvest_records.csv").toString();
    }

    @Test
    void unknownPlayerHasNoRecord() {
        final FileHarvestRecordDataAccessObject dao = new FileHarvestRecordDataAccessObject(path());

        assertNull(dao.get("nobody"));
    }

    @Test
    void savedRecordSurvivesARestart() {
        new FileHarvestRecordDataAccessObject(path())
                .save(new HarvestRecord("tiffany", 7, HarvestRecord.PUBLIC));

        final HarvestRecord loaded = new FileHarvestRecordDataAccessObject(path()).get("tiffany");

        assertNotNull(loaded);
        assertEquals(7, loaded.getHarvestCount());
        assertEquals(HarvestRecord.PUBLIC, loaded.getVisibility());
    }

    @Test
    void usernamesWithCommasAndSpacesSurviveARestart() {
        new FileHarvestRecordDataAccessObject(path())
                .save(new HarvestRecord("a, b c", 2, HarvestRecord.PRIVATE));

        final HarvestRecord loaded = new FileHarvestRecordDataAccessObject(path()).get("a, b c");

        assertNotNull(loaded);
        assertEquals(2, loaded.getHarvestCount());
        assertEquals(HarvestRecord.PRIVATE, loaded.getVisibility());
    }

    @Test
    void savingAgainReplacesTheOldRecord() {
        final FileHarvestRecordDataAccessObject dao = new FileHarvestRecordDataAccessObject(path());
        dao.save(new HarvestRecord("tiffany", 1, HarvestRecord.UNSET));
        dao.save(new HarvestRecord("tiffany", 5, HarvestRecord.PUBLIC));

        final HarvestRecord loaded = new FileHarvestRecordDataAccessObject(path()).get("tiffany");

        assertEquals(5, loaded.getHarvestCount());
        assertEquals(HarvestRecord.PUBLIC, loaded.getVisibility());
    }
}