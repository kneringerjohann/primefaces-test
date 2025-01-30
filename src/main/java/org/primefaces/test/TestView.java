package org.primefaces.test;

import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Data
@Named
@ViewScoped
public class TestView implements Serializable {

    private String string;
    private Integer integer;
    private BigDecimal decimal;
    private LocalDateTime localDateTime;
    private List<TestObject> list;

    @PostConstruct
    public void init() {
        string = "Welcome to PrimeFaces!!!";
        list = new ArrayList<>(Arrays.asList(
                new TestObject("Thriller", "Michael Jackson", 1982, RatingEnum.BESTEST),
                new TestObject("Back in Black", "AC/DC", 1980, RatingEnum.BETTER),
                new TestObject("The Bodyguard", "Whitney Houston", 1992, RatingEnum.STANDARD),
                new TestObject("The Dark Side of the Moon", "Pink Floyd", 1973, RatingEnum.BESTEST)
        ));
    }

    public RatingEnum[] retrieveRatings() {
        return RatingEnum.values();
    }

    public int sortRatingEnum(RatingEnum articleVariantEnum1, RatingEnum articleVariantEnum2) {
        List<RatingEnum> order = List.of(RatingEnum.STANDARD, RatingEnum.BETTER, RatingEnum.BESTEST);
        return Integer.compare(order.indexOf(articleVariantEnum1), order.indexOf(articleVariantEnum2));
    }

}
