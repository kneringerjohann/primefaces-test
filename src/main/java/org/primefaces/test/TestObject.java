package org.primefaces.test;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
public class TestObject implements Serializable {

    private String id;
    private String name;

    public TestObject(String name) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
    }

    List<TestObjectChild> childList = new ArrayList<>();

}
