package com.systemsimulator.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class StreamProcessorComponent extends Component {

    public StreamProcessorComponent() {
        super();
        setType(ComponentType.STREAM_PROCESSOR);
    }

    public StreamProcessorComponent(String id, String name) {
        super(id, name, ComponentType.STREAM_PROCESSOR);
    }
}

