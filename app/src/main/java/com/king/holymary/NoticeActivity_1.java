package com.king.holymary;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.king.holymary.adapter.NoticeAdapter_1;
import com.king.holymary.data_handler.NoticeData;
import com.king.holymary.database_sqlite.DataBaseAwake_1;

import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;

import static com.king.holymary.constant.Config.NOTICE_ACTIVITY;

/**
 * Created by Arvindo on 19-03-2017.
 * Company KinG
 * email at support@towardtheinfinity.com
 */

public class NoticeActivity_1 extends Activity{
    private ListView listView;
    private NoticeAdapter_1 adapter;

    private boolean start_type;
    private int lastPrimaryId;

    private ArrayList<NoticeData> noticeDataList;
    private String dataType;
    private DataBaseAwake_1 db;
    private String userID = "";
    private String clgName = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notice_update_1);

        dataType = getIntent().getStringExtra("dataType");
        TextView activityText = (TextView) findViewById(R.id.activity_name);

        start_type = true;
        lastPrimaryId = -1;
        noticeDataList = new ArrayList<>();

        listView = (ListView) findViewById(R.id.notice_update);
        adapter = new NoticeAdapter_1(NoticeActivity_1.this, noticeDataList);
        listView.setAdapter(adapter);

        switch (dataType){
            case "1": activityText.setText(getResources().getString(R.string.new_updates));
                break;
            case "2": activityText.setText(getResources().getString(R.string.examination));
                break;
            case "3": activityText.setText(getResources().getString(R.string.magazine));
                break;
            case "4": activityText.setText(getResources().getString(R.string.sports));
                break;
            case "5": activityText.setText(getResources().getString(R.string.library));
                break;
            case "6": activityText.setText(getResources().getString(R.string.scholar));
                break;
            case "7": activityText.setText(getResources().getString(R.string.training));
                break;
            case "8": activityText.setText(getResources().getString(R.string.higer_ed));
                break;
            case "9": activityText.setText(getResources().getString(R.string.accounts));
                break;
            case "10": activityText.setText(getResources().getString(R.string.placement));
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        db = new DataBaseAwake_1(this);
        try {
            userID = db.getTrueUserId();
            String[] data = db.getUserDetails(userID);
            if (data[3].equals("1")){
                clgName = data[0];
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if(isNetworkAvailable()){
            new ConnectingToServer().execute("");
        }
        else{
            //from internal db

        }
    }

    private class ConnectingToServer extends AsyncTask<String, Integer, String> {
//        private ProgressDialog dialog = new ProgressDialog(getActivity());

        @Override
        protected void onPreExecute() {
        }

        @SuppressLint("SetTextI18n")
        @Override
        protected void onProgressUpdate(Integer... progress) {

        }

        @Override
        protected String doInBackground(String... url) {
            return requestServerForDataString();
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {
                if (result != null) {
//                dialog.dismiss();
//                Toast.makeText(NoticeActivity_1.this, "", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(NoticeActivity_1.this, "Try Again", Toast.LENGTH_SHORT).show();
                }

                Log.e("DownloadTextTask", result);
            }catch (Exception ignored){}
        }
    }

    private String requestServerForDataString() {
        try {
            URL url = new URL(NOTICE_ACTIVITY);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            //connection.connect();

            String reqHead = "Accept:application/json";
            connection.setRequestMethod("POST");
            connection.setRequestProperty("connection","Keep-Alive"+reqHead);
            //Header header = new Header();

            @SuppressWarnings("deprecation")
            MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
//            entity.addPart("register_type",new StringBody(id_s));

            if(lastPrimaryId < 0) {
                entity.addPart("start_type", new StringBody(String.valueOf(start_type)));
                entity.addPart("data_type", new StringBody(String.valueOf(dataType)));
                entity.addPart("user_id",new StringBody(userID));
                entity.addPart("college_name", new StringBody(clgName));
            }
            else{
                entity.addPart("start_type", new StringBody(String.valueOf(start_type)));
                entity.addPart("data_type", new StringBody(String.valueOf(dataType)));
                entity.addPart("user_id",new StringBody(userID));
                entity.addPart("college_name", new StringBody(clgName));
                entity.addPart("last_primary_id", new StringBody(String.valueOf(lastPrimaryId)));
            }

            connection.addRequestProperty("content-length",entity.getContentLength()+"");
            connection.addRequestProperty(entity.getContentType().getName(),entity.getContentType().getValue());

            OutputStream os = connection.getOutputStream();
            entity.writeTo(connection.getOutputStream());
            os.close();
            Log.d("HITTING","hitting url");
            connection.connect();

            if(connection.getResponseCode()==HttpURLConnection.HTTP_OK){
                return readStream(connection.getInputStream());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return "fails";
    }

    private String readStream(InputStream inputStream) {

        String response = "";
        BufferedReader reader;
        StringBuilder builder = new StringBuilder();

        reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        try {
            while ((line = reader.readLine())!=null){
                builder.append(line);
                Log.e("\n", builder.toString());
            }
            response = builder.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Log.e("responset form server ", response);

        try {
            initializedData(response);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }

    private void initializedData(String response) throws Exception{
        if(response != null && !response.equals("")){
            try {
                JSONArray jsonArray = new JSONArray(response);

                Log.e("jaso", String.valueOf(jsonArray));

                String primary_id = "-1";
                for(int i=0;i<jsonArray.length();i++) {
                    JSONObject c = jsonArray.getJSONObject(i);
                    primary_id = c.getString("primary_id");
                    String user_id = c.getString("user_id");
                    String name = c.getString("name");
                    String userType;// = c.getString("user_type");
                    String pic_path_name = c.getString("pic_path_name");
                    String department_name = c.getString("department_name");
                    String branch_name = c.getString("branch_name");
                    String msg_header = c.getString("msg_header");
                    String msg_body = c.getString("msg_body");
                    String msg_image = "null";
                    try {
                        msg_image = c.getString("msg_image");
                    } catch (JSONException ignored) {}
                    String msg_file = "null";
                    try {
                        msg_file = c.getString("msg_file");
                    } catch (JSONException ignored){}

                    String dateTime = c.getString("data_time");

                    String date = dateTime.substring(0, 10);
                    String time = dateTime.substring(11);


                    userType = "";//temp

//                    String msg_image = c.getString("msg_image");
//                    String msg_file = c.getString("msg_file");

                    noticeDataList.add(new NoticeData(user_id, name, userType
                            , department_name, branch_name,
                            msg_header, msg_body, pic_path_name, msg_file, msg_image, date, time));
                }

                this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        adapter.notifyDataSetChanged();
                    }
                });

                this.lastPrimaryId = Integer.parseInt(primary_id);
                if (lastPrimaryId > 0){
                    start_type = false;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
