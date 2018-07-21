package com.revesoft.grs.database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import com.revesoft.grs.util.DateConverter;

import java.util.Date;



@Entity
public class UserModel {

    @PrimaryKey(autoGenerate = true)
    public int id;
    private String userName;
    private String passWord;
    @TypeConverters(DateConverter.class)
    private Date borrowDate;

    public UserModel(String itemName, String personName, Date borrowDate) {
        this.userName = itemName;
        this.passWord = personName;
        this.borrowDate = borrowDate;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassWord() {
        return passWord;
    }

    public Date getBorrowDate() {
        return borrowDate;
    }
}
