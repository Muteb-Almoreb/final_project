package org.example.trucksy.DTO;

public record GeocodeResult(
        Double lat, Double lon,
        String city, String district, String region,
        String countryCode,
        String label,
        String placeId
) {}

