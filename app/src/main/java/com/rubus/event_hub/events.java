package com.rubus.event_hub;

public class events {


    private String event_title,college,description,category,venue,date1,date2,time1,phone_number,userUid,bannerUrl,email,website,postId;
    private boolean status;


    public events() {
    }

    public events(String event_title,String college,String description,String category,String venue,String date1,String date2,String time1,String phone_number,boolean status,String userUid,String bannerUrl,String email,String website,String postId) {
        this.event_title = event_title;
        this.college = college;
        this.description=description;
        this.category=category;
        this.venue=venue;
        this.date1=date1;
        this.date2=date2;
        this.time1=time1;
        this.phone_number=phone_number;
        this.status=status;
        this.userUid=userUid;
        this.bannerUrl=bannerUrl;
        this.email=email;
        this.website=website;
        this.postId=postId;

    }


    public void setEvent_title(String event_title) {
        this.event_title = event_title;
    }

    public String getEvent_title() {
        return event_title;
    }

    public void setCollege(String college) {
        this.college = college;
    }

    public String getCollege() {
        return college;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }


    public void setCategory(String category) {
        this.category = category;
    }

    public String getCategory() {
        return category;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }


    public String getVenue() {
        return venue;
    }


    public void setDate1(String date1) {
        this.date1 = date1;
    }

    public String getDate1() {
        return date1;
    }

    public void setDate2(String date2) {
        this.date2 = date2;
    }

    public String getDate2() {
        return date2;
    }

    public void setTime1(String time1) {
        this.time1 = time1;
    }

    public String getTime1() {
        return time1;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public boolean isStatus() {
        return status;
    }

    public void setUserUid(String userUid) {
        this.userUid = userUid;
    }

    public String getUserUid() {
        return userUid;
    }


    public void setBannerUrl(String bannerUrl){

        this.bannerUrl=bannerUrl;
    }

    public String getBannerUrl(){

        return bannerUrl;
    }

    public String getEmail(){

        return email;
    }

    public void setEmail(String email){

        this.email=email;
    }


    public String getWebsite(){

        return website;
    }

    public void setPostId(String postId){

        this.postId=postId;
    }

    public String getPostId(){

        return postId;
    }


}
