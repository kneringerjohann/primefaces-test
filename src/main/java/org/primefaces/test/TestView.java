package org.primefaces.test;

import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Named
@ViewScoped
@Getter
@Setter
public class TestView implements Serializable {

    private String string;
    private Integer integer;
    private BigDecimal decimal;
    private LocalDateTime localDateTime;
    private List<TestObject> list = new ArrayList<>();
    private String selected;
    private CustomLazyDataModel<TestObject> lazyDataModel = new CustomLazyDataModel<TestObject>();

    @PostConstruct
    public void init() {
        string = "Welcome to PrimeFaces!!!";
        list.add(new TestObject("Thriller", "Michael Jackson", 1982));
        list.add(new TestObject("Back in Black", "AC/DC", 1980));
        list.add(new TestObject("The Bodyguard", "Whitney Houston", 1992));
        list.add(new TestObject("The Dark Side of the Moon", "Pink Floyd", 1973));

        lazyDataModel.setDatasource(list);
    }

    public List<TestObject> completeText(String query) {
        String queryLowerCase = query.toLowerCase();
        return list.stream().filter(t -> t.getName().toLowerCase().startsWith(queryLowerCase)).collect(Collectors.toList());
    }
}
