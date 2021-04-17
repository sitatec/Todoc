package com.cleanup.todoc.models;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

@RunWith(JUnit4.class)
public class ProjectTest {
    private final String projectName = "test";
    private final int projectColor = 0xAABBCC;
    private final Project project = new Project(projectName, projectColor);

    @Test
    public void should_get_the_project_name(){
        assertSame(project.getName(), projectName);
    }

    @Test
    public void should_get_the_project_color(){
        assertEquals(projectColor, project.getColor());
    }

    @Test
    public void should_set_the_project_id(){
        final Long projectId = 46L;
        project.setId(projectId);
        assertSame(projectId, project.getId());
    }
}
