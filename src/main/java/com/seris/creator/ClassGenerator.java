package com.seris.creator;

import lombok.SneakyThrows;

import java.io.FileOutputStream;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Json-оос Java class үүсгэх
 *
 * @author Bayarkhuu.Luv
 */
public class ClassGenerator {

    /**
     * Эхлэл - 1
     *
     * @param model      model
     * @param saveFolder java классуудыг хадгалах folder (path нь '/' тэмдэгтээр төгссөн байх ёстой)
     */
    public void generate(Service model, String saveFolder) {
        if (model == null || model.getAttributes() == null || model.getAttributes().isEmpty())
            throw new NullPointerException("Parameters null байна.");
        if (saveFolder == null || !saveFolder.endsWith("/")) {
            throw new IllegalArgumentException("saveFolder parameter folder-н path биш байна. saveFolder parameter нь '/' тэмдэгтээр төгссөн байх ёстой.");
        }

        AtomicReference<String> savePath = new AtomicReference<>(saveFolder + model.getClassName() + ".java");

        init(model.getAttributes(), model.getClassName(), savePath.get());

        model.getAttributes().stream().filter(f -> f.getType() == Type.object).forEach(attribute -> {
            savePath.set(saveFolder + attribute.getName() + ".java");
            init(attribute.getObjects(), attribute.getName(), savePath.get());
        });

        model.getAttributes().stream().filter(f -> f.getType() == Type.array && f.getListName() != null).forEach(attribute -> {
            savePath.set(saveFolder + attribute.getListName() + ".java");
            init(attribute.getObjects(), attribute.getListName(), savePath.get());
        });
    }

    private void init(List<Attribute> attributes, String className, String savePath) {
        StringBuilder createdClass = new StringBuilder(createClass(className));
        createdClass.append(createAttribute(attributes));
        createdClass.append("}");
        createdClass = new StringBuilder(createImports(attributes, createdClass.toString()));
        createdClass = new StringBuilder(createPackage(createdClass.toString()));
        writeClass(createdClass.toString(), savePath);
    }

    /**
     * String java class-г файлд бичих
     *
     * @param value String java class
     */
    @SneakyThrows
    private void writeClass(String value, String savePath) {
        FileOutputStream outputStream = new FileOutputStream(savePath);
        outputStream.write(value.getBytes());
        outputStream.close();
    }

    /**
     * Java class-н нэрийг бичих
     *
     * @param className class-н нэр
     */
    private String createClass(String className) {
        return "public class " + className + " {\n";
    }

    /**
     * Java package заагчийг бичих
     *
     * @param value String java class
     */
    private String createPackage(String value) {
        return "package " + Constants.groupId + ";\n\n" + value;
    }

    /**
     * Ашигласан class-уудыг import хийх
     *
     * @param getList List of Attribute.java
     * @param value   String java class
     */
    private String createImports(List<Attribute> getList, String value) {
        StringBuilder s = new StringBuilder();
        getList.forEach(f -> {
            if (f.getType() == Type.date && !s.toString().contains("import java.time.LocalDateTime;")) {
                s.append("import java.time.LocalDateTime;\n");
            } else if (f.getType() == Type.object && !s.toString().contains("import " + Constants.groupId + "." + f.getName())) {
                s.append("import ").append(Constants.groupId).append(".").append(f.getName()).append(";\n");
            } else if (f.getType() == Type.array && !s.toString().contains("import java.util.List;")) {
                s.append("import java.util.List;").append("\n");
            } else if (f.getType() == Type.array && f.getListName() != null && !s.toString().contains("import " + Constants.groupId + "." + f.getListName())) {
                s.append("import ").append(Constants.groupId).append(".").append(f.getListName()).append(";\n");
            }
        });

        return s.isEmpty() ? value : s.append("\n").append(value).toString();
    }

    /**
     * Классын attribute-уудыг үүсгэх
     *
     * @param getList List of Attribute.java
     * @return private *Type* *attribute* гэх загвараар attribute-уудыг бичсэн String
     */
    private String createAttribute(List<Attribute> getList) {
        StringBuilder value = new StringBuilder();
        for (Attribute model : getList) {
            switch (model.getType()) {
                case string -> value.append("\tprivate String ").append(model.getName()).append(";").append("\n");
                case array -> value.append("\tprivate List<").append(model.getListType() == null ? model.getListName() : model.getListType() == Type.object ? model.getListName() : model.getListType().getType()).append("> ").append(Character.toLowerCase(model.getName().charAt(0))).append(model.getName(), 1, model.getName().length()).append(";").append("\n");
                case integer -> value.append("\tprivate int ").append(model.getName()).append(";").append("\n");
                case doubles -> value.append("\tprivate double ").append(model.getName()).append(";").append("\n");
                case object -> value.append("\tprivate ").append(model.getName()).append(" ").append(Character.toLowerCase(model.getName().charAt(0))).append(model.getName(), 1, model.getName().length()).append(";").append("\n");
                case date -> value.append("\tprivate LocalDateTime ").append(model.getName()).append(";").append("\n");
            }
        }
        return value.isEmpty() ? null : value.toString();
    }
}
