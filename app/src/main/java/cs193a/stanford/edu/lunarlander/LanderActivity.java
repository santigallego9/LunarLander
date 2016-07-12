/*
 * CS 193A, Marty Stepp
 * This activity is just a container that displays our lunar lander game.
 */

package cs193a.stanford.edu.lunarlander;

import android.os.Bundle;
import android.view.View;
import stanford.androidlib.*;

public class LanderActivity extends SimpleActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lander);
    }
}
