package gg.nils.minecraftstatusapi.model;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.bson.types.ObjectId;
import org.jetbrains.annotations.Nullable;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.Objects;

@Data
@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@Document("servers")
@CompoundIndexes({
        @CompoundIndex(useGeneratedName = true, unique = true, def = "{'name': 1}")
})
public class Server {

    @Id
    private ObjectId id;

    @Nullable
    private Long oldId;

    @NotNull
    private String name;

    @NotNull
    private String address;

    @NotNull
    private Type type;

    private boolean archived = false;

    @NotNull
    @CreatedDate
    private Instant createdAt;

    @NotNull
    @LastModifiedDate
    private Instant updatedAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Server server = (Server) o;

        return Objects.equals(id, server.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    public enum Type {
        JAVA,
        BEDROCK
    }
}
