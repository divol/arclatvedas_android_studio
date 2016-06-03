package com.alv.app;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.SimpleTimeZone;
import java.util.TimeZone;

import android.os.SystemClock;
import android.widget.Chronometer;
import android.widget.TextView;





/**
 * https://github.com/retrohacker/Chronometer
 * Created by divol on 02/06/16.
 */
public class ChronoOnTick implements Chronometer.OnChronometerTickListener {

    public interface timedAction {
        /**
         * Called when a view has been clicked.
         *
         * @param etape
         */
        public void todonext(int etape);
        public void drawcurcolor();
    }

public timedAction delegate;
    TextView clockFace;			// The textview where the current time will be shown
    public int acount;
    public int etape;
    public boolean running;
    Date time;					//The time since this clock was started
    SimpleDateFormat timeFormat;//The format that will be applied when converting the time to a string

    // The amount of time between the time this counter is counting up from
    // and the Unix Epoch Time
    long epochTime;

    /**
     * @param clockFace
     * The TextView that will be updated every Tick of the clock.
     * @param format
     * The format to print the current time in. Supports the same formats that SimpleDateFormat supports.
     */
    public ChronoOnTick(TextView clockFace,String format) {
        this.clockFace = clockFace;

        resetIt(10,1,false);
    }

    public void onChronometerTick(Chronometer chronometer) {
        delegate.drawcurcolor();

        acount--;
        String str = Integer.toString(acount);

        clockFace.setText(str);

        if (etape == 1) {
            if (acount == 0) {
                etape++;
                delegate.todonext(etape);
            }
        }else {
            if (etape == 2) {
                if (acount == 30) {
                    etape++;
                    delegate.todonext(etape);
                }
            }else{

                if (acount == 0) {
                    etape++;
                    delegate.todonext(etape);
                }
            }
        }
    }




    public void resetIt(int acount, int etape,boolean running) {

        this.acount = acount;
        this.etape = etape;
        this.running = running;
       // clockFace.setText(""+acount);

    }
}
