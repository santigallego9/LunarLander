/*
 * CS 193A, Marty Stepp
 * This activity is just a container that displays our lunar lander game.
 */

package cs193a.stanford.edu.lunarlander;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import stanford.androidlib.*;

public class LanderActivity extends SimpleActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /*this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);      //Remove notification bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);       //Remove title bar*/
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lander);
    }

    public void startClick(View view) {
        //final LanderView faceView = find(R.id.class);
        LanderView canvas = (LanderView) findViewById(R.id.playingCanvas);
        canvas.startGame();
    }

    public void stopClick(View view) {
        LanderView canvas = (LanderView) findViewById(R.id.playingCanvas);
        canvas.stopGame();
    }
}
