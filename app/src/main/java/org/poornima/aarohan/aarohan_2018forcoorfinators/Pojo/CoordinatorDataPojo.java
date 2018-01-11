package org.poornima.aarohan.aarohan_2018forcoorfinators.Pojo;

/**
 * Created by Bhoomika on 11-01-2018.
 */

public class CoordinatorDataPojo {
private String event_id,event_name,event_category,event_participation_category,event_type,event_detail,
        event_location,event_date,event_time,co_name,event_map_coordinates_latt,event_map_coordinates_long;

    public CoordinatorDataPojo(String event_id, String event_name, String event_category, String event_participation_category, String event_type, String event_detail, String event_location, String event_date, String event_time, String co_name, String event_map_coordinates_latt, String event_map_coordinates_long) {
        this.event_id = event_id;
        this.event_name = event_name;
        this.event_category = event_category;
        this.event_participation_category = event_participation_category;
        this.event_type = event_type;
        this.event_detail = event_detail;
        this.event_location = event_location;
        this.event_date = event_date;
        this.event_time = event_time;
        this.co_name = co_name;
        this.event_map_coordinates_latt = event_map_coordinates_latt;
        this.event_map_coordinates_long = event_map_coordinates_long;
    }

    public String getEvent_id() {
        return event_id;
    }

    public String getEvent_name() {
        return event_name;
    }

    public String getEvent_category() {
        return event_category;
    }

    public String getEvent_participation_category() {
        return event_participation_category;
    }

    public String getEvent_type() {
        return event_type;
    }

    public String getEvent_detail() {
        return event_detail;
    }

    public String getEvent_location() {
        return event_location;
    }

    public String getEvent_date() {
        return event_date;
    }

    public String getEvent_time() {
        return event_time;
    }

    public String getCo_name() {
        return co_name;
    }

    public String getEvent_map_coordinates_latt() {
        return event_map_coordinates_latt;
    }

    public String getEvent_map_coordinates_long() {
        return event_map_coordinates_long;
    }
}
