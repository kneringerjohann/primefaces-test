package org.primefaces.test;

import java.io.Serializable;
import java.util.UUID;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
public class TestObject implements Serializable {

    private String id;
    private String name;
    private String artist;
    private Integer released;
    private RatingEnum ratingEnum;

    public TestObject(String name, String artist, Integer released, RatingEnum ratingEnum) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.artist = artist;
        this.released = released;
        this.ratingEnum = ratingEnum;
    }

}
