package gg.nils.minecraftstatusapi.repository;

import gg.nils.minecraftstatusapi.model.DataCollector;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.lang.NonNull;

public interface DataCollectorRepository extends MongoRepository<DataCollector, ObjectId> {
    DataCollector findByTokenLike(@NonNull String token);

}
