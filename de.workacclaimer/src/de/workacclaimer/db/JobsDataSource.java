package de.workacclaimer.db;

import java.util.ArrayList;
import java.util.List;

import de.workacclaimer.model.Group;
import de.workacclaimer.model.Job;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

/**
 * This class performs all the changes to the database
 * @author Jan Barocka
 *
 */
public class JobsDataSource {
	// Database fields
		private SQLiteDatabase database;
		private MySQLiteHelper dbHelper;
		private String[] allJobColumns = { MySQLiteHelper.COLUMN_ID,
				MySQLiteHelper.COLUMN_TITLE,
				MySQLiteHelper.COLUMN_DESCRIPTION, 
				MySQLiteHelper.COLUMN_PAUSETIME, 
				MySQLiteHelper.COLUMN_STARTTIME, 
				MySQLiteHelper.COLUMN_STOPTIME,
				MySQLiteHelper.COLUMN_JGID};
		private String[] allGroupColumns = {
				MySQLiteHelper.COLUMN_GID,
				MySQLiteHelper.COLUMN_GTITLE,
				MySQLiteHelper.COLUMN_GDESCR
		};

		public JobsDataSource(Context context) {
			this.dbHelper = new MySQLiteHelper(context);
		}

		public void open() throws SQLException {
			this.database = dbHelper.getWritableDatabase();
		}

		public void close() {
			dbHelper.close();
		}

		public void createJob(String title, long start, long stop, long pause, String descr, int jgid){
			ContentValues vals = new ContentValues();
			vals.put(MySQLiteHelper.COLUMN_TITLE, title);
			vals.put(MySQLiteHelper.COLUMN_DESCRIPTION, descr);
			vals.put(MySQLiteHelper.COLUMN_PAUSETIME, pause);
			vals.put(MySQLiteHelper.COLUMN_STARTTIME, start);
			vals.put(MySQLiteHelper.COLUMN_STOPTIME, stop);
			vals.put(MySQLiteHelper.COLUMN_JGID, jgid);
			
			long insertId = database.insert(MySQLiteHelper.TABLE_JOBS, null,
					vals);
			Cursor cursor = database.query(MySQLiteHelper.TABLE_JOBS,
					allJobColumns, MySQLiteHelper.COLUMN_ID + " = " + insertId, null,
					null, null, null);
		}
		
		public void createGroup(String title, String descr){
			ContentValues vals = new ContentValues();
			vals.put(MySQLiteHelper.COLUMN_GTITLE, title);
			vals.put(MySQLiteHelper.COLUMN_GDESCR, descr);
			long insertId = database.insert(MySQLiteHelper.TABLE_GROUPS, null,
					vals);
			Cursor cursor = database.query(MySQLiteHelper.TABLE_GROUPS,
					allGroupColumns, MySQLiteHelper.COLUMN_ID + " = " + insertId, null,
					null, null, null);
		}
		
		public void deleteJob(Job job){
			long id = job.getId();
			deleteJobById(id);
		}
		
		public void deleteGroup(Group group){
			long id = group.getId();
			deleteGroupById(id);
		}
		
		
		public void deleteJobById(long id){
			database.delete(MySQLiteHelper.TABLE_JOBS, MySQLiteHelper.COLUMN_ID
					+ " = " + id, null);
		}
		
		public void deleteGroupById(long id){
			database.delete(MySQLiteHelper.TABLE_GROUPS, MySQLiteHelper.COLUMN_GID
					+ " = " + id, null);
		}
		
		public void updateJobTitelAndDescrById(long id, String title, String descr, int jgid){
			ContentValues cv = new ContentValues();
			cv.put(MySQLiteHelper.COLUMN_TITLE, title);
			cv.put(MySQLiteHelper.COLUMN_DESCRIPTION, descr);
			cv.put(MySQLiteHelper.COLUMN_JGID, jgid);
			database.update(MySQLiteHelper.TABLE_JOBS, cv, MySQLiteHelper.COLUMN_ID
					+ " = " + id, null);
		}
		
		public void updateGroupTitelAndDescrById(long id, String title, String descr){
			ContentValues cv = new ContentValues();
			cv.put(MySQLiteHelper.COLUMN_GTITLE, title);
			cv.put(MySQLiteHelper.COLUMN_GDESCR, descr);
			database.update(MySQLiteHelper.TABLE_GROUPS, cv, MySQLiteHelper.COLUMN_GID
					+ " = " + id, null);
		}

		public List<Job> getAllJobs() {
			List<Job> jobs = new ArrayList<Job>();

			Cursor cursor = database.query(MySQLiteHelper.TABLE_JOBS,
					allJobColumns, null, null, null, null, null);

			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				jobs.add(cursorToJob(cursor));
				cursor.moveToNext();
			}
			// Make sure to close the cursor
			cursor.close();
			return jobs;
		}
		
		public List<Group> getAllGroups(){
			List<Group> g = new ArrayList<Group>();
			
			Cursor cursor = database.query(MySQLiteHelper.TABLE_GROUPS,
					allGroupColumns, null, null, null, null, null);

			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				g.add(cursorToGroup(cursor));
				cursor.moveToNext();
			}
			// Make sure to close the cursor
			cursor.close();
			return g;
		}
		
		public Cursor getAllJobsCursor() {
			return database.query(MySQLiteHelper.TABLE_JOBS,
					allJobColumns, null, null, null, null, null);
			
		}
		
		public Cursor getAllGroupsCursor(){
			return database.query(MySQLiteHelper.TABLE_GROUPS,
					allGroupColumns, null, null, null, null, null);
			
		}

		private Job cursorToJob(Cursor cursor) {
			Job j = new Job();
			j.setId(cursor.getLong(0));
			j.setTitle(cursor.getString(1));
			j.setDescription(cursor.getString(2));
			j.setPausetime(cursor.getLong(3));
			j.setStarttime(cursor.getLong(4));
			j.setStoptime(cursor.getLong(5));
			j.setJgid(cursor.getInt(6));
			return j;
		}
		
		private Group cursorToGroup(Cursor cursor){
			Group g = new Group();
			g.setId(cursor.getLong(0));
			g.setTitle(cursor.getString(1));
			g.setDescription(cursor.getString(2));
			return g;
		}
}
