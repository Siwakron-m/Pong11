package com.example.pong;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

class PongGame extends SurfaceView implements Runnable {

    private final boolean DEBUGGING = true;
    private SurfaceHolder mOurHolder;
    private Canvas mCanvas;
    private Paint mPaint;
    private long mFPS;
    private final int MILLIS_IN_SECOND = 1000;
    private int mScreenX;
    private int mScreenY;
    private int mFontSize;
    private int mFontMargin;
    private Bat mBat;
    private Ball mBall;
    private int mScore;
    private int mLives;
    private Thread mGameThread = null;
    private volatile boolean mPlaying;
    private boolean mPaused = true;


    public PongGame(Context context, int x, int y) {
        super(context);

        // Initialize these two members/fields
// With the values passed in as parameters
        mScreenX = x;
        mScreenY = y;
// Font is 5% (1/20th) of screen width
        mFontSize = mScreenX / 20;
// Margin is 1.5% (1/75th) of screen width
        mFontMargin = mScreenX / 75;
// Initialize the objects
// ready for drawing with
// getHolder is a method of SurfaceView
        mOurHolder = getHolder();
        mPaint = new Paint();
// Initialize the bat and ball
// Everything is ready so start the game
        startNewGame();
    }

    private void startNewGame() {

        mScore = 0;
        mLives = 3;

    }

    private void draw() {
        if (mOurHolder.getSurface().isValid()) {
            mCanvas = mOurHolder.lockCanvas(); // Lock the canvas (graphics memory)
            mCanvas.drawColor(Color.argb(255, 26, 128, 182));
            mPaint.setColor(Color.argb(255, 255, 255, 255));
            mPaint.setTextSize(mFontSize);
            mCanvas.drawText("Score: " + mScore + " Lives: " + mLives,

                    mFontMargin, mFontSize, mPaint);

            if (DEBUGGING) {
                printDebuggingText();
            }
            mOurHolder.unlockCanvasAndPost(mCanvas);
        }
    }


    private void printDebuggingText() {
        int debugSize = mFontSize / 2;
        int debugStart = 150;
        mPaint.setTextSize(debugSize);
        mCanvas.drawText("FPS: " + mFPS,

                10, debugStart + debugSize, mPaint);
    }

    @Override
    public void run() {

        while (mPlaying) {

            long frameStartTime = System.currentTimeMillis();

            if (!mPaused) {
                update(); // update new positions
                detectCollisions(); // detect collisions
            }

            draw();
            long timeThisFrame = System.currentTimeMillis() - frameStartTime;
            if (timeThisFrame > 0) {
                mFPS = MILLIS_IN_SECOND / timeThisFrame;

            }
        }
    }

    private void update() {
    }

    private void detectCollisions() {
    }

    public void pause() {

// Set mPlaying to false. Stopping the thread isn’t always instant
        mPlaying = false;
        try {

            mGameThread.join();

        } catch (InterruptedException e) {

            Log.e("Error:", "joining thread");

        }
    }
    public void resume() {
        mPlaying = true;
// Initialize the instance of Thread
        mGameThread = new Thread(this);
// Start the thread
        mGameThread.start();

    }

    
}

