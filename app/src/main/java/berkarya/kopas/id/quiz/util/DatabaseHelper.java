package berkarya.kopas.id.quiz.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ravi on 15/03/18.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String TABLE_NAME = "soal_jawab";

    public static final String COLUMN_SOAL_JAWAB_ID = "soal_jawab_id";
    public static final String COLUMN_JAWABAN = "jawaban";
    public static final String COLUMN_RAGU = "ragu";
    public static final String COLUMN_SOAL_ID= "soal_id";
    public static final String COLUMN_USERS_UJI_KODE = "users_uji_kode";

    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "quiz";
    // Create table SQL query
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_SOAL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_JAWABAN + " TEXT,"
                    + COLUMN_RAGU + " TEXT,"
                    + COLUMN_USERS_UJI_KODE + " TEXT"
                    + ")";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        // create jadwals table
        db.execSQL(CREATE_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        // Create tables again
        onCreate(db);
    }

    /*
    public long insertMengerjakanSoal(SQLiteDatabase db, String soal_id, String jawaban, String ragu) {
        // get writable database as we want to write data

        ContentValues values = new ContentValues();
        // `id` and `timestamp` will be inserted automatically.
        // no need to add them
        values.put(COLUMN_SOAL_ID, soal_id);
        values.put(COLUMN_JAWABAN, jawaban);
        values.put(COLUMN_RAGU, ragu);
        // insert row
        long id = db.insert(TABLE_NAME, null, values);

        // close db connection
        db.close();

        // return newly inserted row id
        return id;
    }

    public MengerjakanSoal getMengerjakanSoal(SQLiteDatabase db, String id) {
        // get readable database as we are not inserting anything
        MengerjakanSoal ms = null;
        Cursor cursor = db.query(TABLE_NAME,
                new String[]{ COLUMN_SOAL_ID,COLUMN_JAWABAN,COLUMN_RAGU, COLUMN_USERS_UJI_KODE},
                COLUMN_SOAL_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();

            // prepare jadwal object
            ms = new MengerjakanSoal(
                    cursor.getString(cursor.getColumnIndex(COLUMN_SOAL_ID)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_JAWABAN)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_RAGU))
            );

            // close the db connection
            cursor.close();
        }

        return ms;
    }

    public ArrayList<MengerjakanSoal> getAllMengerjakanSoal() {
        ArrayList<MengerjakanSoal> mj = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_NAME + " ORDER BY " +
                COLUMN_SOAL_ID + " ASC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        mj.clear();
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {

                mj.add(
                        new MengerjakanSoal(
                                cursor.getString(cursor.getColumnIndex(COLUMN_SOAL_ID)),
                                cursor.getString(cursor.getColumnIndex(COLUMN_JAWABAN)),
                                cursor.getString(cursor.getColumnIndex(COLUMN_RAGU))
                        )

                );

            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        // return jadwals list
        return mj;
    }

    public int getMengerjakanSoalCount() {

        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_NAME + " ORDER BY " +
                COLUMN_SOAL_ID + " ASC";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        int count = cursor.getCount();
        cursor.close();

        // return count
        return count;
    }

    public int updateMengerjakanSoal(MengerjakanSoal mj) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_SOAL_ID, mj.get_nomor());
        values.put(COLUMN_JAWABAN, mj.get_jawab_soal());
        values.put(COLUMN_RAGU, mj.get_jawab_soal_ragu());

        // updating row
        return db.update(TABLE_NAME, values, COLUMN_SOAL_ID + " = ?",
                new String[]{String.valueOf(mj.get_nomor())});
    }

    public void deleteMengerjakanSoal(MengerjakanSoal mj) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, COLUMN_SOAL_ID + " = ?",
                new String[]{String.valueOf(mj.get_nomor())});
        db.close();
    }*/

}
