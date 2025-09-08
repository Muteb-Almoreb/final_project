package org.example.trucksy.DTOOut;

public record NearbyTruckResponse(
        Integer id,
        String name,
        String description,
        String category,
        Double latitude,
        Double longitude,
        double distanceKm,
        String imageUrl

) {}

