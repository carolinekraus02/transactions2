import java.util.HashMap;
import java.util.Map;

interface InMemoryDB {
    Integer get(String key);
    void put(String key, int val);
    void begin_transaction();
    void commit();
    void rollback();
}

class TransactionMachine implements InMemoryDB {
    private Map<String, Integer> db;
    private boolean active;
    private Map<String, Integer> edits;


    public TransactionMachine() {
        this.db = new HashMap<>();
        this.active = false;
        this.edits = new HashMap<>();

    }

    @Override
    public void begin_transaction() {
        if (active) {
            throw new IllegalStateException("Transaction already in progress");
        }
        active = true;
    }


    @Override
    public void put(String key, int val) {
        if (!active) {
            throw new IllegalStateException("No transactions active");
        }
        edits.put(key, val);
    }

    @Override
    public Integer get(String key) {
        //return db.containsKey(key) ? db.get(key) : null;
        return db.getOrDefault(key, null);
    }



    @Override
    public void commit() {
        if (!active) {
            throw new IllegalStateException("No transactions active");
        }
        db.putAll(edits);
        edits.clear();
        active = false;
    }

    @Override
    public void rollback() {
        if (!active) {
            throw new IllegalStateException("No transactions active");
        }
        edits.clear();
        active = false;
    }
}

public class Main {
    public static void main(String[] args) {
        InMemoryDB inmemoryDB = new TransactionMachine();

        System.out.println(inmemoryDB.get("A"));
        inmemoryDB.begin_transaction();
        inmemoryDB.put("A", 5);
        System.out.println(inmemoryDB.get("A"));
        inmemoryDB.put("A", 6);
        inmemoryDB.commit();
        System.out.println(inmemoryDB.get("A"));
        System.out.println(inmemoryDB.get("B"));
        inmemoryDB.begin_transaction();
        inmemoryDB.put("B", 10);
        inmemoryDB.rollback();
        System.out.println(inmemoryDB.get("B"));

/*
// should return null, because A doesn’t exist in the DB yet
inmemoryDB.get(“A”)

// should throw an error because a transaction is not in progress
inmemoryDB.put(“A”, 5);

// starts a new transaction
inmemoryDB.begin_transaction();

// set’s value of A to 5, but its not committed yet
inmemoryDB.put(“A”, 5);

// should return null, because updates to A are not committed yet
inmemoryDB.get(“A”)

// update A’s value to 6 within the transaction
inmemoryDB.put(“A”, 6)

// commits the open transaction
inmemoryDB.commit()

// should return 6, that was the last value of A to be committed
inmemoryDB.get(“A”);

// throws an error, because there is no open transaction
inmemoryDB.commit();

// throws an error because there is no ongoing transaction
inmemoryDB.rollback();

// should return null because B does not exist in the db
inmemoryDB.get(“B”);

// starts a new transaction
inmemoryDB.begin_transaction();

// Set key B’s value to 10 within the transaction
inmemoryDB.put(“B”, 10);

// Rollback the transaction - revert any changes made to B
inmemoryDB.rollback();

// Should return null because changes to B were rolled back
inmemoryDB.get(“B”)
*/

    }
}
