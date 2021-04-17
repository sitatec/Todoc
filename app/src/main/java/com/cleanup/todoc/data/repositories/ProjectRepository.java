package com.cleanup.todoc.data.repositories;

import androidx.lifecycle.LiveData;

import com.cleanup.todoc.data.dao.ProjectDao;
import com.cleanup.todoc.models.Project;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ProjectRepository {
    private final ProjectDao projectDao;
    private LiveData<List<Project>> allProjects;
    private final Executor doInBackground = Executors.newSingleThreadExecutor();

    public ProjectRepository(ProjectDao projectDao){
        this.projectDao = projectDao;
    }

    public LiveData<List<Project>> getAllProjects(){
        if(allProjects == null) allProjects = projectDao.getAll();
        return allProjects;
    }

    public void getById(Long id, GetProjectListener listener){
        doInBackground.execute(() -> listener.onGet(projectDao.getById(id)));
    }

    public interface GetProjectListener {
        void onGet(Project project);
    }

//    public void add(Project project){
//        doInBackground.execute(() -> projectDao.insert(project));
//    }
}
