package com.example.administrator.scanningcutvideo;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * 显示本地视频
 */
public class ShowLocalVideoListAdapter extends RecyclerView.Adapter<ShowLocalVideoListAdapter.ViewHolder> {

    private static final String TAG = "----->ShowLocalVideoListAdapter";

    public ArrayList<ScanningLocalVideoUtils.MediaBean> videoList = new ArrayList<>();
    public Context context;
    public OnClickSelectVideoListener listener;

    public List<Bitmap> bitmapList = new ArrayList<>();

    public ShowLocalVideoListAdapter(ArrayList<ScanningLocalVideoUtils.MediaBean> list, List<Bitmap> bitmapLists, Context context, OnClickSelectVideoListener listener) {
        this.videoList = list;
        this.bitmapList = bitmapLists;
        this.context = context;
        this.listener = listener;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.show_local_video_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        holder.tv_name.setText(videoList.get(position).getMediaName());

        //final Bitmap bitmap = getVideoThumbnail(videoList.get(position).getPath(), ScreenUtil.dp2px(context, 100), ScreenUtil.dp2px(context, 100), MediaStore.Images.Thumbnails.MINI_KIND);

        final Bitmap bitmap = bitmapList.get(position);

        if (bitmap == null) {
            holder.iv_preview.setImageBitmap(null);
            holder.iv_preview.setBackground(context.getResources().getDrawable(R.mipmap.photo_pictures_no));
        } else {
            holder.iv_preview.setBackground(null);
            holder.iv_preview.setImageBitmap(bitmap);
        }

        holder.llt_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select(holder, position, bitmap);
            }
        });
        holder.tv_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select(holder, position, bitmap);
            }
        });
        holder.iv_preview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select(holder, position, bitmap);
            }
        });

    }

    @Override
    public int getItemCount() {
        return videoList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        public ImageView iv_preview;
        public TextView tv_name;
        public LinearLayout llt_container;

        public ViewHolder(View itemView) {
            super(itemView);

            iv_preview = itemView.findViewById(R.id.iv_preview);
            tv_name = itemView.findViewById(R.id.tv_name);
            llt_container = itemView.findViewById(R.id.llt_container);
        }
    }

    /**
     * 获取视频缩略图
     *
     * @param videoPath
     * @param width
     * @param height
     * @param kind
     * @return
     */
    protected Bitmap getVideoThumbnail(String videoPath, int width, int height, int kind) {
        Bitmap bitmap = null;
        bitmap = ThumbnailUtils.createVideoThumbnail(videoPath, kind);
        bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height, ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        return bitmap;
    }

    boolean play = false;

    public void select(final ViewHolder holder, final int position, Bitmap bitmap) {

        String path = videoList.get(position).getPath();
        if (bitmap != null) {
            if (!TextUtils.isEmpty(path) && path.length() > 4) {
                String substring = path.substring(path.length() - 4);
                LogUtil.logDebug(TAG, "--->substring = " + substring);

                if (!substring.equals(".mp4")) {

                    if (!play) {
                        ToastUtil.showToastSHORT("非mp4格式，可能导致视频无法播放！");
                        play = !play;
                        return;
                    }
                    listener.onClickSelectVideoListener(holder.tv_name.getText().toString().trim(), videoList.get(position).getPath());
                } else {
                    listener.onClickSelectVideoListener(holder.tv_name.getText().toString().trim(), videoList.get(position).getPath());
                }
            }
        } else {
            ToastUtil.showToastSHORT("无效的视频！");
        }
    }

    public interface OnClickSelectVideoListener {
        void onClickSelectVideoListener(String name, String path);
    }
}
