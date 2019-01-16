package com.redis.util;

import sun.misc.Unsafe;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

public class AtomicReferenceTest {
    public static void main(String[] args) {
        Person p = new Person(28);
        Integer integer = 444;
        Integer integer1=444;
        System.out.println(integer==integer1);
        AtomicReference<Integer> atomciReference = new AtomicReference<>(122);
        atomciReference.compareAndSet(122,new Integer(225));
        System.out.println(atomciReference.get());
    }
}

class Person{
    private Integer age;

    public Person(Integer age){
        this.age=age;
    }

    @Override
    public String toString() {
        return "Person{" +
                "age=" + age +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return Objects.equals(age, person.age);
    }

    @Override
    public int hashCode() {
        return Objects.hash(age);
    }
}
