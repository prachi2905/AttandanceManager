package com.pm.attandancemanager.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.pm.attandancemanager.models.Classes;
import com.pm.attandancemanager.models.DateModel;
import com.pm.attandancemanager.models.Students;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ghanshyamnayma on 08/10/17.
 */

public class DatabaseHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "record_manager";

    // Contacts table name
    private static final String TABLE_CLASSES = "classes";
    private static final String TABLE_STUDENTS = "students";
    private static final String TABLE_ATTENDANCE = "attendance";
    private static final String TABLE_DATE = "dates";


    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_CLASS_NAME = "class_name";
    private static final String KEY_STUDENT_NAME = "student_name";
    private static final String KEY_STUDENT_ROLL_NUMBER = "student_roll_number";
    private static final String KEY_PRESENT_STATUS = "present_status";
    private static final String KEY_DATE_TIME = "date_time";
    private static final String KEY_TOTAL_LECTURES = "total_lectures";
    private static final String KEY_TOTAL_PRESENTS = "total_presents";
    private static final String KEY_TOTAL_ABSENTS = "total_absents";
    private static final String KEY_TOTAL_LEAVES = "total_leaves";
    private static final String KEY_TOTAL_LATE = "total_late";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CLASSES_TABLE = "CREATE TABLE " + TABLE_CLASSES + "(" + KEY_ID + " INTEGER PRIMARY KEY autoincrement," + KEY_CLASS_NAME + " TEXT" + ")";
        db.execSQL(CREATE_CLASSES_TABLE);

        String CREATE_STUDENT_TABLE = "CREATE TABLE " + TABLE_STUDENTS + "(" + KEY_ID + " INTEGER PRIMARY KEY autoincrement," + KEY_STUDENT_NAME + " TEXT," + KEY_STUDENT_ROLL_NUMBER + " TEXT, " + KEY_DATE_TIME + " TEXT, " + KEY_PRESENT_STATUS + " TEXT, " + KEY_TOTAL_LECTURES + " INTEGER, " + KEY_TOTAL_PRESENTS + " INTEGER, " + KEY_TOTAL_ABSENTS + " INTEGER, " + KEY_TOTAL_LEAVES + " INTEGER, " + KEY_TOTAL_LATE + " INTEGER, " + KEY_CLASS_NAME + " TEXT " + ")";
        db.execSQL(CREATE_STUDENT_TABLE);

        String CREATE_DATE_TABLE = "CREATE TABLE " + TABLE_DATE + "(" + KEY_ID + " INTEGER PRIMARY KEY autoincrement," + KEY_TOTAL_LECTURES + " INTEGER, " + KEY_TOTAL_PRESENTS + " INTEGER, " + KEY_TOTAL_ABSENTS + " INTEGER, " + KEY_TOTAL_LEAVES + " INTEGER, " + KEY_TOTAL_LATE + " INTEGER, " + KEY_DATE_TIME + " TEXT " + ")";
        db.execSQL(CREATE_DATE_TABLE);

        String CREATE_ATTENDANCE_TABLE = "CREATE TABLE " + TABLE_ATTENDANCE + "(" + KEY_ID + " INTEGER PRIMARY KEY autoincrement," + KEY_STUDENT_NAME + " TEXT," + KEY_STUDENT_ROLL_NUMBER + " TEXT, " + KEY_DATE_TIME + " INTEGER, " + KEY_PRESENT_STATUS + " TEXT, " + KEY_TOTAL_LECTURES + " INTEGER, " + KEY_TOTAL_PRESENTS + " INTEGER, " + KEY_TOTAL_ABSENTS + " INTEGER, " + KEY_TOTAL_LEAVES + " INTEGER, " + KEY_TOTAL_LATE + " INTEGER, " + KEY_CLASS_NAME + " TEXT " + ")";
        db.execSQL(CREATE_ATTENDANCE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CLASSES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STUDENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DATE);

        onCreate(db);
    }

    public void addClass(Classes classes) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues value = new ContentValues();
        value.put(KEY_CLASS_NAME, classes.getSubjectName());
        // Inserting Row
        db.insert(TABLE_CLASSES, null, value);
        db.close(); // Closing database connection
    }

    public void updateClass(String subjectOldName, String subjectNewName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_CLASS_NAME, subjectNewName);
        db.update(TABLE_CLASSES, values, KEY_CLASS_NAME + " = ?", new String[]{subjectOldName});
        db.update(TABLE_STUDENTS, values, KEY_CLASS_NAME + " = ?", new String[]{subjectOldName});
    }

    // Deleting single contact
    public void deleteClass(String subjectName) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CLASSES, KEY_CLASS_NAME + " = ?", new String[]{String.valueOf(subjectName)});
        db.delete(TABLE_STUDENTS, KEY_CLASS_NAME + " = ?", new String[]{String.valueOf(subjectName)});
        db.close();
    }

    public void deleteStudent(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_STUDENTS, KEY_ID + " = ?", new String[]{String.valueOf(id)});
        db.delete(TABLE_ATTENDANCE, KEY_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }

    // inserting date in table Dates
    public void addDateTime(int totalLectures, int totalPresents, int totalAbsent, int totalLeaves, int totalLate, String dateTime) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues value = new ContentValues();
        value.put(KEY_TOTAL_LECTURES, totalLectures);
        value.put(KEY_TOTAL_PRESENTS, totalPresents);
        value.put(KEY_TOTAL_ABSENTS, totalAbsent);
        value.put(KEY_TOTAL_LEAVES, totalLeaves);
        value.put(KEY_TOTAL_LATE, totalLate);
        value.put(KEY_DATE_TIME, dateTime);
        // Inserting Row
        db.insert(TABLE_DATE, null, value);
        db.close(); // Closing database connection
    }

    public List<DateModel> getAllDates(String date) {
        List<DateModel> dates = new ArrayList<>();
        // Select All Query
        String selectQuery;
        if (date.equalsIgnoreCase("ALL"))
            selectQuery = "SELECT  * FROM " + TABLE_DATE;
        else
            selectQuery = "SELECT  * FROM " + TABLE_DATE + " WHERE " + KEY_DATE_TIME + "=" + "'" + date + "'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                DateModel model = new DateModel();
                model.setTotalLectures(cursor.getInt(1));
                model.setTotalPresents(cursor.getInt(2));
                model.setTotalAbsents(cursor.getInt(3));
                model.setTotalLeaves(cursor.getInt(4));
                model.setTotalLate(cursor.getInt(5));
                model.setDateTime(cursor.getString(6));
                dates.add(model);
            } while (cursor.moveToNext());
        }
        return dates;
    }


    public void addStudentRecord(Students student) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_STUDENT_NAME, student.getName());
        values.put(KEY_STUDENT_ROLL_NUMBER, student.getRollNumber());
        values.put(KEY_CLASS_NAME, student.getClassName());
        values.put(KEY_PRESENT_STATUS, student.getStatus());
        values.put(KEY_DATE_TIME, student.getDateTime());
        values.put(KEY_TOTAL_LECTURES, student.getTotalLectures());
        values.put(KEY_TOTAL_PRESENTS, student.getTotalPresents());
        values.put(KEY_TOTAL_ABSENTS, student.getTotalAbsents());
        values.put(KEY_TOTAL_LEAVES, student.getTotalLeaves());
        values.put(KEY_TOTAL_LATE, student.getTotalLate());
        db.insert(TABLE_STUDENTS, null, values);
        db.insert(TABLE_ATTENDANCE, null, values);
        db.close(); // Closing database connection
    }


    // Updating single record
    public int updateAttendanceRecord(Students student) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_STUDENT_NAME, student.getName());
        values.put(KEY_STUDENT_ROLL_NUMBER, student.getRollNumber());
        values.put(KEY_CLASS_NAME, student.getClassName());
        values.put(KEY_PRESENT_STATUS, student.getStatus());
        values.put(KEY_DATE_TIME, student.getDateTime());
        values.put(KEY_TOTAL_LECTURES, student.getTotalLectures());
        values.put(KEY_TOTAL_PRESENTS, student.getTotalPresents());
        values.put(KEY_TOTAL_ABSENTS, student.getTotalAbsents());
        values.put(KEY_TOTAL_LEAVES, student.getTotalLeaves());
        values.put(KEY_TOTAL_LATE, student.getTotalLate());

        // updating row
        return db.update(TABLE_STUDENTS, values, KEY_STUDENT_ROLL_NUMBER + " = ?", new String[]{student.getRollNumber()});
    }


    public List<Classes> getAllClassList() {
        List<Classes> classesList = new ArrayList<Classes>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_CLASSES;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Classes classes = new Classes();
                classes.setId((cursor.getInt(0)));
                classes.setSubjectName((cursor.getString(1)));
                // Adding contact to list
                classesList.add(classes);
            } while (cursor.moveToNext());
        }

        // return contact list
        return classesList;
    }

    public List<Students> getAllStudentList(String subjectName) {
        List<Students> studentsList = new ArrayList<Students>();
        // Select All Query
        String selectQuery;
        if (subjectName.equalsIgnoreCase("ALL"))
            selectQuery = "SELECT  * FROM " + TABLE_STUDENTS; //+ " WHERE " + KEY_CLASS_NAME + "=" + "'" + subjectName + "'";
        else
            selectQuery = "SELECT  * FROM " + TABLE_STUDENTS + " WHERE " + KEY_CLASS_NAME + "=" + "'" + subjectName + "'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {

                Students students = new Students();
                students.setId((cursor.getInt(0)));
                students.setName((cursor.getString(1)));
                students.setRollNumber((cursor.getString(2)));
                students.setDateTime((cursor.getString(3)));
                students.setStatus((cursor.getString(4)));
                students.setTotalLectures((cursor.getInt(5)));
                students.setTotalPresents((cursor.getInt(6)));
                students.setTotalAbsents((cursor.getInt(7)));
                students.setTotalLeaves((cursor.getInt(8)));
                students.setTotalLate((cursor.getInt(9)));
                students.setClassName((cursor.getString(10)));
                // Adding contact to list
                studentsList.add(students);
            } while (cursor.moveToNext());
        }

        // return contact list
        return studentsList;

    }

    public void addAttendance(Students students) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues value = new ContentValues();
        value.put(KEY_STUDENT_NAME, students.getName());
        value.put(KEY_STUDENT_ROLL_NUMBER, students.getRollNumber());
        value.put(KEY_DATE_TIME, students.getDateTime());
        value.put(KEY_PRESENT_STATUS, students.getStatus());
        value.put(KEY_TOTAL_LECTURES, students.getTotalLectures());
        value.put(KEY_TOTAL_PRESENTS, students.getTotalPresents());
        value.put(KEY_TOTAL_ABSENTS, students.getTotalAbsents());
        value.put(KEY_TOTAL_LEAVES, students.getTotalLeaves());
        value.put(KEY_TOTAL_LATE, students.getTotalLate());
        value.put(KEY_CLASS_NAME, students.getClassName());
        db.insert(TABLE_ATTENDANCE, null, value);
        db.close(); // Closing database connection
    }

    public List<Students> getAttendance(int id, String date) {
        List<Students> studentsList = new ArrayList<Students>();
        // Select All Query
        String selectQuery;
        if (date.equalsIgnoreCase("NA"))
            selectQuery = "SELECT  * FROM " + TABLE_ATTENDANCE + " WHERE " + KEY_ID + "=" + "'" + id + "'";
        else
            selectQuery = "SELECT  * FROM " + TABLE_ATTENDANCE + " WHERE " + KEY_DATE_TIME + "=" + "'" + date + "'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Students students = new Students();
                students.setId((cursor.getInt(0)));
                students.setName((cursor.getString(1)));
                students.setRollNumber((cursor.getString(2)));
                students.setDateTime((cursor.getString(3)));
                students.setStatus((cursor.getString(4)));
                students.setTotalLectures((cursor.getInt(5)));
                students.setTotalPresents((cursor.getInt(6)));
                students.setTotalAbsents((cursor.getInt(7)));
                students.setTotalLeaves((cursor.getInt(8)));
                students.setTotalLate((cursor.getInt(9)));
                students.setClassName((cursor.getString(10)));
                // Adding contact to list
                studentsList.add(students);
            } while (cursor.moveToNext());
        }

        // return contact list
        return studentsList;

    }

}