package com.seris.creator;

/**
 * DataTypes
 *
 * @author Bayarkhuu.Luv
 */
public enum Type {
    string("String"),
    array("Array"),
    integer("Integer"),
    doubles("Double"),
    object("Object"),
    date("LocalDateTime");

    private final String type;

    Type(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
