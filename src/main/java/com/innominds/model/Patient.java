package com.innominds.model;

import java.util.List;

public class Patient {

    private Long id;
    private String name;
    private Integer age;
    private String address;

    private List<Prescription> prescriptions;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<Prescription> getPrescriptions() {
        return prescriptions;
    }

    public void setPrescriptions(List<Prescription> prescriptions) {
        this.prescriptions = prescriptions;
    }

    public String toString() {
        return "[" + id + ", " + name + ", " + age + ", " + address + ", " + prescriptions + "]";
    }
}
