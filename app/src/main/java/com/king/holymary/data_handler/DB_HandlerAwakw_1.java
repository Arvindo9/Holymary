package com.king.holymary.data_handler;

/**
 * Created by Arvindo on 31-03-2017.
 * Company KinG
 * email at support@towardtheinfinity.com
 */

public class DB_HandlerAwakw_1 {

    private String user_id_;
    private String name;
    private String clgName;
    private String deptName;
    private String branchName;
    private String user_reg_type_;
    private String user_status;
    private String registration_ok;


    public DB_HandlerAwakw_1(String user_id_, String  user_reg_type_, String user_status, String reg_ok) {
        this.user_id_ = user_id_;
        this.user_status = user_status;
        this.user_reg_type_ = user_reg_type_;
        this.registration_ok = reg_ok;
    }

    public DB_HandlerAwakw_1(String user_id_,String name, String  clgName, String deptName, String branchName) {
        this.user_id_ = user_id_;
        this.name = name;
        this.clgName = clgName;
        this.deptName = deptName;
        this.branchName = branchName;
    }

    public String getUser_id_() {
        return user_id_;
    }

    public String  getUser_reg_type_() {
        return user_reg_type_;
    }

    public String getUser_status() {
        return user_status;
    }

    public String getRegistration_ok() {
        return registration_ok;
    }

    public String getClgName() {
        return clgName;
    }

    public String getDeptName() {
        return deptName;
    }

    public String getBranchName() {
        return branchName;
    }

    public String getName() {
        return name;
    }
}
