package ru.abradox.carsbusinesscard.benchmark;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

@Disabled
@Slf4j
public class ApiBenchmarkTest {

    @Test
    public void test() {

        int numRequests = 1000;
        String apiUrl = "https://cars.oficerov.tw1.ru/api/v1/unit";

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .GET()
                .build();

        log.info("Отправка {} GET-запросов на {}...", numRequests, apiUrl);

        long[] results = IntStream.range(0, numRequests)
                .mapToLong(i -> send(client, request))
                .toArray();
        List<Long> executionTimes = Arrays.stream(results).boxed().sorted().toList();

        log.info("Среднее время выполнения: {}", executionTimes.stream().mapToLong(Long::longValue).average().orElse(0));
        log.info("Время выполнения по процентилям:");
        log.info("  50-й процентиль: {}", calculatePercentile(executionTimes, 50.0));
        log.info("  90-й процентиль: {}", calculatePercentile(executionTimes, 90.0));
        log.info("  95-й процентиль: {}", calculatePercentile(executionTimes, 95.0));
        log.info("  99-й процентиль: {}", calculatePercentile(executionTimes, 99.0));
    }

    @SneakyThrows
    private static Long send(HttpClient client, HttpRequest request) {
        Instant start = Instant.now();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Instant end = Instant.now();
        if (response.statusCode() == 200) {
            return Duration.between(start, end).toMillis();
        } else {
            return Long.MAX_VALUE;
        }
    }

    private static double calculatePercentile(List<Long> times, double percentile) {
        int n = times.size();
        double rank = (percentile / 100.0) * (n - 1);
        int lowerIndex = (int) Math.floor(rank);
        int upperIndex = (int) Math.ceil(rank);
        if (lowerIndex == upperIndex) {
            return times.get(lowerIndex);
        } else {
            double fraction = rank - lowerIndex;
            return times.get(lowerIndex) + (times.get(upperIndex) - times.get(lowerIndex)) * fraction;
        }
    }
}