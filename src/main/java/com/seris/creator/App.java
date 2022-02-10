package com.seris.creator;

import java.util.List;

/**
 * Application эхлэл
 *
 * @author Bayarkhuu.Luv
 */
public class App {
    public static void main(String[] args) {
        serviceClassToJavaClassExample();
        jsonToJavaClassExample();

        System.out.println("ПРОГРАМ АМЖИЛТТАЙ АЖИЛЛАЖ ДУУССАН.");
        System.out.println("d:/TEST/ folder-г шалгана уу.");
    }

    /**
     * Service.java класс-н дагуу үүсгэсэн object-оос класс үүсгэж буй жишээ
     * Үр дүнг D:/Test/ folder-т хадгалж байгаа
     *
     * @see Service
     */
    private static void serviceClassToJavaClassExample() {
        ClassGenerator classGenerator = new ClassGenerator();
        String folder = "d:/TEST/";

        //Тестийн өгөгдөл бэлдэх
        Service model = new Service();
        model.setUrl("http://localhost:80/save/patient");
        model.setClassName("Patient");
        model.setCompany("МИТПС ХХК");
        model.setDescription("Иргэн хадгалмаар байна");
        model.setAttributes(List.of(
                new Attribute("id", Type.integer),
                new Attribute("firstName", Type.string),
                new Attribute("lastName", Type.string),
                new Attribute("birthDay", Type.date),
                new Attribute("height", Type.doubles),
                new Attribute("Address", Type.object, List.of(new Attribute("country", Type.string), new Attribute("city", Type.string))),
                new Attribute("Position", Type.object, List.of(new Attribute("name", Type.string), new Attribute("from", Type.date))),
                new Attribute("images", Type.array, Type.string),
                new Attribute("families", Type.array, Type.object, "Family", List.of(new Attribute("who", Type.string), new Attribute("phone", Type.string))),
                new Attribute("created", Type.date)
        ));

        classGenerator.generate(model, folder);
    }

    /**
     * String JSON-оос java class үүсгэсэн жишээ
     * Үр дүнг D:/Test/ folder-т хадгалж байгаа
     *
     * @see Service createFromJson
     */
    private static void jsonToJavaClassExample() {
        ClassGenerator classGenerator = new ClassGenerator();
        String folder = "d:/TEST/";

        //Тест хийх json String
        Service model = Service.createFromJson("""
                {
                  "id": 9,
                  "title": "Infinix INBOOK",
                  "description": "Infinix Inbook X1 Ci3 10th 8GB...",
                  "price": 1099,
                  "discountPercentage": 11.83,
                  "rating": 4.54,
                  "stock": 96,
                  "brand": "Infinix",
                  "category": "laptops",
                  "thumbnail": "https://dummyjson.com/image/i/products/9/thumbnail.jpg",
                  "created": "2012-04-23T18:25:43.511",
                  "images": [
                    "https://dummyjson.com/image/i/products/9/1.jpg",
                    "https://dummyjson.com/image/i/products/9/2.png",
                    "https://dummyjson.com/image/i/products/9/3.png",
                    "https://dummyjson.com/image/i/products/9/4.jpg",
                    "https://dummyjson.com/image/i/products/9/thumbnail.jpg"
                  ]
                }""");

        model.setClassName("Product");

        classGenerator.generate(model, folder);
    }

}
