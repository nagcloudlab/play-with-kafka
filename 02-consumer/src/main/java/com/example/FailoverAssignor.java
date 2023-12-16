package com.example;


import org.apache.kafka.clients.consumer.internals.AbstractPartitionAssignor;
import org.apache.kafka.common.Configurable;
import org.apache.kafka.common.TopicPartition;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class FailoverAssignor extends AbstractPartitionAssignor implements Configurable {

    private FailoverAssignorConfig config;

    /**
     * {@inheritDoc}
     */
    @Override
    public String name() {
        return "failover";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void configure(final Map<String, ?> configs) {
        this.config = new FailoverAssignorConfig(configs);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, List<TopicPartition>> assign(final Map<String, Integer> partitionsPerTopic,
                                                    final Map<String, Subscription> subscriptions) {

        // Generate all topic-partitions using the number of partitions for each subscribed topic.
        final List<TopicPartition> assignments = partitionsPerTopic
                .entrySet()
                .stream()
                .flatMap(entry -> {
                    final String topic = entry.getKey();
                    final int numPartitions = entry.getValue();
                    return IntStream.range(0, numPartitions)
                            .mapToObj( i -> new TopicPartition(topic, i));
                }).collect(Collectors.toList());

        // Decode consumer priority from each subscription and
        Stream<ConsumerPriority> consumerOrdered = subscriptions.entrySet()
                .stream()
                .map(e -> {
                    int priority = e.getValue().userData().getInt();
                    String memberId = e.getKey();
                    return new ConsumerPriority(memberId, priority);
                })
                .sorted(Comparator.reverseOrder());

        // Select the consumer with the highest priority
        ConsumerPriority priority = consumerOrdered.findFirst().get();

        final Map<String, List<TopicPartition>> assign = new HashMap<>();
        subscriptions.keySet().forEach(memberId -> assign.put(memberId, Collections.emptyList()));
        assign.put(priority.memberId, assignments);
        return assign;
    }

    /**
     * {@inheritDoc}
     */
    public Subscription subscription(final Set<String> topics) {
        return new Subscription(new ArrayList<>(topics), encodeUserData());
    }

    private ByteBuffer encodeUserData() {
        return ByteBuffer.allocate(4).putInt(config.priority()).rewind();
    }

    /**
     * {@inheritDoc}
     */
    public void onAssignment(final Assignment assignment) {
        // this assignor maintains no internal state, so nothing to do
    }

    private static class ConsumerPriority implements Comparable<ConsumerPriority> {

        private final String memberId;
        private final Integer priority;

        ConsumerPriority(final String memberId, final Integer priority) {
            this.memberId = memberId;
            this.priority = priority;
        }

        @Override
        public int compareTo(final ConsumerPriority that) {
            return Integer.compare(priority, that.priority);
        }
    }

}
