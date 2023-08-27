package gg.nils.minecraftstatusapi.controller;

import gg.nils.minecraftstatusapi.dto.DataCollectorRegisterDto;
import gg.nils.minecraftstatusapi.model.DataCollector;
import gg.nils.minecraftstatusapi.repository.DataCollectorRepository;
import gg.nils.minecraftstatusapi.util.NameGenerator;
import jakarta.validation.Valid;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DataCollectorController {

    private final DataCollectorRepository dataCollectorRepository;

    public DataCollectorController(DataCollectorRepository dataCollectorRepository) {
        this.dataCollectorRepository = dataCollectorRepository;
    }

    @PostMapping("/v1/datacollector/register")
    public DataCollector register(@Valid @RequestBody DataCollectorRegisterDto data) {
        DataCollector dataCollector = new DataCollector();
        dataCollector.setName(NameGenerator.generate());
        dataCollector.setEmail(data.getEmail());
        dataCollector.setToken(RandomStringUtils.randomAlphanumeric(32));

        return this.dataCollectorRepository.save(dataCollector);
    }
}
