package com.project.entity;




import lombok.Data;

import javax.persistence.*;

@Entity(name="USER_DETAILS")
@Data
public class User {

    @Id
    @Column(name="USER_ID")
    @GeneratedValue(strategy= GenerationType.AUTO)
    private int parentId;

    @Column(name="USER_NAME")
    private String username;

    @Column(name="PASSWORD")
    private String password;

    @Column(name="EMAIL")
    private String email;
}
