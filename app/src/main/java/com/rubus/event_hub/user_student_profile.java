package com.rubus.event_hub;

public class user_student_profile {



    public String name,college, email,department,course_end,phone;


    public user_student_profile(){

    }
    public user_student_profile(String name, String college, String email,String department,String course_end,String phone) {
        this.name = name;
        this.college=college;
        this.email = email;
        this.department=department;
        this.course_end=course_end;
        this.phone = phone;
    }
}
