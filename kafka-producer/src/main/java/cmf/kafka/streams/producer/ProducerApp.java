package cmf.kafka.streams.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ProducerApp {
    public static void main(String[] args) throws InterruptedException, JsonProcessingException {
        SpringApplication.run(ProducerApp.class, args);
    }

}
