package gg.nils.minecraftstatusapi.model;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.time.Instant;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Document("pings")
public class Ping {

    @Id
    private ObjectId id;

    @DocumentReference(lazy = true)
    private Server server;

    @DocumentReference(lazy = true)
    private DataCollector dataCollector;

    @NotNull
    private Long count;

    @NotNull
    @CreatedDate
    private Instant createdAt;

    @NotNull
    @LastModifiedDate
    private Instant updatedAt;
}
