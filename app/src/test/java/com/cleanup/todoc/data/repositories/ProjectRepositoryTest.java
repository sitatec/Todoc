package com.cleanup.todoc.data.repositories;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.cleanup.todoc.data.dao.ProjectDao;
import com.cleanup.todoc.models.Project;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.verification.VerificationMode;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertSame;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@RunWith(JUnit4.class)
public class ProjectRepositoryTest {
    private ProjectRepository projectRepository;

    @Rule
    public TestRule rule = new InstantTaskExecutorRule();

    private final Project project = new Project("project_test", 0xFFAACC);
    private LiveData<List<Project>> allProjects;
    private ProjectDao projectDao;
    @Before
    public void init(){
        final MutableLiveData<List<Project>> temp_projects = new MutableLiveData<>();
        temp_projects.setValue(Arrays.asList(project));
        allProjects = temp_projects;

        projectDao = mock(ProjectDao.class);
        when(projectDao.getAll()).thenReturn(allProjects);
        when(projectDao.getById(anyLong())).thenReturn(project);

        projectRepository = new ProjectRepository(projectDao);
    }

    @Test
    public void should_get_all_projects(){
        assertSame(allProjects, projectRepository.getAllProjects());
    }

    @Test
    public void should_fetch_projects_through_the_dao_only_once(){
        projectRepository.getAllProjects();
        verify(projectDao).getAll();

        projectRepository.getAllProjects();
        projectRepository.getAllProjects();
        verifyNoMoreInteractions(projectDao);
    }

    @Test
    public void should_get_project_by_id(){
        projectRepository.getById(1L, projectFromDao -> assertSame(projectFromDao, project));
    }
}
