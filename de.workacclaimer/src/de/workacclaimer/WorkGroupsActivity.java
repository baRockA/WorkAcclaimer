package de.workacclaimer;

import java.util.List;

import de.workacclaimer.db.JobsDataSource;
import de.workacclaimer.model.Group;
import de.workacclaimer.model.Job;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TwoLineListItem;
import android.widget.AdapterView.AdapterContextMenuInfo;

public class WorkGroupsActivity extends ListActivity{
	private JobsDataSource datasource;
	private ListView list;
	private ArrayAdapter<Group> adapter;
	
	private EditText titleET;
	private EditText descrET;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.workgroups);
		
		titleET = (EditText) findViewById(R.id.groupTitleEditText);
		descrET = (EditText) findViewById(R.id.groupDescriptionEditText);
		
		datasource = new JobsDataSource(this);
		datasource.open();
		
		List<Group> gList = datasource.getAllGroups();
		
		adapter = new ArrayAdapter<Group>(this, android.R.layout.simple_list_item_1, gList);
		
		list = getListView();
		list.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		list.setAdapter(adapter);
		
		// register the items for our context menu
		registerForContextMenu(list);

	}
	

	@Override
	protected void onResume() {
		datasource.open();
		List<Group> gList = datasource.getAllGroups();
		adapter = new ArrayAdapter<Group>(this, android.R.layout.simple_list_item_1, gList);
		list = getListView();
		list.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		list.setAdapter(adapter);
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
		Group g = adapter.getItem(position);

		Intent i = new Intent(this, GroupViewerActivity.class);
		i.putExtra("gid", g.getId());
		i.putExtra("gtitle", g.getTitle());
		i.putExtra("gdescr", g.getDescription());
		
		startActivity(i);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);

		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.gcontext_menu, menu);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
				.getMenuInfo();
		switch (item.getItemId()) {
		case R.id.delete:
			deleteListItem(info.id);
			return true;
		default:
			return super.onContextItemSelected(item);
		}
	}

	private void deleteListItem(long id) {
		Group g = adapter.getItem((int)id);
		adapter.remove(adapter.getItem((int) id));
		datasource.deleteGroup(g);
		adapter.notifyDataSetChanged();
	}
	
	public void addClicked(View v){
		datasource.createGroup(titleET.getText().toString(), descrET.getText().toString());
		List<Group> gList = datasource.getAllGroups();
		adapter = new ArrayAdapter<Group>(this, android.R.layout.simple_list_item_1, gList);
		list.setAdapter(adapter);
		adapter.notifyDataSetChanged();
	}
}
