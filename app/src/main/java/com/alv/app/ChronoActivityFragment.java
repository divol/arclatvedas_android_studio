package com.alv.app;

import android.app.Activity;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

/**
 * A placeholder fragment containing a simple view.
 * https://github.com/retrohacker/Chronometer

 */
public class ChronoActivityFragment extends Fragment implements View.OnClickListener ,CompoundButton.OnCheckedChangeListener,ChronoOnTick.timedAction{

    final static int mredColor = Color.argb(200,255,0,0);
    final static int mgreenColor = Color.argb(200,0,255,0);
    final static int myellowColor = Color.argb(200,255,255,0);
    Typeface lcdFont;
    Chronometer chron;
    ChronoOnTick chronInterface;
    TextView clockFace;
    Button startstopbut;
    View rootView;
     MediaPlayer bip ;
     MediaPlayer bip2 ;
     MediaPlayer bip3 ;
    int maxcount = 120;

    int curcolor=mredColor;
    public ChronoActivityFragment() {
    }




    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment LieuxFragment.
     */
    public static ChronoActivityFragment newInstance() {
        ChronoActivityFragment fragment = new ChronoActivityFragment();
        return fragment;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

         rootView = inflater.inflate(R.layout.fragment_chrono, container, false);


         chron = (Chronometer) rootView.findViewById(R.id.chronometer);
         clockFace = (TextView) rootView.findViewById(R.id.clock);

        clockFace.setText("10");

        Switch timeswitch = (Switch)rootView.findViewById(R.id.switch1);
        timeswitch.setOnCheckedChangeListener(this);


         startstopbut = (Button)rootView.findViewById(R.id.startbutton);
        startstopbut.setOnClickListener(this);


        //http://www.101apps.co.za/index.php/articles/using-custom-fonts-in-your-android-apps.html
        // lcdFont = Typeface.createFromAsset(inflater.getContext().getAssets(),"fonts/DBLCDTemp-Black.ttf");
        clockFace.setTextSize(120.0f);

        clockFace.setTypeface(lcdFont);
        clockFace.setTextColor(mredColor);
        curcolor=mredColor;
        rootView.setBackgroundColor(mredColor);
        //clockFace.setBackgroundColor(Color.BLACK);
        // Create an object for interfacing with the Chronometer
         chronInterface = new ChronoOnTick(clockFace, "SSS");
        chronInterface.delegate = this;
        chron.setOnChronometerTickListener(chronInterface);

         bip = MediaPlayer.create(rootView.getContext(), R.raw.bip);
         bip2 = MediaPlayer.create(rootView.getContext(), R.raw.bip2);
         bip3 = MediaPlayer.create(rootView.getContext(), R.raw.bip3);



        return rootView;

    }


    public void onClick(View view) {


          switch (view.getId()) {
              case R.id.startbutton:
                  if (!chronInterface.running) {
                      chronInterface.resetIt(11, 1, true);
                      bip.start();
                      chron.start();
                      rootView.setBackgroundColor(mredColor);
                      curcolor=mredColor;
                      startstopbut.setText("Stop");
                  }else{
                      bip3.start();
                      chron.stop();
                      chronInterface.resetIt(11, 1, false);
                      clockFace.setText("10");
                      rootView.setBackgroundColor(mredColor);
                      curcolor=mredColor;
                      startstopbut.setText("Début");
                  }
                  break;

          }
    }



    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            maxcount = 240;
        } else {
            maxcount = 120;
        }
    }


    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        lcdFont = Typeface.createFromAsset(activity.getApplicationContext().getAssets(),"fonts/DBLCDTemp-Black.ttf");
    }

    public void drawcurcolor(){

        Drawable background = rootView.getBackground();

        if (background instanceof ColorDrawable){
            int color = ((ColorDrawable) background).getColor();

            if (color != curcolor){
                rootView.setBackgroundColor(curcolor);
            }
        }


    }
    public void todonext(int etape){
        if (etape == 1){


        }else {
            if (etape == 2){
                bip2.start();
                chronInterface.resetIt(maxcount, 2, true); //
               // clockFace.setText(""+40);
                curcolor=mgreenColor;


            }else {
                if (etape == 3) {
                    curcolor=myellowColor;

                } else {
                    bip3.start();
                    chron.stop();
                    chronInterface.resetIt(0, 1, false); //maxcount
                    startstopbut.setText("Début");
                    curcolor=mredColor;
                    rootView.setBackgroundColor(curcolor);
                }
            }

        }
    }
}
