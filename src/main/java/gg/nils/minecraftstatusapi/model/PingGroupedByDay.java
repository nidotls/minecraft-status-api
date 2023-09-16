package gg.nils.minecraftstatusapi.model;

import org.bson.types.ObjectId;

public record PingGroupedByDay(ObjectId server, String date, Long min, Long max, Double avg) {
}
