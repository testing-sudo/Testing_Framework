package com.project.entity;


import lombok.Data;

import javax.persistence.*;

@Entity(name = "PROFILE_DETAILS")
@Data
public class Profile {

    @Id
    @Column(name="PROFILE_ID")
    @GeneratedValue(strategy= GenerationType.AUTO)
    private int profileId;

    @Column(name="PROFILE_NAME")
    private String profileName;

    @Column(name="USER_NAME")
    private String userName;

    @Column(name="PLAYED_BEDROOM")
    private int playedBedroom;

    @Column(name="PLAYED_KITCHEN")
    private int playedKitchen;
}
