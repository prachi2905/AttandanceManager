package com.pm.attandancemanager.models;

import java.io.Serializable;

public class Students implements Serializable {

    private int id;
    private String name;
    private String rollNumber;
    private String dateTime;
    private String status;
    private String className;
    private int totalLectures;
    private int totalPresents;
    private int totalAbsents;


    public int getTotalPresents() {
        return totalPresents;
    }

    public void setTotalPresents(int totalPresents) {
        this.totalPresents = totalPresents;
    }

    public int getTotalAbsents() {
        return totalAbsents;
    }

    public void setTotalAbsents(int totalAbsents) {
        this.totalAbsents = totalAbsents;
    }

    public int getTotalLeaves() {
        return totalLeaves;
    }

    public void setTotalLeaves(int totalLeaves) {
        this.totalLeaves = totalLeaves;
    }

    public int getTotalLate() {
        return totalLate;
    }

    public void setTotalLate(int totalLate) {
        this.totalLate = totalLate;
    }

    private int totalLeaves;
    private int totalLate;


    public int getTotalLectures() {
        return totalLectures;
    }

    public void setTotalLectures(int totalLectures) {
        this.totalLectures = totalLectures;
    }


    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    @Override
    public String toString() {
        return "Students : id " + id + " name "+name +" rollNumber " + rollNumber+" className " + className + " dateTime " + dateTime + " status " + status + " totalLectures " + totalLectures + " totalPresents " + totalPresents + " totalAbsents " + totalAbsents + " totalLeaves " + totalLeaves + " totalLate " + totalLate;
    }
}
