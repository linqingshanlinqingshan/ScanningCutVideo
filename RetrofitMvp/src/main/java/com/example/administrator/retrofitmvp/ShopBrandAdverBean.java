package com.example.administrator.retrofitmvp;

import java.util.ArrayList;
import java.util.List;

/**
 * 进入视频
 */
public class ShopBrandAdverBean extends BaseResponse {

    private List<DataBean> data;

    private static String isNull(String string) {
        return string == null ? "" : string;
    }

    public List<DataBean> getData() {
        if (data == null) {
            return new ArrayList<>();
        }
        return data;
    }

    public ShopBrandAdverBean setData(List<DataBean> data) {
        this.data = data;
        return this;
    }

    public static class DataBean {
        /**
         * id : 10
         * type : 2
         * update_url : uploads/adver/video/5af16044743d3_adver.mp4
         * sort : 0
         */

        private String id;
        private String type;
        private String phone_url;
        private String sort;

        public String getId() {
            return isNull(id);
        }

        public DataBean setId(String id) {
            this.id = id;
            return this;
        }

        public String getType() {
            return isNull(type);
        }

        public DataBean setType(String type) {
            this.type = type;
            return this;
        }

        public String getUpdate_url() {
            return isNull(phone_url);
        }

        public DataBean setUpdate_url(String update_url) {
            this.phone_url = update_url;
            return this;
        }

        public String getSort() {
            return isNull(sort);
        }

        public DataBean setSort(String sort) {
            this.sort = sort;
            return this;
        }
    }
}
