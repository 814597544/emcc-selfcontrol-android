package app.emcc_selfcontrol_android.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBAdapter {

	private static final String DATABASE_NAME = "zkl.db";
	private static final int DATABASE_VERSION = 1;
	private static String DATABASE_CREATE="create table dream(_id integer not null primary key autoincrement,date text ,delta_time text ,	rest_time text ,waste_time text)";;
	private final Context context;
	private DatabaseHelper DBHelper;
	private SQLiteDatabase db;
    private String tableName;

	public DBAdapter(Context ctx) {
		this.context = ctx;
		DBHelper = new DatabaseHelper(context);
	}

	private static class DatabaseHelper extends SQLiteOpenHelper
	{
		DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(DATABASE_CREATE);
            Log.i("TAG", "create database--------");
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS titles");
			onCreate(db);
		}
	}


	public DBAdapter open() throws SQLException {
		db = DBHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		DBHelper.close();
	}

    public long insertItem(String tableName,String  id, String date, String delta_time, String rest_time,
                              String waste_time) {
        ContentValues cv = new ContentValues();
        cv.put("date", date);
        cv.put("delta_time", delta_time);
        cv.put("rest_time", rest_time);
        cv.put("waste_time", waste_time);
        return  db.insert(tableName, null, cv);
    }
	public boolean deleteTitle(String tableName,String date) {
		return db.delete(tableName, "date=" + date, null) > 0;
	}

	public Cursor getAllItem() {
        Cursor mCursor = db.query(tableName,
                new String[] { "date", "delta_time", "rest_time", "waste_time",
                }, null, null,
                null, null, null);
        return mCursor;
	}

	public Cursor getItem(String tableName,String date) throws SQLException {
		Cursor mCursor = db.query(tableName,
				new String[] { "date", "delta_time", "rest_time", "waste_time",
						 }, "date="+date, null,
				null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}

	public boolean updateTitle(String tableName,String  id, String date, String delta_time, String rest_time,
			String waste_time) {
		ContentValues cv = new ContentValues();
		cv.put("date", date);
		cv.put("delta_time", delta_time);
		cv.put("rest_time", rest_time);
		cv.put("waste_time", waste_time);
		return db.update(tableName, cv,  "date=" + date, null) > 0;
	}

    public  void setTableName(String tableName){

    }

    public static String getTableName(){
       return DATABASE_CREATE;
    }
}