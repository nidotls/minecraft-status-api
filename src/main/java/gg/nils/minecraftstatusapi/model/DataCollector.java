package gg.nils.minecraftstatusapi.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Data
@Builder
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Document("dataCollectors")
@CompoundIndexes({
        @CompoundIndex(useGeneratedName = true, unique = true, def = "{'name': 1}")
})
public class DataCollector {

    @Id
    private ObjectId id;

    private Long oldId;

    @NotNull
    private String name;

    @Email
    private String email;

    private String token;

    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant updatedAt;
}
