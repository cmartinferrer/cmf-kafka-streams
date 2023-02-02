package cmf.kafka.streams.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

@Getter
@Builder
@Jacksonized
public class Moto {
    private long id;
    private String brand;
    private String model;
    private Integer price;

}
