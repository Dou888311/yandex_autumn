package dou888311.MapperModule;

import com.fasterxml.jackson.core.JsonGenerator;
import org.testcontainers.shaded.com.fasterxml.jackson.core.JsonProcessingException;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.JsonSerializer;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.SerializerProvider;


import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateTimeSerialization extends JsonSerializer<LocalDateTime> {

    private final DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");

    @Override
    public void serialize(LocalDateTime localDateTime, org.testcontainers.shaded.com.fasterxml.jackson.core.JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
        jsonGenerator.writeString(localDateTime.format(format));
    }
}
