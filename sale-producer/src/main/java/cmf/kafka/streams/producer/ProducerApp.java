package cmf.kafka.streams.producer;

import cmf.kafka.streams.producer.service.SaleProducerService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.concurrent.TimeUnit;

@SpringBootApplication
public class ProducerApp {
    public static void main(String[] args) throws InterruptedException {
        ConfigurableApplicationContext context = SpringApplication.run(ProducerApp.class, args);

        SaleProducerService producer = context.getBean(SaleProducerService.class);


        // Durante 5 minutos (300) va ha estar enviado mensajes
        for (int i = 0; i < 300; i++) {
            String message = "{\"id\":" + i + ",\"brand\":\"BMW\", \"model\":\"F800GS\", \"price\":10000}";
            producer.sendMessage(String.valueOf(i), message);
            TimeUnit.SECONDS.sleep(1);
        }

        context.close();

    }
}