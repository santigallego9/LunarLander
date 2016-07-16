/*
 * CS 193A, Marty Stepp
 * This activity is just a container that displays our lunar lander game.
 */

package cs193a.stanford.edu.lunarlander;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import stanford.androidlib.*;

public class LanderActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {      //Remove title bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //setFullScreenMode(true);
        //getActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lander);

        LanderView canvas = (LanderView) findViewById(R.id.playingCanvas);


    }

    public void startClick(View view) {
        //final LanderView faceView = find(R.id.class);
        LanderView canvas = (LanderView) findViewById(R.id.playingCanvas);
        canvas.startGame();
    }

    public void stopClick(View view) {
        LanderView canvas = (LanderView) findViewById(R.id.playingCanvas);
        canvas.gameOver();
    }

    public void pauseClick(View view) {
        LanderView canvas = (LanderView) findViewById(R.id.playingCanvas);
        Button pause = (Button) findViewById(R.id.pauseButton);

        /*if(canvas.isPaused()) {
            canvas.resumeGame();
            pause.setText("Pause");
        } else {
            canvas.pauseGame();
            pause.setText("Resume");
        }*/
    }
}
