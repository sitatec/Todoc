package com.cleanup.todoc.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.cleanup.todoc.models.Project;

import java.util.List;

@Dao
public interface ProjectDao {
    @Insert
    public void insert(Project project); // To prepopulate the database && for testing

    @Query("SELECT * FROM project WHERE name = :name")
    public Project getByName(String name); //for testing

    @Query("SELECT * FROM project")
    public LiveData<List<Project>> getAll();

    @Query("SELECT * FROM project WHERE id = :id")
    public Project getById(Long id);

//    @Query("SELECT * FROM project JOIN task ON task.project_id = project.id")
//    public List<Task> getAllTasks();
}
