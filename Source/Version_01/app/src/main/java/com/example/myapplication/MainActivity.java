package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.myapplication.Adapter.TaskAdapter;
import com.example.myapplication.Model.Task;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ListView lvTasks;
    private AutoCompleteTextView actvSearch;
    private ImageButton ibtnSearch, ibtnAdd, ibtnFilter;
    private TextView tvStatus, tvOrderBy;
    private List<Task> taskList;
    //    private final String DEFAULT_FILTER_STATUS = getResources().getString(R.string.all); => ERROR : Cannot access to null object
    //    Cannot do this => Vậy nên chuyển thành biến thường và đưa vào Initialize()
    //    để getStringResource gán vào
    private String DEFAULT_FILTER_STATUS;
    private String DEFAULT_FILTER_ORDER_BY;
    private String filterStatus;
    private String filterOrderBy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        viewMapping();
        initialize();
        fakeData();
        TaskAdapter taskAdapter = new TaskAdapter(this, R.layout.task_item, taskList);
        lvTasks.setAdapter(taskAdapter);

        ibtnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFilterDialog(MainActivity.this);
            }
        });
    }

    private void initialize() {
        DEFAULT_FILTER_STATUS = getResources().getString(R.string.all);
        DEFAULT_FILTER_ORDER_BY = getResources().getString(R.string.none);
        filterStatus = DEFAULT_FILTER_STATUS;
        filterOrderBy = DEFAULT_FILTER_ORDER_BY;

        String statusMsg = getResources().getString(R.string.status) + ": " + filterStatus;
        String orderByMsg = getResources().getString(R.string.order_by) + ": " + filterOrderBy;
        tvStatus.setText(statusMsg);
        tvOrderBy.setText(orderByMsg);
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

    private void showFilterDialog(Context context) {
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.filter_dialog);
        dialog.setCanceledOnTouchOutside(false);

        // Mapping views
        ConstraintLayout constraintLayout = dialog.findViewById(R.id.constraintFilter);
        TextView tvResetDefault = dialog.findViewById(R.id.tvResetDefaultFilter);
        TextView tvCancel = dialog.findViewById(R.id.tvCancelFilter);
        TextView tvSubmit = dialog.findViewById(R.id.tvSubmitFilter);
        Spinner spinnerStatus = dialog.findViewById(R.id.spinnerStatus);
        Spinner spinnerOrderBy = dialog.findViewById(R.id.spinnerOrderBy);


        // Get width of device
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;

        // Q:
        // Is Dialog always smaller than Activity that contain it?
        // Because I setMinWidth(10000), but maxWidth of Dialog don't change (Always smaller than Activity container)

        // If u don't setMinWidth => Dialog will be scaled down. I don't know why...
        constraintLayout.setMinWidth(width);
        dialog.show();


        ArrayAdapter<CharSequence> spinnerAdapterStatus = ArrayAdapter.createFromResource(context, R.array.filter_status, android.R.layout.simple_spinner_item);
        spinnerAdapterStatus.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerStatus.setAdapter(spinnerAdapterStatus);

        ArrayAdapter<CharSequence> spinnerAdapterOrderBy = ArrayAdapter.createFromResource(context, R.array.filter_order_by, android.R.layout.simple_spinner_item);
        spinnerAdapterOrderBy.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerOrderBy.setAdapter(spinnerAdapterOrderBy);


        String[] tempFilterStatusArray = getResources().getStringArray(R.array.filter_status);
        ArrayList<String> arrayListFilterStatus = new ArrayList<String>();
        Collections.addAll(arrayListFilterStatus, tempFilterStatusArray);

        String[] tempFilterOrderByArray = getResources().getStringArray(R.array.filter_order_by);
        ArrayList<String> arrayListFilterOrderBy = new ArrayList<String>();
        Collections.addAll(arrayListFilterOrderBy, tempFilterOrderByArray);


        // Initial value
        spinnerStatus.setSelection(arrayListFilterStatus.indexOf(filterStatus));
        spinnerOrderBy.setSelection(arrayListFilterOrderBy.indexOf(filterOrderBy));

        tvResetDefault.setOnClickListener(view -> {
            spinnerStatus.setSelection(arrayListFilterStatus.indexOf(DEFAULT_FILTER_STATUS));
            spinnerOrderBy.setSelection(arrayListFilterOrderBy.indexOf(DEFAULT_FILTER_ORDER_BY));
        });

        tvCancel.setOnClickListener(view -> dialog.dismiss());

        tvSubmit.setOnClickListener(view -> {
            filterStatus = arrayListFilterStatus.get(spinnerStatus.getSelectedItemPosition());
            filterOrderBy = arrayListFilterOrderBy.get(spinnerOrderBy.getSelectedItemPosition());
            String statusMsg = getResources().getString(R.string.status) + ": " + filterStatus;
            String orderByMsg = getResources().getString(R.string.order_by) + ": " + filterOrderBy;
            tvStatus.setText(statusMsg);
            tvOrderBy.setText(orderByMsg);
            dialog.dismiss();
        });
    }
}