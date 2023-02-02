# Learning Kafka Streams

Pretty simple example exercise using Kafka Streams.

## Description
The excersise consists of the following modules:
- **Producer:** This module is responsible for sending messages to a Kafka topic.
- **Consumer:** This module is responsible for listening to the various available Kafka topics.
- **Streams:** This module is responible for processing information and publish the result in output topics.
- **Domain:** Contain the domain objects.


## Getting started

### Start environment

To launch a dev environment:
1. Make sure you have docker and docker-compose installed.
2. Go to project root directory and execute the following commnad:
````
$ docker-compose up -d
````
3. Check docker containers
````
$ docker ps -a
CONTAINER ID   IMAGE                              COMMAND                  CREATED        STATUS                    PORTS                                         NAMES
e6454b7ee81e   confluentinc/cp-kafka:latest       "/etc/confluent/dock…"   24 hours ago   Up 24 hours               0.0.0.0:9092->9092/tcp                        kafka
cf0c912a8fdd   confluentinc/cp-zookeeper:latest   "/etc/confluent/dock…"   24 hours ago   Up 24 hours               2888/tcp, 3888/tcp, 0.0.0.0:22181->2181/tcp   zookeeper
````
4. Check port connectivity
````
$ nc -z localhost 22181
Connection to localhost port 22181 [tcp/*] succeeded!

$ nc -z localhost 9092
Connection to localhost port 9092 [tcp/XmlIpcRegSvc] succeeded!
````
5. Create topics
````
$ docker exec -it kafka kafka-topics --bootstrap-server localhost:9092 --create --replication-factor 1 -partitions 3 --topic sales
$ docker exec -it kafka kafka-topics --bootstrap-server localhost:9092 --create --replication-factor 1 -partitions 3 --topic brandCounterTopic
$ docker exec -it kafka kafka-topics --bootstrap-server localhost:9092 --create --replication-factor 1 -partitions 3 --topic modelCounterTopic
$ docker exec -it kafka kafka-topics --bootstrap-server localhost:9092 --create --replication-factor 1 -partitions 3 --topic totalSalesAmountTopic
````

### Executing program

1. Run producer:
````
$ mvn compile exec:java -pl kafka-producer -Dexec.mainClass="cmf.kafka.streams.producer.ProducerApp"
````

3. Run consumer:
````
$ mvn compile exec:java -pl kafka-consumer -Dexec.mainClass="cmf.kafka.streams.consumer.ConsumerApp"
````

4. Run kafka Streams:
````
$ mvn compile exec:java -pl kafka-streams -Dexec.mainClass="cmf.kafka.streams.core.StreamsApp"
````
