package com.systemsimulator.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ClientComponent extends Component {

    public ClientComponent() {
        super();
        setType(ComponentType.CLIENT);
    }

    public ClientComponent(String id, String name) {
        super(id, name, ComponentType.CLIENT);
    }
}

