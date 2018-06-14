package me.demo.email.api;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import me.demo.email.activity.SplashActivity;
import me.demo.email.db.EmailSQLHelper;

public class Email implements EmailApi{
    private final String TAG = "Email";
    private static EmailSQLHelper sqlHelper = null;

    public Email(Context context) {
        sqlHelper = new EmailSQLHelper(context, "data.db", null, 1);
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        sqlHelper.close();
    }

    public boolean login(String usr, String pwd) {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        boolean r = false;
        try {
            db = sqlHelper.getReadableDatabase();
            cursor = db.rawQuery("select * from user where usr=? and pwd=?", new String[]{usr, pwd});
            r = cursor.getCount() == 1;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
            if (db != null)
                db.close();
        }
        return r;
    }

    public RegisterCode register(String usr, String pwd) {
        SQLiteDatabase db = null;
        try {
            db = sqlHelper.getReadableDatabase();
            db.execSQL("insert into user (usr, pwd) values(?, ?)", new Object[]{usr, pwd});
        } catch (SQLException e) {
            e.printStackTrace();
            return RegisterCode.UNKNOWN_ERROR;
        } finally {
            if (db != null)
                db.close();
        }
        return RegisterCode.SUCC;
    }
}
