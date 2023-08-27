package gg.nils.minecraftstatusapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@EnableMongoAuditing
@SpringBootApplication
public class MinecraftStatusApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(MinecraftStatusApiApplication.class, args);
    }

}
