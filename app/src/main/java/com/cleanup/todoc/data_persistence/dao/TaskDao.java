package com.cleanup.todoc.data_persistence.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.cleanup.todoc.model.Task;

import java.util.List;

@Dao
public interface TaskDao {
    @Insert
    public void insert(Task task);

    @Query("SELECT * FROM task")
    LiveData<List<Task>> getAll();

    @Query("SELECT * FROM task " +
            "JOIN project ON project.id = project_id " +
            "ORDER BY project.name ASC")
    LiveData<List<Task>> getSortedInAscendingOrderOfProject();

    @Query("SELECT * FROM task " +
            "JOIN project ON project.id = project_id " +
            "ORDER BY project.name DESC")
    LiveData<List<Task>> getSortedInDescendingOrderOfProject();

    @Query("SELECT * FROM task ORDER BY creationTimestamp ASC")
    LiveData<List<Task>> getSortedInAscendingOrderOfDate();

    @Query("SELECT * FROM task ORDER BY creationTimestamp DESC")
    LiveData<List<Task>> getSortedInDescendingOrderOfDate();
}
