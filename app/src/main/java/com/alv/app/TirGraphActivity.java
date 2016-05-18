package com.alv.app;

import com.alv.app.R;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;

import android.view.MenuItem;

public class TirGraphActivity extends AppCompatActivity  {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		final ActionBar actionbar = getSupportActionBar();
		if(actionbar != null) {
			actionbar.setDisplayHomeAsUpEnabled(true); // pour activer le retour Home
		}
		setContentView(R.layout.activity_graph);
		if (savedInstanceState == null) {
			
			
			
			TirGraphFragment frag = new TirGraphFragment();
			
			 getSupportFragmentManager().beginTransaction()
            .add(R.id.activityGraph, frag)
            .commit();

	
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. Use NavUtils to allow users
            // to navigate up one level in the application structure. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            NavUtils.navigateUpTo(this, new Intent(this, PageListActivity.class));
            return true;
        }
		return super.onOptionsItemSelected(item);
	}
	
	
}
