package de.workacclaimer;

import java.util.List;

import de.workacclaimer.R;
import de.workacclaimer.db.JobsDataSource;
import de.workacclaimer.model.Group;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class JobViewerActivity extends Activity {
	private EditText titleET;
	private EditText descrET;
	private TextView dateTV;
	private TextView pausetimeTV;
	private TextView timeTV;
	private String title;
	private String descr;
	private long id;
	private JobsDataSource datasource;
	private Spinner workGroupSpin;
	private ArrayAdapter<Group> sAdapter;
	private int gidSel;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.jobviewer);
		datasource = new JobsDataSource(this);
		datasource.open();
		workGroupSpin = (Spinner) findViewById(R.id.workGroupSpinner);
		titleET = (EditText) findViewById(R.id.titleEditText);
		descrET = (EditText) findViewById(R.id.descrEditText);
		dateTV = (TextView) findViewById(R.id.dateTextView);
		pausetimeTV = (TextView) findViewById(R.id.pauseTextView);
		timeTV = (TextView) findViewById(R.id.timeTextView);

		id = getIntent().getLongExtra("id", -1);

		title = getIntent().getStringExtra("title");
		titleET.setText(title);

		descr = getIntent().getStringExtra("descr");
		descrET.setText(descr);

		dateTV.setText(getIntent().getStringExtra("date"));
		pausetimeTV.setText(getIntent().getStringExtra("pausetime"));
		timeTV.setText(getIntent().getStringExtra("time"));

		// fill spinner with work groupos from db
		List<Group> gList = datasource.getAllGroups();
		
		sAdapter = new ArrayAdapter<Group>(this, android.R.layout.simple_spinner_item, gList);
		sAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		workGroupSpin.setAdapter(sAdapter);
		
		gidSel = getIntent().getIntExtra("group", 0);
		workGroupSpin.setSelection(gidSel);
	}

	@Override
	public void onResume(){
		datasource.open();
		workGroupSpin.setSelection(gidSel);
		super.onResume();
	}
	
	@Override
	public void onPause(){
		datasource.close();
		super.onPause();
	}
	
	public void saveClicked(View v) {
		if (id >= 0) {
			if (!titleET.getText().toString().equals(title)
					|| !descrET.getText().toString().equals(descr)) {
				datasource.updateJobTitelAndDescrById(id, titleET.getText()
						.toString(), descrET.getText().toString(), workGroupSpin.getSelectedItemPosition());
			}

		} else {
			Toast.makeText(this, R.string.notsaved, Toast.LENGTH_SHORT).show();
		}
	}

}
