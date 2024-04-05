package com.project.session;


import lombok.Data;

@Data
public class SessionSingleton {

    private static SessionSingleton INSTANCE;
    private String username;
    private String activeProfileName;
    private String activePreference;

    // session object
    // Session Constuctor, new object can't be created
    private SessionSingleton() {
    }

    public static SessionSingleton getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new SessionSingleton();
        }

        return INSTANCE;
    }
}
