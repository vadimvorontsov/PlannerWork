package lib.clientbase.procedures;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashSet;

import lib.clientbase.ClientBaseOpenHelper;

public class Sessions {

    private final String TAG = "Sessions";
    private Context mContext;

    public Sessions(Context context) {
        mContext = context;
    }

    public long addSession(String clientName, String procedureName, int procedurePrice,
                           String procedureNote, int procedureColor, String sessionTimeStart,
                           String sessionTimeEnd, String phone, String email, int isNotified) {

        long insertSessionID = 0;

        Clients clients = new Clients(mContext);
        long clientID = clients.addClient(clientName);
        if (clientID == -1) {
            clientID = clients.getClientID(clientName);
        }

        Phones phones = new Phones(mContext);
        long phoneID = phones.addPhone(phone, clientID);
        if (phoneID == -1) {
            phoneID = phones.getPhoneID(phone);
        }

        Emails emails = new Emails(mContext);
        long emailID = emails.addEmail(email, clientID);
        if (emailID == -1) {
            emailID = emails.getEmailID(email);
        }

        Procedures procedures = new Procedures(mContext);
        long procedureID = procedures.addProcedure(procedureName, procedurePrice, procedureNote, procedureColor);
        if (procedureID == -1) {
            procedureID = procedures.getProcedureID(procedureName);
        }

        //ClientBaseOpenHelper ClientBaseOpenHelper.getHelper(mContext) = new ClientBaseOpenHelper(mContext);
        SQLiteDatabase db_write = ClientBaseOpenHelper.getHelper(mContext).getWritableDatabase();

        try {
            ContentValues cv = new ContentValues();
            cv.put(ClientBaseOpenHelper.ID_CLIENT_SESSION, clientID);
            cv.put(ClientBaseOpenHelper.ID_PROCEDURE, procedureID);
            cv.put(ClientBaseOpenHelper.TIME_START, sessionTimeStart);
            cv.put(ClientBaseOpenHelper.TIME_END, sessionTimeEnd);
            cv.put(ClientBaseOpenHelper.ID_PHONE, phoneID);
            cv.put(ClientBaseOpenHelper.ID_EMAIL, emailID);
            cv.put(ClientBaseOpenHelper.IS_NOTIFIED, isNotified);
            if (cv != null) {
                insertSessionID = db_write.insert(ClientBaseOpenHelper.TABLE_SESSIONS,
                        ClientBaseOpenHelper.ID_CLIENT_SESSION, cv);
            }
            return insertSessionID;

        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            return insertSessionID;

        } finally {
            if (db_write != null && db_write.isOpen()) {
                db_write.close();
            }
            if (ClientBaseOpenHelper.getHelper(mContext) != null) {
                ClientBaseOpenHelper.getHelper(mContext).close();
            }
        }
    }

    public /*ArrayList<Integer>*/ boolean getSessionsBetweenTimes(String timeStart, String timeEnd) {

        if (timeStart.isEmpty()) {
            timeStart = " datetime('2015-01-01 01:01:01') ";
        }

        if (timeEnd.isEmpty()) {
            timeEnd = " datetime('now') ";
        }

        SQLiteDatabase db_read = ClientBaseOpenHelper.getHelper(mContext).getReadableDatabase();
        Cursor cursor = null;
        ArrayList<Integer> sessionsID = new ArrayList<>();

        try {
            cursor = db_read.query(ClientBaseOpenHelper.TABLE_SESSIONS, new String[]{BaseColumns._ID},
                    ClientBaseOpenHelper.TIME_START + " BETWEEN " + timeStart + " AND " + timeEnd + "",
                    null, null, null, null);

            while (cursor.moveToNext()) {
                sessionsID.add(cursor.getInt(cursor.getColumnIndex(BaseColumns._ID)));
            }

            if (sessionsID.isEmpty()) {
                cursor = db_read.query(ClientBaseOpenHelper.TABLE_SESSIONS, new String[]{BaseColumns._ID},
                        ClientBaseOpenHelper.TIME_END + " BETWEEN " + timeStart + " AND " + timeEnd + "",
                        null, null, null, null);
                while (cursor.moveToNext()) {
                    sessionsID.add(cursor.getInt(cursor.getColumnIndex(BaseColumns._ID)));
                }
            }

            if (sessionsID.isEmpty()) {
                return false;
            } else {
                HashSet<Integer> sessionsIdNotDuplicate = new HashSet<>();
                sessionsIdNotDuplicate.addAll(sessionsID);
                sessionsID.clear();
                sessionsID.addAll(sessionsIdNotDuplicate);
                return true;
            }

        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            return !sessionsID.isEmpty();

        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
            if (db_read != null && db_read.isOpen()) {
                db_read.close();
            }
            if (ClientBaseOpenHelper.getHelper(mContext) != null) {
                ClientBaseOpenHelper.getHelper(mContext).close();
            }
        }

    }

    public ArrayList<Integer> getSessionsBeforeTime(String time) {

        SQLiteDatabase db_read = ClientBaseOpenHelper.getHelper(mContext).getReadableDatabase();
        Cursor cursor = null;
        ArrayList<Integer> sessionsID = new ArrayList<>();

        try {
            cursor = db_read.query(ClientBaseOpenHelper.TABLE_SESSIONS,
                    new String[]{BaseColumns._ID},
                    ClientBaseOpenHelper.TIME_END + " = '" + time + "'",
                    null, null, null, null);
            while (cursor.moveToNext()) {
                sessionsID.add(cursor.getInt(cursor.getColumnIndex(BaseColumns._ID)));
            }

        } catch (Exception e) {
            Log.e(TAG, e.getMessage());

        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
            if (db_read != null && db_read.isOpen()) {
                db_read.close();
            }
            if (ClientBaseOpenHelper.getHelper(mContext) != null) {
                ClientBaseOpenHelper.getHelper(mContext).close();
            }
        }
        return sessionsID;
    }

    public ArrayList<Long> getSessionsAfterTime(String time, int count) {

        SQLiteDatabase db_read = ClientBaseOpenHelper.getHelper(mContext).getReadableDatabase();
        Cursor cursor = null;
        ArrayList<Long> sessionsID = new ArrayList<>();

        try {
            cursor = db_read.query(ClientBaseOpenHelper.TABLE_SESSIONS, new String[]{BaseColumns._ID},
                    ClientBaseOpenHelper.TIME_START + " > " + time,
                    null, null, null, ClientBaseOpenHelper.TIME_START);
            while (cursor.moveToNext() && count != 0) {
                sessionsID.add(cursor.getLong(cursor.getColumnIndex(BaseColumns._ID)));
                count--;
            }

            return sessionsID;

        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            return null;

        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
            if (db_read != null && db_read.isOpen()) {
                db_read.close();
            }
            if (ClientBaseOpenHelper.getHelper(mContext) != null) {
                ClientBaseOpenHelper.getHelper(mContext).close();
            }
        }
    }

    public Object[] getSessionById(long sessionID) {

        SQLiteDatabase db_read = ClientBaseOpenHelper.getHelper(mContext).getReadableDatabase();
        Cursor cursor = null;
        Object[] session = null;

        try {
            String clientName = "";
            Object[] procedureName = null;
            String time_start = "";
            String time_end = "";
            String phone = "";
            String email = "";
            String isNotified = "";

            cursor = db_read.query(ClientBaseOpenHelper.TABLE_SESSIONS,
                    new String[]{ClientBaseOpenHelper.ID_CLIENT_SESSION,
                            ClientBaseOpenHelper.ID_PROCEDURE, ClientBaseOpenHelper.TIME_START,
                            ClientBaseOpenHelper.TIME_END,
                            ClientBaseOpenHelper.ID_PHONE, ClientBaseOpenHelper.ID_EMAIL,
                            ClientBaseOpenHelper.IS_NOTIFIED},
                    BaseColumns._ID + "=" + sessionID, null, null, null, null);

            while (cursor.moveToNext()) {
                clientName = getClientName(cursor.getInt(cursor.getColumnIndex
                        (ClientBaseOpenHelper.ID_CLIENT_SESSION)));
                procedureName = getProcedure(cursor.getInt(cursor.getColumnIndex
                        (ClientBaseOpenHelper.ID_PROCEDURE)));
                time_start = cursor.getString(cursor.getColumnIndex
                        (ClientBaseOpenHelper.TIME_START));
                time_end = cursor.getString(cursor.getColumnIndex
                        (ClientBaseOpenHelper.TIME_END));
                phone = getPhone(cursor.getInt(cursor.getColumnIndex
                        (ClientBaseOpenHelper.ID_PHONE)));
                email = getEmail(cursor.getInt(cursor.getColumnIndex
                        (ClientBaseOpenHelper.ID_EMAIL)));
                isNotified = getIsNotified(cursor.getInt(cursor.getColumnIndex
                        (ClientBaseOpenHelper.IS_NOTIFIED)));
            }
            session = new Object[]{clientName, phone, email, procedureName, time_start, time_end, isNotified};

            return session;
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            return session;
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
            if (db_read != null && db_read.isOpen()) {
                db_read.close();
            }
            if (ClientBaseOpenHelper.getHelper(mContext) != null) {
                ClientBaseOpenHelper.getHelper(mContext).close();
            }
        }
    }

    public ArrayList<Long> getAllSessionsId() {

        SQLiteDatabase db_read = ClientBaseOpenHelper.getHelper(mContext).getReadableDatabase();
        Cursor cursor = null;
        ArrayList<Long> sessions = new ArrayList<>();

        try {
            cursor = db_read.query(ClientBaseOpenHelper.TABLE_SESSIONS,
                    new String[]{BaseColumns._ID}, null,
                    null, null, null, null);
            while (cursor.moveToNext()) {
                sessions.add(cursor.getLong(cursor.getColumnIndex(BaseColumns._ID)));
            }
            return sessions;

        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            return sessions;

        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
            if (db_read != null && db_read.isOpen()) {
                db_read.close();
                ClientBaseOpenHelper.getHelper(mContext).close();
            }
        }
    }

    public int isNotifiedById(long id) {

        SQLiteDatabase db_read = ClientBaseOpenHelper.getHelper(mContext).getReadableDatabase();
        Cursor cursor = null;
        Integer isNotified;

        try {
            cursor = db_read.query(ClientBaseOpenHelper.TABLE_SESSIONS
                    , new String[]{ClientBaseOpenHelper.IS_NOTIFIED},
                    ClientBaseOpenHelper._ID + "=" + id, null, null, null, null);
            while (cursor.moveToNext()) {
                isNotified = cursor.getInt(cursor.getColumnIndex(ClientBaseOpenHelper.IS_NOTIFIED));
                return isNotified ; //== 1
            }

        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            return -1;

        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
            if (db_read != null && db_read.isOpen()) {
                db_read.close();
                ClientBaseOpenHelper.getHelper(mContext).close();
            }
        }
        return -1;
    }

    public int deleteSessionById(long id){

        SQLiteDatabase db = ClientBaseOpenHelper.getHelper(mContext).getWritableDatabase();

        try {
            int index = db.delete(ClientBaseOpenHelper.TABLE_SESSIONS, "_id=" + id, null);
            return 1;

        } catch (SQLiteConstraintException e) {
            Log.e(TAG, e.getMessage());
            return 0;

        }finally {
            if (db != null && db.isOpen()) {
                db.close();
                ClientBaseOpenHelper.getHelper(mContext).close();
            }
        }
    }


    private String getClientName(int clientID) {
        return new Clients(mContext).getClientName(clientID);
    }

    private Object[] getProcedure(int procedureID) {
        return new Procedures(mContext).getProcedureInfo(procedureID);
    }

    private String getPhone(int phoneID) {
        return new Phones(mContext).getPhoneById(phoneID);
    }

    private String getEmail(int emailID) {
        return new Emails(mContext).getEmailById(emailID);
    }

    private String getIsNotified(int isNotifiedInt) {
        if (isNotifiedInt == -1 || isNotifiedInt == 0)
            return "Не оповещен";
        else
            return "Оповещен";
    }

}
