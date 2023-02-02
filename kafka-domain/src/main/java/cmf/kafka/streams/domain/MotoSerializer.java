package cmf.kafka.streams.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.serialization.Serializer;

public class MotoSerializer implements Serializer<Moto> {
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public byte[] serialize(String topic, Moto moto) {
        try {
            return objectMapper.writeValueAsBytes(moto);
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    @Override
    public byte[] serialize(String topic, Headers headers, Moto data) {
        return serialize(topic, data);
    }


}
