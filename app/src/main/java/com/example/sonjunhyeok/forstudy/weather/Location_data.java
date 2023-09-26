package com.example.sonjunhyeok.forstudy.weather;

public class Location_data {
    private String state_name;
    private String city_name;
    private String leaf_name;
    private int state_code;
    private int city_code;
    private int leaf_code;
    private int x_code;
    private int y_code;

    public String getLeaf_name() {
        return leaf_name;
    }

    public void setLeaf_name(String leaf_name) {
        this.leaf_name = leaf_name;
    }

    public int getLeaf_code() {
        return leaf_code;
    }

    public void setLeaf_code(int leaf_code) {
        this.leaf_code = leaf_code;
    }

    public String getState_name() {
        return state_name;
    }

    public void setState_name(String state_name) {
        this.state_name = state_name;
    }

    public String getCity_name() {
        return city_name;
    }

    public void setCity_name(String city_name) {
        this.city_name = city_name;
    }

    public int getState_code() {
        return state_code;
    }

    public void setState_code(int state_code) {
        this.state_code = state_code;
    }

    public int getCity_code() {
        return city_code;
    }

    public void setCity_code(int city_code) {
        this.city_code = city_code;
    }

    public int getX_code() {
        return x_code;
    }

    public void setX_code(int x_code) {
        this.x_code = x_code;
    }

    public int getY_code() {
        return y_code;
    }

    public void setY_code(int y_code) {
        this.y_code = y_code;
    }
}
