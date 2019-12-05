package com.king.holymary;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.king.holymary.database_sqlite.DataBaseAwake_1;

import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.SQLException;

import static com.king.holymary.constant.Config.CHANGE_PHONE_NUM;

/**
 * Created by Arvindo on 06-04-2017.
 * Company KinG
 * email at support@towardtheinfinity.com
 */

public class ChangePhoneNum extends Activity {

    private String userId_s = "";
    private String password_s = "";
    private String newPhone_s = "";
    private String oldPhone_s = "";
    private String registrationType_s = "";
    private DataBaseAwake_1 db;
    private EditText newPhoneE;
    private EditText oldPhoneE;
    private EditText confirmPhoneE;
    private EditText currentPassE;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_phone_num);

        newPhoneE = (EditText) findViewById(R.id.new_phone);
        oldPhoneE = (EditText) findViewById(R.id.current_phone);
        confirmPhoneE = (EditText) findViewById(R.id.confirm_phone);
        currentPassE = (EditText) findViewById(R.id.current_pass);

        db = new DataBaseAwake_1(this);
        try {
            if(db.checkStatus()){
                userId_s = db.getTrueUserId();
                registrationType_s = db.userType();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (!isNetworkAvailable()){
            Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show();
        }

//        new ChangePhoneNum.ConnectingToServer().execute("");
    }

    public void onClick(View view) {
        switch (view.getId()){
            case R.id.submit:
                newPhone_s = newPhoneE.getText().toString();
                oldPhone_s = oldPhoneE.getText().toString();
                password_s = currentPassE.getText().toString();
                String confirmPhone = confirmPhoneE.getText().toString();

                if(confirmPhone.equals(newPhone_s)) {
                    if (!isNetworkAvailable()) {
                        Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show();
                    } else {
                        new ChangePhoneNum.ConnectingToServer().execute("");
                    }
                }
                else{
                    Toast.makeText(this, "password not match", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private class ConnectingToServer extends AsyncTask<String, Integer, String> {

        private ProgressDialog dialog = new ProgressDialog(ChangePhoneNum.this);

        @Override
        protected void onPreExecute() {
            dialog.setTitle("Uploading");
            dialog.setMessage("Processing...");
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... url) {
            return requestServerForDataString();
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result != null) {
                Toast.makeText(ChangePhoneNum.this, result, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(ChangePhoneNum.this, "Try Again", Toast.LENGTH_SHORT).show();
            }

            dialog.dismiss();

            Log.e("DownloadTextTask", result);
        }
    }

    private String requestServerForDataString() {
        try {
            URL url = new URL(CHANGE_PHONE_NUM);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            //connection.connect();

            String reqHead = "Accept:application/json";
            connection.setRequestMethod("POST");
            connection.setRequestProperty("connection","Keep-Alive"+reqHead);
            //Header header = new Header();

            @SuppressWarnings("deprecation")
            MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
//            entity.addPart("register_type",new StringBody(id_s));

            entity.addPart("register_type", new StringBody(registrationType_s));
            entity.addPart("user_id",new StringBody(userId_s));
            entity.addPart("password", new StringBody(password_s));
            entity.addPart("phone_number_old", new StringBody(oldPhone_s));
            entity.addPart("phone_number_new", new StringBody(newPhone_s));

            connection.addRequestProperty("content-length",entity.getContentLength()+"");
            connection.addRequestProperty(entity.getContentType().getName(),entity.getContentType().getValue());

            OutputStream os = connection.getOutputStream();
            entity.writeTo(connection.getOutputStream());
            os.close();
            Log.e("HITTING","hitting url");
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

        return response;
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}
