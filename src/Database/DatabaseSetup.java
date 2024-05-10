package Database;

import Database.Setup.DataPopulator;
import Database.Setup.SchemaCreator;

public class DatabaseSetup {

    public static void main(String[] args) {
        SchemaCreator.createDatabaseTables();
        DataPopulator.populateData();
    }
}
