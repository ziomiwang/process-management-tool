package com.example.processmanagementtool.utlitiy;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.*;
import io.r2dbc.postgresql.codec.Json;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;

@Slf4j
@JsonComponent
public class PgJsonConverters {

    public static class Deserializer extends JsonDeserializer<Json> {

        @Override
        public Json deserialize(JsonParser p, DeserializationContext context) throws IOException {
            JsonNode value = context.readTree(p);
            return Json.of(value.toString());
        }
    }

    public static class Serializer extends JsonSerializer<Json> {

        @Override
        public void serialize(Json value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            String text = value.asString();
            JsonFactory factory = new JsonFactory();
            JsonParser parser = factory.createParser(text);
            TreeNode node = gen.getCodec().readTree(parser);
            serializers.defaultSerializeValue(node, gen);
        }
    }
}