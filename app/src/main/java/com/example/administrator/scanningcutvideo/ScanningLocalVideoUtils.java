package com.example.administrator.scanningcutvideo;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

/**
 * 扫描本地所有视频
 */
public class ScanningLocalVideoUtils {

    /**
     * 获取视频文件
     *
     * @param list
     * @param file
     * @return
     */
    public static ArrayList<MediaBean> getVideoFile(final ArrayList<MediaBean> list, File file) {

        file.listFiles(new FileFilter() {

            @Override
            public boolean accept(File file) {

                String name = file.getName();

                int i = name.indexOf('.');
                if (i != -1) {
                    name = name.substring(i);
                    if (name.equalsIgnoreCase(".mp4") || name.equalsIgnoreCase(".3gp") || name.equalsIgnoreCase(".wmv")
                            || name.equalsIgnoreCase(".ts") || name.equalsIgnoreCase(".rmvb")
                            || name.equalsIgnoreCase(".mov") || name.equalsIgnoreCase(".m4v")
                            || name.equalsIgnoreCase(".avi") || name.equalsIgnoreCase(".m3u8")
                            || name.equalsIgnoreCase(".3gpp") || name.equalsIgnoreCase(".3gpp2")
                            || name.equalsIgnoreCase(".mkv") || name.equalsIgnoreCase(".flv")
                            || name.equalsIgnoreCase(".divx") || name.equalsIgnoreCase(".f4v")
                            || name.equalsIgnoreCase(".rm") || name.equalsIgnoreCase(".asf")
                            || name.equalsIgnoreCase(".ram") || name.equalsIgnoreCase(".mpg")
                            || name.equalsIgnoreCase(".v8") || name.equalsIgnoreCase(".swf")
                            || name.equalsIgnoreCase(".m2v") || name.equalsIgnoreCase(".asx")
                            || name.equalsIgnoreCase(".ra") || name.equalsIgnoreCase(".ndivx")
                            || name.equalsIgnoreCase(".xvid")) {

                        MediaBean video = new MediaBean();

                        file.getUsableSpace();
                        video.setMediaName(file.getName());
                        video.setPath(file.getAbsolutePath());

                        list.add(video);

                        return true;
                    }

                    // 判断是不是目录
                } else if (file.isDirectory()) {
                    getVideoFile(list, file);
                }
                return false;
            }
        });

        return list;
    }



    public static class MediaBean {

        private String mediaName;
        private String path;

        public String getMediaName() {
            return mediaName;
        }

        public void setMediaName(String mediaName) {
            this.mediaName = mediaName;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }
    }
}
