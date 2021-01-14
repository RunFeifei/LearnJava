package com.test.fei;

import com.test.model.Dog;

import java.util.List;

public class Person {

    public String name;
    public List<String> names;
    public List<Integer> ages;
    public Integer age;
    public Long longValue;
    public Boolean needBad;
    public Cat cat;
    public List<Dog> dogs;


    public static class Cat {
        public String kind;
        public String name;
        public Integer age;
        public Boolean isBad;
        public Dog dog;
        public Cat son;

    }

}
