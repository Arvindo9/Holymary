package com.king.holymary.data_handler;

/**
 * Created by Arvindo on 07-04-2017.
 * Company KinG
 * email at support@towardtheinfinity.com
 */

public class ClassData {
    private final String user_id;
    private final String name;
    private final String userType;
    private final String current_year;
    private final String current_section;
    private final String department_name;
    private final String branch_name;
    private final String msg_header;
    private final String msg_body;
    private final String pic_path_name;
    private final String msg_image;
    private final String msg_file;
    private final String date;
    private final String time;

    public ClassData(String user_id, String name, String userType,
                     String current_year,
                     String current_section, String department_name,
                     String branch_name, String msg_header, String msg_body, String pic_path_name,
                     String msg_image, String msg_file, String date, String time) {

        this.user_id = user_id;
        this.name = name;
        this.userType = userType;
        this.current_year = current_year;
        this.current_section = current_section;
        this.department_name = department_name;
        this.branch_name = branch_name;
        this.msg_header = msg_header;
        this.msg_body = msg_body;
        this.pic_path_name = pic_path_name;
        this.msg_image = msg_image;
        this.msg_file = msg_file;
        this.date = date;
        this.time = time;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getName() {
        return name;
    }

    public String getCurrent_year() {
        return current_year;
    }

    public String getCurrent_section() {
        return current_section;
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

    public String getMsg_image() {
        return msg_image;
    }

    public String getMsg_file() {
        return msg_file;
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
