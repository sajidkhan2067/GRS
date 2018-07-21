package com.revesoft.grs.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.TypeConverters;

import com.revesoft.grs.util.DateConverter;

import java.util.List;


import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
@TypeConverters(DateConverter.class)
public interface BorrowModelDao {

    @Query("select * from UserModel")
    LiveData<List<UserModel>> getAllBorrowedItems();

    @Query("select * from UserModel where id = :id")
    UserModel getItembyId(String id);

    @Insert(onConflict = REPLACE)
    void addBorrow(UserModel userModel);

    @Delete
    void deleteBorrow(UserModel userModel);
}
