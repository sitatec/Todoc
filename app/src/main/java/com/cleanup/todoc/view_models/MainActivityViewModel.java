package com.cleanup.todoc.view_models;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.cleanup.todoc.data.repositories.ProjectRepository;
import com.cleanup.todoc.data.repositories.TaskRepository;
import com.cleanup.todoc.models.Project;
import com.cleanup.todoc.models.Task;

import java.util.List;

public class MainActivityViewModel extends AndroidViewModel {

    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;

    private LiveData<List<Task>> tasksSortedInAscendingOrderOfProject;
    private LiveData<List<Task>> tasksSortedInDescendingOrderOfProject;

    private LiveData<List<Task>> tasksSortedByNewestFirst;
    private LiveData<List<Task>> tasksSortedByOldestFirst;

    private List<Project> allProjects;
    private LiveData<List<Task>> allTasks;

    public MainActivityViewModel(@NonNull Application application) {
        super(application);
        projectRepository = new ProjectRepository(application);
        taskRepository = new TaskRepository(application);
    }

    public void addNewTask(Task task){
        taskRepository.add(task);
    }

    public void deleteTask(Task task) {
        taskRepository.delete(task);
    }

    public List<Project> getAllProjects(){
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

}
