package gg.nils.minecraftstatusapi.repository;

import gg.nils.minecraftstatusapi.model.Server;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ServerRepository extends MongoRepository<Server, ObjectId> {

    Optional<Server> findByName(String name);

}
