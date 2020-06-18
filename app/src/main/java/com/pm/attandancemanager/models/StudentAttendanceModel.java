package com.pm.attandancemanager.models;

/**
 * Created by ghanshyamnayma on 08/10/17.
 */

public class StudentAttendanceModel {
    private String name;
    private String rollNumber;
    private String status;
    private String className;
    private String dateTime;

    public StudentAttendanceModel(){}
    public StudentAttendanceModel(String name, String rollNumber, String status, String className,String dateTime ) {
        this.name = name;
        this.rollNumber = rollNumber;
        this.status = status;
        this.className = className;
        this.dateTime = dateTime;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String subjectName) {
        this.className = subjectName;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRollNumber() {
        return rollNumber;
    }

    public void setRollNumber(String rollNumber) {
        this.rollNumber = rollNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
