package de.workacclaimer.db;

import android.content.Context;
import android.database.CrossProcessCursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * class for creating and upgrading the database
 * see SQLite Tutorial of http://www.vogella.com/articles/AndroidSQLite/article.html
 * @author Jan Barocka
 *
 */
public class MySQLiteHelper extends SQLiteOpenHelper{

	public static final String TABLE_JOBS = "jobs";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_TITLE = "title";
	public static final String COLUMN_STARTTIME = "starttime";
	public static final String COLUMN_STOPTIME = "stoptime";
	public static final String COLUMN_DESCRIPTION = "description";
	public static final String COLUMN_PAUSETIME = "pausetime";
	public static final String COLUMN_JGID = "jgid";
	
	public static final String TABLE_GROUPS = "groups";
	public static final String COLUMN_GID = "_id";
	public static final String COLUMN_GTITLE = "gtitle";
	public static final String COLUMN_GDESCR = "gdescription";
	
	private static final String DATABASE_NAME = "jobs.db";
	private static final int DATABASE_VERSION = 1;
	
	// Database creation sql statement
	private static final String JOBS_CREATE = "create table "
				+ TABLE_JOBS + "(" + COLUMN_ID
				+ " integer primary key autoincrement, " + COLUMN_TITLE
				+ " text not null, "+ COLUMN_STARTTIME
				+ " integer not null, " + COLUMN_STOPTIME
				+ " integer not null, " + COLUMN_PAUSETIME
				+ " integer, " + COLUMN_DESCRIPTION
				+ " text, " + COLUMN_JGID
				+ " integer);";
	
	private static final String GROUPS_CREATE = "create table "
			+ TABLE_GROUPS + "(" + COLUMN_GID
			+ " integer primary key autoincrement, " + COLUMN_GTITLE
			+ " text not null, " + COLUMN_GDESCR
			+ " text);";
	
	public MySQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(JOBS_CREATE);
		Log.d("CREATE", JOBS_CREATE);
		db.execSQL(GROUPS_CREATE);
		Log.d("CREATE", GROUPS_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_JOBS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_GROUPS);
		onCreate(db);
		
	}
	
	

}
