package de.workacclaimer;

import java.util.List;

import de.workacclaimer.R;
import de.workacclaimer.db.JobsDataSource;
import de.workacclaimer.model.Group;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

/**
 * TODO: Implement Pause/Resume Timer TODO: Location Based Start TODO:
 * Preferences: set pause time, set pause end sound TODO: Workpackages: DB
 * scheme, generation and management TODO: Different statistics TODO: Credit
 * system for jobs
 * 
 * @author Jan Barocka
 * 
 */
public class MainActivity extends Activity {

	private long startTime;
	private long stopTime;
	private long pauseDuration;
	private long startPauseTime;
	private Button startStopBtn;
	private Button pauseResumeBtn;
	private EditText descrText;
	private EditText titleText;
	private ProgressBar pauseBar;
	private Spinner groupSpin;
	private JobsDataSource datasource;
	private ArrayAdapter<Group> sAdapter;
	// Preferences
	private String pref_tone;
	private int pref_pauseTime;

	// HAndler to perform pausetime
	private Handler mHandler;
	private int cPausetime;
	private final int DELAY = 200;
	boolean loaded = false;

	private String uriRingtoneDefault;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(this);
		
		uriRingtoneDefault = RingtoneManager.getDefaultUri(
				RingtoneManager.TYPE_ALARM).toString();
		pref_tone = prefs.getString("pausetone", uriRingtoneDefault);

		pref_pauseTime = Integer.valueOf(prefs.getString("pauseTime", "5"));

		mHandler = new Handler();

		groupSpin = (Spinner) findViewById(R.id.groupSpinner);
		pauseBar = (ProgressBar) findViewById(R.id.progressBar1);
		pauseBar.setMax(pref_pauseTime * 1000 * 60);
		startStopBtn = (Button) findViewById(R.id.btnStartStop);
		pauseResumeBtn = (Button) findViewById(R.id.btnPauseResume);
		descrText = (EditText) findViewById(R.id.descrEditText);
		titleText = (EditText) findViewById(R.id.titleEditText);
		
		datasource = new JobsDataSource(this);

		//fill spinner with work groupos from db
		datasource.open();
		List<Group> gList = datasource.getAllGroups();
		
		//different layouts for adapter and dropdown because of the item height and circle for selection
		sAdapter = new ArrayAdapter<Group>(this, android.R.layout.simple_spinner_item, gList);
		sAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		groupSpin.setAdapter(sAdapter);
		
		stopTime = 0;

		// check if state was saved before
		if (savedInstanceState == null) {
			startTime = 0;
			pauseDuration = 0;
			cPausetime = 0;
		} else {
			cPausetime = savedInstanceState.getInt("cPausetime");
			groupSpin.setSelection(savedInstanceState.getInt("group"));
			if (cPausetime != 0) {
				mHandler.post(mUpdateTimeTask);
			}

			startTime = savedInstanceState.getLong("startTime");
			pauseDuration = savedInstanceState.getLong("pauseDuration");
			startStopBtn.setText(savedInstanceState
					.getString("startStopBtnText"));
			pauseResumeBtn.setText(savedInstanceState
					.getString("pauseResumeBtnText"));
			pauseResumeBtn.setClickable(false);
		}

		if (startStopBtn.getText().toString().equals(getString(R.string.start))) {
			pauseResumeBtn.setEnabled(false);
		} else {
			pauseResumeBtn.setEnabled(true);
		}
		


	}

	public void startStopClicked(View v) {
		if (startStopBtn.getText().toString().equals(getString(R.string.start))) {
			startStopBtn.setText(R.string.stop);
			startTime = System.currentTimeMillis();
			pauseResumeBtn.setEnabled(true);
		} else {
			if (titleText.getText() == null) {
				Toast.makeText(getApplicationContext(), R.string.no_title,
						Toast.LENGTH_LONG);
			} else {
				pauseResumeBtn.setEnabled(false);
				startStopBtn.setText(R.string.start);
				stopTime = System.currentTimeMillis();
				datasource.open();
				datasource
						.createJob(titleText.getText().toString(), startTime,
								stopTime, pauseDuration, descrText.getText()
										.toString(), groupSpin.getSelectedItemPosition());
				datasource.close();
				descrText.setText("");
				titleText.setText("");
				startTime = 0;
				stopTime = 0;
				pauseDuration = 0;
			}
		}
	}

	public void pauseResumeClicked(View v) {
		if (pauseResumeBtn.isClickable()) {
			if (pauseResumeBtn.getText().toString()
					.equals(getString(R.string.pause))) {
				Toast.makeText(getApplicationContext(), "Pause",
						Toast.LENGTH_LONG);
				pauseResumeBtn.setText(R.string.resume);
				startPauseTime = System.currentTimeMillis();
				// TODO: pausieren und Counter gegeben durch Einstellungen herab
				// zählen
				cPausetime = pref_pauseTime * 1000 * 60;
				mHandler.post(mUpdateTimeTask);
			} else {
				pauseBar.setProgress(0);
				cPausetime = 0;
				pauseResumeBtn.setText(R.string.pause);
				pauseDuration += System.currentTimeMillis() - startPauseTime;
				mHandler.removeCallbacks(mUpdateTimeTask);
			}
		}
	}

	@Override
	public void onPause(){
		datasource.close();
		super.onPause();
	}
	
	@Override
	public void onResume() {
		datasource.open();
		List<Group> gList = datasource.getAllGroups();
		sAdapter = new ArrayAdapter<Group>(this, android.R.layout.simple_spinner_item, gList);
		sAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		groupSpin.setAdapter(sAdapter);

		if (startStopBtn.getText().toString().equals(getString(R.string.start))) {
			pauseResumeBtn.setEnabled(false);
		} else {
			pauseResumeBtn.setEnabled(true);
		}

		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(this);

		pref_tone = prefs.getString("pausetone", "<unset>");
		pref_pauseTime = Integer.valueOf(prefs.getString("pauseTime", "5"));
		super.onResume();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.opt_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.joblist:
			Intent i = new Intent(this, JobListActivity.class);
			startActivity(i);
			return true;
		case R.id.pref:
			startActivity(new Intent(this, SettingsActivity.class));
			return true;
		case R.id.workgroups:
			startActivity(new Intent(this, WorkGroupsActivity.class));
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	protected void onSaveInstanceState(final Bundle b) {
		b.putString("startStopBtnText", startStopBtn.getText().toString());
		b.putString("pauseResumeBtnText", pauseResumeBtn.getText().toString());
		b.putLong("startTime", startTime);
		b.putLong("pauseDuration", pauseDuration);
		b.putInt("cPausetime", cPausetime);
		b.putInt("group", groupSpin.getSelectedItemPosition());
	}

	private Runnable mUpdateTimeTask = new Runnable() {
		public void run() {
			if (cPausetime >= 0) {
				cPausetime -= DELAY;
				pauseBar.setProgress(pref_pauseTime * 1000 * 60 - cPausetime);
				mHandler.postDelayed(mUpdateTimeTask, DELAY);
			} else {

				Ringtone ring = RingtoneManager.getRingtone(MainActivity.this,
						Uri.parse(pref_tone));
				ring.play();
				pauseBar.setProgress(0);
				pauseResumeBtn.setText(R.string.pause);
				pauseDuration += System.currentTimeMillis() - startPauseTime;
				mHandler.removeCallbacks(mUpdateTimeTask);
				cPausetime = 0;
			}

		}
	};
}