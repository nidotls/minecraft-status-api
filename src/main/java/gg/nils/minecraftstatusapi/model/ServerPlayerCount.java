package gg.nils.minecraftstatusapi.model;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DocumentReference;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.TimeSeries;
import org.springframework.data.mongodb.core.timeseries.Granularity;

import java.time.Instant;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@TimeSeries(collection = "serverPlayerCounts",
        timeField = "timestamp", metaField = "metadata",
        granularity = Granularity.SECONDS)
public class ServerPlayerCount {

    @Id
    private ObjectId id;

    @NotNull
    @CreatedDate
    @Field("timestamp")
    private Instant timestamp;

    @NotNull
    @Field("metadata")
    private Metadata metadata;

    @NotNull
    private Long value;

    @DocumentReference(lazy = true)
    private DataCollector dataCollector;

    @Getter
    @Setter
    @ToString
    @AllArgsConstructor
    @RequiredArgsConstructor
    public static class Metadata {

        @DocumentReference(lazy = true)
        private Server server;

    }
}
