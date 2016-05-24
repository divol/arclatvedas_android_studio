package com.alv.app;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;



//http://stackoverflow.com/questions/14702621/answer-draw-path-between-two-points-using-google-maps-android-api-v2



/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
    * {@link LieuxFragment.OnFragmentInteractionListener} interface
    * to handle interaction events.
            * Use the {@link LieuxFragment#newInstance} factory method to
    * create an instance of this fragment.
    */
    public class LieuxFragment extends SupportMapFragment implements OnMapReadyCallback ,GoogleMap.OnMarkerClickListener{

    private GoogleMap mMap;
    private Context mContext;
   // private FollowMeLocationSource followMeLocationSource;

    private OnFragmentInteractionListener mListener;
    private LatLng locationcourante;
    private Polyline line;
    public LieuxFragment() {
        // Required empty public constructor
    }



    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment LieuxFragment.
     */
    public static LieuxFragment newInstance() {
        LieuxFragment fragment = new LieuxFragment();
        return fragment;
    }


    public void setMContext(Context param1){
            mContext = param1;


        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 2);
            return;
        }

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getMapAsync(this);

        if (getArguments() != null) {
        }
    }



    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }


    @Override
    public boolean onMarkerClick(final Marker marker) {

        if (line != null)
            mMap.clear();
/*
        line = mMap.addPolyline(new PolylineOptions()
                .add(locationcourante, marker.getPosition())
                .width(5)
                .color(Color.BLUE));
        return true;
        */
        return false;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }

        setMContext(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    @Override
    public void onResume() {
        super.onResume();


/* We query for the best Location Provider everytime this fragment is displayed
         * just in case a better provider might have become available since we last displayed it */
        //followMeLocationSource.getBestAvailableProvider();
        if (mMap != null) {
            mMap.setMyLocationEnabled(true);
        }
        // Get a reference to the map/GoogleMap object
        //setUpMapIfNeeded();
    }

    @Override
    public void onPause() {
        /* Disable the my-location layer (this causes our LocationSource to be automatically deactivated.) */
        if (mMap != null) {
            mMap.setMyLocationEnabled(false);
        }
        super.onPause();
    }



/*
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            mMap = getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                // The Map is verified. It is now safe to manipulate the map:

                // Replace the (default) location source of the my-location layer with our custom LocationSource
                mMap.setLocationSource(followMeLocationSource);
                mMap.setOnMarkerClickListener(this);
                addLieuxToMap(mMap,this.getActivity().getApplicationContext());
            }
        }
    }

*/
    @Override
    public void onMapReady(GoogleMap map) {

      /*  [{"title":"Terrain","subtitle":"ALV-MMM","lat":"43.581268","long":"3.838444" },
        {"title":"Salle","subtitle":"ALV-MMM","lat":"43.567051","long":"3.898359" },
        {"title":"Complexe Courtoujours","subtitle":"ALV-MMM","lat":"43.559447","long":"3.880295" },
        {"title":"Comité départemental de Tir à l'Arc","subtitle":"Hérault","lat":"43.625852","long":"3.824049" },
        {"title":"Ligue de Tir à l'Arc","subtitle":"Languedoc-Roussillon","lat":"43.606953","long":"3.923194" }]
*/


        mMap = map;
       // mMap.setLocationSource(followMeLocationSource);
        mMap.setMyLocationEnabled(true);
        mMap.setOnMarkerClickListener(this);


        addLieuxToMap(map,this.getActivity().getApplicationContext());





    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }


    private static String getJSONString(Context context)
    {
        String str = "";
        try
        {
            AssetManager assetManager = context.getAssets();
            InputStream in = assetManager.open("lieux.json");
            InputStreamReader isr = new InputStreamReader(in);
            char [] inputBuffer = new char[100];

            int charRead;
            while((charRead = isr.read(inputBuffer))>0)
            {
                String readString = String.copyValueOf(inputBuffer,0,charRead);
                str += readString;
            }
        }
        catch(IOException ioe)
        {
            ioe.printStackTrace();
        }

        return str;
    }

//this.getActivity().getApplicationContext()
private static JSONArray parseLieuxJSON(Context context)
    {
        JSONArray json = new JSONArray();

        try {
            json = new JSONArray(getJSONString(context));
        } catch (JSONException e) {
            e.printStackTrace();
        }

       return json;
    }




    public static void addLieuxToMap(GoogleMap map,Context context) {

      /*  [{"title":"Terrain","subtitle":"ALV-MMM","lat":"43.581268","long":"3.838444" },
        {"title":"Salle","subtitle":"ALV-MMM","lat":"43.567051","long":"3.898359" },
        {"title":"Complexe Courtoujours","subtitle":"ALV-MMM","lat":"43.559447","long":"3.880295" },
        {"title":"Comité départemental de Tir à l'Arc","subtitle":"Hérault","lat":"43.625852","long":"3.824049" },
        {"title":"Ligue de Tir à l'Arc","subtitle":"Languedoc-Roussillon","lat":"43.606953","long":"3.923194" }]
*/


        LatLng salle = new LatLng(43.567051, 3.898359);

        map.setMyLocationEnabled(true);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(salle, 13));



        JSONArray json = parseLieuxJSON(context);

        for(int i=0; i<json.length(); i++) {

            try {
                JSONObject json_data = json.getJSONObject(i);
                double lat = json_data.getDouble("lat");
                double longi = json_data.getDouble("long");
                LatLng position = new LatLng(lat, longi);

                String title =json_data.getString("title");
                String subtitle =json_data.getString("subtitle");
                map.addMarker(new MarkerOptions()
                        .title(title)
                        .snippet(subtitle)
                        .position(position));

            } catch (JSONException e) {
                e.printStackTrace();
            }



        }




    }



    /*
    //pour construire une route

    public String makeURL (double sourcelat, double sourcelog, double destlat, double destlog ){
        StringBuilder urlString = new StringBuilder();
        urlString.append("http://maps.googleapis.com/maps/api/directions/json");
        urlString.append("?origin=");// from
        urlString.append(Double.toString(sourcelat));
        urlString.append(",");
        urlString
                .append(Double.toString( sourcelog));
        urlString.append("&destination=");// to
        urlString
                .append(Double.toString( destlat));
        urlString.append(",");
        urlString.append(Double.toString( destlog));
        urlString.append("&sensor=false&mode=driving&alternatives=true");
        urlString.append("&key=AIzaSyDRmDobEXAPojzu17Clh97hjR677jyQnuc");
        return urlString.toString();
    }

    private List<LatLng> decodePoly(String encoded) {

        List<LatLng> poly = new ArrayList<LatLng>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng( (((double) lat / 1E5)),
                    (((double) lng / 1E5) ));
            poly.add(p);
        }

        return poly;
    }

    public void drawPath(String  result) {

        try {
            //Tranform the string into a json object
            final JSONObject json = new JSONObject(result);
            JSONArray routeArray = json.getJSONArray("routes");
            JSONObject routes = routeArray.getJSONObject(0);
            JSONObject overviewPolylines = routes.getJSONObject("overview_polyline");
            String encodedString = overviewPolylines.getString("points");
            List<LatLng> list = decodePoly(encodedString);
            Polyline line = mMap.addPolyline(new PolylineOptions()
                    .addAll(list)
                    .width(12)
                    .color(Color.parseColor("#05b1fb"))//Google maps blue color
                    .geodesic(true)
            );

          // for(int z = 0; z<list.size()-1;z++){
          //      LatLng src= list.get(z);
           //     LatLng dest= list.get(z+1);
           //     Polyline line = mMap.addPolyline(new PolylineOptions()
           //     .add(new LatLng(src.latitude, src.longitude), new LatLng(dest.latitude,   dest.longitude))
                .width(2)
            //    .color(Color.BLUE).geodesic(true));
          //  }

        }
        catch (JSONException e) {

        }
    }

    private class connectAsyncTask extends AsyncTask<Void, Void, String>{
        private ProgressDialog progressDialog;
        String url;
        connectAsyncTask(String urlPass){
            url = urlPass;
        }
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setMessage("Fetching route, Please wait...");
            progressDialog.setIndeterminate(true);
            progressDialog.show();
        }
        @Override
        protected String doInBackground(Void... params) {
            JSONParser jParser = new JSONParser();
            String json = jParser.getJSONFromUrl(url);
            return json;
        }
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progressDialog.hide();
            if(result!=null){
                drawPath(result);
            }
        }
    }





    public class MyOverlay extends Overlay {

        private ArrayList all_geo_points;

        public MyOverlay(ArrayList allGeoPoints) {
            super();
            this.all_geo_points = allGeoPoints;
        }

        @Override
        public boolean draw(Canvas canvas, MapView mv, boolean shadow, long when) {
            super.draw(canvas, mv, shadow);
            drawPath(mv, canvas);
            return true;
        }

        public void drawPath(MapView mv, Canvas canvas) {
            int xPrev = -1, yPrev = -1, xNow = -1, yNow = -1;
            Paint paint = new Paint();
            paint.setColor(Color.BLUE);
            paint.setStyle(Paint.Style.FILL_AND_STROKE);
            paint.setStrokeWidth(4);
            paint.setAlpha(100);
            if (all_geo_points != null)
                for (int i = 0; i < all_geo_points.size() - 4; i++) {
                    GeoPoint gp = all_geo_points.get(i);
                    Point point = new Point();
                    mv.getProjection().toPixels(gp, point);
                    xNow = point.x;
                    yNow = point.y;
                    if (xPrev != -1) {
                        canvas.drawLine(xPrev, yPrev, xNow, yNow, paint);
                    }
                    xPrev = xNow;
                    yPrev = yNow;
                }
        }
    }




    MapView mv = (MapView) findViewById(R.id.mvGoogle);
    mv.setBuiltInZoomControls(true);
    MapController mc = mv.getController();
    ArrayList all_geo_points = getDirections(17.3849, 78.4866, 28.63491, 77.22461);
    GeoPoint moveTo = all_geo_points.get(0);
    mc.animateTo(moveTo);
    mc.setZoom(12);
    mv.getOverlays().add(new MyOverlay(all_geo_points));



*/





    /* Our custom LocationSource.
  * We register this class to receive location updates from the Location Manager
  * and for that reason we need to also implement the LocationListener interface. */
    private class FollowMeLocationSource implements LocationSource, LocationListener {

        private OnLocationChangedListener mListener;
        private LocationManager locationManager;
        private final Criteria criteria = new Criteria();
        private String bestAvailableProvider;
        /* Updates are restricted to one every 10 seconds, and only when
         * movement of more than 10 meters has been detected.*/
        private final int minTime = 10000;     // minimum time interval between location updates, in milliseconds
        private final int minDistance = 10;    // minimum distance between location updates, in meters

        private FollowMeLocationSource() {
            // Get reference to Location Manager
            locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);

            // Specify Location Provider criteria
            criteria.setAccuracy(Criteria.ACCURACY_FINE);
            criteria.setPowerRequirement(Criteria.POWER_LOW);
            criteria.setAltitudeRequired(true);
            criteria.setBearingRequired(true);
            criteria.setSpeedRequired(true);
            criteria.setCostAllowed(true);
        }

        private void getBestAvailableProvider() {
            /* The preffered way of specifying the location provider (e.g. GPS, NETWORK) to use
             * is to ask the Location Manager for the one that best satisfies our criteria.
             * By passing the 'true' boolean we ask for the best available (enabled) provider. */
            bestAvailableProvider = locationManager.getBestProvider(criteria, true);
        }

        /* Activates this provider. This provider will notify the supplied listener
         * periodically, until you call deactivate().
         * This method is automatically invoked by enabling my-location layer. */
        @Override
        public void activate(OnLocationChangedListener listener) {
            // We need to keep a reference to my-location layer's listener so we can push forward
            // location updates to it when we receive them from Location Manager.
            mListener = listener;

            // Request location updates from Location Manager
            if (bestAvailableProvider != null) {
                locationManager.requestLocationUpdates(bestAvailableProvider, minTime, minDistance, this);

                Location location = locationManager.getLastKnownLocation(bestAvailableProvider);
                if (location != null) {
                    LatLng locationcourante = new LatLng(location.getLatitude(), location.getLongitude());

                    if (mMap != null) {
                        mMap.addMarker(new MarkerOptions()
                                .title("")
                                .snippet("")
                                .position(locationcourante));
                    }
                }else{
                     locationcourante = new LatLng(50.0, 50.0);

                    if (mMap != null) {
                        mMap.addMarker(new MarkerOptions()
                                .title("test")
                                .snippet("pas de gps")
                                .position(locationcourante));
                    }
                }

            } else {
                // (Display a message/dialog) No Location Providers currently available.
            }
        }

        /* Deactivates this provider.
         * This method is automatically invoked by disabling my-location layer. */
        @Override
        public void deactivate() {
            // Remove location updates from Location Manager
            locationManager.removeUpdates(this);

            mListener = null;
        }

        @Override
        public void onLocationChanged(Location location) {
            /* Push location updates to the registered listener..
             * (this ensures that my-location layer will set the blue dot at the new/received location) */
            if (mListener != null) {
                mListener.onLocationChanged(location);
            }

            /* ..and Animate camera to center on that location !
             * (the reason for we created this custom Location Source !) */
            // mMap.animateCamera(CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(), location.getLongitude())));
             locationcourante = new LatLng(location.getLatitude(), location.getLongitude());
            if (mMap != null) {
                mMap.addMarker(new MarkerOptions()
                        .title("")
                        .snippet("")
                        .position(locationcourante));



            }

        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    }



}
