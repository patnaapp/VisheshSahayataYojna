package com.bih.nic.in.visheshsahayata.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import com.bih.nic.in.visheshsahayata.entity.Block;
import com.bih.nic.in.visheshsahayata.entity.District;
import com.bih.nic.in.visheshsahayata.entity.Project;
import com.bih.nic.in.visheshsahayata.entity.State;
import com.bih.nic.in.visheshsahayata.entity.panchayt;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;


public class DataBaseHelper  extends SQLiteOpenHelper {


    private static String DB_PATH = "";// "/data/data/com.bih.nic.app.biharmunicipalcorporation/databases/";
    private static String DB_NAME = "PACSDB1";

    private SQLiteDatabase myDataBase;

    private final Context myContext;

    /**
     * Constructor Takes and keeps a reference of the passed context in order to
     * access to the application assets and resources.
     *
     * @param context
     */
    public DataBaseHelper(Context context) {

        super(context, DB_NAME, null, 1);
        if (android.os.Build.VERSION.SDK_INT >= 4.2) {


            DB_PATH = context.getApplicationInfo().dataDir + "/databases/";
        } else {
            DB_PATH = "/data/data/" + context.getPackageName() + "/databases/";
        }
        this.myContext = context;
    }

    /**
     * Creates a empty database on the system and rewrites it with your own
     * database.
     */
    public void createDataBase() throws IOException {

        boolean dbExist = checkDataBase();

        if (dbExist) {
            // do nothing - database already exist


        } else {

            // By calling this method and empty database will be created into
            // the default system path
            // of your application so we are gonna be able to overwrite that
            // database with our database.
            this.getReadableDatabase();

            try {

                copyDataBase();

            } catch (IOException e) {

                throw new Error("Error copying database");

            }
        }

    }

    /**
     * Check if the database already exist to avoid re-copying the file each
     * time you open the application.
     *
     * @return true if it exists, false if it doesn't
     */
    private boolean checkDataBase() {

        SQLiteDatabase checkDB = null;

        try {
            String myPath = DB_PATH + DB_NAME;
            //this.getReadableDatabase();

            checkDB = SQLiteDatabase.openDatabase(myPath, null,
                    SQLiteDatabase.NO_LOCALIZED_COLLATORS
                            | SQLiteDatabase.OPEN_READWRITE);


        } catch (SQLiteException e) {

            // database does't exist yet.

        }

        if (checkDB != null) {

            checkDB.close();

        }

        return checkDB != null ? true : false;

    }

    public boolean databaseExist() {


        File dbFile = new File(DB_PATH + DB_NAME);

        return dbFile.exists();
    }

    /**
     * Copies your database from your local assets-folder to the just created
     * empty database in the system folder, from where it can be accessed and
     * handled. This is done by transfering bytestream.
     */
    private void copyDataBase() throws IOException {

        // Open your local db as the input stream
        InputStream myInput = myContext.getAssets().open(DB_NAME);

        // Path to the just created empty db
        String outFileName = DB_PATH + DB_NAME;

        // Open the empty db as the output stream
        OutputStream myOutput = new FileOutputStream(outFileName);
        this.getReadableDatabase().close();

        // transfer bytes from the inputfile to the outputfile
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }

        // Close the streams
        myOutput.flush();
        myOutput.close();
        myInput.close();


    }

    public void openDataBase() throws SQLException {

        // Open the database
        this.getReadableDatabase();
        String myPath = DB_PATH + DB_NAME;
        myDataBase = SQLiteDatabase.openDatabase(myPath, null,
                SQLiteDatabase.OPEN_READONLY);

    }

    @Override
    public synchronized void close() {

        if (myDataBase != null)
            myDataBase.close();

        super.close();

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {


    }


    public ArrayList<District> getDistrictLocal(String statecode) {

        ArrayList<District> districtList = new ArrayList<District>();

        try {

            SQLiteDatabase db = this.getReadableDatabase();
            String[] params = new String[]{statecode.trim()};

            Cursor cur = db
                    .rawQuery(
                            "SELECT * from  District_All where STATE_CODE='" + statecode.trim() + "' order by DISTRICTNAME", null);
            int x = cur.getCount();

            while (cur.moveToNext()) {

                District district = new District();
                district.setDistrictCode(cur.getString(cur
                        .getColumnIndex("DISTRICT_CODE")));
                district.setDistrictName(cur.getString(cur.getColumnIndex("DISTRICTNAME")));
                //district.setDistrictName(cur.getString(cur.getColumnIndex("DistName")));



                districtList.add(district);
            }

            cur.close();
            db.close();

        } catch (Exception e) {
            e.printStackTrace();
            // TODO: handle exception

        }
        return districtList;

    }
    public ArrayList<State> getStateLocal() {

        ArrayList<State> districtList = new ArrayList<State>();

        try {

            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cur = db
                    .rawQuery(
                            "SELECT * from  StateAll order by StateName", null);
            int x = cur.getCount();

            while (cur.moveToNext()) {

                State district = new State();
                district.setStateCode(cur.getString(cur
                        .getColumnIndex("StateCode")));
                district.setStateName(cur.getString(cur
                        .getColumnIndex("StateName")));


                districtList.add(district);
            }

            cur.close();
            db.close();

        } catch (Exception e) {
            e.printStackTrace();
            // TODO: handle exception

        }
        return districtList;

    }

    public ArrayList<panchayt> getPanchayatLocal(String blockCode) {

        ArrayList<panchayt> districtList = new ArrayList<panchayt>();

        try {

            SQLiteDatabase db = this.getReadableDatabase();
            String[] params = new String[]{blockCode.trim()};
            Cursor cur = db
                    .rawQuery(
                            "SELECT * from  NewPanchayatAangan where BlockCode='" + blockCode.trim() + "' order by PanchayatCode asc", null);
            int x = cur.getCount();

            while (cur.moveToNext()) {

                panchayt district = new panchayt();
                district.setBlockCode(cur.getString(cur.getColumnIndex("BlockCode")).trim());
                district.setPanchCode(cur.getString(cur.getColumnIndex("PanchayatCode")).trim());
                district.setPanchyatName(cur.getString(cur
                        .getColumnIndex("PanchayatNameHN")));

                district.setAreaTpte(cur.getString(cur
                        .getColumnIndex("AreaType")));

                districtList.add(district);



            }

            cur.close();
            db.close();

        } catch (Exception e) {
            e.printStackTrace();
            // TODO: handle exception

        }
        return districtList;

    }

    public long insertPANCHAYATData(ArrayList<panchayt> result, String blockcodecode) {

        long c = -1;
        try {
            // CREATE TABLE "BankList" ( `BankId` TEXT, `BankName` TEXT, `BankType` TEXT )

            SQLiteDatabase db = this.getWritableDatabase();
            // db.execSQL("Delete from RANGE");
            for (panchayt block : result) {

                ContentValues values = new ContentValues();
                values.put("PanchayatCode", block.getPanchCode());
                values.put("PanchayatName", block.getPanchyatName());
                values.put("PanchayatNameHN", block.getPanchyatName_HN());
                values.put("BlockCode",blockcodecode);
                values.put("AreaType",block.getAreaTpte());

                String[] whereArgs = new String[]{block.getPanchCode()};
                c = db.update("NewPanchayatAangan", values, "PanchayatCode=?", whereArgs);
                if (!(c > 0)) {

                    c = db.insert("NewPanchayatAangan", null, values);
                }

            }
            db.close();


        } catch (Exception e) {
            // TODO: handle exception
        }
        // return plantationList;
        return c;
    }

    public long insertStateData(ArrayList<State> result) {

        long c = -1;
        try {

            SQLiteDatabase db = this.getWritableDatabase();

            for (State block : result) {

                ContentValues values = new ContentValues();
                values.put("StateCode", block.getStateCode());
                values.put("StateName", block.getStateName());


                String[] whereArgs = new String[]{block.getStateCode()};
                c = db.update("State", values, "StateCode=?", whereArgs);
                if (!(c > 0)) {

                    c = db.insert("State", null, values);
                }



            }
            db.close();


        } catch (Exception e) {
            // TODO: handle exception
        }
        // return plantationList;
        return c;
    }


    public ArrayList<Project> getBlocFromLocal(String distCode) {

        ArrayList<Project> districtList = new ArrayList<Project>();

        try {

            SQLiteDatabase db = this.getReadableDatabase();
            String[] params = new String[]{distCode.trim()};
            Cursor cur = db
                    .rawQuery("SELECT * from  BlockMaster_all where DISTRICT_CODE='" + distCode.trim() + "' order by BLOCK_NAME", null);
            int x = cur.getCount();

            while (cur.moveToNext()) {

                Project project = new Project();
                //project.setProjCode(cur.getString(cur.getColumnIndex("BlockCode")).trim());
                project.setProjName(cur.getString(cur.getColumnIndex("BLOCK_NAME")).trim());
                project.setDistCode(cur.getString(cur.getColumnIndex("DISTRICT_CODE")));
                project.setBlockCode(cur.getString(cur.getColumnIndex("BLOCK_CODE")));

                districtList.add(project);
            }

            cur.close();
            db.close();

        } catch (Exception e) {
            e.printStackTrace();
            // TODO: handle exception

        }
        return districtList;

    }


    public long insertDistData(ArrayList<District> result) {

        long c = -1;
        try {

            SQLiteDatabase db = this.getWritableDatabase();

            for (District block : result) {

                ContentValues values = new ContentValues();
                values.put("DISTRICT_CODE", block.getDistrictCode());
                values.put("DISTRICTNAME", block.getDistrictName());
                values.put("STATE_CODE", block.getStateCode());


                String[] whereArgs = new String[]{block.getStateCode()};
                c = db.update("District_All", values, "DISTRICT_CODE=?", whereArgs);
                if (!(c > 0)) {

                    c = db.insert("District_All", null, values);
                }



            }
            db.close();


        } catch (Exception e) {
            // TODO: handle exception
        }
        // return plantationList;
        return c;
    }



    public long insertBlkData(ArrayList<Block> result) {

        long c = -1;
        try {

            SQLiteDatabase db = this.getWritableDatabase();

            for (Block block : result) {

                ContentValues values = new ContentValues();
                values.put("BLOCK_CODE", block.getBlockCode());
                values.put("BLOCK_NAME", block.getBlockName());
                values.put("STATE_CODE", block.getStateCode());
                values.put("DISTRICT_CODE", block.getDistrictCode());


                String[] whereArgs = new String[]{block.getStateCode()};
                c = db.update("BlockMaster_all", values, "BLOCK_CODE=?", whereArgs);
                if (!(c > 0)) {

                    c = db.insert("BlockMaster_all", null, values);
                }



            }
            db.close();


        } catch (Exception e) {
            // TODO: handle exception
        }
        // return plantationList;
        return c;
    }

}
