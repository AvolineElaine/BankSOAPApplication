package bank.commons;

import com.mongodb.MongoClient;
import bank.models.Sequence;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

/**
 * Class handling connection to the Mongo database server
 */

public class DatastoreHandler {
    private static DatastoreHandler Instance = new DatastoreHandler();
    private Datastore dataStore;


    /**
     * Creates database representation object
     */
    public void initializeDataStore() {
        final Morphia morphia = new Morphia();
        dataStore = morphia.createDatastore(new MongoClient("localhost", 8004), "bank");
        morphia.mapPackage("bank.models");
        dataStore.ensureIndexes();

        if (dataStore.getCount(Sequence.class) == 0) {
            dataStore.save(new Sequence("accountNoSequence"));
        }
    }

    /**
     * Gets instance of the handler
     * @return instance of the class
     */
    public static DatastoreHandler getInstance() {
        return Instance;
    }

    /**
     * Gets the representation of the database
     * @return database representation
     */
    public Datastore getDataStore() {
        return dataStore;
    }

}
