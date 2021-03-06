package com.test.fei;

import com.google.gson.Gson;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;

public class Main {

    public static void main(String[] args) throws Exception {

        Person person = new Person();
        reflection(person, person.getClass().getName(), 0);
        System.out.println(new Gson().toJson(person));

    }

    private static Object reflection(Object object, final String reflectClassName, int deep) throws Exception {
        Class rootClass = object.getClass();
        String rootPackage = rootClass.getPackage().getName();
        if (deep > 1) {
            return object;
        }


        Field[] fields = rootClass.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            String name = field.getName();
            String typeName = field.getType().getTypeName();
            if (field.getType().getName().equals("java.lang.String")) {
                field.set(object, "a");
            }
            if (field.getType().getName().equals("java.lang.Integer")) {
                field.set(object, 123);
            }
            if (field.getType().getName().equals("java.lang.Long")) {
                field.set(object, 123L);
            }
            if (field.getType().getName().equals("java.lang.Boolean")) {
                field.set(object, Boolean.TRUE);
            }
            if (field.getType().getName().equals("java.util.List")) {

                field.set(object, new ArrayList<>());

                Type genericType = field.getGenericType();
                //typeNameT 泛型
                //java.util.List<java.lang.String>
                String typeNameT = genericType.getTypeName();
                typeNameT = typeNameT.replace("java.util.List<", "");
                typeNameT = typeNameT.replace(">", "");
                //java.lang.String
                if (typeNameT.equals("java.lang.String")) {
                    field.set(object, Arrays.asList("a", "b"));
                } else if (typeNameT.equals("java.lang.Integer")) {
                    field.set(object, Arrays.asList(1, 2));
                } else if (typeNameT.equals("java.lang.Long")) {
                    field.set(object, Arrays.asList(1L, 2L));
                } else if (typeNameT.equals("java.lang.Boolean")) {
                    field.set(object, Arrays.asList(false, true));
                } else {
                    //这里有循环嵌套 Person有List<Person>类型的变量
                    boolean isNested = typeNameT.equals(rootClass.getName()) && typeNameT.equals(reflectClassName);
                    System.out.println(typeNameT);
                    if (isNested) {
                        deep++;
                    }
                    field.set(object, Arrays.asList(reflection(Class.forName(typeNameT).newInstance(), typeNameT, deep)));
                }
            }
            if (typeName.startsWith("com")) {
                //这里有循环嵌套 Person有Person类型的变量
                boolean isNested = typeName.equals(rootClass.getName()) && typeName.equals(reflectClassName);
                if (isNested) {
                    deep++;
                }
                field.set(object, reflection(Class.forName(typeName).newInstance(), typeName, deep));
            }
        }
        return object;
    }


}

