package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.example.myapplication.Adapter.TaskAdapter;
import com.example.myapplication.Model.Task;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    ListView lvTasks;
    AutoCompleteTextView actvSearch;
    ImageButton ibtnSearch, ibtnAdd, ibtnFilter;
    TextView tvStatus, tvOrderBy;
    List<Task> taskList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        viewMapping();
        fakeData();
        TaskAdapter taskAdapter = new TaskAdapter(this, R.layout.task_item, taskList);
        lvTasks.setAdapter(taskAdapter);
    }

    private void viewMapping() {
        lvTasks = findViewById(R.id.lv_tasks);
        actvSearch = findViewById(R.id.actvSearch);
        ibtnSearch = findViewById(R.id.ibtnSearch);
        ibtnAdd = findViewById(R.id.ibtnAdd);
        ibtnFilter = findViewById(R.id.ibtnFilter);
        tvStatus = findViewById(R.id.tvStatus);
        tvOrderBy = findViewById(R.id.tvOrderBy);
    }

    private void fakeData() {
        taskList = new ArrayList<Task>();
        Date date = new Date();
        Timestamp timestamp = new Timestamp(date.getTime());
        taskList.add(new Task("task 1", timestamp));
        taskList.add(new Task("task 2", timestamp));
        taskList.add(new Task("task 3", timestamp));
        taskList.add(new Task("task 4", timestamp));
        taskList.add(new Task("task 5", timestamp));
        taskList.add(new Task("task 6", timestamp));
        taskList.add(new Task("task 7", timestamp));
        taskList.add(new Task("task 8", timestamp));
        taskList.add(new Task("task 9", timestamp));
        taskList.add(new Task("task 10", timestamp));
        taskList.add(new Task("task 1", timestamp));
        taskList.add(new Task("task 2", timestamp));
        taskList.add(new Task("task 3", timestamp));
        taskList.add(new Task("task 4", timestamp));
        taskList.add(new Task("task 5", timestamp));
        taskList.add(new Task("task 6", timestamp));
        taskList.add(new Task("task 7", timestamp));
        taskList.add(new Task("task 8", timestamp));
        taskList.add(new Task("task 9", timestamp));
        taskList.add(new Task("task 10", timestamp));
        taskList.add(new Task("task 1", timestamp));
        taskList.add(new Task("task 2", timestamp));
        taskList.add(new Task("task 3", timestamp));
        taskList.add(new Task("task 4", timestamp));
        taskList.add(new Task("task 5", timestamp));
        taskList.add(new Task("task 6", timestamp));
        taskList.add(new Task("task 7", timestamp));
        taskList.add(new Task("task 8", timestamp));
        taskList.add(new Task("task 9", timestamp));
        taskList.add(new Task("task 10", timestamp));
    }
}