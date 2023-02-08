package cmf.kafka.streams.producer.service;

import cmf.kafka.streams.domain.Moto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class SaleProducerService {

    @Value(value = "${message.topic.sales.name}")
    private String topicName;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private static final List<Moto> MOTO_LIST = getMotoList();
    private final Random rand = new Random();

    @PostConstruct
    public void init() {
        log.info("Start kafka producer");
        try {
            produceMessages();
        } catch (JsonProcessingException | InterruptedException e) {
            log.error("Unexpected error", e);
        }

        log.info("End kafka producer");
    }

    public void produceMessages() throws JsonProcessingException, InterruptedException {
        for (int i = 0; i < 300; i++) {
            val message = createMessage();

            sendMessage(String.valueOf(i), message);

            TimeUnit.SECONDS.sleep(1);
        }
    }

    private String createMessage() throws JsonProcessingException {
        val randomMoto = MOTO_LIST.get(rand.nextInt(MOTO_LIST.size()));

        return new ObjectMapper().writeValueAsString(randomMoto);

    }

    private void sendMessage(String key, String message) {
        kafkaTemplate.send(topicName, key, message);
    }

    private static List<Moto> getMotoList() {
        return List.of(
            Moto.builder().id(1).brand("BMW").model("F800GS").price(15000).build(),
            Moto.builder().id(1).brand("BMW").model("R1200GS").price(15000).build(),
            Moto.builder().id(1).brand("BULTACO").model("LOBITO MK6").price(15000).build(),
            Moto.builder().id(1).brand("SUZUKI").model("RMZ 450").price(15000).build(),
            Moto.builder().id(1).brand("KAWASAKI").model("KXF 450").price(15000).build(),
            Moto.builder().id(1).brand("KYMCO").model("PEOPLE 125").price(15000).build()
        );
    }
}
