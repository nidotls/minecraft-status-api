package gg.nils.minecraftstatusapi.repository;

import gg.nils.minecraftstatusapi.model.Server;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ServerRepository extends MongoRepository<Server, ObjectId> {

}
