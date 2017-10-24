package com.example.s1k0de.entry;


public class User {
    private String name;
    private String login;
    private String password;

    public User() {
        name="";
        login ="";
        password="";
    }
    public User(String name, String logname, String password) {
        this.name = name;
        this.login = logname;
        this.password = password;
    }
    public String getName(){
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setLogin(String login) {
        this.login = login;
    }
    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
