package co.mrktplaces.android.ui.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;

import co.mrktplaces.android.ui.activity.MapsActivity;

/**
 * Created by ugar on 25.04.16.
 */
public class TouchableWrapper extends FrameLayout {

    private MapsActivity.TouchEvent touchEvent;

    public TouchableWrapper(Context context) {
        super(context);
        init();
    }

    public TouchableWrapper(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TouchableWrapper(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        touchEvent.eventTouch(ev);
        return super.dispatchTouchEvent(ev);
    }

    public void setTouchEventListener(MapsActivity.TouchEvent touchEvent) {
        this.touchEvent = touchEvent;
    }
}
