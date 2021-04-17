package com.cleanup.todoc.view_models;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.cleanup.todoc.data.repositories.ProjectRepository;
import com.cleanup.todoc.data.repositories.TaskRepository;
import com.cleanup.todoc.models.Project;
import com.cleanup.todoc.models.Task;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

public class MainActivityViewModel extends ViewModel {

    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;

    private LiveData<List<Task>> tasksSortedInAscendingOrderOfProject;
    private LiveData<List<Task>> tasksSortedInDescendingOrderOfProject;

    private LiveData<List<Task>> tasksSortedByNewestFirst;
    private LiveData<List<Task>> tasksSortedByOldestFirst;

    private LiveData<List<Project>> allProjects;
    private LiveData<List<Task>> allTasks;

    public MainActivityViewModel(ProjectRepository projectRepository, TaskRepository taskRepository) {
        this.projectRepository = projectRepository;
        this.taskRepository = taskRepository;
    }

    public void addNewTask(Task task){
        taskRepository.add(task);
    }

    public void deleteTask(Task task) {
        taskRepository.delete(task);
    }

    public LiveData<List<Project>> getAllProjects(){
        if(allProjects == null) allProjects = projectRepository.getAllProjects();
        return allProjects;
    }

    public LiveData<List<Task>> getAllTasks(){
        if(allTasks == null) allTasks = taskRepository.getAllTasks();
        return allTasks;
    }

    public LiveData<List<Task>> getTasksInAscendingOrderOfProject(){
        if(tasksSortedInAscendingOrderOfProject == null)
            tasksSortedInAscendingOrderOfProject = taskRepository.getTasksInAscendingOrderOfProject();
        return tasksSortedInAscendingOrderOfProject;
    }

    public LiveData<List<Task>> getTasksInDescendingOrderOfProject(){
        if (tasksSortedInDescendingOrderOfProject == null) {
            tasksSortedInDescendingOrderOfProject = taskRepository.getTasksInDescendingOrderOfProject();
        }
        return tasksSortedInDescendingOrderOfProject;
    }

    public LiveData<List<Task>> getTasksSortedByNewestFirst() {
        if(tasksSortedByNewestFirst == null) {
            tasksSortedByNewestFirst = taskRepository.getTasksSortedByNewestFirst();
        }
        return tasksSortedByNewestFirst;
    }

    public LiveData<List<Task>> getTasksSortedByOldestFirst() {
        if(tasksSortedByOldestFirst == null){
            tasksSortedByOldestFirst = taskRepository.getTasksSortedByOldestFirst();
        }
        return tasksSortedByOldestFirst;
    }

    public static class Factory implements ViewModelProvider.Factory {

        private final ProjectRepository projectRepository;
        private final TaskRepository taskRepository;

        public Factory(ProjectRepository projectRepository, TaskRepository taskRepository){
            this.taskRepository = taskRepository;
            this.projectRepository = projectRepository;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            try {
                return modelClass.getConstructor(ProjectRepository.class, TaskRepository.class)
                        .newInstance(projectRepository, taskRepository);
            } catch (IllegalAccessException | InstantiationException | InvocationTargetException | NoSuchMethodException e) {
                throw new RuntimeException("Cannot create an instance of " + modelClass, e);
            }
        }
    }
}
