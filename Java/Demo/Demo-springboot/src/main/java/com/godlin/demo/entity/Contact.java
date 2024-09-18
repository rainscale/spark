package com.godlin.demo.entity;

import lombok.Data;

@Data
public class Contact {
    private Integer id;
    private String name;
    private String birthday;
    private String email;
    private String phone;
    private String address;
    private String gender;
    private String create_date;
    private String last_update;
    private String title;
    private boolean status;
}
