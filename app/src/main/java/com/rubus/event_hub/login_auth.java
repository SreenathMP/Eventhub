package com.rubus.event_hub;

public class login_auth {

    public login_auth() {

    }

   private String name,account;

    public  login_auth(String name,String account){

        this.account=account;
        this.name=name;

    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName(String name){

        return name;
    }

    public void setAccount(String account){
        this.account=account;
    }

    public String getAccount(){
        return account;
    }
}
