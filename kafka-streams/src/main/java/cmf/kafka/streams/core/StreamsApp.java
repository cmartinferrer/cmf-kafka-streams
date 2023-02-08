package cmf.kafka.streams.core;

import cmf.kafka.streams.domain.Moto;
import cmf.kafka.streams.domain.MotoDeserializer;
import cmf.kafka.streams.domain.MotoSerializer;
import lombok.val;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.Grouped;
import org.apache.kafka.streams.kstream.KeyValueMapper;
import org.apache.kafka.streams.kstream.Produced;

import java.util.Properties;

public class StreamsApp {
    public static final String INPUT_TOPIC = "sales";
    public static final String BRAND_COUNTER_TOPIC = "brandCounterTopic";
    public static final String MODEL_COUNTER_TOPIC = "modelCounterTopic";
    public static final String TOTAL_SALES_AMOUNT_TOPIC = "totalSalesAmountTopic";
    public static final String BOOTSTRAP_SERVER = "localhost:9092";
    private static final MotoSerializer motoSerializer = new MotoSerializer();
    private static final MotoDeserializer motoDeserializer = new MotoDeserializer();
    private static final Serde<Moto> motoSerde = Serdes.serdeFrom(motoSerializer, motoDeserializer);

    /**
     * Read messages from input topic and do the following actions with Kafka Streams:
     * <ul>
     *     <li>Count all events with same brand</li>
     *     <li>Count all events with same brand and model</li>
     *     <li>Calculate total amount of all sales</li>
     * </ul>
     * <p>
     * Example of input event from Kafka Topic: <p>
     * {@code `1: {"id":1,"brand":"BMW", "model":"F800GS", "price":10000}`}
     *
     */
    public static void main(String[] args) {

        val properties = getStreamsConfiguration(BOOTSTRAP_SERVER);
        val builder = new StreamsBuilder();

        // Count all events with same brand
        createCounterByKey(builder, BRAND_COUNTER_TOPIC, (k, v) -> v.getBrand());

        // Count all events with same brand and model
        createCounterByKey(builder, MODEL_COUNTER_TOPIC, (k, v) -> v.getBrand() + v.getModel());

        // Calculate total amount of all sales
        calculateTotalAmountOfAllSales(builder);


        val streams = new KafkaStreams(builder.build(), properties);
        streams.cleanUp();
        streams.start();

        // Add shutdown hook to respond to SIGTERM and gracefully close the Streams application.
        Runtime.getRuntime().addShutdownHook(new Thread(streams::close));

    }

    /**
     * Configure the Streams application.
     * <p>
     * Various Kafka Streams related settings are defined here such as the location of the target Kafka cluster to use.
     * Additionally, you could also define Kafka Producer and Kafka Consumer settings when needed.
     *
     * @param bootstrapServers Kafka cluster address
     * @return Properties getStreamsConfiguration
     */
    static Properties getStreamsConfiguration(final String bootstrapServers) {
        final Properties streamsConfiguration = new Properties();
        streamsConfiguration.put(StreamsConfig.APPLICATION_ID_CONFIG, "wordcount-lambda-example");
        streamsConfiguration.put(StreamsConfig.CLIENT_ID_CONFIG, "wordcount-lambda-example-client");
        streamsConfiguration.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        streamsConfiguration.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        streamsConfiguration.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        streamsConfiguration.put(StreamsConfig.COMMIT_INTERVAL_MS_CONFIG, 10 * 1000);
        streamsConfiguration.put(StreamsConfig.CACHE_MAX_BYTES_BUFFERING_CONFIG, 0);
        return streamsConfiguration;
    }

    static void createCounterByKey(final StreamsBuilder builder,
                                   final String targetTopic,
                                   KeyValueMapper<String, Moto, String> keyValueMapperr) {

        builder
                .stream(INPUT_TOPIC, Consumed.with(Serdes.String(), motoSerde))
                .selectKey(keyValueMapperr)
                .groupByKey()
                .count()
                .toStream()
                .map((key, value) -> KeyValue.pair(key, value.toString()))
                .to(targetTopic);

    }

    private static void calculateTotalAmountOfAllSales(StreamsBuilder builder) {
        builder.stream(INPUT_TOPIC, Consumed.with(Serdes.String(), motoSerde))
                // Set empty key and value to price
                .map((k, v) -> new KeyValue<>("", v.getPrice()))
                // Group by "empty" (all records)
                .groupByKey(Grouped.with(Serdes.String(), Serdes.Integer()))
                // Apply SUM aggregation
                .reduce(Integer::sum)
                // Write to stream specified by outputTopic
                .toStream().mapValues(v -> v.toString() + " total sales").to(TOTAL_SALES_AMOUNT_TOPIC, Produced.with(Serdes.String(), Serdes.String()));
    }

}


