package com.systemsimulator.model;

public class QueueComponent extends Component {
    public enum QueueType {
        MESSAGE_QUEUE, TASK_QUEUE, STREAM, EVENT_BUS
    }

    private QueueType queueType;

    public QueueComponent() {
        super();
        setType(ComponentType.QUEUE);
    }

    public QueueComponent(String id, String name, QueueType queueType) {
        super(id, name, ComponentType.QUEUE);
        this.queueType = queueType;
    }

    public QueueType getQueueType() { return queueType; }
    public void setQueueType(QueueType queueType) { this.queueType = queueType; }
}

