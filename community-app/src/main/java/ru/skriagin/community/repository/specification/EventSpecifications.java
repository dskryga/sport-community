package ru.skriagin.community.repository.specification;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;
import org.springframework.data.jpa.domain.Specification;
import ru.skriagin.community.dto.event.EventSearchDto;
import ru.skriagin.community.model.Event;

import java.util.ArrayList;
import java.util.List;

public final class EventSpecifications {

    private static final GeometryFactory GEOMETRY_FACTORY = new GeometryFactory(new PrecisionModel(), 4326);

    private EventSpecifications() {
    }

    public static Specification<Event> fromSearchParams(EventSearchDto params) {
        List<Specification<Event>> specifications = new ArrayList<>();

        addIfPresent(specifications, hasNameContaining(params.getName()));
        addIfPresent(specifications, hasCategoryId(params.getCategoryId()));
        addIfPresent(specifications, hasAuthorId(params.getAuthorId()));
        addIfPresent(specifications, isNearLocation(params.getLatitude(), params.getLongitude(), params.getRadiusKm()));

        return Specification.allOf(specifications);
    }

    private static void addIfPresent(List<Specification<Event>> specifications, Specification<Event> specification) {
        if (specification != null) {
            specifications.add(specification);
        }
    }

    private static Specification<Event> hasNameContaining(String name) {
        if (name == null || name.isBlank()) {
            return null;
        }
        String pattern = "%" + name.trim().toLowerCase() + "%";
        return (root, query, cb) -> cb.like(cb.lower(root.get("name")), pattern);
    }

    private static Specification<Event> hasCategoryId(Long categoryId) {
        if (categoryId == null) {
            return null;
        }
        return (root, query, cb) -> cb.equal(root.get("category").get("id"), categoryId);
    }

    private static Specification<Event> hasAuthorId(Long authorId) {
        if (authorId == null) {
            return null;
        }
        return (root, query, cb) -> cb.equal(root.get("author").get("id"), authorId);
    }

    private static Specification<Event> isNearLocation(Double latitude, Double longitude, Double radiusKm) {
        if (latitude == null || longitude == null || radiusKm == null) {
            return null;
        }
        Point origin = GEOMETRY_FACTORY.createPoint(new Coordinate(longitude, latitude));
        double radiusMeters = radiusKm * 1000;
        return (root, query, cb) -> cb.le(
                cb.function("ST_DistanceSphere", Double.class, root.get("location"), cb.literal(origin)),
                radiusMeters
        );
    }
}
