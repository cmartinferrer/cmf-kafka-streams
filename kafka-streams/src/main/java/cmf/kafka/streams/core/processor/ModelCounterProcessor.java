package cmf.kafka.streams.core.processor;

import cmf.kafka.streams.domain.Moto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Slf4j
@Component
@RequiredArgsConstructor
public class ModelCounterProcessor {

    @Value("${kafka.topic.model-counter}")
    private String targetTopic;

    private final KStream<String, Moto> kStream;

    @PostConstruct
    public void process() {
        log.info("Start ModelCounterProcessor");
        kStream
            .selectKey((k, v) -> v.getBrand() + v.getModel())
            .groupByKey()
            .count()
            .toStream()
            .map((key, value) -> KeyValue.pair(key, value.toString()))
            .to(targetTopic);
    }

}
