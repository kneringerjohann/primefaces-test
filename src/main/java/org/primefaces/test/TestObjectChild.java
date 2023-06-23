package org.primefaces.test;

import lombok.Data;

@Data
public class TestObjectChild {

    public TestObjectChild(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    private Long id;

    private String name;
}
