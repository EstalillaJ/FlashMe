package com.cs380.flashme.flashme.Util;

/**
 * Created by josh on 5/1/16.
 */
import android.os.Handler;

import com.dd.processbutton.ProcessButton;

import java.util.Random;

public class ProgressGenerator {

    public interface OnCompleteListener {

        public void onComplete();
    }

    private OnCompleteListener mListener;
    private int mProgress;

    public ProgressGenerator(OnCompleteListener listener) {
        mListener = listener;
    }

    public void start(final ProcessButton button) {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mProgress += 10;
                if(mProgress < 90 ){
                    button.setProgress(mProgress);
                }
                if (mProgress < 100) {
                    handler.postDelayed(this, generateDelay());
                } else {
                    mListener.onComplete();
                }
                button.setProgress(-1);
            }
        }, generateDelay());
    }

    public void success(){
        mListener.onComplete();
    }

    private Random random = new Random();

    private int generateDelay() {
        return random.nextInt(1000);
    }
}