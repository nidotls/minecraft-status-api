package gg.nils.minecraftstatusapi.controller;

import gg.nils.minecraftstatusapi.model.ServerPlayerCount;
import gg.nils.minecraftstatusapi.repository.PingRepository;
import gg.nils.minecraftstatusapi.repository.ServerPlayerCountRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
public class MigrateController {

    private final PingRepository pingRepository;
    private final ServerPlayerCountRepository serverPlayerCountRepository;

    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public MigrateController(PingRepository pingRepository, ServerPlayerCountRepository serverPlayerCountRepository) {
        this.pingRepository = pingRepository;
        this.serverPlayerCountRepository = serverPlayerCountRepository;
    }

    @GetMapping("/v1/migrate")
    public ResponseEntity<?> migrate() {
        this.executor.submit(() -> {
            System.out.println("Starting migration...");

            AtomicInteger atomicInteger = new AtomicInteger();

            this.pingRepository.findAll().forEach(ping -> {
                Optional<ServerPlayerCount> optional = this.serverPlayerCountRepository.findById(ping.getId());

                if (optional.isPresent()) {
                    return;
                }

                ServerPlayerCount serverPlayerCount = new ServerPlayerCount();
                serverPlayerCount.setId(ping.getId());
                serverPlayerCount.setTimestamp(ping.getCreatedAt());
                serverPlayerCount.setMetadata(new ServerPlayerCount.Metadata(ping.getServer()));
                serverPlayerCount.setValue(ping.getCount());
                serverPlayerCount.setDataCollector(ping.getDataCollector());
                this.serverPlayerCountRepository.save(serverPlayerCount);

                int i = atomicInteger.incrementAndGet();

                if(i % 500 == 0) {
                    System.out.println("Migrated " + i + " entries");
                }
            });

            System.out.println("Finished migration of " + atomicInteger.get() + " entries!");
        });

        return ResponseEntity.ok().build();
    }
}
