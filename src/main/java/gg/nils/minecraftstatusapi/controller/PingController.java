package gg.nils.minecraftstatusapi.controller;

import gg.nils.minecraftstatusapi.dto.PingSubmitDto;
import gg.nils.minecraftstatusapi.dto.PingTargetsDto;
import gg.nils.minecraftstatusapi.model.DataCollector;
import gg.nils.minecraftstatusapi.model.Ping;
import gg.nils.minecraftstatusapi.model.Server;
import gg.nils.minecraftstatusapi.repository.DataCollectorRepository;
import gg.nils.minecraftstatusapi.repository.PingRepository;
import gg.nils.minecraftstatusapi.repository.ServerRepository;
import jakarta.validation.Valid;
import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
public class PingController {

    private final DataCollectorRepository dataCollectorRepository;
    private final PingRepository pingRepository;
    private final ServerRepository serverRepository;

    public PingController(DataCollectorRepository dataCollectorRepository, PingRepository pingRepository, ServerRepository serverRepository) {
        this.dataCollectorRepository = dataCollectorRepository;
        this.pingRepository = pingRepository;
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

        Ping ping = new Ping();
        ping.setServer(server);
        ping.setDataCollector(dataCollector);
        ping.setCount(data.getCount());
        this.pingRepository.save(ping);

        return ResponseEntity.ok().build();
    }
}
