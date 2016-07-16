/*
 * CS 193A, Santi Gallego
 * This view draws the main graphics of the Lunar Lander game.
 */

package cs193a.stanford.edu.lunarlander;

import android.content.Context;
import android.graphics.*;
import android.os.CountDownTimer;
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
    private ArrayList<Bitmap> rocketImages;
    private ArrayList<Bitmap> thrustImages;
    private ArrayList<Bitmap> asteroidImages;
    private ArrayList<GSprite> allAsteroids = new ArrayList<>();
    public int asteroidRate;
    private int score;
    private GLabel result, pause, scoreLabel;
    private boolean isPaused;

    /*
     * Required auto-generated constructor.
     */
    public LanderView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void init() {
        // TODO

        pause = new GLabel(R.string.paused);
        result = new GLabel(R.string.final_score + score);
        result.setX((getWidth() - result.getWidth()) / 2);
        result.setY(getHeight() / 3);
        result.setColor(GColor.BLACK);
        result.setFontSize(100);
        add(result);
        result.setX((getWidth() - result.getWidth()) / 2);

        Log.d("WIDTH", "start: " + result.getWidth());

        scoreLabel = new GLabel(R.string.score + score);
        scoreLabel.setColor(GColor.WHITE);
        scoreLabel.setFontSize(70);
        scoreLabel.setX(getWidth() - scoreLabel.getWidth() - 10);
        scoreLabel.setY(10);
        add(scoreLabel);
        score = 0;

        populateAsteroidRotation();

        setBackgroundColor(GColor.BLACK);
        thrustImages = new ArrayList<>();
        thrustImages.add(resizeBitmap(R.drawable.rocketshipthrust1, 6));
        thrustImages.add(resizeBitmap(R.drawable.rocketshipthrust2, 6));
        thrustImages.add(resizeBitmap(R.drawable.rocketshipthrust3, 6));
        thrustImages.add(resizeBitmap(R.drawable.rocketshipthrust4, 6));

        rocketImages = new ArrayList<>();
        rocketImages.add(resizeBitmap(R.drawable.rocketship1, 6));

        GSprite.setDebug(false);

        rocket = new GSprite(rocketImages, 100, 20);
        rocket.setFramesPerBitmap(3);
        //rocket = new GSprite(rocketImage, 50, 50);
        rocket.setVelocityY(2);
        rocket.setCollisionMargin(30);
        rocket.setAccelerationY(GRAVITY_ACCELERATION);

        moonSurface = new GSprite(loadScreenWideBitmap(R.drawable.moonsurface));
        moonSurface.setBottomY(getHeight());
        moonSurface.setCollisionMarginTop(30);
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

        if(score < 30) {
            asteroidRate = 30;
        } else if(score >= 30 && score < 60) {
            asteroidRate = 20;
        } else if(score >= 60 && score < 200) {
            asteroidRate = 10;
        } else {
            asteroidRate = 5;
        }


        frames++;
        if(frames % asteroidRate == 0) {
            //add an asteroid
            addAsteroid();
            score++;
            scoreLabel.setText(R.string.score + score);
            scoreLabel.setX(getWidth() - scoreLabel.getWidth() - 10);
            result.setText(R.string.score + score);
            result.setX((getWidth() - result.getWidth()) / 2);
        }

        doCollisions();
    }

    public void addAsteroid() {
        GSprite asteroid = new GSprite(asteroidImages);
        asteroid.setRightX(getWidth());
        float y = RandomGenerator.getInstance().nextFloat(getHeight() - moonSurface.getHeight());
        float yVelocity = RandomGenerator.getInstance().nextFloat(10);
        asteroid.setY(y);
        if(RandomGenerator.getInstance().nextInt(2) == 0) {
            yVelocity = -yVelocity;
        }
        asteroid.setVelocity(-20, yVelocity);
        asteroid.setCollisionMargin(10);
        rocket.setFramesPerBitmap(4);
        add(asteroid);
        allAsteroids.add(asteroid);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            // user pressed the screen
            rocket.setAccelerationY(THRUST_ACCELERATION);
            rocket.setBitmaps(thrustImages);
            rocket.setFramesPerBitmap(5);
            rocket.setCollisionMargin(50);
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            // user let go of the screen
            rocket.setAccelerationY(GRAVITY_ACCELERATION);
            rocket.setBitmaps(rocketImages);
            rocket.setCollisionMargin(50);
        }

        return super.onTouch(v, event);
    }

    public void doCollisions() {
        if(rocket.collidesWith(moonSurface)) {
            if(MAX_SAFE_LANDING_VELOCITY > rocket.getVelocityY()) {
                gameOver();
            } else {
                gameOver();
            }
        } else {
            for(GSprite asteroid : allAsteroids) {
                if(rocket.collidesWith(asteroid)) {
                    gameOver();
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



    private Bitmap resizeBitmap(int id, int factor) {
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
        result.setColor(GColor.BLACK);
        result.sendToFront();
        isPaused = false;
        score = 0;
        rocket.setLocation(100, 20);
        for(GSprite asteroid: allAsteroids) {
            remove(asteroid);
        }
        allAsteroids.clear();
        animate(30);
    }

    public void gameOver() {

        stopGame();

        Log.d("WIDTH", "gameOver: " + result.getWidth());

        result.setColor(GColor.WHITE);

        rocket.setVelocityY(0);
        rocket.setAccelerationY(GRAVITY_ACCELERATION);
    }

    public void stopGame() {
        animationStop();
    }

    public void pauseGame() {
        animationStop();
        isPaused = true;

        pause.setColor(GColor.WHITE);
        pause.setFontSize(70);
        pause.setLocation((getWidth() - pause.getWidth()) / 2, getHeight() / 2);
        add(pause);
    }

    public void resumeGame() {
        remove(pause);
        final GLabel resume = new GLabel("3");
        resume.setColor(GColor.WHITE);
        resume.setFontSize(70);
        resume.setLocation((getWidth() - resume.getWidth()) / 2, getHeight() / 2);
        add(resume);

        new CountDownTimer(3000, 1000) {

            public void onTick(long millisUntilFinished) {
                resume.setText("" + (int) millisUntilFinished / 1000);
            }

            public void onFinish() {
                remove(resume);
                animationResume();
                isPaused = false;
            }
        }.start();
    }

    public boolean isPaused() {
        return isPaused;
    }

    public void populateAsteroidRotation() {
        asteroidImages = new ArrayList<>();
        asteroidImages.add(resizeBitmap(R.drawable.asteroid2rot000, 8));
        asteroidImages.add(resizeBitmap(R.drawable.asteroid2rot030, 8));
        asteroidImages.add(resizeBitmap(R.drawable.asteroid2rot060, 8));
        asteroidImages.add(resizeBitmap(R.drawable.asteroid2rot090, 8));
        asteroidImages.add(resizeBitmap(R.drawable.asteroid2rot120, 8));
        asteroidImages.add(resizeBitmap(R.drawable.asteroid2rot150, 8));
        asteroidImages.add(resizeBitmap(R.drawable.asteroid2rot180, 8));
        asteroidImages.add(resizeBitmap(R.drawable.asteroid2rot210, 8));
        asteroidImages.add(resizeBitmap(R.drawable.asteroid2rot240, 8));
        asteroidImages.add(resizeBitmap(R.drawable.asteroid2rot270, 8));
        asteroidImages.add(resizeBitmap(R.drawable.asteroid2rot300, 8));
        asteroidImages.add(resizeBitmap(R.drawable.asteroid2rot330, 8));
    }
}
