package com.cleanup.todoc.data.repositories;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MutableLiveData;

import com.cleanup.todoc.data.dao.TaskDao;
import com.cleanup.todoc.models.Task;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(JUnit4.class)
public class TaskRepositoryTest {
    private TaskRepository taskRepository;

    private final Task fakeTask  = new Task(88L, "88", 8899944L);
    private final Task fakeTask1 = new Task(66L, "66", 3554636L);
    private final Task fakeTask2 = new Task(33L, "33", 5834547L);

    private final MutableLiveData<List<Task>> allTasks = new MutableLiveData<>();
    private final MutableLiveData<List<Task>> allTasksInAscendingOrderOfProjects = new MutableLiveData<>();
    private final MutableLiveData<List<Task>> allTasksInDescendingOrderOfProjects = new MutableLiveData<>();
    private final MutableLiveData<List<Task>> allTasksInAscendingOrderOfDate = new MutableLiveData<>();
    private final MutableLiveData<List<Task>> allTasksInDescendingOrderOfDate = new MutableLiveData<>();

    private TaskDao taskDao;

    @Rule
    public TestRule rule = new InstantTaskExecutorRule();

    @Before
    public void init(){
        taskDao = mock(TaskDao.class);
        setUpMocks();
        taskRepository = new TaskRepository(taskDao);
    }

    @Test
    public void should_insert_a_task() {
        taskRepository.add(fakeTask);
        invokedOnceWithTimeout(taskDao, 100).insert(fakeTask);
    }

    @Test
    public void should_delete_a_task() {
        taskRepository.delete(fakeTask);
        invokedOnceWithTimeout(taskDao, 100).delete(fakeTask);
    }

    @Test
    public void should_return_all_tasks(){
        Assert.assertSame(taskRepository.getAllTasks(), allTasks);
    }

    @Test
    public void should_return_tasks_in_ascending_order_of_project_name(){
        taskRepository.getTasksInAscendingOrderOfProject();
        invokedOnce(taskDao).getSortedInAscendingOrderOfProject();
    }

    @Test
    public void should_return_tasks_in_descending_order_of_project_name(){
        taskRepository.getTasksInDescendingOrderOfProject();
        invokedOnce(taskDao).getSortedInDescendingOrderOfProject();
    }

    @Test
    public void should_return_tasks_in_ascending_order_of_date(){
        taskRepository.getTasksSortedByNewestFirst();
        invokedOnce(taskDao).getSortedInDescendingOrderOfDate();
    }

    @Test
    public void should_return_tasks_in_descending_order_of_date(){
        taskRepository.getTasksSortedByOldestFirst();
        invokedOnce(taskDao).getSortedInAscendingOrderOfDate();
    }

    @Test
    public void should_fetch_all_tasks_through_the_dao_only_once(){
        taskRepository.getAllTasks();
        invokedOnce(taskDao).getAll();

        taskRepository.getAllTasks();
        taskRepository.getAllTasks();
        verifyNoMoreInteractions(taskDao);
    }


    @Test
    public void should_fetch_tasks_in_ascending_order_of_project_name_through_the_dao_only_once(){
        taskRepository.getTasksInAscendingOrderOfProject();
        invokedOnce(taskDao).getSortedInAscendingOrderOfProject();

        taskRepository.getTasksInAscendingOrderOfProject();
        verifyNoMoreInteractions(taskDao);
    }

    @Test
    public void should_fetch_tasks_in_descending_order_of_project_name_through_the_dao_only_once(){
        taskRepository.getTasksInDescendingOrderOfProject();
        invokedOnce(taskDao).getSortedInDescendingOrderOfProject();

        taskRepository.getTasksInDescendingOrderOfProject();
        verifyNoMoreInteractions(taskDao);
    }


    @Test
    public void should_fetch_tasks_in_ascending_order_of_date_through_the_dao_only_once(){
        taskRepository.getTasksSortedByNewestFirst();
        invokedOnce(taskDao).getSortedInDescendingOrderOfDate();

        taskRepository.getTasksSortedByNewestFirst();
        verifyNoMoreInteractions(taskDao);
    }


    @Test
    public void should_fetch_tasks_in_descending_order_of_date_through_the_dao_only_once(){
        taskRepository.getTasksSortedByOldestFirst();
        invokedOnce(taskDao).getSortedInAscendingOrderOfDate();

        taskRepository.getTasksSortedByOldestFirst();
        verifyNoMoreInteractions(taskDao);
    }


    /////////// -------- UTILS METHODS -------- /////////////

    public <T> T invokedOnce(T object){
        return verify(object, times(1));
    }
    
    public <T> T invokedOnceWithTimeout(T object, int millis){
        return verify(object, timeout(millis).times(1));
    }
    
    public void setUpMocks(){
        initFakeValues();
        when(taskDao.getAll()).thenReturn(allTasks);

        when(taskDao.getSortedInAscendingOrderOfProject()).thenReturn(allTasksInAscendingOrderOfProjects);
        when(taskDao.getSortedInDescendingOrderOfProject()).thenReturn(allTasksInDescendingOrderOfProjects);

        when(taskDao.getSortedInAscendingOrderOfDate()).thenReturn(allTasksInAscendingOrderOfDate);
        when(taskDao.getSortedInDescendingOrderOfDate()).thenReturn(allTasksInDescendingOrderOfDate);
    }

    public void initFakeValues(){;
        allTasks.setValue(Arrays.asList(fakeTask, fakeTask1, fakeTask2));

        allTasksInAscendingOrderOfProjects.setValue(Arrays.asList(fakeTask2, fakeTask1, fakeTask));
        allTasksInDescendingOrderOfProjects.setValue(Arrays.asList(fakeTask, fakeTask1, fakeTask2));

        allTasksInAscendingOrderOfDate.setValue(Arrays.asList(fakeTask1, fakeTask2, fakeTask));
        allTasksInDescendingOrderOfDate.setValue(Arrays.asList(fakeTask, fakeTask2, fakeTask1));
    }

}
