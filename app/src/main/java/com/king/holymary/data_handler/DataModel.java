package com.king.holymary.data_handler;

/**
 * Created by Arvindo on 19-03-2017.
 * Company KinG
 * email at support@towardtheinfinity.com
 */

public class DataModel  {
    String name;
    String version;
    int id_;
    int image;

    public DataModel(String name, String version, int id_, int image) {
        this.name = name;
        this.version = version;
        this.id_ = id_;
        this.image=image;
    }

    public String getName() {
        return name;
    }

    public String getVersion() {
        return version;
    }

    public int getImage() {
        return image;
    }

    public int getId() {
        return id_;
    }
}
