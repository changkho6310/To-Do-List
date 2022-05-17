package com.example.myapplication.Adapter;

import static com.example.myapplication.MainActivity.DEVICE_WIDTH;
import static com.example.myapplication.Utils.Database.NAME;
import static com.example.myapplication.Utils.Database.VERSION;
import static com.example.myapplication.Utils.Helpers.dateFormat;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.myapplication.Model.Task;
import com.example.myapplication.R;
import com.example.myapplication.Utils.Database;
import com.example.myapplication.Utils.Helpers;

import java.util.Calendar;
import java.util.List;


public class TaskAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private List<Task> taskList;
    private Database db;
    private TaskAdapter taskAdapter;

    public TaskAdapter(Context context, int layout, List<Task> taskList, TaskAdapter taskAdapter) {
        this.context = context;
        this.layout = layout;
        this.taskList = taskList;

        db = new Database(context, NAME, null, VERSION);
        db.openDatabase();
    }

    public void setTaskAdapter(TaskAdapter taskAdapter) {
        this.taskAdapter = taskAdapter;
    }

    @Override
    public int getCount() {
        return taskList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    private class ViewHolder {
        CheckBox chkTask;
        TextView tvDeadline;
        ImageButton ibtnEdit, ibtnDelete;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;

        if (view == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(layout, null);
            holder = new ViewHolder();

            holder.chkTask = view.findViewById(R.id.chkTaskDone);
            holder.tvDeadline = view.findViewById(R.id.tvDeadline);
            holder.ibtnEdit = view.findViewById(R.id.ibtnEdit);
            holder.ibtnDelete = view.findViewById(R.id.ibtnDelete);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        Task task = taskList.get(i);
        holder.chkTask.setText(task.getContent());
        holder.tvDeadline.setText(task.getStrDeadline());

        holder.chkTask.setTag(i);
        holder.ibtnEdit.setTag(i);
        holder.ibtnDelete.setTag(i);

        if (task.isDone()) {
            holder.chkTask.setChecked(true);
            strikeThrough(holder.chkTask);
        } else {
            holder.chkTask.setChecked(false);
            unStrikeThrough(holder.chkTask);
        }

        holder.ibtnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = (Integer) view.getTag();
                showEditTaskDialog(pos);
            }
        });

        holder.ibtnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = (Integer) view.getTag();
                showDeleteTaskDialog(pos);
            }
        });

        holder.chkTask.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                int pos = (int) holder.chkTask.getTag();
                if (checked) {
                    strikeThrough(holder.chkTask);
                    db.setDone(taskList.get(i).getId(), true);
                } else {
                    unStrikeThrough(holder.chkTask);
                    db.setDone(taskList.get(i).getId(), false);
                }
            }
        });

        return view;
    }

    private void strikeThrough(CheckBox chk) {
        chk.setPaintFlags(chk.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
    }

    private void unStrikeThrough(CheckBox chk) {
        chk.setPaintFlags(chk.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
    }

    private void showDeleteTaskDialog(int pos) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setTitle(context.getResources().getString(R.string.warning));
        alertDialog.setMessage(context.getResources().getString(R.string.msg_confirm_delete_task));
        alertDialog.setPositiveButton(context.getResources().getString(R.string.submit), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Task task = taskList.get(pos);
                db.deleteTask(task);
                String msg = context.getResources().getString(R.string.task) + " : "
                        + task.getContent() + " "
                        + context.getResources().getString(R.string.msg_task_deleted);
                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();

                taskList.remove(pos);
                taskAdapter.notifyDataSetChanged();
            }
        });
        alertDialog.setNegativeButton(context.getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        alertDialog.show();
    }

    private void showEditTaskDialog(int pos) {
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.add_dialog);
        dialog.setCanceledOnTouchOutside(false);

        // Mapping views
        Button btnCancel = dialog.findViewById(R.id.btnCancelAddTask);
        Button btnSubmit = dialog.findViewById(R.id.btnSubmitAddTask);
        ConstraintLayout constraintLayout = dialog.findViewById(R.id.constraintAdd);
        EditText edtAddTask = dialog.findViewById(R.id.edtAddTask);
        TextView tvShowDeadline = dialog.findViewById(R.id.ivShowDeadline);
        Button btnPickDeadline = dialog.findViewById(R.id.btnPickDeadline);


        Task editingTask = taskList.get(pos);

        // Assign initial data
        edtAddTask.setText(editingTask.getContent());
        tvShowDeadline.setText(editingTask.getStrDeadline());

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
                    Toast.makeText(context, context.getResources().getString(R.string.msg_task_empty), Toast.LENGTH_SHORT).show();
                }

                String deadline = tvShowDeadline.getText().toString();
                if (deadline.equals("")) {
                    valid = false;
                    Toast.makeText(context, context.getResources().getString(R.string.msg_deadline_empty), Toast.LENGTH_SHORT).show();
                } else if (!Helpers.isDateAfterNow(deadline)) {
                    valid = false;
                    Toast.makeText(context, context.getResources().getString(R.string.msg_deadline_not_valid), Toast.LENGTH_SHORT).show();
                }
                if (valid) {
                    if (content.equals(editingTask.getContent()) && deadline.equals(editingTask.getStrDeadline())) {
                        // No update

                    } else {
                        editingTask.setContent(content);
                        editingTask.setDeadline(deadline);
                        db.updateTask(editingTask);

                        String msg = context.getResources().getString(R.string.task) + ": " + content + " " + context.getResources().getString(R.string.msg_task_edited);
                        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();

                        // Update ListView
                        taskAdapter.notifyDataSetChanged();
                    }
                    dialog.dismiss();
                }
            }
        });

        dialog.show();
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
}
