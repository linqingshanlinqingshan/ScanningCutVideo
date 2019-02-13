package com.example.administrator.scanningcutvideo;

import android.content.Context;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;


/**
 * 微见证添加视频的来源
 */
public class SelectVideoResourcesPopupWindows extends BasePopupWindow {

    private static final String TAG = "----->SelectVideoResourcesPopupWindows";

    public TextView tv_shooting;
    public TextView tv_local;
    public OnClickVideoResourcesPopupListener listener;

    public SelectVideoResourcesPopupWindows(Context context, View view, OnClickVideoResourcesPopupListener listener) {
        super(context, view);
        this.listener = listener;
    }

    @Override
    public void setPopupOnKeyListener(View contentView) {
        contentView.setOnKeyListener(new View.OnKeyListener() {

            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event) {
                switch (keyCode) {
                    case KeyEvent.KEYCODE_BACK:

                        Log.d(TAG, "----> KEYCODE_BACK  dismiss");

                        dismiss();

                        return true;
                    default:
                        break;
                }
                return false;
            }
        });
    }

    @Override
    public void initListener() {
        if (tv_shooting != null && tv_local != null) {

            tv_shooting.setOnClickListener(this);
            tv_local.setOnClickListener(this);

        }
    }

    @Override
    public void initView(View contentView) {
        if (contentView != null) {

            tv_shooting = contentView.findViewById(R.id.tv_shooting);
            tv_local = contentView.findViewById(R.id.tv_local);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_shooting:
                listener.onClickVideoResourcesPopup(mContext.getResources().getString(R.string.shooting));
                break;
            case R.id.tv_local:
                listener.onClickVideoResourcesPopup(mContext.getResources().getString(R.string.local));
                break;
        }
        dismiss();
    }

    public interface OnClickVideoResourcesPopupListener {
        void onClickVideoResourcesPopup(String flag);
    }
}
