package com.cleanup.todoc.view_models;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MutableLiveData;

import com.cleanup.todoc.data.repositories.ProjectRepository;
import com.cleanup.todoc.data.repositories.TaskRepository;
import com.cleanup.todoc.models.Project;
import com.cleanup.todoc.models.Task;


import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(JUnit4.class)
public class MainActivityViewModelTest {

    private MainActivityViewModel viewModel;
    private ProjectRepository projectRepository;
    private TaskRepository taskRepository;

    private final Task fakeTask  = new Task(88L, "88", 8899944L);
    private final Task fakeTask1 = new Task(66L, "66", 3554636L);
    private final Task fakeTask2 = new Task(33L, "33", 5834547L);

    private final MutableLiveData<List<Task>> allTasks = new MutableLiveData<>();
    private final MutableLiveData<List<Task>> allTasksInAscendingOrderOfProjects = new MutableLiveData<>();
    private final MutableLiveData<List<Task>> allTasksInDescendingOrderOfProjects = new MutableLiveData<>();
    private final MutableLiveData<List<Task>> allTasksInAscendingOrderOfDate = new MutableLiveData<>();
    private final MutableLiveData<List<Task>> allTasksInDescendingOrderOfDate = new MutableLiveData<>();


    private final Project project = new Project("project_test", 0xFFAACC);
    private final Project project1 = new Project("project_test1", 0xFFAABC);
    private MutableLiveData<List<Project>> allProjects = new MutableLiveData<>();

    @Rule
    public TestRule rule = new InstantTaskExecutorRule();
    
    @Before
    public void init(){
        projectRepository = mock(ProjectRepository.class);
        taskRepository = mock(TaskRepository.class);
        viewModel = new MainActivityViewModel(projectRepository, taskRepository);
        setUpMocks();
    }

    @Test
    public void the_factory_should_create_the_view_model(){
        final MainActivityViewModel.Factory factory =
                new MainActivityViewModel.Factory(projectRepository, taskRepository);
        assertTrue(factory.create(MainActivityViewModel.class) instanceof MainActivityViewModel);
    }

    @Test
    public void should_add_new_Task(){
        viewModel.addNewTask(fakeTask);
        verify(taskRepository).add(fakeTask);
    }
    
    @Test
    public void should_delete_a_Task(){
        viewModel.deleteTask(fakeTask);
        verify(taskRepository).delete(fakeTask);
    }
    
    @Test
    public void should_return_all_tasks(){
        assertSame(viewModel.getAllTasks(), allTasks);
    }
    
    @Test
    public void should_return_all_projects(){
        assertSame(viewModel.getAllProjects(), allProjects);
    }

    @Test
    public void should_return_tasks_in_ascending_order_of_project_name(){
        viewModel.getTasksInAscendingOrderOfProject();
        verify(taskRepository).getTasksInAscendingOrderOfProject();
    }

    @Test
    public void should_return_tasks_in_descending_order_of_project_name(){
        viewModel.getTasksInDescendingOrderOfProject();
        verify(taskRepository).getTasksInDescendingOrderOfProject();
    }

    @Test
    public void should_return_tasks_in_ascending_order_of_date(){
        viewModel.getTasksSortedByOldestFirst();
        verify(taskRepository).getTasksSortedByOldestFirst();
    }

    @Test
    public void should_return_tasks_in_descending_order_of_date(){
        viewModel.getTasksSortedByNewestFirst();
        verify(taskRepository).getTasksSortedByNewestFirst();
    }


    //// --------------- Utils ---------------- ////
    
    public void setUpMocks(){
        initFakeValues();
        ///////////////  TASKS  ////////////////
        when(taskRepository.getAllTasks()).thenReturn(allTasks);

        when(taskRepository.getTasksInAscendingOrderOfProject()).thenReturn(allTasksInAscendingOrderOfProjects);
        when(taskRepository.getTasksInDescendingOrderOfProject()).thenReturn(allTasksInDescendingOrderOfProjects);

        when(taskRepository.getTasksSortedByOldestFirst()).thenReturn(allTasksInAscendingOrderOfDate);
        when(taskRepository.getTasksSortedByNewestFirst()).thenReturn(allTasksInDescendingOrderOfDate);
        ///////////////  PROJECTS  ////////////////
        when(projectRepository.getAllProjects()).thenReturn(allProjects);
    }

    public void initFakeValues(){;
            ///////////////  TASKS  ////////////////
        allTasks.setValue(Arrays.asList(fakeTask, fakeTask1, fakeTask2));

        allTasksInAscendingOrderOfProjects.setValue(Arrays.asList(fakeTask2, fakeTask1, fakeTask));
        allTasksInDescendingOrderOfProjects.setValue(Arrays.asList(fakeTask, fakeTask1, fakeTask2));

        allTasksInAscendingOrderOfDate.setValue(Arrays.asList(fakeTask1, fakeTask2, fakeTask));
        allTasksInDescendingOrderOfDate.setValue(Arrays.asList(fakeTask, fakeTask2, fakeTask1));

            ///////////////  PROJECTS  ////////////////
        allProjects.setValue(Arrays.asList(project, project1));
    }

}
