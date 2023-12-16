package com.example;


import org.apache.kafka.common.config.AbstractConfig;
import org.apache.kafka.common.config.ConfigDef;

import java.util.Map;

public class FailoverAssignorConfig extends AbstractConfig {

    public static final String CONSUMER_PRIORITY_CONFIG = "assignment.consumer.priority";
    public static final String CONSUMER_PRIORITY_DOC = "The priority attached to the consumer that must be used for assigning partitions. " +
            "Available partitions for subscribed topics are assigned to the consumer with the highest priority within the group.";

    private static final ConfigDef CONFIG;

    static {
        CONFIG = new ConfigDef()
                .define(CONSUMER_PRIORITY_CONFIG, ConfigDef.Type.INT, Integer.MIN_VALUE,
                        ConfigDef.Importance.HIGH, CONSUMER_PRIORITY_DOC);
    }


    public FailoverAssignorConfig(final Map<?, ?> originals) {
        super(CONFIG, originals);
    }

    public int priority() {
        return getInt(CONSUMER_PRIORITY_CONFIG);
    }
}