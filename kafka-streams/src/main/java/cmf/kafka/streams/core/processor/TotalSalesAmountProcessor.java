package cmf.kafka.streams.core.processor;

import cmf.kafka.streams.domain.Moto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.Grouped;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Produced;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Slf4j
@Component
@RequiredArgsConstructor
public class TotalSalesAmountProcessor {

    @Value("${kafka.topic.total-sales-amount}")
    private String targetTopic;

    private final KStream<String, Moto> kStream;

    @PostConstruct
    public void process() {
        log.info("Start ModelCounterProcessor");
        kStream
            // Set empty key and value to price
            .map((k, v) -> new KeyValue<>("", v.getPrice()))
            // Group by "empty" (all records)
            .groupByKey(Grouped.with(Serdes.String(), Serdes.Integer()))
            // Apply SUM aggregation
            .reduce(Integer::sum)
            // Write to stream specified by outputTopic
            .toStream()
            .mapValues(v -> v.toString() + " total sales")
            .to(targetTopic, Produced.with(Serdes.String(), Serdes.String()));
    }

}
