package com.podowd;

import com.google.gson.Gson;
import com.podowd.domain.UserShare;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Suppressed;
import org.apache.kafka.streams.kstream.TimeWindows;
import org.apache.kafka.streams.state.WindowStore;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

@SpringBootApplication
public class User1MinHashrateStreamsApp {

    public static final String TIME_WINDOWED_USER_HASHRATE_STORE = "time-windowed-user-hashrate-store";

    public static void main(String[] args) {
        SpringApplication.run(User1MinHashrateStreamsApp.class, args);
    }

    public static class UserHashrateProcessorApplication {

        public static final int WINDOW_SIZE_1MIN_IN_MS = 60000; // Kafka Streams is using a 1 minute window
        private static final Gson gson = new Gson();
        private final BigDecimal sharesAtDifficultyOne = BigDecimal.valueOf(Math.pow(2, 32));
        private final BigDecimal peta = BigDecimal.valueOf(Math.pow(10, 15));
        private final BigDecimal secondsIn1Minute = BigDecimal.valueOf(TimeUnit.MINUTES.toSeconds(1));

        @Bean
        public Function<KStream<String, String>, KStream<String, Double>> process() {
            return userShares -> userShares
                    .groupByKey()
                    .windowedBy(TimeWindows.of(Duration.ofMillis(WINDOW_SIZE_1MIN_IN_MS)).grace(Duration.ofMillis(0)))
                    .aggregate(
                            () -> 0l,
                            (user, userShare, aggPow) -> aggPow + getPow(userShare),
                            Materialized.<String, Long, WindowStore<Bytes, byte[]>>as(TIME_WINDOWED_USER_HASHRATE_STORE)
                                    .withKeySerde(Serdes.String())
                                    .withValueSerde(Serdes.Long()))
                    .suppress(Suppressed.untilWindowCloses(Suppressed.BufferConfig.unbounded()))
                    .mapValues((user, pow) -> calcHashrate(pow))
                    .toStream()
                    .map((user, hashrate) -> KeyValue.pair(user.key(), hashrate));
        }

        private long getPow(String event) {
            return gson.fromJson(event, UserShare.class).getProofOfWork();
        }

        private double calcHashrate(long pow) {

            return BigDecimal.valueOf(pow).divide(secondsIn1Minute, RoundingMode.HALF_UP)
                    .multiply(sharesAtDifficultyOne)
                    .divide(peta, 4, RoundingMode.HALF_UP).doubleValue();
        }
    }
}
