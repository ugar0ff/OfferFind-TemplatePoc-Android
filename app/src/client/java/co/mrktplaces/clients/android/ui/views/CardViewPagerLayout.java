package co.mrktplaces.clients.android.ui.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

/**
 * Created by ugar on 15.02.16.
 */
public class CardViewPagerLayout extends RelativeLayout {

    public CardViewPagerLayout(Context context) {
        super(context);
    }

    public CardViewPagerLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CardViewPagerLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        float scale = 1140 / 1080f;
        int heightPixels = (int) (View.MeasureSpec.getSize(widthMeasureSpec) * scale);
        int heightMode = View.MeasureSpec.getMode(heightMeasureSpec);
        int newHeightSpec = View.MeasureSpec.makeMeasureSpec(heightPixels, heightMode);
        super.onMeasure(widthMeasureSpec, newHeightSpec);
    }
}
