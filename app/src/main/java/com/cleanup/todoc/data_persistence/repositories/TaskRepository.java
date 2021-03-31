package com.cleanup.todoc.data_persistence.repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.cleanup.todoc.data_persistence.Database;
import com.cleanup.todoc.data_persistence.dao.TaskDao;
import com.cleanup.todoc.model.Task;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class TaskRepository {

    private final TaskDao taskDao;
    private final Executor doInBackground;

    private LiveData<List<Task>> allTasks;

    private LiveData<List<Task>> tasksSortedInAscendingOrderOfProject;
    private LiveData<List<Task>> tasksSortedInDescendingOrderOfProject;

    private LiveData<List<Task>> tasksSortedByNewestFirst;
    private LiveData<List<Task>> tasksSortedByOldestFirst;

    public TaskRepository(Application application){
        taskDao = Database.getInstance(application).taskDao();
        doInBackground = Executors.newSingleThreadExecutor();
    }

    public void add(Task task){
        doInBackground.execute(() -> {
            taskDao.insert(task);
        });
    }

    public LiveData<List<Task>> getAllTasks(){
        if(allTasks == null) allTasks = taskDao.getAll();
        return allTasks;
    }

    public LiveData<List<Task>> getTasksSortedInAscendingOrderOfProject(){
        if(tasksSortedInAscendingOrderOfProject == null)
            tasksSortedInAscendingOrderOfProject = taskDao.getSortedInAscendingOrderOfProject();
        return tasksSortedInAscendingOrderOfProject;
    }

    public LiveData<List<Task>> getTasksSortedInDescendingOrderOfProject(){
        if (tasksSortedInDescendingOrderOfProject == null) {
          tasksSortedInDescendingOrderOfProject = taskDao.getSortedInDescendingOrderOfProject();
        }
        return tasksSortedInDescendingOrderOfProject;
    }

    public LiveData<List<Task>> getTasksSortedByNewestFirst() {
        if(tasksSortedByNewestFirst == null) {
            tasksSortedByNewestFirst = taskDao.getSortedInAscendingOrderOfDate();
        }
        return tasksSortedByNewestFirst;
    }

    public LiveData<List<Task>> getTasksSortedByOldestFirst() {
        if(tasksSortedByOldestFirst == null){
            tasksSortedByOldestFirst = taskDao.getSortedInDescendingOrderOfDate();
        }
        return tasksSortedByOldestFirst;
    }
}
