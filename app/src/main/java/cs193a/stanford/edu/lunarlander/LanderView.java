/*
 * CS 193A, Santi Gallego
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
    private GSprite moonSurface;
    int frames = 0, asteroids = 0;
    private Bitmap rocketImage;
    private ArrayList<Bitmap> rocketPics;
    private ArrayList<GSprite> allAsteroids = new ArrayList<>();
    private GLabel result = new GLabel();

    /*
     * Required auto-generated constructor.
     */
    public LanderView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void init() {
        // TODO
        result.setVisible(false);
        add(result);

        setBackgroundColor(GColor.BLACK);
        rocketPics = new ArrayList<>();
        rocketPics.add(loadScaledBitmap(R.drawable.rocketshipthrust1, 4));
        rocketPics.add(loadScaledBitmap(R.drawable.rocketshipthrust2, 4));
        rocketPics.add(loadScaledBitmap(R.drawable.rocketshipthrust3, 4));
        rocketPics.add(loadScaledBitmap(R.drawable.rocketshipthrust4, 4));

        Bitmap rocketImage = loadScaledBitmap(R.drawable.rocketship1, 4);

        GSprite.setDebug(false);

        rocket = new GSprite(rocketPics, 50, 10);
        rocket.setFramesPerBitmap(3);
        //rocket = new GSprite(rocketImage, 50, 50);
        rocket.setVelocityY(2);
        rocket.setCollisionMargin(20);
        rocket.setAccelerationY(GRAVITY_ACCELERATION);

        moonSurface = new GSprite(loadScreenWideBitmap(R.drawable.moonsurface));
        moonSurface.setBottomY(getHeight());
        moonSurface.setCollisionMarginTop(50);
        add(moonSurface);
        add(rocket);
    }

    @Override
    public void onAnimateTick() {
        super.onAnimateTick();

        for(GSprite asteroid : allAsteroids) {
            if (!isObjectInBounds(asteroid)) {
                remove(asteroid);
            }
        }


        frames++;
        if(frames % 30 == 0) {
            //add an asteroid
            GSprite asteroid = new GSprite(loadScaledBitmap(R.drawable.asteroid2, 8));
            asteroid.setRightX(getWidth());
            float y = RandomGenerator.getInstance().nextFloat(getHeight() - moonSurface.getHeight());
            asteroid.setY(y);
            asteroid.setVelocityX(-12);
            asteroid.setCollisionMargin(10);
            add(asteroid);
            allAsteroids.add(asteroid);
        }

        doCollisions();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            // user pressed the screen
            rocket.setAccelerationY(THRUST_ACCELERATION);
            //rocket.setBitmaps(rocketPics);
            //rocket.setFramesPerBitmap(5);
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            // user let go of the screen
            rocket.setAccelerationY(GRAVITY_ACCELERATION);
            //rocket.setBitmap(rocketImage);
        }

        return super.onTouch(v, event);
    }

    public void doCollisions() {
        if(rocket.collidesWith(moonSurface)) {
            if(MAX_SAFE_LANDING_VELOCITY > rocket.getVelocityY()) {
                result.setText("YOU WIN!!!");
                result.setLocation(getWidth() / 2 - result.getWidth() / 2, getHeight() / 3 * 2);
                result.setColor(GColor.WHITE);
                result.setVisible(true);
                result.sendToFront();
                stopGame();
            } else {
                result.setText("Game over...");
                result.setLocation(getWidth() / 2 - result.getWidth() / 2, getHeight() / 3 * 2);
                result.setColor(GColor.WHITE);
                result.setVisible(true);
                result.sendToFront();
                stopGame();
            }
        } else {
            for(GSprite asteroid : allAsteroids) {
                if(rocket.collidesWith(asteroid)) {
                    result.setText("Game over...");
                    result.setLocation(getWidth() / 2 - result.getWidth() / 2, getHeight() / 3 * 2);
                    result.setColor(GColor.WHITE);
                    result.setVisible(true);
                    result.sendToFront();
                    stopGame();
                }
            }
        }
    }

    public boolean isObjectInBounds(GObject obj) {
        float x = obj.getX();
        float y = obj.getY();

        if(x > getWidth() || x < 0 - obj.getWidth()) {
            return false;
        } else if(y > getHeight() || y < 0 - obj.getHeight()) {
            return false;
        } else {
            return true;
        }
    }



    private Bitmap loadScaledBitmap(int id, int factor) {
        Bitmap image = BitmapFactory.decodeResource(getResources(), id);

        image = Bitmap.createScaledBitmap(image, image.getWidth() / factor,
                image.getHeight() / factor, true);

        return image;
    }

    private Bitmap loadScreenWideBitmap(int id) {
        Bitmap image = BitmapFactory.decodeResource(getResources(), id);

        int width = getWidth();
        int factor = image.getWidth() / getWidth();

        image = image.createScaledBitmap(image, width, image.getHeight() / factor, true);
        return image;
    }

    public void startGame() {
        result.sendToBack();
        result.setVisible(false);
        rocket.setLocation(50, 10);
        for(GSprite asteroid: allAsteroids) {
            remove(asteroid);
        }
        allAsteroids.clear();
        animate(30);
    }

    public void stopGame() {
        rocket.setVelocityY(0);
        rocket.setAccelerationY(GRAVITY_ACCELERATION);
        animationStop();

    }
}
