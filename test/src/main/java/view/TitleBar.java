package view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jike.test.R;

/**
 * Created by wancc on 2016/3/25.
 */
public class TitleBar extends RelativeLayout {

    public TitleBar(Context context) {
        super(context);
        init(null);
    }

    public TitleBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        View view = View.inflate(getContext(), R.layout.titlebar, null);
        String bt_left_text = attrs.getAttributeValue("http://schemas.android.com/apk/res-auto", "bt_left_text");
        String bt_right_text = attrs.getAttributeValue("http://schemas.android.com/apk/res-auto", "bt_right_text");
        String bt_title_text = attrs.getAttributeValue("http://schemas.android.com/apk/res-auto", "bt_title_text");

        Button bt_titlebar_left = (Button) view.findViewById(R.id.bt_titlebar_left);
        Button bt_titlebar_right = (Button) view.findViewById(R.id.bt_titlebar_right);
        TextView tv_titlebar_title = (TextView) view.findViewById(R.id.tv_titlebar_title);

        bt_titlebar_left.setText(bt_left_text);
        bt_titlebar_right.setText(bt_right_text);
        tv_titlebar_title.setText(bt_title_text);

        addView(view);
    }
}
