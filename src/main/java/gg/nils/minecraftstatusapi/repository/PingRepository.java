package gg.nils.minecraftstatusapi.repository;

import gg.nils.minecraftstatusapi.model.Ping;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PingRepository extends MongoRepository<Ping, ObjectId> {

}
