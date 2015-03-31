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
    private static final String TABLE_NAME = "dream";
	private static final int DATABASE_VERSION = 1;
	private static String DATABASE_CREATE="create table dream(_id integer not null primary key autoincrement,dream_name text,date text ,delta_time text ,	rest_time text ,waste_time text,goal_time text)";
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

    public long insertItem( String dream_name,String date, String delta_time, String rest_time,
                              String waste_time,String goal_time) {
        ContentValues cv = new ContentValues();
        cv.put("dream_name", dream_name);
        cv.put("date", date);
        cv.put("delta_time", delta_time);
        cv.put("rest_time", rest_time);
        cv.put("waste_time", waste_time);
        cv.put("goal_time", goal_time);
        return  db.insert(TABLE_NAME, null, cv);
    }
	public boolean deleteTitle(String date) {
		return db.delete(TABLE_NAME, "date=" + date, null) > 0;
	}

	public Cursor getAllItem() {
        Cursor mCursor = db.query(TABLE_NAME,
                new String[] {"dream_name", "date", "delta_time", "rest_time", "waste_time",
                "goal_time"}, null, null,
                null, null, null);
        return mCursor;
	}

	public Cursor getItem(String date) throws SQLException {
		Cursor mCursor = db.query(TABLE_NAME,
				new String[] {"dream_name", "date", "delta_time", "rest_time", "waste_time",
                        "goal_time"}, "date="+date, null,
				null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}

	public boolean updateTitle( String dream_name,String date, String delta_time, String rest_time,
			String waste_time,String goal_time) {
		ContentValues cv = new ContentValues();
        cv.put("dream_name", dream_name);
        cv.put("date", date);
		cv.put("delta_time", delta_time);
		cv.put("rest_time", rest_time);
		cv.put("waste_time", waste_time);
        cv.put("goal_time", goal_time);
		return db.update(TABLE_NAME, cv,  "date=" + date, null) > 0;
	}

    public  void setTableName(String tableName){

    }

    public static String getTableName(){
       return DATABASE_CREATE;
    }
}