# kafka-streams-demo

This Kafka Streams demo is based on a real world streams application I developed while working on the bitcoin.com 
mining pool. The mining pool already had alerting on the overall pool hash rate, but I wanted to add realtime alerting on the individual 
user hash rate. User share requests from all the Stratum servers were already being sent to a Kafka cluster in the 
backend, so I was able to very quickly write a Kafka Streams application with Spring that could calculate the 1 minute 
user hash rate in realtime for all users in the pool and output the events to a Kafka topic.

Note, that when using Spring Cloud with the Kafka Streams API, all I needed to write to get the required functionality was one method and add the 
required properties to a yaml file.