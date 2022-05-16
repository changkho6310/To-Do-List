package com.example.myapplication.Adapter;

import static com.example.myapplication.Utils.Database.NAME;
import static com.example.myapplication.Utils.Database.VERSION;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.example.myapplication.Model.Task;
import com.example.myapplication.R;
import com.example.myapplication.Utils.Database;

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
        db = new Database(context, NAME, null, VERSION);
        db.openDatabase();

        ViewHolder holder;

        if (view == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(layout, null);
            holder = new ViewHolder();
            // Mapping view
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

        holder.ibtnEdit.setTag(i);
        holder.ibtnDelete.setTag(i);

        holder.ibtnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = (Integer) view.getTag();
            }
        });

        holder.ibtnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = (Integer) view.getTag();
                confirmDeleteTask(pos);
            }
        });

        holder.chkTask.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked) {
                    strikeThrough(holder.chkTask);
                } else {
                    unStrikeThrough(holder.chkTask);
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

    private void confirmDeleteTask(int pos) {
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
}
