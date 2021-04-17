package com.cleanup.todoc.view_models;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.cleanup.todoc.data.Database;
import com.cleanup.todoc.data.repositories.ProjectRepository;
import com.cleanup.todoc.data.repositories.TaskRepository;
import com.cleanup.todoc.models.Project;
import com.cleanup.todoc.models.Task;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;

import java.util.List;

import static com.cleanup.todoc.test_utils.LiveDataUtils.getLiveDataValue;
import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class MainActivityViewModelIntegrationTest {

    private final Task fakeTask  = new Task(1L, "88", 00000000L);
    private final Task fakeTask1 = new Task(2L, "66", 11111111L);
    private final Task fakeTask2 = new Task(3L, "33", 22222222L);

    private final Project project = new Project( "Projet Circus", 0xFFA3CED2);
    private final Project project1 = new Project("Projet Lucidia", 0xFFB4CDBA);
    private final Project project2 = new Project("Projet Tartampion", 0xFFEADAD1);

    private TaskRepository taskRepository;
    private MainActivityViewModel viewModel;

    Database database;

    @Rule
    public TestRule rule = new InstantTaskExecutorRule();

    @Before
    public void init(){
        database = Database.getTestInstance(ApplicationProvider.getApplicationContext());
        final ProjectRepository projectRepository = new ProjectRepository(database.projectDao());
        taskRepository = new TaskRepository(database.taskDao());
        viewModel = new MainActivityViewModel(projectRepository, taskRepository);
        fillDb();
    }

    @Test
    public void should_add_new_Task() throws InterruptedException {
        final int initialTasksCount = getLiveDataValue(viewModel.getAllTasks()).size();
        viewModel.addNewTask(new Task(2L, "name_add_new", 757538));
        Thread.sleep(100); // Waiting for the added Task to take effect
        assertEquals(getLiveDataValue(viewModel.getAllTasks()).size(), initialTasksCount + 1);
    }

    @Test
    public void should_delete_a_Task() throws InterruptedException {
        final int initialTasksCount = getLiveDataValue(viewModel.getAllTasks()).size();
        viewModel.deleteTask(fakeTask);
        Thread.sleep(100); // Waiting for the deletion to take effect
        assertEquals(getLiveDataValue(viewModel.getAllTasks()).size(), initialTasksCount - 1);
    }

    @Test
    public void should_return_all_tasks() throws InterruptedException {
        assertEquals(getLiveDataValue(viewModel.getAllTasks()).size(), 3);
    }

    @Test
    public void should_return_all_projects() throws InterruptedException {
        assertEquals(getLiveDataValue(viewModel.getAllProjects()).size(), 3);
    }

    @Test
    public void should_return_tasks_in_ascending_order_of_project_name() throws InterruptedException {
        final List<Task> tasks = getLiveDataValue(viewModel.getTasksInAscendingOrderOfProject());
        assertEquals(tasks.get(0).getProjectId(), new Long(1));
        assertEquals(tasks.get(1).getProjectId(), new Long(2));
        assertEquals(tasks.get(2).getProjectId(), new Long(3));
    }

    @Test
    public void should_return_tasks_in_descending_order_of_project_name() throws InterruptedException {
        final List<Task> tasks = getLiveDataValue(viewModel.getTasksInDescendingOrderOfProject());
        assertEquals(tasks.get(0).getProjectId(), new Long(3));
        assertEquals(tasks.get(1).getProjectId(), new Long(2));
        assertEquals(tasks.get(2).getProjectId(), new Long(1));
    }

    @Test
    public void should_return_tasks_in_ascending_order_of_date() throws InterruptedException {
        final List<Task> tasks = getLiveDataValue(viewModel.getTasksSortedByOldestFirst());
        assertEquals(tasks.get(0).getName(), fakeTask.getName());
        assertEquals(tasks.get(1).getName(), fakeTask1.getName());
        assertEquals(tasks.get(2).getName(), fakeTask2.getName());
    }

    @Test
    public void should_return_tasks_in_descending_order_of_date() throws InterruptedException {
        final List<Task> tasks = getLiveDataValue(viewModel.getTasksSortedByNewestFirst());
        assertEquals(tasks.get(0).getName(), fakeTask2.getName());
        assertEquals(tasks.get(1).getName(), fakeTask1.getName());
        assertEquals(tasks.get(2).getName(), fakeTask.getName());
    }


    ////////////////  UTILS ////////////////

    private void fillDb(){
        database.projectDao().insert(project);
        database.projectDao().insert(project1);
        database.projectDao().insert(project2);

        taskRepository.add(fakeTask);
        taskRepository.add(fakeTask1);
        taskRepository.add(fakeTask2);
        fakeTask.setId(1L);
    }
}
