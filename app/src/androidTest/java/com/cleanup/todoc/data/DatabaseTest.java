package com.cleanup.todoc.data;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.cleanup.todoc.data.dao.ProjectDao;
import com.cleanup.todoc.data.dao.TaskDao;
import com.cleanup.todoc.models.Project;
import com.cleanup.todoc.models.Task;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;

import static com.cleanup.todoc.test_utils.LiveDataUtils.getLiveDataValue;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@RunWith(AndroidJUnit4.class)
public class DatabaseTest {

    private TaskDao taskDao;
    private ProjectDao projectDao;

    private final static String fakeName = "task_name";
    private final static long fakeCreationTimestamp = 29352526;
    private static final Project fakeProject = new Project("project", 0xFFFFFF);
    private static Task fakeTask;

    @Rule
    public TestRule rule = new InstantTaskExecutorRule();

    @BeforeClass
    public static void init(){
        fakeProject.setId(1L);
        fakeTask = new Task(fakeProject.getId(), fakeName, fakeCreationTimestamp);
    }

    @Before
    public void createDbAndDao(){
        final Database database = Database.getTestInstance(ApplicationProvider.getApplicationContext());
        projectDao = database.projectDao();

        taskDao = database.taskDao();
    }

    @Test
    public void the_task_id_should_be_auto_generated() throws InterruptedException {
        assertNull(fakeTask.getId());
        projectDao.insert(fakeProject);
        taskDao.insert(fakeTask);

        final Task taskFromDb = getLiveDataValue(taskDao.getAll()).get(0);
        assertNotNull(taskFromDb.getId());
    }

    @Test
    public void the_project_id_should_be_auto_generated() throws InterruptedException {
        projectDao.insert(new Project("test_name", 0x00AABBCC));

        assertNotNull(projectDao.getByName("test_name").getId());
    }
}
