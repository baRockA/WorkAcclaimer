package de.workacclaimer;


import java.util.List;

import de.workacclaimer.R;
import de.workacclaimer.db.JobsDataSource;
import de.workacclaimer.model.Job;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TwoLineListItem;
import android.view.ViewGroup;

/**
 * Handling for the joblist
 * 
 * @author Jan Barocka
 * 
 */
public class JobListActivity extends ListActivity {
	private JobsDataSource datasource;
	private ListView list;
	private ArrayAdapter adapter;
	private List<Job> adapterJobs;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.joblist);
		datasource = new JobsDataSource(this);
		datasource.open();
		
		buildData();

		list = getListView();
		list.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

		// register the items for our context menu
		registerForContextMenu(list);

	}
	
	private void buildData() {
		adapterJobs = datasource.getAllJobs();
		
		adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_2,
				adapterJobs) {
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				TwoLineListItem row;
				if (convertView == null) {
					LayoutInflater inflater = (LayoutInflater) getApplicationContext()
							.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
					row = (TwoLineListItem) inflater.inflate(
							android.R.layout.simple_list_item_2, null);
				} else {
					row = (TwoLineListItem) convertView;
				}
				Job data = adapterJobs.get(position);
				row.getText1().setText(data.getTitle());
				row.getText2().setText(data.getStartDate());

				return row;
			}
		};
		setListAdapter(adapter);
	}

	@Override
	protected void onResume() {
		datasource.open();
		buildData();
		super.onResume();
	}

	@Override
	protected void onPause() {
		datasource.close();
		super.onPause();
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		// Get the item that was clicked
		Object o = this.getListAdapter().getItem(position);
		String keyword = o.toString();
		Job selectedJob = adapterJobs.get((int) id);
		Intent i = new Intent(this, JobViewerActivity.class);
		i.putExtra("title", selectedJob.getTitle());
		i.putExtra("descr", selectedJob.getDescription());
		i.putExtra("date", selectedJob.getStartDate());
		i.putExtra("time", selectedJob.getTimeString());
		i.putExtra("pausetime", selectedJob.getPausetimeString());
		i.putExtra("id", selectedJob.getId());
		i.putExtra("group", selectedJob.getJgid());
		
		startActivity(i);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);

		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.context_menu, menu);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
				.getMenuInfo();
		switch (item.getItemId()) {
		case R.id.deletejob:
			deleteListItem(info.id);
			return true;
		default:
			return super.onContextItemSelected(item);
		}
	}

	private void deleteListItem(long id) {
		Job j = adapterJobs.get((int) id);
		adapter.remove(adapter.getItem((int) id));
		datasource.deleteJob(j);
		
		adapter.notifyDataSetChanged();
		
	}
}
