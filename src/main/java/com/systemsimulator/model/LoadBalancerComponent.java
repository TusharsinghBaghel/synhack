package com.systemsimulator.model;

public class LoadBalancerComponent extends Component {
    public enum LoadBalancerType {
        ROUND_ROBIN, LEAST_CONNECTIONS, IP_HASH, WEIGHTED
    }

    private LoadBalancerType lbType;

    public LoadBalancerComponent() {
        super();
        setType(ComponentType.LOAD_BALANCER);
    }

    public LoadBalancerComponent(String id, String name, LoadBalancerType lbType) {
        super(id, name, ComponentType.LOAD_BALANCER);
        this.lbType = lbType;
    }

    public LoadBalancerType getLbType() { return lbType; }
    public void setLbType(LoadBalancerType lbType) { this.lbType = lbType; }
}

