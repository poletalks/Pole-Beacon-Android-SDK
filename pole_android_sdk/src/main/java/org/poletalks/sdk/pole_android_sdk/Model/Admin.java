package org.poletalks.sdk.pole_android_sdk.Model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by anjal on 10/30/17.
 */

public class Admin implements Serializable {

    private String _id, name, hash, user_id,login_id,user_hash,password,role,active_location, store_id ;
    private Double created_at , __v;
    private ArrayList<String> colour = new ArrayList<>();
    private ArrayList<Double> location = new ArrayList<>();


    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getLogin_id() {
        return login_id;
    }

    public String getUser_hash() {
        return user_hash;
    }

    public void setUser_hash(String user_hash) {
        this.user_hash = user_hash;
    }

    public void setLogin_id(String login_id) {
        this.login_id = login_id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getActive_location() {
        return active_location;
    }

    public void setActive_location(String active_location) {
        this.active_location = active_location;
    }

    public Double getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Double created_at) {
        this.created_at = created_at;
    }

    public Double get__v() {
        return __v;
    }

    public void set__v(Double __v) {
        this.__v = __v;
    }

    public ArrayList<String> getColour() {
        return colour;
    }

    public void setColour(ArrayList<String> colour) {
        this.colour = colour;
    }

    public ArrayList<Double> getLocation() {
        return location;
    }

    public void setLocation(ArrayList<Double> location) {
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getStore_id() {
        return store_id;
    }

    public void setStore_id(String store_id) {
        this.store_id = store_id;
    }
}
