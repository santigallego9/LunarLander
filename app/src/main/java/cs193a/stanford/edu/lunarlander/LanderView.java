/*
 * CS 193A, Marty Stepp
 * This view draws the main graphics of the Lunar Lander game.
 */

package cs193a.stanford.edu.lunarlander;

import android.content.Context;
import android.graphics.*;
import android.util.*;
import android.view.*;
import java.util.*;
import stanford.androidlib.graphics.*;
import stanford.androidlib.util.*;

public class LanderView extends GCanvas {

    private static final float MAX_SAFE_LANDING_VELOCITY = 7.0f;    // maximum velocity at which the rocket can hit the ground without crashing
    private static final float GRAVITY_ACCELERATION = .5f;          // downward acceleration due to gravity
    private static final float THRUST_ACCELERATION = -.3f;          // upward acceleration due to thrusters

    private GSprite rocket;

    /*
     * Required auto-generated constructor.
     */
    public LanderView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void init() {
        // TODO
        Bitmap rocketImage = BitmapFactory.decodeResource(getResources(), R.drawable.rocketship1);

        rocket = new GSprite(rocketImage, 50, 10);
        rocket.setVelocityY(5);
        add(rocket);

        animate(30);
    }

    @Override
    public void onAnimateTick() {
        super.onAnimateTick();
    }

    public void startGame() {

    }

    public void stopGame() {

    }
}
