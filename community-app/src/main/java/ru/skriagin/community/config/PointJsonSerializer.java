package ru.skriagin.community.config;

import org.locationtech.jts.geom.Point;
import tools.jackson.core.JacksonException;
import tools.jackson.core.JsonGenerator;
import tools.jackson.databind.SerializationContext;
import tools.jackson.databind.ser.std.StdScalarSerializer;

public class PointJsonSerializer extends StdScalarSerializer<Point> {

    public PointJsonSerializer() {
        super(Point.class);
    }

    @Override
    public void serialize(Point point, JsonGenerator gen, SerializationContext ctxt) throws JacksonException {
        gen.writeStartObject();
        gen.writeNumberProperty("x", point.getX());
        gen.writeNumberProperty("y", point.getY());
        gen.writeEndObject();
    }
}
