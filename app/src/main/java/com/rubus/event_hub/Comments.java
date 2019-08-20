package com.rubus.event_hub;



public class Comments {

    private String message, user_id;


    public Comments(){

    }

    public Comments(String message, String user_id) {
        this.message = message;
        this.user_id = user_id;

    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }


}

