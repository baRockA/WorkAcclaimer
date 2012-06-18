package de.workacclaimer;

import de.workacclaimer.db.JobsDataSource;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;


public class GroupViewerActivity extends Activity{

	private EditText titleET;
	private EditText descrET;
	
	private long gid;
	private String gtitle;
	private String gdescr;
	
	private JobsDataSource datasource; 
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.groupviewer);
		
		datasource = new JobsDataSource(this);
		
		titleET = (EditText) findViewById(R.id.gtitleEditText);
		descrET = (EditText) findViewById(R.id.gdescrEditText);
		
		gtitle = getIntent().getStringExtra("gtitle");
		gdescr = getIntent().getStringExtra("gdescr");
		gid = getIntent().getLongExtra("gid", -1);
		Log.d("G_VIEWER", gid+"");
		titleET.setText(gtitle);
		descrET.setText(gdescr);
	}
	
	public void saveClicked(View v){
		
		if(!titleET.getText().toString().equals(gtitle) ||
			!descrET.getText().toString().equals(gdescr)){
			datasource.open();
			datasource.updateGroupTitelAndDescrById(gid, titleET.getText().toString(), descrET.getText().toString());
			datasource.close();
		}
		
		finish();
	}
	
	
}
