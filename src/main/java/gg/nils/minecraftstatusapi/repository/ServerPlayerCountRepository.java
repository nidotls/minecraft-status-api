package gg.nils.minecraftstatusapi.repository;

import gg.nils.minecraftstatusapi.model.ServerPlayerCount;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ServerPlayerCountRepository extends MongoRepository<ServerPlayerCount, ObjectId> {
}
