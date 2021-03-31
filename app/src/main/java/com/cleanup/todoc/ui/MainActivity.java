package com.cleanup.todoc.ui;

import android.content.DialogInterface;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.cleanup.todoc.R;
import com.cleanup.todoc.models.Project;
import com.cleanup.todoc.models.Task;
import com.cleanup.todoc.view_models.MainActivityViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * <p>Home activity of the application which is displayed when the user opens the app.</p>
 * <p>Displays the list of tasks.</p>
 *
 * @author Gaëtan HERFRAY
 */
public class MainActivity extends AppCompatActivity {
    /**
     * List of all projects available in the application
     */
    private Project[] allProjects;

    /**
     * List of all current tasks of the application
     */
    private LiveData<List<Task>> tasks;

    /**
     * The adapter which handles the list of tasks
     */
    private TasksAdapter adapter;

    /**
     * The sort method to be used to display tasks
     */
    private SortMethod sortMethod = SortMethod.NONE;

    /**
     * Dialog to create a new task
     */
    @Nullable
    public AlertDialog dialog;

    /**
     * EditText that allows user to set the name of a task
     */
    @Nullable
    private EditText dialogEditText;

    /**
     * Spinner that allows the user to associate a project to a task
     */
    @Nullable
    private Spinner dialogSpinner;

    /**
     * The RecyclerView which displays the list of tasks
     */
    private RecyclerView listTasks;

    /**
     * The TextView displaying the empty state
     */
    private TextView lblNoTasks;

    private MainActivityViewModel viewModel;

    private boolean isTaskListEmpty = true;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory())
                .get(MainActivityViewModel.class);
        tasks = viewModel.getAllTasks();
        allProjects = viewModel.getAllProjects().toArray(new Project[]{});
        setUpViews();
    }

    private void setUpViews(){
        lblNoTasks = findViewById(R.id.lbl_no_task);
        findViewById(R.id.fab_add_task).setOnClickListener(this::showAddTaskDialog);
        setUpTasksRecyclerView();
    }

    private void setUpTasksRecyclerView(){
        listTasks = findViewById(R.id.list_tasks);
        listTasks.setLayoutManager(new LinearLayoutManager(this));
        adapter = new TasksAdapter(new ArrayList<>(), this::onDeleteTask);
        listTasks.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actions, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        tasks.removeObservers(this);

        if (id == R.id.filter_alphabetical) {
            tasks = viewModel.getTasksInAscendingOrderOfProject();
        } else if (id == R.id.filter_alphabetical_inverted) {
            tasks = viewModel.getTasksInDescendingOrderOfProject();
        } else if (id == R.id.filter_oldest_first) {
            tasks = viewModel.getTasksSortedByOldestFirst();
        } else if (id == R.id.filter_recent_first) {
            tasks = viewModel.getTasksSortedByNewestFirst();
        }

        updateTasks();

        return super.onOptionsItemSelected(item);
    }

    public void onDeleteTask(Task task) {
        viewModel.deleteTask(task);
        updateTasks();
    }

    /**
     * Called when the user clicks on the positive button of the Create Task Dialog.
     *
     * @param dialogInterface the current displayed dialog
     */
    private void onPositiveButtonClick(DialogInterface dialogInterface) {
        // If dialog is open
        if (dialogEditText != null && dialogSpinner != null) {
            // Get the name of the task
            String taskName = dialogEditText.getText().toString();

            // Get the selected project to be associated to the task
            Project taskProject = null;
            if (dialogSpinner.getSelectedItem() instanceof Project) {
                taskProject = (Project) dialogSpinner.getSelectedItem();
            }

            // If a name has not been set
            if (taskName.trim().isEmpty()) {
                dialogEditText.setError(getString(R.string.empty_task_name));
            }
            // If both project and name of the task have been set
            else if (taskProject != null) {
                // TODO: Replace this by id of persisted task
                long id = (long) (Math.random() * 50000);


                Task task = new Task(
                        taskProject.getId(),
                        taskName,
                        new Date().getTime()
                );

                addTask(task);

                dialogInterface.dismiss();
            }
            // If name has been set, but project has not been set (this should never occur)
            else{
                dialogInterface.dismiss();
            }
        }
        // If dialog is aloready closed
        else {
            dialogInterface.dismiss();
        }
    }

    /**
     * Shows the Dialog for adding a Task
     */
    private void showAddTaskDialog(View v) {
        final AlertDialog dialog = getAddTaskDialog();

        dialog.show();

        dialogEditText = dialog.findViewById(R.id.txt_task_name);
        dialogSpinner = dialog.findViewById(R.id.project_spinner);

        populateDialogSpinner();
    }

    /**
     * Adds the given task to the list of created tasks.
     *
     * @param task the task to be added to the list
     */
    private void addTask(@NonNull Task task) {
        viewModel.addNewTask(task);
    }

    /**
     * Updates the list of tasks in the UI
     */
    private void updateTasks() {
//            switch (sortMethod) {
//                case ALPHABETICAL:
//                    Collections.sort(tasks, new Task.TaskAZComparator());
//                    break;
//                case ALPHABETICAL_INVERTED:
//                    Collections.sort(tasks, new Task.TaskZAComparator());
//                    break;
//                case RECENT_FIRST:
//                    Collections.sort(tasks, new Task.TaskRecentComparator());
//                    break;
//                case OLD_FIRST:
//                    Collections.sort(tasks, new Task.TaskOldComparator());
//                    break;
//
//            }
        tasks.observe(this, newTasks -> {
            isTaskListEmpty = newTasks.isEmpty();
            updateViewState();
            adapter.updateTasks(newTasks);
        });
    }

    private void updateViewState(){
        if (isTaskListEmpty) {
            lblNoTasks.setVisibility(View.VISIBLE);
            listTasks.setVisibility(View.GONE);
        } else {
            lblNoTasks.setVisibility(View.GONE);
            listTasks.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Returns the dialog allowing the user to create a new task.
     *
     * @return the dialog allowing the user to create a new task
     */
    @NonNull
    private AlertDialog getAddTaskDialog() {
        final AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this, R.style.Dialog);

        alertBuilder.setTitle(R.string.add_task);
        alertBuilder.setView(R.layout.dialog_add_task);
        alertBuilder.setPositiveButton(R.string.add, null);
        alertBuilder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                dialogEditText = null;
                dialogSpinner = null;
                dialog = null;
            }
        });

        dialog = alertBuilder.create();

        // This instead of listener to positive button in order to avoid automatic dismiss
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(DialogInterface dialogInterface) {

                Button button = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        onPositiveButtonClick(dialog);
                    }
                });
            }
        });

        return dialog;
    }

    /**
     * Sets the data of the Spinner with projects to associate to a new task
     */
    private void populateDialogSpinner() {
        final ArrayAdapter<Project> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, allProjects);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        if (dialogSpinner != null) {
            dialogSpinner.setAdapter(adapter);
        }
    }

    /**
     * List of all possible sort methods for task
     */
    private enum SortMethod {
        /**
         * Sort alphabetical by name
         */
        ALPHABETICAL,
        /**
         * Inverted sort alphabetical by name
         */
        ALPHABETICAL_INVERTED,
        /**
         * Lastly created first
         */
        RECENT_FIRST,
        /**
         * First created first
         */
        OLD_FIRST,
        /**
         * No sort
         */
        NONE
    }
}
