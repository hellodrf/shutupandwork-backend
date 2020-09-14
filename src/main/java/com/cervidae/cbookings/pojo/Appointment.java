package com.cervidae.cbookings.pojo;

import lombok.Data;

import java.util.Date;

@Data
public class Appointment {

    private int id;

    private int userId;

    private int bcServiceId;

    private Date date;

    private int timeSlot;

    private int status;


}
