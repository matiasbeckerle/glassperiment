package com.example.glassperiment;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.TextView;

public class HeadlineView extends FrameLayout {
	
	/**
     * Interface to listen for changes on the view layout.
     */
    public interface ChangeListener {
        /** Notified of a change in the view. */
        public void onChange();
    }
    
    private final TextView mSourceView;
    private final TextView mDescriptionView;
    private final TextView mMomentView;
    private ChangeListener mChangeListener;
    
	public HeadlineView(Context context) {
		super(context);

        LayoutInflater.from(context).inflate(R.layout.headline, this);

        mSourceView = (TextView) findViewById(R.id.source);
        mDescriptionView = (TextView) findViewById(R.id.description);
        mMomentView = (TextView) findViewById(R.id.moment);
	}
	
	/**
     * Set a {@link ChangeListener}.
     */
    public void setListener(ChangeListener listener) {
        mChangeListener = listener;
    }
	
	/**
     * Updates the displayed text with the provided values.
     * 
     * @param description the description of the headline
     */
	public void updateText(String source, String description, String moment){
		mSourceView.setText(source);
		mDescriptionView.setText(description);
		mMomentView.setText(moment);

        if (mChangeListener != null) {
            mChangeListener.onChange();
        }
	}
    
}