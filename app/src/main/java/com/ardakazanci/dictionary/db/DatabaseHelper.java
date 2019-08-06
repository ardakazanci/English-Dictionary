package com.ardakazanci.dictionary.db;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DatabaseHelper extends SQLiteOpenHelper {

    private String DB_PATH = null;

    private static String DB_NAME = "eng_dictionary.db";

    private SQLiteDatabase myDatabase;

    private final Context context;


    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, 1);
        this.context = context;
        this.DB_PATH = "/data/data/" + context.getPackageName() + "/" + "databases/";
        Log.i("DB_PATH", DB_PATH);
    }


    public void createDatabase() throws IOException {

        boolean dbExist = checkDatabase();

        if (!dbExist) {

            this.getReadableDatabase();
            try {
                copyDatabase();
            } catch (IOException e) {
                throw new Error("Error Copying Database");
            }

        }

    }


    public boolean checkDatabase() {

        SQLiteDatabase checkDB = null;

        try {

            String myPath = DB_PATH + DB_NAME;
            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);


        } catch (SQLiteException e) {


        }

        if (checkDB != null) {
            checkDB.close();
        }

        return checkDB != null ? true : false;


    }


    private void copyDatabase() throws IOException {

        InputStream myInput = context.getAssets().open(DB_NAME);
        String outFileName = DB_PATH + DB_NAME;
        OutputStream myOutput = new FileOutputStream(outFileName);

        byte[] buffer = new byte[64];
        int length;

        while ((length = myInput.read(buffer)) > 0) {

            myOutput.write(buffer, 0, length);

        }

        myOutput.flush();
        myOutput.close();
        myInput.close();

    }

    public void openDatabase() throws SQLException {

        String path = DB_PATH + DB_NAME;
        myDatabase = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READWRITE);

    }


    @Override
    public synchronized void close() {

        if (myDatabase != null) {
            myDatabase.close();
        }

        super.close();


    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        try {

            this.getReadableDatabase();
            context.deleteDatabase(DB_NAME);
            copyDatabase();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public Cursor getMeaning(String text)
    {
        Cursor c= myDatabase.rawQuery("SELECT en_definition,example,synonyms,antonyms FROM words WHERE en_word==UPPER('"+text+"')",null);
        return c;
    }

    public Cursor getSuggestions(String text)
    {
        Cursor c= myDatabase.rawQuery("SELECT _id, en_word FROM words WHERE en_word LIKE '"+text+"%' LIMIT 40",null);
        return c;
    }


    public void inserHistory(String text){

        myDatabase.execSQL("INSERT INTO history(word) VALUES (UPPER ('"+text+"'))");

    }

    public Cursor getHistory()
    {
        Cursor c= myDatabase.rawQuery("select distinct  word, en_definition from history h join words w on h.word==w.en_word order by h._id desc",null);
        return c;
    }


    public void  deleteHistory()
    {
        myDatabase.execSQL("DELETE  FROM history");
    }


}
