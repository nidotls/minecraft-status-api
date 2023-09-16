package gg.nils.minecraftstatusapi.repository;

import gg.nils.minecraftstatusapi.model.PingGroupedByDay;
import gg.nils.minecraftstatusapi.model.Server;
import gg.nils.minecraftstatusapi.model.ServerPlayerCount;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.Instant;
import java.util.List;

public interface ServerPlayerCountRepository extends MongoRepository<ServerPlayerCount, ObjectId> {

    @Aggregation({
            "{ $match: { 'metadata.server': :#{#server.getId()}," +
                    "    timestamp:         { $gte: ?1, $lt: ?2 } } }",
            "{ $group: { _id: { server: '$metadata.server'," +
                    "           date: { $dateToString: {" +
                    "                      date: '$timestamp'," +
                    "                      format: '%Y-%m-%d'," +
                    "                      timezone: 'Europe/Berlin'} } }," +
                    "    min: {$min: '$value'}," +
                    "    max: {$max: '$value'}," +
                    "    avg: {$avg: '$value'}        } }",
            "{ $project: { date: '$_id.date', min: 1, max: 1, avg: 1, _id: 0 } }",
            "{ $sort: { date: 1 } }"
    })
    List<PingGroupedByDay> getPingsGroupedByDayFor(Server server, Instant timeGte, Instant timeLt);

    @Aggregation({
            "{ $match: { 'metadata.server': :#{#server.getId()}," +
                    "    timestamp:         { $gte: ?1, $lt: ?2 } } }",
            "{ $bucket: { groupBy: '$timestamp', boundaries: ?3, output: {" +
                    "     min: {$min: '$value'}," +
                    "     max: {$max: '$value'}," +
                    "     avg: {$avg: '$value'}" +
                    " } } }",
            "{ $project: { date: '$_id', min: 1, max: 1, avg: 1, _id: 0 } }",
            "{ $sort: { date : 1 } }"
    })
    List<PingGroupedByDay> findBuckets(Server server, Instant timeGE, Instant timeLT, List<Instant> bucketBoundaries);
}
