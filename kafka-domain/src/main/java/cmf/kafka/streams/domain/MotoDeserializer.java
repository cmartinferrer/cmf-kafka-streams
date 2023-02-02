package cmf.kafka.streams.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.serialization.Deserializer;

public class MotoDeserializer implements Deserializer<Moto> {
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Moto deserialize(String s, byte[] bytes) {
        try {
            return objectMapper.readValue(bytes, Moto.class);
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    @Override
    public Moto deserialize(String topic, Headers headers, byte[] data) {
        return deserialize(topic, data);
    }
}