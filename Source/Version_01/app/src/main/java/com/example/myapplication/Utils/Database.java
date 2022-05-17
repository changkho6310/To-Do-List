package com.example.myapplication.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.myapplication.Model.Task;
import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.List;

public class Database extends SQLiteOpenHelper {
    public static String FILTER_STATUS_ALL;
    public static String FILTER_STATUS_DOING;
    public static String FILTER_STATUS_DONE;
    public static String FILTER_ORDER_BY_NONE;
    public static String FILTER_ORDER_BY_DEADLINE;
    public static String FILTER_DEADLINE_ALL;
    public static String FILTER_DEADLINE_TODAY;
    public static String FILTER_DEADLINE_THIS_WEEK;
    public static String FILTER_DEADLINE_THIS_MONTH;
    public static String DEFAULT_SEARCH_KEYS;

    public static final String DEADLINE_FORMAT = "dd/MM/yyyy";
    public static final String TAG = Database.class.getName();
    public static final int VERSION = 1;
    public static final String NAME = "ToDoListDatabase";
    public static final String TODO_TABLE = "ToDo";
    public static final String ID = "id";
    public static final String CONTENT = "content";
    public static final String DONE = "done";
    public static final String DEADLINE = "deadline";
    public static final String CREATE_TODO_TABLE = "CREATE TABLE IF NOT EXISTS " + TODO_TABLE + "("
            + ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + CONTENT + " TEXT, "
            + DONE + " INTEGER, "
            + DEADLINE + " TEXT)";

    private SQLiteDatabase db;

    public Database(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);

        FILTER_STATUS_ALL = context.getResources().getString(R.string.filter_status_all);
        FILTER_STATUS_DOING = context.getResources().getString(R.string.filter_status_doing);
        FILTER_STATUS_DONE = context.getResources().getString(R.string.filter_status_done);
        FILTER_ORDER_BY_NONE = context.getResources().getString(R.string.filter_order_by_none);
        FILTER_ORDER_BY_DEADLINE = context.getResources().getString(R.string.filter_order_by_deadline);
        FILTER_DEADLINE_ALL = context.getResources().getString(R.string.filter_deadline_all);
        FILTER_DEADLINE_TODAY = context.getResources().getString(R.string.filter_deadline_Today);
        FILTER_DEADLINE_THIS_WEEK = context.getResources().getString(R.string.filter_deadline_this_week);
        FILTER_DEADLINE_THIS_MONTH = context.getResources().getString(R.string.filter_deadline_this_month);
        DEFAULT_SEARCH_KEYS = "";
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TODO_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop the older table
        this.db.execSQL("DROP TABLE IF EXISTS " + TODO_TABLE);

        // Create newest table
        onCreate(this.db);
    }

    public void openDatabase() {
        db = this.getWritableDatabase();
    }

    public List<Task> getAllTasks() {
        List<Task> taskList = new ArrayList<>();
        db.beginTransaction();
        try {
            Cursor cursor = db.query(TODO_TABLE, null, null, null, null, null, null, null);
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    int idIdx = cursor.getColumnIndex(ID);
                    int contentIdx = cursor.getColumnIndex(CONTENT);
                    int deadlineIdx = cursor.getColumnIndex(DEADLINE);
                    int doneIdx = cursor.getColumnIndex(DONE);
                    if (contentIdx >= 0 && deadlineIdx >= 0 && doneIdx >= 0) {
                        do {
                            Task task = new Task(
                                    cursor.getInt(idIdx),
                                    cursor.getString(contentIdx),
                                    cursor.getString(deadlineIdx),
                                    cursor.getInt(doneIdx)
                            );
                            Log.d("AAA", task.getFullTask());
                            taskList.add(task);
                        } while (cursor.moveToNext());
                    }
                }
                db.setTransactionSuccessful();
                cursor.close();
            }
        } catch (SQLException exception) {
            Log.e(TAG, "getAllTasks: " + exception);
        } finally {
            db.endTransaction();
        }
        return taskList;
    }

    public void insertTask(Task task) {
        ContentValues cv = new ContentValues();
        cv.put(CONTENT, task.getContent());
        cv.put(DONE, task.isDone());
        cv.put(DEADLINE, task.getStrDeadline());
        db.insert(TODO_TABLE, null, cv);
    }

    public void insertTask(String taskContent, String taskDeadline) {
        ContentValues cv = new ContentValues();
        cv.put(CONTENT, taskContent);
        cv.put(DONE, false);
        cv.put(DEADLINE, taskDeadline);
        db.insert(TODO_TABLE, null, cv);
    }

    public void deleteTask(Task task) {
        this.deleteTask(task.getId());
    }

    public void deleteTask(int id) {
        String whereClause = ID + "=?";
        db.delete(TODO_TABLE, whereClause, new String[]{String.valueOf(id)});
    }

    public void updateTask(Task task) {
        ContentValues cv = new ContentValues();
        cv.put(CONTENT, task.getContent());
        cv.put(DEADLINE, task.getStrDeadline());
        cv.put(DONE, task.isDone());
        db.beginTransaction();
        try {
            String whereClause = ID + "=?";
            db.update(TODO_TABLE, cv, whereClause, new String[]{String.valueOf(task.getId())});
            db.setTransactionSuccessful();
        } catch (SQLException exception) {
            Log.e(TAG, "updateTask: " + exception);
        } finally {
            db.endTransaction();
        }
    }

    public List<Task> getFilteredTasks(String searchKeys, String status, String deadline, String orderBy) {
        List<Task> taskList = new ArrayList<>();
        db.beginTransaction();
        try {
            Cursor cursor = null;
            if (status.equals(FILTER_STATUS_ALL)) {
                cursor = db.query(TODO_TABLE, null, null, null, null, null, null, null);
            } else if (status.equals(FILTER_STATUS_DOING)) {
                String[] conditionArgs = {"0"};
                cursor = db.query(TODO_TABLE, null, DONE + " = ?", conditionArgs, null, null, null, null);
            } else if (status.equals(FILTER_STATUS_DONE)) {
                String[] conditionArgs = {"1"};
                cursor = db.query(TODO_TABLE, null, DONE + " = ?", conditionArgs, null, null, null, null);
            }

            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    int idIdx = cursor.getColumnIndex(ID);
                    int contentIdx = cursor.getColumnIndex(CONTENT);
                    int deadlineIdx = cursor.getColumnIndex(DEADLINE);
                    int doneIdx = cursor.getColumnIndex(DONE);
                    if (contentIdx >= 0 && deadlineIdx >= 0 && doneIdx >= 0) {
                        do {
                            Task task = new Task(
                                    cursor.getInt(idIdx),
                                    cursor.getString(contentIdx),
                                    cursor.getString(deadlineIdx),
                                    cursor.getInt(doneIdx)
                            );
                            Log.d("AAA", task.getFullTask());
                            taskList.add(task);
                        } while (cursor.moveToNext());
                    }
                }
                db.setTransactionSuccessful();
                cursor.close();
            }
        } catch (
                SQLException exception) {
            Log.e(TAG, "getAllTasks: " + exception);
        } finally {
            db.endTransaction();
        }
        getTasksByDeadline(taskList, deadline);
        orderBy(taskList, orderBy);
        getTaskBySearchKeys(taskList, searchKeys);
        return taskList;
    }

    public void getTaskBySearchKeys(List<Task> taskList, String searchKeys) {
        if (!searchKeys.equals(DEFAULT_SEARCH_KEYS)) {
            taskList.removeIf(task -> !task.getContent().contains(searchKeys));
        }
    }

    public void getTasksByDeadline(List<Task> taskList, String deadline) {
        if (deadline.equals(FILTER_DEADLINE_TODAY)) {
            long startOfToday = Helpers.getStartOfToday().getTime();
            long endOfToday = Helpers.getEndOfToday().getTime();
            taskList.removeIf(task -> !(task.getDeadline().getTime() >= startOfToday && task.getDeadline().getTime() <= endOfToday));
        } else if (deadline.equals(FILTER_DEADLINE_THIS_WEEK)) {
            long startOfThisWeek = Helpers.getStartOfThisWeek().getTime();
            long endOfThisWeek = Helpers.getEndOfTheComingSunday().getTime();
            taskList.removeIf(task -> !(task.getDeadline().getTime() >= startOfThisWeek && task.getDeadline().getTime() <= endOfThisWeek));
        } else if (deadline.equals(FILTER_DEADLINE_THIS_MONTH)) {
            long startOfThisMonth = Helpers.getStartOfThisMonth().getTime();
            long endOfThisMonth = Helpers.getEndOfThisMonth().getTime();
            taskList.removeIf(task -> !(task.getDeadline().getTime() >= startOfThisMonth && task.getDeadline().getTime() <= endOfThisMonth));
        } else {
            // deadline.equals(FILTER_DEADLINE_ALL)
            // Do nothing
        }
    }

    private Task findMinDeadline(List<Task> taskList) {
        if (taskList != null) {
            long minDeadline = taskList.get(0).getDeadline().getTime();
            int minIdx = 0;
            for (int i = 1; i < taskList.size(); i++) {
                long compareValue = taskList.get(i).getDeadline().getTime();
                if (minDeadline > compareValue) {
                    minDeadline = compareValue;
                    minIdx = i;
                }
            }
            return taskList.get(minIdx);
        } else {
            return null;
        }
    }

    public void orderBy(List<Task> taskList, String orderBy) {
        if (taskList == null) {
            return;
        }
        if (taskList.size() < 2) {
            return;
        }
        if (orderBy.equals(FILTER_ORDER_BY_DEADLINE)) {
            List<Task> orderedTaskList = new ArrayList<>();
            while (taskList.size() != 0) {
                Task task = findMinDeadline(taskList);
                orderedTaskList.add(task);
                taskList.remove(task);
            }
            taskList.addAll(orderedTaskList);
        }
    }

    public void setDone(Task task) {
        setDone(task.getId(), task.isDone());
    }

    public void setDone(int id, boolean isDone) {
        ContentValues cv = new ContentValues();
        if (isDone) {
            cv.put(DONE, 1);
        } else {
            cv.put(DONE, 0);
        }
        db.beginTransaction();
        try {
            String whereClause = ID + "=?";
            db.update(TODO_TABLE, cv, whereClause, new String[]{String.valueOf(id)});
            db.setTransactionSuccessful();
        } catch (SQLException exception) {
            Log.e(TAG, "setDone: " + exception);
        } finally {
            db.endTransaction();
        }
    }

}
