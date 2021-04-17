package com.cleanup.todoc.utils;

import android.app.Application;

import com.cleanup.todoc.data.Database;
import com.cleanup.todoc.data.repositories.ProjectRepository;
import com.cleanup.todoc.data.repositories.TaskRepository;

public class DependencyContainer {
    public final ProjectRepository projectRepository;
    public final TaskRepository taskRepository;

    public DependencyContainer(Application application){
        final Database database = Database.getInstance(application);
        projectRepository = new ProjectRepository(database.projectDao());
        taskRepository = new TaskRepository(database.taskDao());
    }
}
