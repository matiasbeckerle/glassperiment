package com.example.glassperiment;

import java.util.concurrent.TimeUnit;

import android.content.Context;
import android.graphics.Canvas;
import android.os.SystemClock;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.View;

public class HeadlineDrawer implements SurfaceHolder.Callback {

	// The duration, in milliseconds, of one frame
    private static final long FRAME_TIME_MILLIS = TimeUnit.SECONDS.toMillis(2);
    
    private RenderThread mRenderThread;
	private SurfaceHolder mHolder;
    private HeadlineView mView;
    
    private HeadlineView.ChangeListener mListener = new HeadlineView.ChangeListener() {
        @Override
        public void onChange() {
            if (mHolder != null) {
                draw();
            }
        }
    };
    
	public HeadlineDrawer(Context context) {
		mView = new HeadlineView(context);
        mView.setListener(mListener);
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		// Measure and layout the view with the canvas dimensions.
        int measuredWidth = View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY);
        int measuredHeight = View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY);

        mView.measure(measuredWidth, measuredHeight);
        mView.layout(0, 0, mView.getMeasuredWidth(), mView.getMeasuredHeight());
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		mHolder = holder;

        mRenderThread = new RenderThread();
        mRenderThread.start();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		mHolder = null;
		mRenderThread.quit();
	}
	
	public void draw() {
        Canvas canvas;
        
        try {
            canvas = mHolder.lockCanvas();
        } catch (Exception e) {
            return;
        }
        
        if (canvas != null) {
            mView.draw(canvas);
            mHolder.unlockCanvasAndPost(canvas);
        }
    }
	
	
	
	/**
     * Redraws the headline in the background.
     */
    private class RenderThread extends Thread {
        private boolean mShouldRun;

        /**
         * Initializes the background rendering thread.
         */
        public RenderThread() {
            mShouldRun = true;
        }

        /**
         * Returns true if the rendering thread should continue to run.
         *
         * @return true if the rendering thread should continue to run
         */
        private synchronized boolean shouldRun() {
            return mShouldRun;
        }

        /**
         * Requests that the rendering thread exit at the next opportunity.
         */
        public synchronized void quit() {
            mShouldRun = false;
        }

        @Override
        public void run() {
            while (shouldRun()) {
            	long frameStart = SystemClock.elapsedRealtime();
                long frameLength = SystemClock.elapsedRealtime() - frameStart;
                
                //Log.v("test", "Glassperimenting..." + String.valueOf(frameStart));
                mView.updateText("Making Sense", "Glassperimenting... " + String.valueOf(frameStart), "a few seconds ago");
                
                long sleepTime = FRAME_TIME_MILLIS - frameLength;
                if (sleepTime > 0) {
                    SystemClock.sleep(sleepTime);
                }
            }
        }
    }
	
}