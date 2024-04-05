package com.project.entity;


import lombok.Data;

import javax.persistence.*;

// Implementing Profile preference system with an entity called Profile_Prefernces
@Entity(name = "PROFILE_PREFERENCES")
@Data
public class Preferences {

    @Id
    @Column(name="PREF_ID")
    @GeneratedValue(strategy= GenerationType.AUTO)
    private int prefId;

    @Column(name="PREF_NAME")
    private String prefName;

    @Column(name="BED")
    private String bed;

    @Column(name="TV")
    private String tv;

    @Column(name="COMPUTER")
    private String computer;

    @Column(name="LAMP")
    private String lamp;

    @Column(name="COUCH")
    private String couch;

    @Column(name="FRIDGE")
    private String fridge;

    @Column(name="RECLINER")
    private String recliner;

    @Column(name="WARDROBE")
    private String wardrobe;

    @Column(name="MIRROR")
    private String mirror;

    @Column(name="MICROWAVE")
    private String microwave;

    @Column(name="CHAIR")
    private String chair;

    @Column(name="CUPBOARD")
    private String cupboard;

    @Column(name="THEME")
    private String theme;

    @Column(name="USER_NAME")
    private String userName;

    @Column(name="PROFILE_NAME")
    private String profileName;

    @Column(name="MESSAGE_BEDROOM")
    private String messageBedroom;

    @Column(name="MESSAGE_KITCHEN")
    private String messageKitchen;

    //getter and setters
}
