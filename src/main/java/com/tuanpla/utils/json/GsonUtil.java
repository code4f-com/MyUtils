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
import com.tuanpla.utils.config.PublicConfig;
import static com.tuanpla.utils.json.JsonValueTypeAdapter.getJsonValueTypeAdapter;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import javax.json.JsonValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author tuanp https://www.mkyong.com/java/how-to-parse-json-with-GSON/
 */
public class GsonUtil {

    private static Logger logger = LoggerFactory.getLogger(PublicConfig.MAIN_LOGGER_NAME);
//    https://www.baeldung.com/gson-json-to-map
//    GsonBuilder gsonBuilder = new GsonBuilder().serializeNulls ();
//    Gson GSON = gsonBuilder.create();
    private static final Gson gson = new GsonBuilder()
            .setDateFormat("dd/MM/yyyy")
            // .registerTypeAdapter(Double.class, new DoubleJsonSerializer()) // => Thằng này làm cho Double thành long :))
            .registerTypeHierarchyAdapter(JsonValue.class, getJsonValueTypeAdapter())
            .create();

    ///********************
//    static Gson GSON = new GsonBuilder()
//            .setDateFormat(DateFormat.TIMEZONE_FIELD)
//            .registerTypeAdapter(Timestamp.class, new MyDateTypeAdapter())
//            .create();
    public static class DoubleJsonSerializer implements JsonSerializer<Double> {

        @Override
        public JsonElement serialize(final Double src, final Type typeOfSrc, final JsonSerializationContext context) {
            String text = String.format("%.0f", src);
//            LogUtils.debug("context=" + context.toString());
//            LogUtils.debug("src=" + src);
            Long l = Long.valueOf(text);
            return new JsonPrimitive(l);
//            BigDecimal bigValue = BigDecimal.valueOf(src);
//            return new JsonPrimitive(bigValue.toPlainString());
        }
    }

    public static Gson getGson() {
        return gson;
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

    public static Boolean getValueBoolean(String jsonStr, String key) {
        try {
            JsonObject obj = gson.fromJson(jsonStr, JsonObject.class);
            return obj.get(key).getAsBoolean();
        } catch (JsonSyntaxException e) {
            throw new JsonSyntaxException(String.format("[JSONParser] Failed to get \"%s\"  from JSON object", key));
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

    public static String toJson(ArrayList<?> obj, String key) {
        try {
            return gson.toJson(obj);
        } catch (JsonSyntaxException e) {
            throw new JsonSyntaxException(String.format("[JSONParser] Failed to convert \"%s\" to JSON array", key));
        }
    }

    public static String toJson(Object obj) {
        if (obj != null) {
            return gson.toJson(obj);
        } else {
            return "{}";
        }
    }

    public static String toJson(Object obj, Type typeOfSrc) {
        if (obj != null) {
            return gson.toJson(obj, typeOfSrc);
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

    /**
     * Type listType = new TypeToken<List<Class>>() {}.getType();
     *
     * @param obj
     * @param typeOfSrc
     * @return
     */
    public static String toJsonArr(Object obj, Type typeOfSrc) {
        if (obj != null) {
            String str = gson.toJson(obj, typeOfSrc);
            return Objects.isNull(str) ? "[]" : str;
        } else {
            return "[]";
        }
    }

    public static String toJsonTreeArr(Object obj) {
        if (obj != null) {
            JsonElement jelement = gson.toJsonTree(obj);
            String str = jelement.toString();
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
        try (FileWriter writer = new FileWriter(path)) {
            _gson.toJson(obj, writer);
        } catch (IOException e) {
            logger.error(LogUtils.getLogMessage(e));
        }
    }

    // File to Java objects
    public static <T> T jsonFileToObject(String path, Class<T> clazz) {
        T result = null;
        try (Reader reader = new FileReader(path)) {
            result = gson.fromJson(reader, clazz);
        } catch (IOException e) {
            logger.error(LogUtils.getLogMessage(e));
        }
        return result;
    }

    public static JsonObject toJsonObject(String content) {
        try {
            return JsonParser.parseString(content).getAsJsonObject();
        } catch (JsonSyntaxException e) {
            logger.error(LogUtils.getLogMessage(e));
            return null;
        }
    }

    public static <T> T toObject(String content) {
        T result = null;
        try {
            result = gson.fromJson(content, new TypeToken<T>() {
            }.getType());
        } catch (JsonSyntaxException e) {
            logger.error(LogUtils.getLogMessage(e));
        }
        return result;
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

    public static <T> ArrayList<T> toArrayList(String content, Class<T> clazz) {
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

    public static <T> List<T> toList(String content, Class<T> clazz) {
        List<T> result = new ArrayList<>();
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

    public static <T> List<T> toList_1(String jsonString, Class<T> clazz) {
        Type listType = new TypeToken<List<T>>() {
        }.getType();
        List<T> dataList = gson.fromJson(jsonString, listType);
        return dataList;
    }

    public static <T> List<T> arrToList(String content, Class<T[]> clazz) {
        List<T> result = new ArrayList<>();
        try {
            T[] arr = gson.fromJson(content, clazz);
            result = Arrays.asList(arr);
        } catch (JsonSyntaxException e) {
            logger.error(LogUtils.getLogMessage(e));
        }
        return result;
    }

    public static <T> Map<String, List<T>> toMapList(String content, Class<T> clazz) {
        Map<String, List<T>> result = null;
        try {
            Map<String, List<T>> mapNew = GsonUtil.toObject(content);
            if (mapNew != null) {
                result = new HashMap<>();
                Set<String> keys = mapNew.keySet();
                for (String key : keys) {
                    List<T> oneList = mapNew.get(key);
                    if (oneList != null) {
                        List<T> listNew = toList(toJson(oneList), clazz);
                        result.put(key, listNew);
                    }
                }
            }
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
