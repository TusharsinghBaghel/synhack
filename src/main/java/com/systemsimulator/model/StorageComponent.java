package com.systemsimulator.model;

public class StorageComponent extends Component {
    public enum StorageType {
        BLOCK_STORAGE, OBJECT_STORAGE, FILE_STORAGE
    }

    private StorageType storageType;

    public StorageComponent() {
        super();
        setType(ComponentType.STORAGE);
    }

    public StorageComponent(String id, String name, StorageType storageType) {
        super(id, name, ComponentType.STORAGE);
        this.storageType = storageType;
    }

    public StorageType getStorageType() { return storageType; }
    public void setStorageType(StorageType storageType) { this.storageType = storageType; }
}

