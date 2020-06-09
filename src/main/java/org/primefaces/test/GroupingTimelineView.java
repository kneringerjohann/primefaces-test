package org.primefaces.test;

import org.primefaces.event.timeline.TimelineDragDropEvent;
import org.primefaces.model.timeline.TimelineEvent;
import org.primefaces.model.timeline.TimelineGroup;
import org.primefaces.model.timeline.TimelineModel;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Named("groupingTimelineView")
@ViewScoped
public class GroupingTimelineView implements Serializable {

    private TimelineModel<Order, Truck> model;
    private TimelineEvent<Order> event; // current changed event
    private List<Order> orderList = new ArrayList<>(); // current changed event

    @PostConstruct
    protected void initialize() {
        // create timeline model
        model = new TimelineModel<>();

        // create groups
        TimelineGroup<Truck> group1 = new TimelineGroup<>("id1", new Truck("10"));
        TimelineGroup<Truck> group2 = new TimelineGroup<>("id2", new Truck("11"));
        TimelineGroup<Truck> group3 = new TimelineGroup<>("id3", new Truck("12"));
        TimelineGroup<Truck> group4 = new TimelineGroup<>("id4", new Truck("13"));
        TimelineGroup<Truck> group5 = new TimelineGroup<>("id5", new Truck("14"));
        TimelineGroup<Truck> group6 = new TimelineGroup<>("id6", new Truck("15"));

        // add groups to the model
        model.addGroup(group1);
        model.addGroup(group2);
        model.addGroup(group3);
        model.addGroup(group4);
        model.addGroup(group5);
        model.addGroup(group6);

        initEvents();

    }

    public void initEvents() {
        int orderNumber = 1;

        // iterate over groups
        for (int j = 1; j <= 6; j++) {
            // iterate over events in the same group

            Order order = new Order(orderNumber);
            orderList.add(order);
            orderNumber++;

        }
    }

    public TimelineModel<Order, Truck> getModel() {
        return model;
    }

    public void onDrop(TimelineDragDropEvent<Order> e) {
        /*
        Group in which the Element is drop is null or empty, which should not be!
         */
        if(e.getGroup()==null || e.getGroup().isEmpty()){
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Group is not set on drop in TimelineDragDropEvent!", null);
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }

    public List<Order> getOrderList() {
        return orderList;
    }

    public void setOrderList(List<Order> orderList) {
        this.orderList = orderList;
    }

    public static class Truck implements Serializable {
        private final String code;

        public Truck(String code) {
            this.code = code;
        }

        public String getCode() {
            return code;
        }
    }
}