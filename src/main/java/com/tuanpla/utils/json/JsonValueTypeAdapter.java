/*
 *  Copyright 2022 by Tuanpla
 *  https://tuanpla.com
 */
package com.tuanpla.utils.json;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import static com.google.gson.stream.JsonToken.NULL;
import static com.google.gson.stream.JsonToken.NUMBER;
import static com.google.gson.stream.JsonToken.STRING;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;

/**
 *
 * @author tuanp
 */
public class JsonValueTypeAdapter extends TypeAdapter<JsonElement> {

    private static final TypeAdapter<JsonElement> jsonValueTypeAdapter = new JsonValueTypeAdapter();

    private JsonValueTypeAdapter() {
    }

    public static TypeAdapter<JsonElement> getJsonValueTypeAdapter() {
        return jsonValueTypeAdapter;
    }

    @Override
    public void write(final JsonWriter out, final JsonElement jsonElement) throws IOException {
        if (jsonElement == null || jsonElement.isJsonNull()) {
            out.nullValue();
            return;
        }
        if (jsonElement.isJsonPrimitive()) {
            JsonPrimitive jsonPrimitive = jsonElement.getAsJsonPrimitive();
            if (jsonPrimitive.isString()) {
                out.value(jsonPrimitive.getAsString());
            } else if (jsonPrimitive.isNumber()) {
                out.value(jsonPrimitive.getAsNumber());
            } else if (jsonPrimitive.isBoolean()) {
                out.value(jsonPrimitive.getAsBoolean());
            }
        } else if (jsonElement.isJsonArray()) {
            out.beginArray();
            JsonArray jsonArray = jsonElement.getAsJsonArray();
            for (JsonElement element : jsonArray) {
                write(out, element);
            }
            out.endArray();
        } else if (jsonElement.isJsonObject()) {
            out.beginObject();
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            for (String key : jsonObject.keySet()) {
                out.name(key);
                write(out, jsonObject.get(key));
            }
            out.endObject();
        } else {
            throw new IllegalStateException("Unexpected JsonElement type: " + jsonElement.getClass());
        }

    }

    @Override
    public JsonElement read(final JsonReader in) throws IOException {
        JsonToken jsonToken = in.peek();
        switch (jsonToken) {
            case BEGIN_ARRAY -> {
                JsonArray jsonArray = new JsonArray();
                in.beginArray();
                while (in.hasNext()) {
                    jsonArray.add(read(in));
                }
                in.endArray();
                return jsonArray;
            }
            case BEGIN_OBJECT -> {
                JsonObject jsonObject = new JsonObject();
                in.beginObject();
                while (in.hasNext()) {
                    String name = in.nextName();
                    jsonObject.add(name, read(in));
                }
                in.endObject();
                return jsonObject;
            }
            case STRING -> {
                return new JsonPrimitive(in.nextString());
            }
            case NUMBER -> {
                return new JsonPrimitive(in.nextDouble());
            }
            case BOOLEAN -> {
                return new JsonPrimitive(in.nextBoolean());
            }
            case NULL -> {
                in.nextNull();
                return JsonNull.INSTANCE;
            }
            default -> throw new JsonSyntaxException("Unexpected token: " + jsonToken);
        }
    }
}
