package com.cleanup.todoc.data.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.cleanup.todoc.models.Project;

import java.util.List;

@Dao
public interface ProjectDao {
    @Insert
    public void insert(Project project);

    @Query("SELECT * FROM project")
    public List<Project> getAll();

//    @Query("SELECT * FROM project JOIN task ON task.project_id = project.id")
//    public List<Task> getAllTasks();
}
