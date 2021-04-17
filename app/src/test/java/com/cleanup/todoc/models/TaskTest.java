package com.cleanup.todoc.models;

import androidx.test.core.app.ApplicationProvider;

import com.cleanup.todoc.data.Database;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

@RunWith(JUnit4.class)
public class TaskTest {

    private final static String fakeName = "task_name";
    private final static long fakeCreationTimestamp = 29352526;
    private static final Project fakeProject = new Project("project", 0xFFFFFF);
    public static Task fakeTask;

    @BeforeClass
    public static void init(){
        fakeProject.setId(1L);
        fakeTask = new Task(fakeProject.getId(), fakeName, fakeCreationTimestamp);
    }

    @Test
    public void should_get_the_task_name(){
        assertEquals(fakeTask.getName(), fakeName);
    }

    @Test
    public void should_get_the_project_id(){
        assertEquals(fakeTask.getProjectId(), fakeProject.getId());
    }

    @Test
    public void should_get_the_task_creation_timestamp(){
        assertEquals(fakeTask.getCreationTimestamp(), fakeCreationTimestamp);
    }

    @Test
    public void should_set_the_task_id(){
        final Long fakeId = 75L;
        fakeTask.setId(fakeId);
        assertSame(fakeTask.getId(), fakeId);
    }

}
