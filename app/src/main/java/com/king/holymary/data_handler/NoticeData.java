package com.king.holymary.data_handler;

/**
 * Created by Arvindo on 19-03-2017.
 * Company KinG
 * email at support@towardtheinfinity.com
 */

public class NoticeData {
    private final String user_id;
    private final String name;
    private final String userType;
    private final String department_name;
    private final String branch_name;
    private final String msg_header;
    private final String msg_body;
    private final String pic_path_name;
    private final String msg_file;
    private final String msg_image;
    private final String date;
    private final String time;

    public NoticeData(String user_id, String name, String userType, String department_name,
                      String branch_name, String msg_header, String msg_body, String pic_path_name,
                      String msg_file, String msg_image, String date, String time) {
        this.user_id = user_id;
        this.name = name;
        this.userType = userType;
        this.department_name = department_name;
        this.branch_name = branch_name;
        this.msg_header = msg_header;
        this.msg_body = msg_body;
        this.pic_path_name = pic_path_name;
        this.msg_file = msg_file;
        this.msg_image = msg_image;
        this.date = date;
        this.time = time;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getName() {
        return name;
    }

    public String getDepartment_name() {
        return department_name;
    }

    public String getBranch_name() {
        return branch_name;
    }

    public String getMsg_header() {
        return msg_header;
    }

    public String getMsg_body() {
        return msg_body;
    }

    public String getPic_path_name() {
        return pic_path_name;
    }

    public String getMsg_file() {
        return msg_file;
    }

    public String getMsg_image() {
        return msg_image;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getUserType() {
        return userType;
    }
}
