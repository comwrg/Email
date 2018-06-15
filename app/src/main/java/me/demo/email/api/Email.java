package me.demo.email.api;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.widget.SimpleAdapter;
import me.demo.email.db.EmailSQLHelper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class Email implements EmailApi {
    private final String TAG = "Email";
    private EmailSQLHelper sqlHelper;
    private int id;
    private String sid;
    private String usr;

    public String getUsr() {
        return usr;
    }

    public Email(Context context) {
        sqlHelper = new EmailSQLHelper(context, "data.db", null, 1);
    }

    public Email(Context context, String usr) {
        this(context);
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = sqlHelper.getReadableDatabase();
            cursor = db.rawQuery("select * from user where usr=?", new String[]{usr});
            cursor.moveToNext();
            this.id = cursor.getInt(cursor.getColumnIndex("id"));;
            this.sid = String.valueOf(id);
            this.usr = usr;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
            if (db != null)
                db.close();
        }
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        sqlHelper.close();
    }

    public boolean login(String usr, String pwd) {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = sqlHelper.getReadableDatabase();
            cursor = db.rawQuery("select * from user where usr=? and pwd=?", new String[]{usr, pwd});
            return  cursor.moveToNext();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
            if (db != null)
                db.close();
        }
        return false;
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

    @Override
    public List<AEmail> read(Box box) {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        List<AEmail> emails = new ArrayList<>();
        try {
            db = sqlHelper.getReadableDatabase();
            if (Box.IN.equals(box)) {
                cursor = db.rawQuery("select * from email where receiver_id=? and send_status=? and delete_status=0",
                        new String[]{sid, String.valueOf(AEmail.SendStatus.HAS_SENT)});
            } else if (Box.OUT.equals(box)) {
                cursor = db.rawQuery("select * from email where sender_id=? and send_status=? and delete_status=0",
                        new String[]{sid, String.valueOf(AEmail.SendStatus.HAS_SENT)});
            } else if (Box.DRAFT.equals(box)) {
                cursor = db.rawQuery("select * from email where sender_id=? and send_status=? and delete_status=0",
                        new String[]{sid, String.valueOf(AEmail.SendStatus.NOT_SEND)});
            } else if (Box.DELETE.equals(box)) {
                cursor = db.rawQuery("select * from email where sender_id=? and delete_status=?",
                        new String[]{sid, String.valueOf(AEmail.DeleteStatus.SOFT_DELETE)});
            }
            while (cursor.moveToNext()) {
                emails.add(cursor2AEmail(cursor));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
            if (db != null)
                db.close();
        }
        return emails;
    }

    protected AEmail cursor2AEmail(Cursor cursor) {
        return new AEmail(
                cursor.getInt(0),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getString(3),
                cursor.getInt(4),
                cursor.getInt(5),
                cursor.getInt(6),
                cursor.getInt(7),
                cursor.getInt(8)
        );
    }

    public AEmail read(int id) {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = sqlHelper.getReadableDatabase();
            cursor = db.rawQuery("select * from email where id=?", new String[]{String.valueOf(id)});
            if (!cursor.moveToNext())
                return null;
            return cursor2AEmail(cursor);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
            if (db != null)
                db.close();
        }
        return null;
    }

    @Override
    public boolean addDrafts(String title, String content, String receiver) {
        int receiverId = getId(receiver);
        return email_insert(title, content, receiverId, AEmail.SendStatus.NOT_SEND, AEmail.ReadStatus.NOT_READ);
    }

    public boolean updateDrafts(int id, String title, String content, String receiver) {
        SQLiteDatabase db = null;
        int receiverId = getId(receiver);
        try {
            db = sqlHelper.getWritableDatabase();
            db.execSQL("update email set title=?, content=?, receiver_id=? where id=?",
                    new Object[]{title, content, receiverId, id});
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db != null)
                db.close();
        }
        return false;
    }

    @Override
    public void markRead(int id) {
        SQLiteDatabase db = null;
        try {
            db = sqlHelper.getWritableDatabase();
            db.execSQL("update email set read_status=? where id=?", new Object[]{AEmail.ReadStatus.HAS_READ, id});
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db != null)
                db.close();
        }
    }

    @Override
    public boolean sendEmail(String receiver, String title, String content) {
        int receiverId = getId(receiver);
        if (receiverId < 0)
            return false;
        return email_insert(title, content, receiverId, AEmail.SendStatus.HAS_SENT, AEmail.ReadStatus.NOT_READ);
    }

    public boolean sendEmail(int id, String receiver, String title, String content) {
        int receiverId = getId(receiver);
        if (receiverId < 0)
            return false;
        SQLiteDatabase db = null;
        try {
            db = sqlHelper.getWritableDatabase();
            db.execSQL("update email set title=?, content=?, date=?, receiver_id=?, send_status=? where id=?",
                    new Object[]{title, content, Now(), receiverId, AEmail.SendStatus.HAS_SENT, id});
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db != null)
                db.close();
        }
        return false;
    }

    protected String Now() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = dateFormat.format(Calendar.getInstance().getTime());
        return date;
    }

    protected boolean email_insert(String title, String content, int receiverId, int sendStatus, int readStatus) {
        SQLiteDatabase db = null;
        try {
            db = sqlHelper.getWritableDatabase();
            db.execSQL("insert into email (title, content, date, sender_id, receiver_id, send_status, read_status) values(?, ?, ?, ?, ?, ?, ?)",
                    new Object[]{title, content, Now(), this.id, receiverId, sendStatus, readStatus});
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db != null)
                db.close();
        }
        return false;
    }

    public void delete(int id, int status) {
        SQLiteDatabase db = null;
        try {
            db = sqlHelper.getWritableDatabase();
            db.execSQL("update email set delete_status=? where id=?", new Object[]{status, id});
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db != null)
                db.close();
        }
    }

    public int getId() {
        return id;
    }

    protected int getId(String usr) {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = sqlHelper.getReadableDatabase();
            cursor = db.rawQuery("select id from user where usr=?", new String[]{usr});
            if (!cursor.moveToNext())
                return -1;
            return cursor.getInt(0);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
            if (db != null)
                db.close();
        }
        return -1;
    }


}
