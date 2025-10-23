package com.systemsimulator.model;

public class DatabaseComponent extends Component {
    public enum DatabaseType {
        SQL, NOSQL, IN_MEMORY, COLUMN_STORE, DOCUMENT_DB, GRAPH_DB
    }

    private DatabaseType databaseType;

    public DatabaseComponent() {
        super();
        setType(ComponentType.DATABASE);
    }

    public DatabaseComponent(String id, String name, DatabaseType databaseType) {
        super(id, name, ComponentType.DATABASE);
        this.databaseType = databaseType;
    }

    public DatabaseType getDatabaseType() { return databaseType; }
    public void setDatabaseType(DatabaseType databaseType) { this.databaseType = databaseType; }
}

