package com.cleanup.todoc.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.cleanup.todoc.models.Task;

import java.util.List;

@Dao
public interface TaskDao {
    @Insert
    public void insert(Task task);

    @Query("SELECT * FROM task")
    public LiveData<List<Task>> getAll();

    @Query("SELECT task.* FROM task " +
            "JOIN project ON project.id = project_id " +
            "ORDER BY project.name ASC")
    public LiveData<List<Task>> getSortedInAscendingOrderOfProject();

    @Query("SELECT task.* FROM task " +
            "JOIN project ON project.id = project_id " +
            "ORDER BY project.name DESC")
    public LiveData<List<Task>> getSortedInDescendingOrderOfProject();

    @Query("SELECT * FROM task ORDER BY creationTimestamp ASC")
    public LiveData<List<Task>> getSortedInAscendingOrderOfDate();

    @Query("SELECT * FROM task ORDER BY creationTimestamp DESC")
    public LiveData<List<Task>> getSortedInDescendingOrderOfDate();

    @Delete
    public void delete(Task task);
}
