package osorio.co.fundacionhuellas;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

public class myScrollView extends ScrollView {
    private boolean enableScrolling = true;

    public myScrollView(Context context) {
        super(context);
    }

    public myScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public myScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public myScrollView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
}
