package com.king.holymary;

import android.annotation.SuppressLint;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.king.holymary.adapter.ClassAdapter;
import com.king.holymary.data_handler.ClassData;
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

import static com.king.holymary.constant.Config.CLASS_UPDATES;

/**
 * Created by Arvindo on 07-04-2017.
 * Company KinG
 * email at support@towardtheinfinity.com
 */

public class ClassUpdates extends Fragment {
    private ListView listView;
    private ClassAdapter classAdapter;

    private ArrayList<ClassData> classDataList;

    private boolean start_type;
    private int lastPrimaryId;
    private DataBaseAwake_1 db;
    private String userID = "";
    private String clgNameStu = "";
    private String branchNameStu = "";
    private String deptNameStu = "";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        start_type = true;
        lastPrimaryId = -1;
        classDataList = new ArrayList<>();

        db = new DataBaseAwake_1(getActivity());
        try {
            if(db.checkStatus()){
                userID = db.getTrueUserId();
                String[] data = db.getUserDetails(userID);
                if (data[3].equals("1")){
                    clgNameStu = data[0];
                    deptNameStu = data[1];
                    branchNameStu = data[2];
                }
            }
        } catch (SQLException | NullPointerException e) {
            e.printStackTrace();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.home, container, false);
//        myOnClickListener = new MyOnClickListener(this);

        listView = (ListView) view.findViewById(R.id.my_recycler_view);
        classAdapter = new ClassAdapter(getActivity(), classDataList);
        listView.setAdapter(classAdapter);
        return  view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (!isNetworkAvailable()){
            Toast.makeText(getActivity(), "No internet connection", Toast.LENGTH_SHORT).show();
        }

        new ClassUpdates.ConnectingToServer().execute("");
    }

    private class ConnectingToServer extends AsyncTask<String, Integer, String> {

//        private ProgressDialog dialog = new ProgressDialog(getActivity());

        @Override
        protected void onPreExecute() {
//            dialog.setTitle("Uploading");
//            dialog.setMessage("Processing...");
//            dialog.setCancelable(false);
//            dialog.show();

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getActivity(), "dept: " + deptNameStu + "\nclg: " + clgNameStu,
                            Toast.LENGTH_SHORT).show();
                }
            });
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
                    Toast.makeText(getActivity(), result, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "Try Again", Toast.LENGTH_SHORT).show();
                }

                Log.e("DownloadTextTask", result);
            }
            catch (Exception ignored){

            }
        }
    }

    private String requestServerForDataString() {
        try {
            URL url = new URL(CLASS_UPDATES);
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
                entity.addPart("college_name",new StringBody(clgNameStu));
                entity.addPart("department_name",new StringBody(deptNameStu));
                entity.addPart("branch_name",new StringBody(branchNameStu));
            }
            else{
                entity.addPart("start_type", new StringBody(String.valueOf(start_type)));
                entity.addPart("user_id",new StringBody(userID));
                entity.addPart("college_name",new StringBody(clgNameStu));
                entity.addPart("department_name",new StringBody(deptNameStu));
                entity.addPart("branch_name",new StringBody(branchNameStu));
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

        initializedData(response);

        return response;
    }

    private void initializedData(String response) {
        if(response != null && !response.equals("")){
            try {
                Log.e("sdf", response);
                JSONArray jsonArray = new JSONArray(response);

                String primary_id = "-1";
                int count =20;
                for(int i=0;i<jsonArray.length();i++){
                    JSONObject c = jsonArray.getJSONObject(i);
                    String user_id = c.getString("user_id");
                    String name = c.getString("name");
                    String userType;// = c.getString("user_type");
                    String current_year = c.getString("current_year");
                    String current_section = c.getString("current_section");
                    String pic_path_name = c.getString("pic_path_name");
                    String department_name = c.getString("department_name");
                    String branch_name = c.getString("branch_name");
                    String msg_header = c.getString("msg_header");
                    String msg_body = c.getString("msg_body");
                    String msg_image = c.getString("msg_image");
                    String msg_file = c.getString("msg_file");
                    String dateTime = c.getString("data_time");


                    String date = dateTime.substring(0, 10);
                    String time = dateTime.substring(11);

                    userType = "";//temp

                    classDataList.add(new ClassData(user_id, name, userType,
                            current_year, current_section,
                            department_name, branch_name,
                            msg_header, msg_body, pic_path_name, msg_image, msg_file, date, time));

                    if( i == count){
                        count = count + 20;
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                classAdapter.notifyDataSetChanged();
                            }
                        });
                    }
                }

                try {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            classAdapter.notifyDataSetChanged();
                        }
                    });
                }
                catch (Exception ignored){};

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
    public void onDestroy() {
        super.onDestroy();
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}
