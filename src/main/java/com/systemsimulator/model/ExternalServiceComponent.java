package com.systemsimulator.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ExternalServiceComponent extends Component {

    public ExternalServiceComponent() {
        super();
        setType(ComponentType.EXTERNAL_SERVICE);
    }

    public ExternalServiceComponent(String id, String name) {
        super(id, name, ComponentType.EXTERNAL_SERVICE);
    }
}

