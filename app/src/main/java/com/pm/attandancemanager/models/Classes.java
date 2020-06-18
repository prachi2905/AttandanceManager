package com.pm.attandancemanager.models;

import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * Created by ghanshyamnayma on 08/10/17.
 */

public class Classes extends BaseModel{

    private int id;
    private String subjectName;
    private String totalLectures;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getTotalLectures() {
        return totalLectures;
    }

    public void setTotalLectures(String totalLectures) {
        this.totalLectures = totalLectures;
    }
}
