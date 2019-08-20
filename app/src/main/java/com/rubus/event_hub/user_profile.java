package com.rubus.event_hub;

public class user_profile {


    private String name,college,department,email,phone_number,course_end;



    public user_profile() {
    }

    public user_profile(String name,String college,String department,String email,String phone_number,String course_end) {
        this.name = name;
        this.college = college;
        this.department=department;
        this.email=email;
        this.phone_number=phone_number;
        this.course_end=course_end;



    }




    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }

    public void setCollege(String college) {
        this.college = college;
    }


    public String getCollege() {
        return college;
    }


    public void setDepartment(String department){

        this.department=department;
    }

    public String getDepartment() {
        return department;
    }


    public void setEmail(String email){

        this.email=email;
    }

    public String getEmail() {
        return email;
    }

    public void setPhone_number(String phone_number){

        this.phone_number=phone_number;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setCourse_end(String course_end){

        this.course_end=course_end;
    }

    public String getCourse_end() {
        return course_end;
    }

}
