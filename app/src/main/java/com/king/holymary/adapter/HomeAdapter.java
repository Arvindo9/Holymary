package com.king.holymary.adapter;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.StrictMode;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.king.holymary.ImageDisplay;
import com.king.holymary.R;
import com.king.holymary.VideoDisplay;
import com.king.holymary.constant.Config;
import com.king.holymary.data_handler.HomeData;
import com.squareup.picasso.Picasso;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import static com.king.holymary.NoticePost.TAG;
import static com.king.holymary.constant.Config.HOME_FILE_FOLDER;
import static com.king.holymary.constant.Config.OUTPUT_FILE_FOLDER;

/**
 * Created by Arvindo on 09-03-2017.
 * Company KinG
 * email at support@towardtheinfinity.com
 */

public class HomeAdapter extends BaseAdapter implements Cloneable{

    private final FragmentActivity activity;
    private final Context context;
    private final ArrayList<HomeData> homeDataList;
    private ProgressDialog mProgressDialog;


    public HomeAdapter(FragmentActivity activity, Context context, ArrayList<HomeData> homeDataList) {
        this.activity = activity;
        this.context = context;
        this.homeDataList = homeDataList;
    }

    @Override
    public int getCount() {
        return homeDataList.size();
    }

    @Override
    public Object getItem(int position) {
        return homeDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final DataHolder holder;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this.activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.home_container,parent,false);
            holder = new DataHolder();
            holder.name_tv = (TextView)convertView.findViewById(R.id.name_tv);
            holder.college_tv = (TextView)convertView.findViewById(R.id.college_name_tv);
            holder.userType_tv = (TextView)convertView.findViewById(R.id.type_show);
            holder.department_tv = (TextView)convertView.findViewById(R.id.dept_name_tv);
            holder.branchName_tv = (TextView)convertView.findViewById(R.id.branch_name_tv);
            holder.date_tv = (TextView)convertView.findViewById(R.id.date_tv);
            holder.time_tv = (TextView)convertView.findViewById(R.id.time_tv);
            holder.msgBody_tv = (TextView)convertView.findViewById(R.id.msg_body);
            holder.msgHeader_tv = (TextView)convertView.findViewById(R.id.msg_header);
            holder.bodyImage = (ImageView) convertView.findViewById(R.id.img_id);
            holder.profilePic = (ImageView) convertView.findViewById(R.id.profile_pic);
            holder.msgFile = (ImageView) convertView.findViewById(R.id.file_img);
            holder.layoutFile = (RelativeLayout) convertView.findViewById(R.id.layout_file);
            convertView.setTag(holder);
        } else {
            holder = (DataHolder) convertView.getTag();
        }


        // instantiate it within the onCreate method
        mProgressDialog = new ProgressDialog(activity);
        mProgressDialog.setMessage("A message");
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.setCancelable(true);


        final HomeData item = (HomeData) this.getItem(position);
        assert item != null;

//        holder.userId_tv.setText(item.get_UserId());
        holder.name_tv.setText(item.get_Name());
        holder.college_tv.setText(item.getClgName());
        holder.userType_tv.setText(item.getUserType());
        holder.branchName_tv.setText(item.get_BranchName());
        holder.department_tv.setText(item.get_DepartmentName());
        holder.date_tv.setText(item.getDate());
        holder.time_tv.setText(item.getTime());
        holder.msgHeader_tv.setText(item.get_MsgHeader());
        holder.msgBody_tv.setText(item.get_MsgBody());

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        final String fileName = item.get_MsgFile();
        final String fileExtention = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());

        Picasso.with(context)
                .load(Config.PROFILE_PIC_FOLDER + item.get_ProfilePic())
                .placeholder(R.drawable.face) //
                .error(R.drawable.face) //
                .fit() //
                .tag(holder) //
                .into(holder.profilePic);

        Log.e("file", fileName + " " + fileExtention);

        if((!fileName.equals("")) &&(fileExtention.equals("jpg") || fileExtention.equals("jpeg") ||
                fileExtention.equals("png"))){
            holder.bodyImage.setVisibility(View.VISIBLE);
            holder.layoutFile.setVisibility(View.GONE);

            final String url = Config.HOME_IMAGE_FOLDER + fileName;

            Log.e("url", url);
            //"http://res.cloudinary.com/oclemy/image/upload/v1460355582/breaking_wbxtzb.jpg";
            Picasso.with(context)
                    .load(url)
                    .placeholder(R.drawable.ic_menu_camera) //
                    .error(R.drawable.error_img) //
                    .fit() //
                    .tag(holder) //
                    .into(holder.bodyImage);

            holder.bodyImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(activity, ImageDisplay.class);
                    i.putExtra("url_pass", url);
                    i.putExtra("file_name", fileName);
                    activity.startActivity(i);
                }
            });
        }
        else if((!fileExtention.equals("")) && (fileExtention.equals("mkv") ||
                fileExtention.equals("flv") || fileExtention.equals("gif")
                || fileExtention.equals("avi") || fileExtention.equals("wmv") || fileExtention.equals("asf")
                || fileExtention.equals("mp4") || fileExtention.equals("m4p ") || fileExtention.equals("m4v")
                || fileExtention.equals("mpg") || fileExtention.equals("3gp"))){
            holder.layoutFile.setVisibility(View.GONE);
            holder.bodyImage.setVisibility(View.VISIBLE);
            String url = Config.HOME_VIDEO_THUMB + item.getVideo_thumb();
            Log.e("url", url);

            Picasso.with(context)
                    .load(url)
                    .placeholder(R.drawable.ic_menu_camera) //
                    .error(R.drawable.error_img) //
                    .fit() //
                    .tag(holder) //
                    .into(holder.bodyImage);

            holder.bodyImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(activity, VideoDisplay.class);
                    i.putExtra("video_m", fileName);
                    activity.startActivity(i);
                }
            });
        }
        else if(!fileName.equals("null") &&
                !fileName.equals("NULL") && !fileName.equals("Null") &&
                !fileName.equals("")){
            //file---------------------------------------------
            holder.layoutFile.setVisibility(View.VISIBLE);
            holder.bodyImage.setVisibility(View.GONE);
            holder.msgFile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    HomeAdapter.DownloadFile downloadFile = new HomeAdapter.DownloadFile();
                    downloadFile.execute(HOME_FILE_FOLDER + fileName, fileName, "");
                }
            });
        }
        else{
            holder.layoutFile.setVisibility(View.GONE);
            holder.bodyImage.setVisibility(View.GONE);
        }

        return convertView;
    }

    private class DownloadFile extends AsyncTask<String, Integer, String> {


        //        private ProgressDialog dialog = new ProgressDialog(activity);
        String response;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // take CPU lock to prevent CPU from going off if the user
            // presses the power button during download
//            PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
//            mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
//                    getClass().getName());
//            mWakeLock.acquire();
            mProgressDialog.show();
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate(progress);
            // if we get here, length is known, now set indeterminate to false
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.setMax(100);
            mProgressDialog.setProgress(progress[0]);
        }

        @Override
        protected String doInBackground(String... params) {
            BufferedInputStream input = null;
            BufferedOutputStream output = null;
            HttpURLConnection connection = null;
            String response = "";

            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                // expect HTTP 200 OK, so we don't mistakenly save error report
                // instead of the file
                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    response = connection.getResponseMessage();
                    return "Server returned HTTP " + connection.getResponseCode()
                            + " " + connection.getResponseMessage();
                }

                // this will be useful to display download percentage
                // might be -1: server did not report the length
                int fileLength = connection.getContentLength();

                // download the file
                input = new BufferedInputStream(connection.getInputStream());
                File dir = new File(OUTPUT_FILE_FOLDER);
                if(!dir.exists()){
                    dir.mkdirs();
                }
                File outPut = new File(dir, params[1]);

                Log.e("path ffff", dir + "/" + params[1]);

//                File outPut = new File(Environment.getExternalStorageDirectory(), params[1]);
                if(params[2].equals("image")) {
                    output = new BufferedOutputStream(new FileOutputStream(OUTPUT_FILE_FOLDER + params[1]));
                }
                else{
                    output = new BufferedOutputStream(new FileOutputStream(outPut));
                }

                byte data[] = new byte[4096];
                long total = 0;
                int count;
                while ((count = input.read(data)) != -1) {
                    // allow canceling with back button
                    if (isCancelled()) {
                        input.close();
                        return null;
                    }
                    total += count;
                    // publishing the progress....
                    if (fileLength > 0) // only if total length is known
                        publishProgress((int) (total * 100 / fileLength));
                    output.write(data, 0, count);
                }
                response += "ok";
            } catch (Exception e) {
                e.printStackTrace();
                return e.toString();
            } finally {
                try {
                    if (output != null)
                        output.close();
                    if (input != null)
                        input.close();
                } catch (IOException ignored) {
                    ignored.printStackTrace();
                }

                if (connection != null)
                    connection.disconnect();
            }

            return response;
        }


        @Override
        protected void onPostExecute(String result) {
            Log.e(TAG, "Response from server: " + result);

            mProgressDialog.dismiss();
            // showing the server response in an alert dialog
            showAlert(result);
            super.onPostExecute(result);
            if (result != null) {
                Toast.makeText(activity, result, Toast.LENGTH_SHORT).show();
            } else
                Toast.makeText(activity, "File downloaded", Toast.LENGTH_SHORT).show();
        }

    }

    private void showAlert(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage(message).setTitle("Response from Servers")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // do nothing
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private class DataHolder{
        private ImageView bodyImage;
        private ImageView profilePic;
        private ImageView msgFile;
        private TextView name_tv;
        private TextView college_tv;
        private TextView userType_tv;
        private TextView department_tv;
        private TextView branchName_tv;
        private TextView date_tv;
        private TextView time_tv;
        private TextView msgBody_tv;
        private TextView msgHeader_tv;
        private RelativeLayout layoutFile;
    }
}
