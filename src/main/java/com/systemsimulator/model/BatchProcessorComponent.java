package com.systemsimulator.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class BatchProcessorComponent extends Component {

    public BatchProcessorComponent() {
        super();
        setType(ComponentType.BATCH_PROCESSOR);
    }

    public BatchProcessorComponent(String id, String name) {
        super(id, name, ComponentType.BATCH_PROCESSOR);
    }
}

