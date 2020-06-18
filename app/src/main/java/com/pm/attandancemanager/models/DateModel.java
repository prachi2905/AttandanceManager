package com.pm.attandancemanager.models;

import java.io.Serializable;

/**
 * Created by ghanshyamnayma on 28/10/17.
 */

public class DateModel implements Serializable {

    private String dateTime;
    private int totalLectures;
    private int totalLeaves;
    private int totalLate;
    private int totalPresents;
    private int totalAbsents;

    public int getTotalLectures() {
        return totalLectures;
    }

    public void setTotalLectures(int totalLectures) {
        this.totalLectures = totalLectures;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
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
}
