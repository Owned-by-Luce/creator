package com.seris.creator;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Attribute object
 *
 * @author Bayarkhuu.Luv
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Attribute {
    private String name;//attribute-н нэр
    private Type type;//attribute-н төрөл

    private Type listType;
    private String listName;
    private List<Attribute> objects;

    public Attribute(String name, Type type, Type listType) {
        this.name = name;
        this.type = type;
        this.listType = listType;
    }

    public Attribute(String name, Type type) {
        this.name = name;
        this.type = type;
    }

    public Attribute(String name, Type type, List<Attribute> objects) {
        this.name = name;
        this.type = type;
        this.objects = objects;
    }
}
