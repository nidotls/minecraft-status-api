package gg.nils.minecraftstatusapi.repository;

import gg.nils.minecraftstatusapi.model.Ping;
import gg.nils.minecraftstatusapi.model.Server;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.Instant;
import java.util.List;

public interface PingRepository extends MongoRepository<Ping, ObjectId> {

    @Aggregation({
            "{ $match: { server: :#{#server.getId()}," +
                    "    createdAt: { $gte: ?1, $lt: ?2 }    } }",
            "{ $group: { _id: { server: \"$server\"," +
                    "           date: { $dateToString: {" +
                    "                      date: \"$createdAt\"," +
                    "                      format: \"%Y-%m-%d\"," +
                    "                      timezone: \"Europe/Berlin\"} } }," +
                    "    min: {$min: \"$count\",}," +
                    "    max: {$max: \"$count\",}," +
                    "    avg: {$avg: \"$count\"}        } }"
    })
    List<Document> getPingsGroupedByDayFor(Server server, Instant timeGte, Instant timeLt);
}
