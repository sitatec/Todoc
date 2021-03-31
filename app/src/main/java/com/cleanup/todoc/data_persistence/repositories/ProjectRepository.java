package com.cleanup.todoc.data_persistence.repositories;

import android.app.Application;

import com.cleanup.todoc.data_persistence.Database;
import com.cleanup.todoc.data_persistence.dao.ProjectDao;
import com.cleanup.todoc.model.Project;

import java.util.List;
import java.util.concurrent.Executors;

public class ProjectRepository {
    private final ProjectDao projectDao;

    public ProjectRepository(Application application){
        projectDao = Database.getInstance(application).projectDao();
    }

    public void add(Project project){
        Executors.newSingleThreadExecutor().execute(() -> {
            projectDao.insert(project);
        });
    }

    public List<Project> getAll(){
        return projectDao.getAll();
    }
}
