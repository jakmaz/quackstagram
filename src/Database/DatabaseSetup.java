package Database;

import Database.Setup.DataPopulator;
import Database.Setup.DatabaseReset;
import Database.Setup.SchemaCreator;

public class DatabaseSetup {

    public static void main(String[] args) {
        DatabaseReset.resetDatabase();
        SchemaCreator.createDatabaseTables();
        DataPopulator.populateData();
    }
}
