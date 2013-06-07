package com.example.TinTin;

import android.content.ContentValues;
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
import java.util.ArrayList;


/**
 * Created with IntelliJ IDEA.
 * User: pawel
 * Date: 19.05.13
 * Time: 21:34
 * To change this template use File | Settings | File Templates.
 */
public class Baza extends SQLiteOpenHelper {



        //The Android's default system path of your application database.
        private static String DB_PATH = "/data/data/com.example.TinTin/databases/";

        private static String DB_NAME = "tin";

        private SQLiteDatabase myDataBase;

        private final Context myContext;

        /**
         * Constructor
         * Takes and keeps a reference of the passed context in order to access to the application assets and resources.
         * @param context
         */
        public Baza(Context context) {

            super(context, DB_NAME, null, 1);
            this.myContext = context;

            try {
                createDataBase();

            } catch (IOException E) {

            }
        }

        /**
         * Creates a empty database on the system and rewrites it with your own database.
         * */
        public void createDataBase() throws IOException {

            boolean dbExist = checkDataBase();

            if(dbExist){
                //do nothing - database already exist
            }else{

                //By calling this method and empty database will be created into the default system path
                //of your application so we are gonna be able to overwrite that database with our database.
                this.getReadableDatabase();

                try {

                    copyDataBase();

                } catch (IOException e) {

                    throw new Error("Error copying database");

                }
            }

        }

        /**
         * Check if the database already exist to avoid re-copying the file each time you open the application.
         * @return true if it exists, false if it doesn't
         */
        private boolean checkDataBase(){

            SQLiteDatabase checkDB = null;

            try{
                String myPath = DB_PATH + DB_NAME;
                checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);

            }catch(SQLiteException e){

                //database does't exist yet.

            }

            if(checkDB != null){

                checkDB.close();

            }

            return checkDB != null ? true : false;
        }

        /**
         * Copies your database from your local assets-folder to the just created empty database in the
         * system folder, from where it can be accessed and handled.
         * This is done by transfering bytestream.
         * */
        private void copyDataBase() throws IOException{

            //Open your local db as the input stream
            InputStream myInput = myContext.getAssets().open(DB_NAME);

            // Path to the just created empty db
            String outFileName = DB_PATH + DB_NAME;

            //Open the empty db as the output stream
            OutputStream myOutput = new FileOutputStream(outFileName);

            //transfer bytes from the inputfile to the outputfile
            byte[] buffer = new byte[1024];
            int length;
            while ((length = myInput.read(buffer))>0){
                myOutput.write(buffer, 0, length);
            }

            //Close the streams
            myOutput.flush();
            myOutput.close();
            myInput.close();

        }

        public void openDataBase() throws SQLException {

            //Open the database
            String myPath = DB_PATH + DB_NAME;
            myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);

        }

        @Override
        public synchronized void close() {

            if(myDataBase != null)
                myDataBase.close();

            super.close();

        }

        @Override
        public void onCreate(SQLiteDatabase db) {

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }

    public void add(int user_id,int res_id,String text,String date)
    {
        try {

            openDataBase();

        } catch (SQLException sqle) {

            throw sqle;

        }
        ContentValues values = new ContentValues();
        values.put("user_id", user_id);
        values.put("res_id", res_id);
        values.put("text", text);
        values.put("date", date);
        long i = myDataBase.insert("komentarze", null, values);
        Log.d("LOL","taka faka " + i);
        close();
    }

    public ArrayList<Komentarz> getLista()
    {
        ArrayList<Komentarz> wynik= new ArrayList<Komentarz>();


        try {

            openDataBase();

        } catch (SQLException sqle) {
            Log.e("lol", "sqle");
            throw sqle;

        }
        Log.e("lol", "sqle1");
        Cursor c = myDataBase.rawQuery("SELECT * FROM komentarze;", null);

        Log.e("lol", "sqle2");
        c.moveToFirst();
        c.moveToNext();


        for (int i = 0; i < c.getCount() -1; i++) {

            Komentarz newPoi = new Komentarz(c.getInt(c.getColumnIndex("user_id")),
                    c.getInt(c.getColumnIndex("res_id")),
                    c.getString(c.getColumnIndex("text")),
                    c.getString(c.getColumnIndex("date")));



            wynik.add(newPoi);

            if (i != c.getCount() - 2)
                c.move(1);
        }
        close();
        return wynik;
    }



    public Komentarz getUsrId(){


        try {

            openDataBase();

        } catch (SQLException sqle) {
            Log.e("lol", "sqle");
            throw sqle;

        }
        Log.e("lol", "sqle1");
        Cursor c = myDataBase.rawQuery("SELECT * FROM komentarze;", null);

        Log.e("lol", "sqle2");
        c.moveToFirst();

            Komentarz newPoi = new Komentarz(c.getInt(c.getColumnIndex("user_id")),
                    c.getInt(c.getColumnIndex("res_id")),
                    c.getString(c.getColumnIndex("text")),
                    c.getString(c.getColumnIndex("date")));

        return newPoi;

    }

    public void sru() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("komentarze", null,null);
        db.close();
    }

    public void resUpdate(String nazwa,int id)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("id", id);

        int a=db.update("res", values, "nazwa = ?",
                new String[] { String.valueOf(nazwa) });


    }


    public void komUpdateId(String nazwa,int id)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("user_id", id);

        int a= db.update("komentarze", values, "text = ?",
                new String[] { String.valueOf(nazwa) });

    }


    ///////////////////////////////////////////////////////////////////////////////////////////////ONLINE

    public ArrayList<Komentarz> getListaOnline()
    {
        ArrayList<Komentarz> wynik= new ArrayList<Komentarz>();


        try {

            openDataBase();

        } catch (SQLException sqle) {
            Log.e("lol", "sqle");
            throw sqle;

        }
        Log.e("lol", "sqle1");
        Cursor c = myDataBase.rawQuery("SELECT * FROM komentarzeON;", null);

        Log.e("lol", "sqle2");
        c.moveToFirst();


        for (int i = 0; i < c.getCount(); i++) {

            Komentarz newPoi = new Komentarz(c.getInt(c.getColumnIndex("user_id")),
                    c.getInt(c.getColumnIndex("res_id")),
                    c.getString(c.getColumnIndex("text")),
                    c.getString(c.getColumnIndex("date")));

            newPoi.setId(c.getInt(c.getColumnIndex("id")));



            wynik.add(newPoi);

            if (i != c.getCount() - 1)
                c.move(1);
        }
        close();
        return wynik;
    }



    public void addOnline(int user_id,int res_id,String text,String date,int id)
    {
        try {

            openDataBase();

        } catch (SQLException sqle) {

            throw sqle;

        }
        ContentValues values = new ContentValues();
        values.put("user_id", user_id);
        values.put("res_id", res_id);
        values.put("text", text);
        values.put("date", date);
        values.put("id", id);
        long i = myDataBase.insert("komentarzeON", null, values);
        Log.d("LOL","taka faka " + i);
        close();
    }



    public void sruOnline() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("komentarzeON", null,null);
        db.close();
    }

    public void sruIdOnline(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("komentarzeON", "id = ?",
                new String[] { String.valueOf(id) });
        db.close();
    }

    public ArrayList<Komentarz> getListaOnlineId(int id)
    {
        ArrayList<Komentarz> wynik= new ArrayList<Komentarz>();


        try {

            openDataBase();

        } catch (SQLException sqle) {
            Log.e("lol", "sqle");
            throw sqle;

        }
        Log.e("lol", "sqle1");
        Cursor c = myDataBase.rawQuery("SELECT * FROM komentarzeON WHERE res_id = " + id + ";", null);

        Log.e("lol", "sqle2");
        c.moveToFirst();


        for (int i = 0; i < c.getCount(); i++) {

            Komentarz newPoi = new Komentarz(c.getInt(c.getColumnIndex("user_id")),
                    c.getInt(c.getColumnIndex("res_id")),
                    c.getString(c.getColumnIndex("text")),
                    c.getString(c.getColumnIndex("date")));

            newPoi.setId(c.getInt(c.getColumnIndex("id")));



            wynik.add(newPoi);

            if (i != c.getCount() - 1)
                c.move(1);
        }
        close();
        return wynik;
    }



    ///////////////////////////////////////////////////////////////////////////////////////////////////RESTAURACJE

    public void sruRes() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("res", null,null);
        db.close();
    }

    public void sruResId(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("res", "id = ?",
                new String[] { String.valueOf(id) });
        db.close();
    }


    public void addRes(int id,String adres,String nazwa,String typ)
    {
        try {

            openDataBase();

        } catch (SQLException sqle) {

            throw sqle;

        }
        ContentValues values = new ContentValues();
        values.put("id", id);
        values.put("adres", adres);
        values.put("nazwa", nazwa);
        values.put("typ", typ);
        long i = myDataBase.insert("res", null, values);
        Log.d("LOL","taka faka " + i);
        close();
    }


    public ArrayList<Res> getListaRes()
    {
        ArrayList<Res> wynik= new ArrayList<Res>();


        try {

            openDataBase();

        } catch (SQLException sqle) {
            Log.e("lol", "sqle");
            throw sqle;

        }
        Log.e("lol", "sqle1");
        Cursor c = myDataBase.rawQuery("SELECT * FROM Res;", null);

        Log.e("lol", "sqle2");
        c.moveToFirst();


        for (int i = 0; i < c.getCount(); i++) {

            Res newPoi = new Res(c.getInt(c.getColumnIndex("id")),
                    c.getString(c.getColumnIndex("adres")),
                    c.getString(c.getColumnIndex("nazwa")),
                    c.getString(c.getColumnIndex("typ")));





            wynik.add(newPoi);

            if (i != c.getCount() - 1)
                c.move(1);
        }
        close();
        return wynik;
    }




    // Add your public helper methods to access and get content from the database.
        // You could return cursors by doing "return myDataBase.query(....)" so it'd be easy
        // to you to create adapters for your views.


}
