package com.example.myapplication;

import static com.example.myapplication.Utils.Database.NAME;
import static com.example.myapplication.Utils.Database.VERSION;
import static com.example.myapplication.Utils.Helpers.dateFormat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.Adapter.TaskAdapter;
import com.example.myapplication.Model.Task;
import com.example.myapplication.Utils.Database;
import com.example.myapplication.Utils.Helpers;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static int DEVICE_WIDTH;
    //    private final String DEFAULT_FILTER_STATUS = getResources().getString(R.string.all); => ERROR : Cannot access to null object
    //    Cannot do this => Vậy nên chuyển thành biến thường và đưa vào Initialize()
    //    để getStringResource gán vào
    private String DEFAULT_FILTER_STATUS;
    private String DEFAULT_FILTER_ORDER_BY;
    private String DEFAULT_FILTER_DEADLINE;

    private Database db;

    private ListView lvTasks;
    private AutoCompleteTextView actvSearch;
    private ImageButton ibtnSearch, ibtnAdd, ibtnFilter;
    private TextView tvShowStatus, tvShowOrderBy, tvShowDeadline;
    private List<Task> taskList;
    private TaskAdapter taskAdapter;

    private String filterStatus;
    private String filterOrderBy;
    private String filterDeadline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        viewMapping();
        initialize();
        taskList = new ArrayList<>();
        taskList.addAll(db.getAllTasks());
        taskAdapter = new TaskAdapter(this, R.layout.task_item, taskList, taskAdapter);
        taskAdapter.setTaskAdapter(taskAdapter);
        lvTasks.setAdapter(taskAdapter);

        ibtnFilter.setOnClickListener(view -> showFilterDialog(MainActivity.this));
        ibtnAdd.setOnClickListener(view -> showAddDialog(MainActivity.this));

        Date date = Helpers.getStartOfThisWeek();
        Log.d("AAA", "onCreate: " + date.toString());
        date = Helpers.getEndOfTheComingSunday();
        Log.d("AAA", "onCreate: " + date.toString());
        date = Helpers.getStartOfThisMonth();
        Log.d("AAA", "onCreate: " + date.toString());
        date = Helpers.getEndOfThisMonth();
        Log.d("AAA", "onCreate: " + date.toString());
    }

    private void initialize() {
        // Get width of device
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        DEVICE_WIDTH = displayMetrics.widthPixels;


        db = new Database(MainActivity.this, NAME, null, VERSION);
        db.openDatabase();
        DEFAULT_FILTER_STATUS = getResources().getString(R.string.all);
        DEFAULT_FILTER_ORDER_BY = getResources().getString(R.string.none);
        DEFAULT_FILTER_DEADLINE = getResources().getString(R.string.all);
        filterStatus = DEFAULT_FILTER_STATUS;
        filterOrderBy = DEFAULT_FILTER_ORDER_BY;
        filterDeadline = DEFAULT_FILTER_DEADLINE;
        String statusMsg = getResources().getString(R.string.status) + ": " + filterStatus;
        String orderByMsg = getResources().getString(R.string.order_by) + ": " + filterOrderBy;
        String deadlineMsg = getResources().getString(R.string.deadline) + ": " + filterDeadline;
        tvShowStatus.setText(statusMsg);
        tvShowOrderBy.setText(orderByMsg);
        tvShowDeadline.setText(deadlineMsg);
    }

    private void viewMapping() {
        lvTasks = findViewById(R.id.lv_tasks);
        actvSearch = findViewById(R.id.actvSearch);
        ibtnSearch = findViewById(R.id.ibtnSearch);
        ibtnAdd = findViewById(R.id.ibtnAdd);
        ibtnFilter = findViewById(R.id.ibtnFilter);
        tvShowStatus = findViewById(R.id.tvShowStatus);
        tvShowOrderBy = findViewById(R.id.tvShowOrderBy);
        tvShowDeadline = findViewById(R.id.tvShowDeadline);
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
        Spinner spinnerDeadline = dialog.findViewById(R.id.spinnerDeadline);

        constraintLayout.setMinWidth(DEVICE_WIDTH);

        ArrayAdapter<CharSequence> spinnerAdapterStatus = ArrayAdapter.createFromResource(context, R.array.filter_status, android.R.layout.simple_spinner_item);
        spinnerAdapterStatus.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerStatus.setAdapter(spinnerAdapterStatus);

        ArrayAdapter<CharSequence> spinnerAdapterOrderBy = ArrayAdapter.createFromResource(context, R.array.filter_order_by, android.R.layout.simple_spinner_item);
        spinnerAdapterOrderBy.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerOrderBy.setAdapter(spinnerAdapterOrderBy);

        ArrayAdapter<CharSequence> spinnerAdapterDeadline = ArrayAdapter.createFromResource(context, R.array.filter_deadline, android.R.layout.simple_spinner_item);
        spinnerAdapterDeadline.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDeadline.setAdapter(spinnerAdapterDeadline);


        // To get initial value
        String[] tempFilterStatusArray = getResources().getStringArray(R.array.filter_status);
        ArrayList<String> arrayListFilterStatus = new ArrayList<>();
        Collections.addAll(arrayListFilterStatus, tempFilterStatusArray);

        String[] tempFilterOrderByArray = getResources().getStringArray(R.array.filter_order_by);
        ArrayList<String> arrayListFilterOrderBy = new ArrayList<>();
        Collections.addAll(arrayListFilterOrderBy, tempFilterOrderByArray);

        String[] tempFilterDeadline = getResources().getStringArray(R.array.filter_deadline);
        ArrayList<String> arrayListFilterDeadline = new ArrayList<>();
        Collections.addAll(arrayListFilterDeadline, tempFilterDeadline);


        // Initial value
        spinnerStatus.setSelection(arrayListFilterStatus.indexOf(filterStatus));
        spinnerOrderBy.setSelection(arrayListFilterOrderBy.indexOf(filterOrderBy));
        spinnerDeadline.setSelection(arrayListFilterDeadline.indexOf(filterDeadline));

        tvResetDefault.setOnClickListener(view -> {
            spinnerStatus.setSelection(arrayListFilterStatus.indexOf(DEFAULT_FILTER_STATUS));
            spinnerOrderBy.setSelection(arrayListFilterOrderBy.indexOf(DEFAULT_FILTER_ORDER_BY));
            spinnerDeadline.setSelection(arrayListFilterDeadline.indexOf(DEFAULT_FILTER_DEADLINE));
        });

        tvCancel.setOnClickListener(view -> dialog.dismiss());

        tvSubmit.setOnClickListener(view -> {
            filterStatus = arrayListFilterStatus.get(spinnerStatus.getSelectedItemPosition());
            filterOrderBy = arrayListFilterOrderBy.get(spinnerOrderBy.getSelectedItemPosition());
            filterDeadline = arrayListFilterDeadline.get(spinnerDeadline.getSelectedItemPosition());
            String statusMsg = getResources().getString(R.string.status) + ": " + filterStatus;
            String orderByMsg = getResources().getString(R.string.order_by) + ": " + filterOrderBy;
            String deadlineMsg = getResources().getString(R.string.deadline) + ": " + filterDeadline;
            tvShowStatus.setText(statusMsg);
            tvShowOrderBy.setText(orderByMsg);
            tvShowDeadline.setText(deadlineMsg);
            updateTaskListView();
            dialog.dismiss();
        });

        dialog.show();
    }

    private void showAddDialog(Context context) {
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.add_dialog);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        // Mapping views
        Button btnCancel = dialog.findViewById(R.id.btnCancelAddTask);
        Button btnSubmit = dialog.findViewById(R.id.btnSubmitAddTask);
        ConstraintLayout constraintLayout = dialog.findViewById(R.id.constraintAdd);
        EditText edtAddTask = dialog.findViewById(R.id.edtAddTask);
        TextView tvShowDeadline = dialog.findViewById(R.id.ivShowDeadline);
        Button btnPickDeadline = dialog.findViewById(R.id.btnPickDeadline);

        // Set min width of dialog
        constraintLayout.setMinWidth(DEVICE_WIDTH);

        // Add DateTimePickerDialog
        addDateTimePickerDialog(tvShowDeadline, btnPickDeadline, context);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean valid = true;
                String content = edtAddTask.getText().toString().trim();
                if (content.equals("")) {
                    valid = false;
                    Toast.makeText(context, getResources().getString(R.string.msg_task_empty), Toast.LENGTH_SHORT).show();
                }

                String deadline = tvShowDeadline.getText().toString();
                if (deadline.equals("")) {
                    valid = false;
                    Toast.makeText(context, getResources().getString(R.string.msg_deadline_empty), Toast.LENGTH_SHORT).show();
                } else if (!Helpers.isDateAfterNow(deadline)) {
                    valid = false;
                    Toast.makeText(context, getResources().getString(R.string.msg_deadline_not_valid), Toast.LENGTH_SHORT).show();
                }
                if (valid) {
                    db.insertTask(content, deadline);
                    Toast.makeText(context, getResources().getString(R.string.msg_task_added), Toast.LENGTH_SHORT).show();
                    updateTaskListView();
                    dialog.dismiss();
                }
            }
        });
    }

    private void addDateTimePickerDialog(TextView showDeadline, Button btnPickDeadline, Context context) {
        btnPickDeadline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DATE);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);
                DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        calendar.set(i, i1, i2);
                        showDeadline.setText(dateFormat.format(calendar.getTime()));
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });
    }

    private void updateTaskListView() {
        taskList.clear();
        taskList.addAll(db.getFilteredTasks(filterStatus, filterDeadline, filterOrderBy));
        taskAdapter.notifyDataSetChanged();
    }
}