package com.cleanup.todoc.data_persistence;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.cleanup.todoc.data_persistence.dao.ProjectDao;
import com.cleanup.todoc.data_persistence.dao.TaskDao;
import com.cleanup.todoc.model.Project;
import com.cleanup.todoc.model.Task;

import java.util.concurrent.Executors;

@androidx.room.Database(entities = {Task.class, Project.class}, version = 1)
public abstract class Database extends RoomDatabase {

    private static Database instance;

    public abstract TaskDao taskDao();
    public abstract ProjectDao projectDao();

    public static synchronized Database getInstance(Context context) {
        if(instance == null){
            instance = Room.databaseBuilder(context, Database.class, "todoc_database")
                    .addCallback(prepopulate)
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }

    private static final Callback prepopulate = new Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            Executors.newSingleThreadExecutor().execute(() -> {
                instance.projectDao().insert(new Project("Projet Tartampion", 0xFFEADAD1));
                instance.projectDao().insert(new Project("Projet Lucidia", 0xFFB4CDBA));
                instance.projectDao().insert(new Project( "Projet Circus", 0xFFA3CED2));
            });
        }
    };

}
