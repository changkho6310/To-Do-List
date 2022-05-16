package com.example.myapplication.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.example.myapplication.Model.Task;
import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.List;

public class Database extends SQLiteOpenHelper {
    public static final String DEADLINE_FORMAT = "dd/MM/yyyy";
    public static final String TAG = Database.class.getName().toString();
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
    }

    public Database(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version, @Nullable DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
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
        List<Task> taskList = new ArrayList<Task>();
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
            Log.e(TAG, "getAllTasks: " + exception.toString());
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
            Log.e(TAG, "updateTask: " + exception.toString());
        } finally {
            db.endTransaction();
        }
    }


}
