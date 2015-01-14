package com.mrn.customprogressbar;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;

import com.fourmob.colorpicker.ColorPickerDialog;
import com.fourmob.colorpicker.ColorPickerSwatch;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            SeekBar seekBarProgress, seekBarThickness;
            seekBarProgress = (SeekBar) rootView.findViewById(R.id.seekBar_progress);
            seekBarThickness = (SeekBar) rootView.findViewById(R.id.seekBar_thickness);
            final Button button = (Button) rootView.findViewById(R.id.button);
            final CircleProgressBar circleProgressBar = (CircleProgressBar) rootView.findViewById(R.id.custom_progressBar);
            //Using ColorPickerLibrary to pick color for our CustomProgressbar
            final ColorPickerDialog colorPickerDialog = new ColorPickerDialog();
            colorPickerDialog.initialize(
                    R.string.select_color,
                    new int[]{
                                Color.CYAN,
                                Color.DKGRAY,
                                Color.BLACK,
                                Color.BLUE,
                                Color.GREEN,
                                Color.MAGENTA,
                                Color.RED,
                                Color.GRAY,
                                Color.YELLOW},
                                Color.DKGRAY, 3, 2);

            colorPickerDialog.setOnColorSelectedListener(new ColorPickerSwatch.OnColorSelectedListener() {
                @Override
                public void onColorSelected(int color) {
                    circleProgressBar.setColor(color);
                }
            });
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    colorPickerDialog.show(getFragmentManager(), "color_picker");
                }
            });
            seekBarProgress.setProgress((int) circleProgressBar.getProgress());
            seekBarProgress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                    if(b)
                        circleProgressBar.setProgressWithAnimation(i);
                    else
                        circleProgressBar.setProgress(i);
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });
            seekBarThickness.setProgress((int) circleProgressBar.getStrokeWidth());
            seekBarThickness.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                    circleProgressBar.setStrokeWidth(i);
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });
            return rootView;
        }
    }
}
