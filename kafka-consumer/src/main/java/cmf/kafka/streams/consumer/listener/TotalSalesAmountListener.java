package cmf.kafka.streams.consumer.listener;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TotalSalesAmountListener {
    public static final String TOPIC = "totalSalesAmountTopic";

    @KafkaListener(topics = TOPIC, groupId = "group_id")
    public void consume(ConsumerRecord<String, String> payload) {
        log.info("[{} - {}] {}: {}", TOPIC, payload.partition(), payload.key(), payload.value());
    }
}
