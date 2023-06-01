/*
 *  Copyright 2022 by Tuanpla
 *  https://tuanpla.com
 */
package com.tuanpla.utils.json;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.tuanpla.utils.logging.LogUtils;
import java.lang.reflect.Type;

/**
 *
 * @author tuanp
 */
public class MyNumberTypeAdapter implements JsonDeserializer<Number> {

    @Override
    public Number deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        if (json != null && json.isJsonPrimitive() && json.getAsJsonPrimitive().isNumber()) {
            JsonPrimitive jsonPrimitive = json.getAsJsonPrimitive();
            LogUtils.debug("Vao day:" + jsonPrimitive);
            if (jsonPrimitive.getAsNumber().doubleValue() % 1 == 0) {
                // Return Long if the number is an integer
                return jsonPrimitive.getAsLong();
            } else {
                // Return Double if the number is a decimal
                return jsonPrimitive.getAsDouble();
            }
        } else {
            return null;
        }
    }

    //        @Override
//        public void write(JsonWriter out, Timestamp value) throws IOException {
//            if (value == null) {
//                out.nullValue();
//            } else {
//                MyConfig.debugOut(DE_BUG,"value write:" + value);
//                out.value(value.getTime() / 1000);
//            }
//        }
//
//        @Override
//        public Timestamp read(JsonReader in) throws IOException {
//            if (in != null) {
////                MyConfig.debugOut(DE_BUG,"JsonReader in.nextString:" + in.nextString());
//                MyConfig.debugOut(DE_BUG,"JsonReader in.isLenient:" + in.isLenient());
//                MyConfig.debugOut(DE_BUG,"JsonReader in.peek:" + in.peek());
//                Instant instant = Instant.ofEpochMilli(in.getAsJsonPrimitive().getAsLong());
//                return new Timestamp(System.currentTimeMillis());
//            } else {
//                return null;
//            }
//        }
}
