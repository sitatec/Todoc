package com.cleanup.todoc.data.repositories;

import android.app.Application;

import com.cleanup.todoc.data.Database;
import com.cleanup.todoc.data.dao.ProjectDao;
import com.cleanup.todoc.models.Project;

import java.util.List;
import java.util.concurrent.Executors;

public class ProjectRepository {
    private final ProjectDao projectDao;
    private List<Project> allProjects;

    public ProjectRepository(Application application){
        projectDao = Database.getInstance(application).projectDao();
    }

    public void add(Project project){
        Executors.newSingleThreadExecutor().execute(() -> {
            projectDao.insert(project);
        });
    }

    public List<Project> getAllProjects(){
        if(allProjects == null) allProjects = projectDao.getAll();
        return allProjects;
    }
}
