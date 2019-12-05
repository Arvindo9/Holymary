package com.king.holymary;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.king.holymary.adapter.HomeAdapter;
import com.king.holymary.data_handler.HomeData;
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

import static com.king.holymary.constant.Config.HOME_ACTIVITY;

/**
 * Created by Arvindo on 22-02-2017.
 * Company KinG
 * email at support@towardtheinfinity.com
 */

public class Home_Tab extends Fragment {
    private ListView listView;
    private HomeAdapter homeAdapter;

    private ArrayList<HomeData> homeDataList;

    private boolean start_type;
    private int lastPrimaryId;
//    private boolean isViewDestroued = false;
    private DataBaseAwake_1 db;
    private String userID = "";


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        start_type = true;
        lastPrimaryId = -1;
        homeDataList = new ArrayList<>();

        db = new DataBaseAwake_1(getActivity());
        try {
            if(db.checkStatus()){
                userID = db.getTrueUserId();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.home, container, false);
//        myOnClickListener = new MyOnClickListener(this);

        listView = (ListView) view.findViewById(R.id.my_recycler_view);
        homeAdapter = new HomeAdapter(getActivity(), getContext(), homeDataList);
        listView.setAdapter(homeAdapter);
        return  view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (!isNetworkAvailable()){
            Toast.makeText(getActivity(), "No internet connection", Toast.LENGTH_SHORT).show();
        }

        new ConnectingToServer().execute("");
    }

    private class ConnectingToServer extends AsyncTask<String, Integer, String> {

//        private ProgressDialog dialog = new ProgressDialog(getActivity());

        @Override
        protected void onPreExecute() {
//            dialog.setTitle("Uploading");
//            dialog.setMessage("Processing...");
//            dialog.setCancelable(false);
//            dialog.show();
            Toast.makeText(getActivity(), "Loading...", Toast.LENGTH_SHORT).show();
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
//                    Toast.makeText(getActivity(), result, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "Try Again", Toast.LENGTH_SHORT).show();
                }

                Log.e("DownloadTextTask", result);
            }
            catch (Exception ignored){}
        }
    }

    private String requestServerForDataString() {
        try {
            URL url = new URL(HOME_ACTIVITY);
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
                entity.addPart("user_id",new StringBody(userID));
            }
            else{
                entity.addPart("start_type", new StringBody(String.valueOf(start_type)));
                entity.addPart("user_id",new StringBody(userID));
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

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                lazyLoading();
//            }
//        }).start();

        return response;
    }

    private void initializedData(String response) throws Exception {
        if(response != null && !response.equals("")){
            try {
                Log.e("sdf", response);
                JSONArray jsonArray = new JSONArray(response);

                String primary_id = "-1";
                int count =20;
                for(int i=0;i<jsonArray.length();i++){
                    JSONObject c = jsonArray.getJSONObject(i);
                    primary_id = c.getString("primary_id");
                    String user_id = c.getString("user_id");
                    String clgName = c.getString("college_name");
                    String name = c.getString("name");
                    String userType;// = c.getString("user_type");
                    String pic_path_name = c.getString("pic_path_name");
                    String department_name = c.getString("department_name");
                    String branch_name = c.getString("branch_name");
                    String msg_header = c.getString("msg_header");
                    String msg_body = c.getString("msg_body");
                    String msg_file = c.getString("msg_file");
                    String video_thumb = c.getString("video_thumb");
                    String dateTime = c.getString("data_time");

                    String date = dateTime.substring(0, 10);
                    String time = dateTime.substring(11);

                    userType = "";//temp

                    homeDataList.add(new HomeData(user_id, clgName, name, userType,
                            department_name, branch_name,
                            msg_header, msg_body, pic_path_name, msg_file, date, time, video_thumb));

                    if( i == count){
                        count = count + 20;
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                homeAdapter.notifyDataSetChanged();
                            }
                        });
                    }
                }

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        homeAdapter.notifyDataSetChanged();
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        homeDataList.clear();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        homeDataList.clear();
        start_type = true;
//        isViewDestroued = true;
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}