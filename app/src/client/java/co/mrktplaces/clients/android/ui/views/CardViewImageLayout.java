package co.mrktplaces.clients.android.ui.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by ugar on 15.02.16.
 */
public class CardViewImageLayout extends ImageView {

    public CardViewImageLayout(Context context) {
        super(context);
    }

    public CardViewImageLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CardViewImageLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        float scale = 560 / 999f;
        int heightPixels = (int) (View.MeasureSpec.getSize(widthMeasureSpec) * scale);
        int heightMode = View.MeasureSpec.getMode(heightMeasureSpec);
        int newHeightSpec = View.MeasureSpec.makeMeasureSpec(heightPixels, heightMode);
        super.onMeasure(widthMeasureSpec, newHeightSpec);
    }
}
