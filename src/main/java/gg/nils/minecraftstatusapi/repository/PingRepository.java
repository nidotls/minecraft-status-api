package gg.nils.minecraftstatusapi.repository;

import gg.nils.minecraftstatusapi.model.Ping;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PingRepository extends JpaRepository<Ping, Long> {

}
