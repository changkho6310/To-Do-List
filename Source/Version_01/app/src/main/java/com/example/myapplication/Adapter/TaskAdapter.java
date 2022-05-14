package com.example.myapplication.Adapter;

import android.content.Context;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.Model.Task;
import com.example.myapplication.R;

import java.util.List;


public class TaskAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private List<Task> taskList;

    public TaskAdapter(Context context, int layout, List<Task> taskList) {
        this.context = context;
        this.layout = layout;
        this.taskList = taskList;
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

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = layoutInflater.inflate(layout, null);

        // Mapping view
        CheckBox chkTask = (CheckBox) view.findViewById(R.id.chkTaskDone);
        TextView tvDeadline = (TextView) view.findViewById(R.id.tvDeadline);
        ImageButton ibtnEdit = (ImageButton) view.findViewById(R.id.ibtnEdit);
        ImageButton ibtnDelete = (ImageButton) view.findViewById(R.id.ibtnDelete);


        Task task = taskList.get(i);
        chkTask.setText(task.getContent());
        tvDeadline.setText(task.getDeadline().toString());

        ibtnEdit.setTag(i);
        ibtnDelete.setTag(i);

        ibtnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "Edit", Toast.LENGTH_SHORT).show();
            }
        });

        ibtnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                int pos = (Integer) view.getTag();
                View parentRow = (View) view.getParent();
                ListView listView = (ListView) parentRow.getParent();
                final int pos = listView.getPositionForView(parentRow);
                Toast.makeText(context, taskList.get(pos).getContent(), Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}
