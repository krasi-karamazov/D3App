package kpk.dev.d3app.widgets;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import kpk.dev.d3app.util.Utils;

/**
 * Created with IntelliJ IDEA.
 * User: kpkdev
 * Date: 3/17/13
 * Time: 10:43 PM
 * To change this template use File | Settings | File Templates.
 */
public class GemToolTipWidget extends LinearLayout {

    public GemToolTipWidget(Context context, Drawable gemImage, String stats) {
        super(context);
        initGemWidget(gemImage, stats);
    }

    private void initGemWidget(Drawable gemImage, String stats){
        this.setOrientation(LinearLayout.HORIZONTAL);
        int pixels = Utils.getPixelsForDisplayScale(getContext(), 20);
        ImageView gemImageView = new ImageView(getContext());
        gemImageView.setLayoutParams(new LayoutParams(pixels, pixels));
        gemImageView.setImageDrawable(gemImage);

        TextView label = new TextView(getContext());
        label.setText(stats);

        addView(gemImageView);
        addView(label);
    }
}
