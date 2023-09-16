package gg.nils.minecraftstatusapi.model;

public record PingGroupedByDay(String date, Long min, Long max, Double avg) {
}
