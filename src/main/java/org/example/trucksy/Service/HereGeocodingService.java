package org.example.trucksy.Service;

import lombok.RequiredArgsConstructor;
import org.example.trucksy.DTO.GeocodeResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class HereGeocodingService {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${here.api.key}")
    private String apiKey;

    private static final double RIYADH_LAT = 24.7136;
    private static final double RIYADH_LON = 46.6753;
    private static final int    RIYADH_RADIUS_M = 50000;

    public GeocodeResult geocodeCityDistrict(String city, String district, String countryCode) {
        String cc = (countryCode == null || countryCode.isBlank()) ? "SAU" : countryCode;

        GeocodeResult r = tryStructured(Map.of(
                "country", cc,
                "city", safe(city),
                "district", safe(district)
        ));
        if (r != null) return r;

        r = tryStructured(Map.of(
                "country", cc,
                "city", safe(city),
                "subdistrict", safe(district)
        ));
        if (r != null) return r;

        r = tryQWithInCircle(safe(district) + " " + safe(city) + " " + cc);
        if (r != null) return r;

        r = tryDiscoverInRiyadh(safe(district) + " " + safe(city));
        if (r != null) return r;

        throw new IllegalArgumentException("Location not found: " + safe(district) + " " + safe(city));
    }

    private GeocodeResult tryStructured(Map<String, String> parts) {
        String qq = parts.entrySet().stream()
                .filter(e -> e.getValue() != null && !e.getValue().isBlank())
                .map(e -> e.getKey() + "=" + e.getValue())
                .collect(java.util.stream.Collectors.joining(";"));

        if (qq.isBlank()) return null;

        String url = UriComponentsBuilder.fromHttpUrl("https://geocode.search.hereapi.com/v1/geocode")
                .queryParam("qq", qq)
                .queryParam("in", "countryCode:" + parts.getOrDefault("country", "SAU"))
                .queryParam("lang", "ar")
                .queryParam("limit", 1)
                .queryParam("apiKey", apiKey)
                .toUriString();

        return parseGeocode(url);
    }

    private GeocodeResult tryQWithInCircle(String q) {
        String url = UriComponentsBuilder.fromHttpUrl("https://geocode.search.hereapi.com/v1/geocode")
                .queryParam("q", q)
                .queryParam("in", String.format("circle:%f,%f;r=%d", RIYADH_LAT, RIYADH_LON, RIYADH_RADIUS_M))
                .queryParam("lang", "ar")
                .queryParam("limit", 1)
                .queryParam("apiKey", apiKey)
                .toUriString();

        return parseGeocode(url);
    }

    private GeocodeResult tryDiscoverInRiyadh(String q) {
        String url = UriComponentsBuilder.fromHttpUrl("https://discover.search.hereapi.com/v1/discover")
                .queryParam("q", q.trim())
                .queryParam("at", RIYADH_LAT + "," + RIYADH_LON)
                .queryParam("lang", "ar")
                .queryParam("limit", 1)
                .queryParam("apiKey", apiKey)
                .toUriString();

        try {
            ResponseEntity<com.fasterxml.jackson.databind.JsonNode> resp =
                    restTemplate.exchange(url, HttpMethod.GET, null, com.fasterxml.jackson.databind.JsonNode.class);
            var items = resp.getBody().path("items");
            if (items.isArray() && items.size() > 0) {
                var first = items.get(0);
                var pos = first.path("position");
                var addr = first.path("address");
                return new GeocodeResult(
                        pos.path("lat").asDouble(),
                        pos.path("lng").asDouble(),
                        text(addr, "city"),
                        text(addr, "district"),
                        text(addr, "county"),
                        text(addr, "countryCode"),
                        text(first, "title"),
                        text(first, "id")
                );
            }
        } catch (Exception ignore) {}
        return null;
    }

    private GeocodeResult parseGeocode(String url) {
        try {
            ResponseEntity<com.fasterxml.jackson.databind.JsonNode> resp =
                    restTemplate.exchange(url, HttpMethod.GET, null, com.fasterxml.jackson.databind.JsonNode.class);
            var items = resp.getBody().path("items");
            if (!items.isArray() || items.size() == 0) return null;

            var first = items.get(0);
            var addr = first.path("address");
            var pos  = first.path("position");
            return new GeocodeResult(
                    pos.path("lat").asDouble(),
                    pos.path("lng").asDouble(),
                    text(addr, "city"),
                    text(addr, "district"),
                    text(addr, "county"),
                    text(addr, "countryCode"),
                    text(first, "label"),
                    text(first, "id")
            );
        } catch (Exception e) {
            return null;
        }
    }

    private static String text(com.fasterxml.jackson.databind.JsonNode n, String f) {
        var v = n.get(f);
        return v == null || v.isNull() ? null : v.asText();
    }
    private static String safe(String s) { return s == null ? "" : s.trim(); }
}

