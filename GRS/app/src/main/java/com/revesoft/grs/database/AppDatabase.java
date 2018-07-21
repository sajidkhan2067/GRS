package com.revesoft.grs.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.revesoft.grs.R;

@Database(entities = {UserModel.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase INSTANCE;

    public static AppDatabase getDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE =
                   Room.databaseBuilder(context.getApplicationContext(),AppDatabase.class,context.getResources().getString(R.string.db_name)).build();
        }
        return INSTANCE;
    }
    public abstract BorrowModelDao itemAndPersonModel();

}
