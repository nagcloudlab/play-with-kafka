package com.example.topology;

import com.example.serdes.JsonSerdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.Produced;

public class TransactionsTopology {

    public static Topology build() {

        StreamsBuilder builder = new StreamsBuilder();
        var sourceProcessor = builder.stream("transactions", Consumed.with(JsonSerdes.transactionKeySerdes(), JsonSerdes.transactionSerdes()));

        sourceProcessor
                .filter((key, value) -> value.getAmount().longValue()>200)
                .to("large-transactions", Produced.with(JsonSerdes.transactionKeySerdes(), JsonSerdes.transactionSerdes()));


        return builder.build();
    }
}
