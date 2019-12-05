package com.king.holymary.data_handler;

/**
 * Created by Arvindo on 08-03-2017.
 * Company KinG
 * email at support@towardtheinfinity.com
 */

public class HomeData {

    private String _UserId;
    private final String clgName;
    private String _Name;
    private final String userType;
    private String _DepartmentName;
    private String _BranchName;
    private String _MsgHeader;
    private String _MsgBody;
    private String _ProfilePic;
    private String _MsgFile;
    private final String date;
    private final String time;
    private final String video_thumb;


    public HomeData(String _UserId, String clgName, String _Name, String userType,
                    String _DepartmentName,
                    String _BranchName, String _MsgHeader, String _MsgBody, String _ProfilePic,
                    String _MsgFile, String date, String time, String video_thumb) {
        this._UserId = _UserId;
        this.clgName = clgName;
        this._Name = _Name;
        this.userType = userType;
        this._DepartmentName = _DepartmentName;
        this._BranchName = _BranchName;
        this._MsgHeader = _MsgHeader;
        this._MsgBody = _MsgBody;
        this._ProfilePic = _ProfilePic;
        this._MsgFile = _MsgFile;
        this.date = date;
        this.time = time;
        this.video_thumb = video_thumb;
    }

    public String get_UserId() {
        return _UserId;
    }

    public String get_Name() {
        return _Name;
    }

    public String get_DepartmentName() {
        return _DepartmentName;
    }

    public String get_BranchName() {
        return _BranchName;
    }

    public String get_MsgHeader() {
        return _MsgHeader;
    }

    public String get_MsgBody() {
        return _MsgBody;
    }

    public String get_ProfilePic() {
        return _ProfilePic;
    }

    public String get_MsgFile() {
        return _MsgFile;
    }

    public String getTime() {
        return time;
    }

    public String getDate() {
        return date;
    }

    public String getVideo_thumb() {
        return video_thumb;
    }

    public String getClgName() {
        return clgName;
    }

    public String getUserType() {
        return userType;
    }
}
