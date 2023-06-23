package org.primefaces.test;

import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@Named
@ViewScoped
public class TestView implements Serializable {


    private List<TestObject> testObjects = new ArrayList<>();

    @PostConstruct
    public void init() {
        TestObject testObject = new TestObject("TestObject");
        List<TestObjectChild> children = List.of(new TestObjectChild(1L, "Child1"), new TestObjectChild(2L, "Child2"));
        testObject.setChildList(children);
        testObjects.add(testObject);
        System.out.println("testObjects = " + testObjects);
    }

}
