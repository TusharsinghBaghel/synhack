package com.systemsimulator.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class APIServiceComponent extends Component {
    public enum APIType {
        REST, GRAPHQL, GRPC, SOAP
    }

    private APIType apiType;

    public APIServiceComponent() {
        super();
        setType(ComponentType.API_SERVICE);
    }

    public APIServiceComponent(String id, String name, APIType apiType) {
        super(id, name, ComponentType.API_SERVICE);
        this.apiType = apiType;
    }

}

