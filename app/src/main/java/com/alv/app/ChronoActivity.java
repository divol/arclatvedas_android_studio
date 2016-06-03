package com.alv.app;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;


//http://stackoverflow.com/questions/13742551/how-to-get-my-location-changed-event-with-google-maps-android-api-v2

public class ChronoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final ActionBar actionbar = getSupportActionBar();
        if(actionbar != null) {
            actionbar.setDisplayHomeAsUpEnabled(true); // pour activer le retour Home
        }


        setContentView(R.layout.activity_chrono);


/*

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 2);
            return;
        }

*/

        if (savedInstanceState == null) {

            ChronoActivityFragment frag =  ChronoActivityFragment.newInstance();

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.activityChrono, frag)
                    .commit();


        }
    }
    public void onFragmentInteraction(Uri uri){

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


    @Override
    public void onResume() {
        super.onResume();




    }

    @Override
    public void onPause() {

        super.onPause();
    }
}


