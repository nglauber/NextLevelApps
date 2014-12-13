package br.com.nglauber.nextlevelapps;

import android.content.Context;
import android.graphics.Typeface;
import android.support.wearable.view.CircledImageView;
import android.support.wearable.view.WearableListView;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ListItem extends LinearLayout
        implements WearableListView.OnCenterProximityListener {

    private final float SELECTED_CIRCLE_RADIUS;
    private final float UNSELECTED_CIRCLE_RADIUS;

    private final int SELECTED_CIRCLE_COLOR;
    private final int UNSELECTED_CIRCLE_COLOR;

    private CircledImageView mCircle;
    private TextView mName;


    public ListItem(Context context) {
        this(context, null);
    }

    public ListItem(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ListItem(Context context, AttributeSet attrs,
                                  int defStyle) {
        super(context, attrs, defStyle);
        SELECTED_CIRCLE_COLOR = getResources().getColor(R.color.circle_selected);
        UNSELECTED_CIRCLE_COLOR = getResources().getColor(R.color.circle_unselected);
        SELECTED_CIRCLE_RADIUS = getResources().getDimension(R.dimen.selected_circle_radius);
        UNSELECTED_CIRCLE_RADIUS = getResources().getDimension(R.dimen.unselected_circle_radius);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mCircle = (CircledImageView) findViewById(R.id.circle);
        mName = (TextView) findViewById(R.id.name);
    }

    @Override
    public void onCenterPosition(boolean b) {
        mName.setAlpha(1f);
        mName.setTypeface(null, Typeface.BOLD);

        mCircle.setCircleColor(SELECTED_CIRCLE_COLOR);
        mCircle.setCircleRadius(SELECTED_CIRCLE_RADIUS);
    }

    @Override
    public void onNonCenterPosition(boolean b) {
        mName.setAlpha(0.6f);
        mName.setTypeface(null, Typeface.NORMAL);

        mCircle.setCircleColor(UNSELECTED_CIRCLE_COLOR);
        mCircle.setCircleRadius(UNSELECTED_CIRCLE_RADIUS);

    }
}