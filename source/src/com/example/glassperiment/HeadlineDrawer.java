package com.example.glassperiment;

import java.util.concurrent.TimeUnit;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Canvas;
import android.os.SystemClock;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.View;

public class HeadlineDrawer implements SurfaceHolder.Callback {

	// The duration, in milliseconds, of one frame
    private static final long FRAME_TIME_MILLIS = TimeUnit.SECONDS.toMillis(3);
    
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
        	int position = 0;
        	int maxPosition = 0;
        	JSONArray headlines = new JSONArray();
        	
        	try {
            	headlines = new JSONArray("[{\"source\":\"Nulla lobortis\",\"description\":\"Lorem ipsum dolor sit amet, consectetur adipiscing elit.\",\"moment\":\"ac pulvinar sapien\"},{\"source\":\"Donec\",\"description\":\"Morbi malesuada commodo magna\",\"moment\":\"eleifend ac\"},{\"source\":\"Aliquam sagittis velit\",\"description\":\"Etiam non diam vitae mauris vulputate ultrices\",\"moment\":\"commodo sollicitudin\"},{\"source\":\"Aliquam sagittis velit\",\"description\":\"Praesent tempor ipsum et\",\"moment\":\"commodo sollicitudin\"},{\"source\":\"Donec\",\"description\":\"Sed malesuada orci neque, sed laoreet\",\"moment\":\"ac pulvinar sapien\"}]");
            	maxPosition = headlines.length() - 1;
        	} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	
            while (shouldRun()) {
            	long frameStart = SystemClock.elapsedRealtime();
                long frameLength = SystemClock.elapsedRealtime() - frameStart;

                try {
					JSONObject headline = headlines.getJSONObject(position);
					Log.v("test", headline.getString("description"));
					mView.updateText(
							headline.getString("source"),
							headline.getString("description"),
							headline.getString("moment"));
					 
	                position = position == maxPosition ? 0 : ++position;
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
                
                long sleepTime = FRAME_TIME_MILLIS - frameLength;
                if (sleepTime > 0) {
                    SystemClock.sleep(sleepTime);
                }
            }
        }
    }
	
}