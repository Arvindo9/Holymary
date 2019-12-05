package com.king.holymary.constant;

import android.os.Environment;

public final class Config {
	public static final String REGISTRATION_URL= "http://jithvar.com/demo/arvindo/user_registration.php";
	public static final String LOGIN_URL= "http://jithvar.com/demo/arvindo/login_verification.php";

	public static final String HOME_ACTIVITY= "http://jithvar.com/demo/arvindo/home_updates.php";
	public static final String HOME_IMAGE_FOLDER = "http://jithvar.com/demo/arvindo/m_images/";
	public static final String HOME_VIDEO_FOLDER = "http://jithvar.com/demo/arvindo/m_videos/";
	public static final String HOME_FILE_FOLDER = "http://jithvar.com/demo/arvindo/m_files/";
	public static final String HOME_VIDEO_THUMB = "http://jithvar.com/demo/arvindo/m_videos_thumb/";
//	public static final String HOME_IMAGE_THUMB = "http://steinmetzils.org/wideawake/m_image_thumb/";

	public static final String PROFILE_PIC_PAGE = "http://jithvar.com/demo/arvindo/profile_pic.php";
	public static final String PROFILE_DATA_LOAD = "http://jithvar.com/demo/arvindo/profile_data_load.php";
	public static final String PROFILE_PIC_FOLDER = "http://jithvar.com/demo/arvindo/m_profile_pic/";

	public static final String NOTICE_ACTIVITY= "http://jithvar.com/demo/arvindo/notice_updates.php";
	public static final String NOTICE_FILE_FOLDER= "http://jithvar.com/demo/arvindo/notice_file/";
	public static final String NOTICE_IMAGE_FOLDER= "http://jithvar.com/demo/arvindo/notice_image/";

	public static final String CLASS_UPDATES = "http://jithvar.com/demo/arvindo/class_updates.php";
	public static final String CLASS_IMAGE_FOLDER = "http://jithvar.com/demo/arvindo/class_images/";
	public static final String CLASS_FILE_FOLDER = "http://jithvar.com/demo/arvindo/class_files/";
//	public static final String CLASS_PROFILE_PIC = "http://steinmetzils.org/wideawake/post_video_ftp.php";

    public static final String HOME_POST_UPLOAD= "http://jithvar.com/demo/arvindo/post.php";
    public static final String NOTICE_POST_UPLOAD= "http://jithvar.com/demo/arvindo/notify_post.php";
    public static final String CLASS_POST = "http://jithvar.com/demo/arvindo/class_post.php";
    public static final String FTP_THUMBNAIL = "http://jithvar.com/demo/arvindo/post_video_ftp.php";

	public static final String CHANGE_PHONE_NUM = "http://jithvar.com/demo/arvindo/change_phone_num.php";
	public static final String CHANGE_PASSWORD = "http://jithvar.com/demo/arvindo/change_password.php";

	public static final String OUTPUT_FILE_FOLDER = Environment.getExternalStorageDirectory() +
			"/holymary/files/";
	public static final String OUTPUT_IMAGE_FOLDER = Environment.getExternalStorageDirectory() + "/holymary/images/";
	public static final String OUTPUT_VIDEO_FOLDER = Environment.getExternalStorageDirectory() + "/holymary/videos/";
}
