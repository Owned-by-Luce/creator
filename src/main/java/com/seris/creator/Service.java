package com.seris.creator;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.SneakyThrows;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

/**
 * Service нэмэх хүсэлт
 *
 * @author Bayarkhuu.Luv
 */
@Data
public class Service {
    private String url;//Service url
    private String className;//Java класс нэр
    private String company;//Ямар компаны энэ Service-г нэмэх хүсэлт тавьсанг хадгалах
    private String description;//Энэ service-г нэмэх тайлбар
    private List<Attribute> attributes;//attributes
    private LocalDateTime created = LocalDateTime.now();//Хүсэлт илгээсэн огноо

    /**
     * String Json-oos Service object үүсгэх
     *
     * @param json Json String
     */
    @SneakyThrows
    public static Service createFromJson(String json) {
        if (json == null || json.isEmpty())
            throw new NullPointerException("'json' parameter null байна.");

        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> map = mapper.readValue(json, new TypeReference<>() {
        });

        Service model = new Service();
        List<Attribute> attributes = new ArrayList<>();//attributes

        BiFunction<String, Object, Type> typeCast = (type, obj) -> {
            switch (type) {
                case "Integer" -> {
                    return Type.integer;
                }
                case "Double" -> {
                    return Type.doubles;
                }
                case "ArrayList" -> {
                    return Type.array;
                }
                case "String" -> {
                    try {
                        LocalDateTime.parse(obj.toString());
                        return Type.date;
                    } catch (DateTimeParseException e) {
                        return Type.string;
                    }
                }
                default -> {
                    return Type.object;
                }
            }
        };

        map.forEach((k, v) -> {
            Attribute attribute = new Attribute(k, typeCast.apply(v.getClass().getSimpleName(), v));
            if (attribute.getType() == Type.array) {
                if (!((ArrayList<?>) v).isEmpty())
                    attribute.setListType(typeCast.apply(((ArrayList<?>) v).get(0).getClass().getSimpleName(), v));
                else attribute.setListType(Type.object);
            }
            attributes.add(attribute);
        });

        model.setAttributes(attributes);
        return model;
    }
}
