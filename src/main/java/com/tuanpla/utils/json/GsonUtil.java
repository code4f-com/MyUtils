/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tuanpla.utils.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.tuanpla.utils.logging.LogUtils;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author tuanp https://www.mkyong.com/java/how-to-parse-json-with-GSON/
 */
public class GsonUtil {

    static Logger logger = LoggerFactory.getLogger(GsonUtil.class);
//    https://www.baeldung.com/gson-json-to-map
//    GsonBuilder gsonBuilder = new GsonBuilder().serializeNulls ();
//    Gson GSON = gsonBuilder.create();
    private static final Gson gson = new GsonBuilder()
            .setDateFormat("dd/MM/yyyy")
            .registerTypeAdapter(Double.class, new DoubleJsonSerializer())
            .create();

//    static final Gson GSON = new GsonBuilder().excludeFieldsWithModifiers(Modifier.STATIC, Modifier.PROTECTED)
//            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
//            .create();
    ///********************
//    static Gson GSON = new GsonBuilder()
//            .setDateFormat(DateFormat.TIMEZONE_FIELD)
//            .registerTypeAdapter(Timestamp.class, new MyDateTypeAdapter())
//            .create();
    public static class DoubleJsonSerializer implements JsonSerializer<Double> {

        @Override
        public JsonElement serialize(final Double src, final Type typeOfSrc, final JsonSerializationContext context) {
            String text = String.format("%.0f", src);
            System.out.println("context=" + context.toString());
            System.out.println("src=" + src);
            Long l = Long.valueOf(text);
            return new JsonPrimitive(l);
//            BigDecimal bigValue = BigDecimal.valueOf(src);
//            return new JsonPrimitive(bigValue.toPlainString());
        }
    }

    public class MyDateTypeAdapter implements JsonDeserializer<Timestamp> {

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
        @Override
        public Timestamp deserialize(JsonElement json, Type type, JsonDeserializationContext jdc) throws JsonParseException {
            if (!Objects.isNull(json) && json.isJsonObject()) {
                JsonObject jobj = json.getAsJsonObject();
                long time;
                if (jobj.has("time")) {
                    time = jobj.get("time").getAsLong();
                } else {
                    Instant instant = Instant.ofEpochMilli(json.getAsJsonPrimitive().getAsLong());
                    time = instant.toEpochMilli();
                }
                return new Timestamp(time);
            } else {
                return null;
            }
        }
    }

    public static String getValue(String jsonStr, String key) {
        try {
            JsonObject obj = gson.fromJson(jsonStr, JsonObject.class);
            return obj.get(key).getAsString();
        } catch (JsonSyntaxException e) {
            throw new JsonSyntaxException(String.format("[JSONParser] Failed to get \"%s\"  from JSON object", key));
        }
    }

    public static int getValueInt(String jsonStr, String key) {
        try {
            JsonObject obj = gson.fromJson(jsonStr, JsonObject.class);
            return obj.get(key).getAsInt();
        } catch (JsonSyntaxException e) {
            throw new JsonSyntaxException(String.format("[JSONParser] Failed to get \"%s\"  from JSON object", key));
        }
    }

    public static double getValueDouble(String jsonStr, String key) {
        try {
            JsonObject obj = gson.fromJson(jsonStr, JsonObject.class);
            return obj.get(key).getAsDouble();
        } catch (JsonSyntaxException e) {
            throw new JsonSyntaxException(String.format("[JSONParser] Failed to get \"%s\"  from JSON object", key));
        }
    }

    public static float getValueFloat(String jsonStr, String key) {
        try {
            JsonObject obj = gson.fromJson(jsonStr, JsonObject.class);
            return obj.get(key).getAsFloat();
        } catch (JsonSyntaxException e) {
            throw new JsonSyntaxException(String.format("[JSONParser] Failed to get \"%s\"  from JSON object", key));
        }
    }

    public static String toJson(ArrayList<?> obj, String key) {
        try {
            return gson.toJson(obj);
        } catch (JsonSyntaxException e) {
            throw new JsonSyntaxException(String.format("[JSONParser] Failed to convert \"%s\" to JSON array", key));
        }
    }

    public static boolean isJSONValid(String jsonInString) {
        try {
            gson.fromJson(jsonInString, Object.class);
            return true;
        } catch (com.google.gson.JsonSyntaxException ex) {
            return false;
        }
    }

    public static String toJson(Object obj) {
        if (obj != null) {
            return gson.toJson(obj);
        } else {
            return "";
        }
    }

    public static String toJsonObj(Object obj) {
        if (obj != null) {
            return gson.toJson(obj);
        } else {
            return "{}";
        }
    }

//    public  String toJsonEscape(Object obj) {
//        if (obj != null) {
//            String str = gson.toJson(obj);
//            str = JSONValue.escape(str);
//            return str;
//        } else {
//            return "{}";
//        }
//    }
    public static String toJsonArr(Object obj) {
        if (obj != null) {
            String str = gson.toJson(obj);
            return Objects.isNull(str) ? "[]" : str;
        } else {
            return "[]";
        }
    }

    public static String toJsonArr(Object obj, Type typeOfSrc) {
        if (obj != null) {
            String str = gson.toJson(obj, typeOfSrc);
            return Objects.isNull(str) ? "[]" : str;
        } else {
            return "[]";
        }
    }

    public static String toJsonInclueNull(Object obj) {
        Gson _gson = new GsonBuilder()
                .serializeNulls()
                .create();
        return _gson.toJson(obj);
    }

    public static String toJsonVersion(Object obj, double v) {
        Gson _gson = new GsonBuilder()
                .serializeNulls()
                .setVersion(v) // version <= 2.0 will be included.
                .create();
        return _gson.toJson(obj);
    }

    // Java objects to File
    public static void toFileJson(Object obj, String path) {
        Gson _gson = new GsonBuilder().setPrettyPrinting().create();
        try ( FileWriter writer = new FileWriter(path)) {
            _gson.toJson(obj, writer);
        } catch (IOException e) {
            logger.error(LogUtils.getLogMessage(e));
        }
    }

    public static <T> T toObject(String content, Class<T> clazz) {
        T result = null;
        try {
            result = gson.fromJson(content, clazz);
        } catch (JsonSyntaxException e) {
            logger.error(LogUtils.getLogMessage(e));
        }
        return result;
    }

    public static JsonObject toObject(String content) {
        JsonObject result = null;
        try {
            result = JsonParser.parseString(content).getAsJsonObject();
        } catch (JsonSyntaxException e) {
            logger.error(LogUtils.getLogMessage(e));
        }
        return result;
    }

    public static <T> T jsonFileToObject(String path, Class<T> clazz) {
        T result = null;
        try ( Reader reader = new FileReader(path)) {
            result = gson.fromJson(reader, clazz);
        } catch (IOException e) {
            logger.error(LogUtils.getLogMessage(e));
        }
        return result;
    }

    public static <T> T toObjectArray(String content) {
        T result = null;
        try {
            result = gson.fromJson(content, new TypeToken<T>() {
            }.getType());
        } catch (JsonSyntaxException e) {
            logger.error(LogUtils.getLogMessage(e));
        }
        return result;
    }

    public static <T> ArrayList<T> toObjectArray(String content, Class<T> clazz) {
        ArrayList<T> result = new ArrayList<>();
        try {
            JsonArray arrs = JsonParser.parseString(content).getAsJsonArray();
            for (JsonElement jsonElement : arrs) {
                T tmp = gson.fromJson(jsonElement, clazz);
                if (tmp != null) {
                    result.add(tmp);
                }
            }
        } catch (JsonSyntaxException e) {
            logger.error(LogUtils.getLogMessage(e));
        }
        return result;
    }

    public static <T> ArrayList<T> toObjectArray_1(String content, Class<T[]> clazz) {
        ArrayList<T> result = new ArrayList<>();
        try {
            T[] arr = gson.fromJson(content, clazz);
            List<T> tmp = Arrays.asList(arr);
            tmp.forEach((one) -> {
                result.add(one);
            });
        } catch (JsonSyntaxException e) {
            logger.error(LogUtils.getLogMessage(e));
        }
        return result;
    }

    public static Map<String, Object> toMap(String content) {
        Map<String, Object> map = null;
        try {
            map = gson.fromJson(content, new TypeToken<Map<String, Object>>() {
            }.getType());
        } catch (JsonSyntaxException e) {
            logger.error(LogUtils.getLogMessage(e));
        }
        return map;
    }

}
