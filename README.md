
````
-- Levantar entorno
$ docker-compose up -d

-- Ver los contenedores
$ docker ps -a
CONTAINER ID   IMAGE                              COMMAND                  CREATED          STATUS                            PORTS                                         NAMES
86e0ca47ef83   confluentinc/cp-kafka:latest       "/etc/confluent/dock…"   22 seconds ago   Up 21 seconds                     9092/tcp, 0.0.0.0:29092->29092/tcp            cmf-kafka-streams-kafka-1
9e487d7804e8   confluentinc/cp-zookeeper:latest   "/etc/confluent/dock…"   22 seconds ago   Up 21 seconds                     2888/tcp, 3888/tcp, 0.0.0.0:22181->2181/tcp   cmf-kafka-streams-zookeeper-1

-- Para ver que hay conectividad con los puertos
 nc -z localhost 22181
Connection to localhost port 22181 [tcp/*] succeeded!
$ nc -z localhost 29092
Connection to localhost port 29092 [tcp/*] succeeded!

-- Para crear el topic
$  cmf-kafka-streams docker exec -it cmf-kafka-streams-kafka-1 kafka-topics --bootstrap-server localhost:9092 --create --replication-factor 1 -partitions 3 --topic sales
Created topic sales.

-- Para abrir un consumidor por linea de comandos
$  docker exec -it cmf-kafka-streams-kafka-1 kafka-console-consumer --bootstrap-server localhost:9092 --topic sales --property print.key=true --from-beginning

```