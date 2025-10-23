package com.systemsimulator.model;

public class CacheComponent extends Component {
    public enum CacheType {
        IN_MEMORY, DISTRIBUTED, LOCAL
    }

    private CacheType cacheType;

    public CacheComponent() {
        super();
        setType(ComponentType.CACHE);
    }

    public CacheComponent(String id, String name, CacheType cacheType) {
        super(id, name, ComponentType.CACHE);
        this.cacheType = cacheType;
    }

    public CacheType getCacheType() { return cacheType; }
    public void setCacheType(CacheType cacheType) { this.cacheType = cacheType; }
}

