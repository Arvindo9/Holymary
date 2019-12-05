package com.king.holymary.adapter;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.king.holymary.ImageDisplay;
import com.king.holymary.NoticeActivity_1;
import com.king.holymary.R;
import com.king.holymary.constant.Config;
import com.king.holymary.data_handler.NoticeData;
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
import static com.king.holymary.constant.Config.NOTICE_FILE_FOLDER;
import static com.king.holymary.constant.Config.OUTPUT_FILE_FOLDER;
import static com.king.holymary.constant.Config.OUTPUT_IMAGE_FOLDER;

/**
 * Created by Arvindo on 19-03-2017.
 * Company KinG
 * email at support@towardtheinfinity.com
 */

public class NoticeAdapter_1 extends BaseAdapter {
    private final NoticeActivity_1 activity;
    private final ArrayList<NoticeData> dataList;
    private ProgressBar progressBar;
    private ProgressDialog pd, mProgressDialog;
    private TextView txtPercentage;

//    private String url = "http://towardtheinfinity.com/temp/uploads/tere_bin.mp4";

    public NoticeAdapter_1(NoticeActivity_1 noticeActivity_1, ArrayList<NoticeData> dataList) {

        this.activity = noticeActivity_1;
        this.dataList = dataList;
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Object getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final NoticeAdapter_1.DataHolder holder;
//        Uri uri = Uri.parse(url);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this.activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.notice_container, parent, false);
            holder = new NoticeAdapter_1.DataHolder();
//            holder.userId_tv = (TextView) convertView.findViewById(R.id.user_id_tv);
            holder.layout = (LinearLayout) convertView.findViewById(R.id.layout_img);
            holder.name_tv = (TextView) convertView.findViewById(R.id.name_tv);
            holder.userType_tv = (TextView) convertView.findViewById(R.id.type_show);
            holder.branchName_tv = (TextView) convertView.findViewById(R.id.branch_name_tv);
            holder.department_tv = (TextView) convertView.findViewById(R.id.dept_name_tv);
            holder.date_tv = (TextView) convertView.findViewById(R.id.date_tv);
            holder.time_tv = (TextView) convertView.findViewById(R.id.time_tv);
            holder.msgBody_tv = (TextView) convertView.findViewById(R.id.msg_body);
            holder.msgHeader_tv = (TextView) convertView.findViewById(R.id.msg_header);
//            holder.fileName_tv = (TextView) convertView.findViewById(R.id.file_name);
            holder.bodyImage = (ImageView) convertView.findViewById(R.id.img_id);
            holder.profilePic = (ImageView) convertView.findViewById(R.id.profile_pic);
            holder.download = (Button) convertView.findViewById(R.id.download);
            holder.msgFile = (ImageView) convertView.findViewById(R.id.file_img);
            holder.layoutFile = (RelativeLayout) convertView.findViewById(R.id.layout_file);
            convertView.setTag(holder);
        } else {
            holder = (NoticeAdapter_1.DataHolder) convertView.getTag();
        }


        final NoticeData item = (NoticeData) this.getItem(position);
        assert item != null;

//        holder.userId_tv.setText(item.getUser_id());
        holder.name_tv.setText(item.getName());
        holder.userType_tv.setText(item.getUserType());
        holder.branchName_tv.setText(item.getBranch_name());
        holder.department_tv.setText(item.getDepartment_name());
        holder.date_tv.setText(item.getDate());
        holder.time_tv.setText(item.getTime());
        holder.msgHeader_tv.setText(item.getMsg_header());
        holder.msgBody_tv.setText(item.getMsg_body());
        Log.e("sdfrwe w dsf ", item.getMsg_file());
//        holder.fileName_tv.setText(item.getMsg_file());


        // instantiate it within the onCreate method
        mProgressDialog = new ProgressDialog(activity);
        mProgressDialog.setMessage("A message");
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.setCancelable(true);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        final String fileName = item.getMsg_file();
        final String imageName = item.getMsg_image();

        //profile pic---------------------------------------
        String urlPic = "";
        try {
            urlPic = Config.PROFILE_PIC_FOLDER + item.getPic_path_name();
            Log.e("urlpic", urlPic);
        } catch (NullPointerException ignored){}

        Picasso.with(activity)
                .load(urlPic)
                .placeholder(R.drawable.face) //
                .error(R.drawable.face) //
//                .fit() ///
                .resize(150, 150)
                .tag(holder) //
                .into(holder.profilePic);

        //image------------------------------------------------
        try {
            final String url = Config.NOTICE_IMAGE_FOLDER + imageName;

            if(imageName != null && !imageName.equals("null") && !imageName.equals("NULL")) {
                holder.layout.setVisibility(View.VISIBLE);
                Picasso.with(activity)
                        .load(url)
                        .placeholder(R.drawable.ic_menu_camera) //
                        .error(R.drawable.error_img) //
                        .fit() //
//                        .resize(0, 100)
                        .tag(holder) //
                        .into(holder.bodyImage);

                holder.bodyImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(activity, ImageDisplay.class);
                        i.putExtra("url_pass", url);
                        i.putExtra("file_name", imageName);
                        activity.startActivity(i);
                    }
                });
            }
            else{
                holder.layout.setVisibility(View.GONE);
            }
        } catch (NullPointerException e) {
            holder.layout.setVisibility(View.GONE);
        }

        //file------------------------------------------
        if(fileName != null && !fileName.equals("null") &&
                !fileName.equals("NULL") && !fileName.equals("Null") &&
                !fileName.equals("")){
            holder.layoutFile.setVisibility(View.VISIBLE);
            holder.msgFile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    NoticeAdapter_1.DownloadFile downloadFile = new NoticeAdapter_1.DownloadFile();
                    downloadFile.execute(NOTICE_FILE_FOLDER + item.getMsg_file(), item.getMsg_file(), "");
                }
            });
        }
        else{
            holder.layoutFile.setVisibility(View.GONE);
        }

        return convertView;
    }

    private class DownloadFile extends AsyncTask<String, Integer, String>{


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

//            Log.e("pa", params[0]);
//            Log.e("pa", params[1]);
//            Log.e("pa", params[2]);
//            Log.e("path", OUTPUT_FILE_FOLDER + params[1]);

//          http://vchmgi.com/holymary/notice_file/TERMSOFUSE.pdf

            try {
//                URL url = new URL("http://vchmgi.com/holymary/notice_file/TERMSOFUSE.pdf");
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
                    output = new BufferedOutputStream(new FileOutputStream(OUTPUT_IMAGE_FOLDER + params[1]));
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
//                dialog.dismiss();
                Toast.makeText(activity, result, Toast.LENGTH_SHORT).show();
            } else
                Toast.makeText(activity, "File downloaded", Toast.LENGTH_SHORT).show();

//            Intent i = new Intent(MainActivity.this,LogIn.class);
//            startActivity(i);
//            Toast.makeText(getBaseContext(), result, Toast.LENGTH_LONG).show();
//            Log.d("DownloadTextTask", result);
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
        private TextView userType_tv;
        private TextView branchName_tv;
        private TextView department_tv;
        private TextView date_tv;
        private TextView time_tv;
        private TextView msgBody_tv;
        private TextView msgHeader_tv;
        private Button download;
        private LinearLayout layout;
        private RelativeLayout layoutFile;
    }
}
