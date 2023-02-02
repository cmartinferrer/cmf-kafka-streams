package cmf.kafka.streams.producer;

import cmf.kafka.streams.domain.Moto;
import cmf.kafka.streams.producer.service.SaleProducerService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.val;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
public class ProducerApp {
    public static void main(String[] args) throws InterruptedException, JsonProcessingException {
        ConfigurableApplicationContext context = SpringApplication.run(ProducerApp.class, args);

        SaleProducerService producer = context.getBean(SaleProducerService.class);

        Random rand = new Random();
        List<Moto> motos = getMotoList();

        // Durante 5 minutos (300) va ha estar enviado mensajes
        String message = "";
        for (int i = 0; i < 300; i++) {
            val randomMoto = motos.get(rand.nextInt(motos.size()));
            message = new ObjectMapper().writeValueAsString(randomMoto);
            producer.sendMessage(String.valueOf(i), message);
            TimeUnit.SECONDS.sleep(1);
        }

        context.close();

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