package gg.nils.minecraftstatusapi.controller;

import gg.nils.minecraftstatusapi.dto.PingSubmitDto;
import gg.nils.minecraftstatusapi.dto.PingTargetsDto;
import gg.nils.minecraftstatusapi.model.*;
import gg.nils.minecraftstatusapi.repository.DataCollectorRepository;
import gg.nils.minecraftstatusapi.repository.PingRepository;
import gg.nils.minecraftstatusapi.repository.ServerPlayerCountRepository;
import gg.nils.minecraftstatusapi.repository.ServerRepository;
import jakarta.validation.Valid;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
public class PingController {

    private final DataCollectorRepository dataCollectorRepository;
    private final PingRepository pingRepository;
    private final ServerPlayerCountRepository serverPlayerCountRepository;
    private final ServerRepository serverRepository;

    public PingController(DataCollectorRepository dataCollectorRepository, PingRepository pingRepository, ServerPlayerCountRepository serverPlayerCountRepository, ServerRepository serverRepository) {
        this.dataCollectorRepository = dataCollectorRepository;
        this.pingRepository = pingRepository;
        this.serverPlayerCountRepository = serverPlayerCountRepository;
        this.serverRepository = serverRepository;
    }

    @PostMapping("/v1/ping/targets")
    public ResponseEntity<List<Server>> targets(@Valid @RequestBody PingTargetsDto data) {
        DataCollector dataCollector = this.dataCollectorRepository.findByTokenLike(data.getToken());

        if (dataCollector == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        List<Server> result = this.serverRepository.findAll().stream()
                .filter(server -> !server.isArchived())
                .toList();

        return ResponseEntity.ok(result);
    }

    @PostMapping("/v1/ping/submit")
    public ResponseEntity<?> submit(@Valid @RequestBody PingSubmitDto data) {
        DataCollector dataCollector = this.dataCollectorRepository.findByTokenLike(data.getToken());

        if (dataCollector == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        if (!ObjectId.isValid(data.getServerId())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        Optional<Server> optionalServer = this.serverRepository.findById(new ObjectId(data.getServerId()));

        if (optionalServer.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        if (optionalServer.get().isArchived()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        Server server = optionalServer.get();
        Instant now = Instant.now();

        Ping ping = new Ping();
        ping.setServer(server);
        ping.setDataCollector(dataCollector);
        ping.setCount(data.getCount());
        ping.setCreatedAt(now);
        this.pingRepository.save(ping);

        ServerPlayerCount serverPlayerCount = new ServerPlayerCount();
        serverPlayerCount.setTimestamp(now);
        serverPlayerCount.setMetadata(new ServerPlayerCount.Metadata(server));
        serverPlayerCount.setValue(data.getCount());
        serverPlayerCount.setDataCollector(dataCollector);
        this.serverPlayerCountRepository.save(serverPlayerCount);

        return ResponseEntity.ok().build();
    }

    // TEST ENDPOINTS START

    @GetMapping("/v1/ping/summary/old")
    public ResponseEntity<?> oldSummary() {
        long start = System.nanoTime();

        Optional<Server> server = this.serverRepository.findByName("opsucht-java");

        if (server.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        List<PingGroupedByDay> documents = this.pingRepository.getPingsGroupedByDayFor(server.get(), Instant.now().minus(7, ChronoUnit.DAYS), Instant.now());

        long took = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start);

        return ResponseEntity.ok(Map.of(
                "took", took,
                "data", documents
        ));
    }

    @GetMapping("/v1/ping/summary/new1")
    public ResponseEntity<?> newOneSummary() {
        long start = System.nanoTime();

        Optional<Server> server = this.serverRepository.findByName("opsucht-java");

        if (server.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        List<PingGroupedByDay> documents = this.serverPlayerCountRepository.getPingsGroupedByDayFor(server.get(), Instant.now().minus(7, ChronoUnit.DAYS), Instant.now());

        long took = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start);

        return ResponseEntity.ok(Map.of(
                "took", took,
                "data", documents
        ));
    }

    @GetMapping("/v1/ping/summary/new2")
    public ResponseEntity<?> newTwoSummary() {
        long start = System.nanoTime();

        Optional<Server> server = this.serverRepository.findByName("opsucht-java");

        if (server.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        LocalDate startDate = LocalDate.now().minusDays(7);
        LocalDate endDate = LocalDate.now().plusDays(1);

        int days = (int) startDate.until(endDate, ChronoUnit.DAYS) + 1;

        List<Instant> boundaries = Stream.iterate(startDate, d -> d.plusDays(1))
                .limit(days)
                .map(localDate -> localDate.atTime(LocalTime.MIN).toInstant(ZoneOffset.UTC))
                .collect(Collectors.toList());

        Instant from = boundaries.get(0);
        Instant to = boundaries.get(boundaries.size() - 1);

        List<PingGroupedByDay> documents = this.serverPlayerCountRepository.findBuckets(server.get(), from, to, boundaries);

        long took = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start);

        return ResponseEntity.ok(Map.of(
                "took", took,
                "data", documents
        ));
    }

    // TEST ENDPOINTS END
}
