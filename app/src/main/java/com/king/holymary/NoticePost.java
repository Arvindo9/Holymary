package com.king.holymary;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.king.holymary.database_sqlite.DataBaseAwake_1;
import com.king.holymary.server.AndroidMultiPartEntity;
import com.king.holymary.server.MyFTPClientFunctions;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.SQLException;

import static com.king.holymary.constant.Config.NOTICE_POST_UPLOAD;
import static com.king.holymary.constant.InitialInfo.NOTICE_TYPE;

/**
 * Created by Arvindo on 25-02-2017.
 * Company KinG
 * email at support@towardtheinfinity.com
 */

public class NoticePost extends Activity implements View.OnClickListener {

    private DataBaseAwake_1 db;
    private ProgressBar progressBar;
    private ProgressDialog pd;
    private long totalSize = 0;

    //    private String reqUrl = "http://192.168.0.22/hm/post.php";
    //    private String reqUrl = "http://192.168.1.65/hm/";
    private Button uploadData, sendingData;
    private EditText msgSub_et, msgBody_et;
    private TextView txtPercentage;

    private Context cntx = null;

    private long fileSize;
    private String massageType = "";

    private static final int SELECT_FILE = 1;
    private String fileName;
    private FileBody picBody;
    public static final String TAG = "MY MESSAGE";
    private File fileForUpload;
    private String fileExtention = "";
    private Spinner msg_Type_sp;
    private MyFTPClientFunctions ftpclient = null;
    private String userIdS = "", clgNameS = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notice_layout);
        uploadData = (Button) findViewById(R.id.uploadData);
        sendingData = (Button) findViewById(R.id.post_data);
        msgSub_et = (EditText) findViewById(R.id.subject);
        msgBody_et = (EditText) findViewById(R.id.message);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        txtPercentage = (TextView) findViewById(R.id.txtPercentage);
        db = new DataBaseAwake_1(this);

        msg_Type_sp = (Spinner)findViewById(R.id.msg_types);

        final ArrayAdapter<String> MsgtypesArrayAdapter = new ArrayAdapter<String>(getApplicationContext(),
                R.layout.marital_item, NOTICE_TYPE){
            @Override
            public boolean isEnabled(int position){
                return position != 0;
            }
            @Override
            public View getDropDownView(int position, View convertView,
                                        @NonNull ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position == 0){
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                }
                else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        MsgtypesArrayAdapter.setDropDownViewResource(R.layout.marital_item);
        msg_Type_sp.setAdapter(MsgtypesArrayAdapter);
        msg_Type_sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (msg_Type_sp.getSelectedItem().toString()){
                    case "EXAMINATION CELL": massageType = "2";
                        break;
                    case "MAGAZINE": massageType = "3";
                        break;
                    case "SPORTS": massageType = "4";
                        break;
                    case "LIBRARY": massageType = "5";
                        break;
                    case "SCHOLARSHIP": massageType = "6";
                        break;
                    case "TRAINING": massageType = "7";
                        break;
                    case "HIGHER EDUCATION CELL": massageType = "8";
                        break;
                    case "ACCOUNTS": massageType = "9";
                        break;
                    case "PLACEMENTS": massageType = "10";
                        break;

                    default: //DEFAULT
                        massageType = "1";
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                massageType = "1";
            }
        });
        ftpclient = new MyFTPClientFunctions();
        cntx = getBaseContext();

        uploadData.setOnClickListener(this);
        sendingData.setOnClickListener(this);
    }

    public void onClick(View view) {
        switch (view.getId()){
            case R.id.uploadData:
                showFileChooser();
                break;

            case R.id.post_data:
//                db = new DataBaseAwake_1(NoticePost.this);
                try {
                    String userType = db.userType();
                    if (userType.equals("2") && db.checkStatus()){
                        new NoticePost.SubmitToServer().execute(NOTICE_POST_UPLOAD);
                    }
                    else {
                        NoticePost.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(NoticePost.this, "Sorry! you can't be post",
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        try {
            userIdS = db.getTrueUserId();
            String[] data = db.getUserDetails(userIdS);
            if (data[3].equals("1")){
                clgNameS = data[0];
//                clgBranchS = data[2];
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("*/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, SELECT_FILE);
//        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            onSelectFromGalleryResult(data);
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void onSelectFromGalleryResult(Intent data) {
        if (data != null) {
            Uri u = data.getData();
            try {
                InputStream inputStream = this.getContentResolver().openInputStream(u);
                String s = String.valueOf(u.getPath());
                int l = s.length() - 1, i = 0;
                while ((':' != s.charAt(l) && '/' != s.charAt(l) && l > 0)) {
                    l--;
                }

                l++;
                char[] ch = new char[s.length() - l];

                while (l < s.length()) {
                    ch[i++] = s.charAt(l++);
                }
                fileName = String.valueOf(ch);
                final String outFileName;
                if(!fileName.contains(".")){
                    ContentResolver cR = this.getContentResolver();
                    MimeTypeMap mime = MimeTypeMap.getSingleton();
                    String type = mime.getExtensionFromMimeType(cR.getType(u));
                    outFileName = fileName + "." + type;
                    fileName = outFileName;
                }
                else{
                    outFileName = fileName;
                }

                ContextWrapper cw = new ContextWrapper(this.getApplicationContext());
                File mainDirectory = cw.getDir("temp_folder", Context.MODE_PRIVATE);

                File fileOutput = new File(mainDirectory, outFileName);

                assert inputStream != null;
                try (
                        // Create an input stream
                        BufferedInputStream input = new BufferedInputStream(inputStream);
                        // Create an output stream
                        BufferedOutputStream output = new BufferedOutputStream(new
                                FileOutputStream(fileOutput));
                ) {
                    // Continuously read a byte from input and write it to output
                    int r;
                    while ((r = input.read()) != -1) {
                        output.write((byte) r);
                    }
                    Log.e(String.valueOf(input), " " + String.valueOf(output));
                } catch (IOException e) {
                    e.printStackTrace();
                }

                fileForUpload = new File(mainDirectory, outFileName);
                fileExtention = outFileName.substring(outFileName.lastIndexOf(".") + 1, outFileName.length());
                if(fileForUpload.exists()){
                    Log.e("file exist", String.valueOf(fileForUpload));
                    picBody = new FileBody(fileForUpload);
                    fileSize = fileForUpload.length();
                }

                this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        TextView tv = (TextView) findViewById(R.id.file_name);
                        tv.setText(outFileName);
                    }
                });


            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }


    //-------------------------------------------------------

    private class SubmitToServer extends AsyncTask<String, Integer, String> {

        private ProgressDialog dialog = new ProgressDialog(NoticePost.this);
        String response;

        @Override
        protected void onPreExecute() {
            // setting progress bar to zero
            progressBar.setProgress(0);
            super.onPreExecute();
        }

        @SuppressLint("SetTextI18n")
        @Override
        protected void onProgressUpdate(Integer... progress) {
            // Making progress bar visible
            progressBar.setVisibility(View.VISIBLE);

            // updating progress bar value
            progressBar.setProgress(progress[0]);

            // updating percentage value
            txtPercentage.setText(String.valueOf(progress[0]) + "%");
        }

        @Override
        protected String doInBackground(String... uid) {
//            return sendData(url[0]);
            if (fileSize > 27262976) {
                if (isOnline(NoticePost.this)) {
                    int ok = connectToFTPAddress();
                    if (ok == 0) {
                        return uploadFileFTP(uid[0]);
                    } else {
                        return "fails";
                    }
                } else {
                    NoticePost.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(NoticePost.this,
                                    "Please check your internet connection!",
                                    Toast.LENGTH_LONG).show();
                        }
                    });
                    return "no internet";
                }
            } else {
                return uploadFile(uid[0]);
            }
        }

        @Override
        protected void onPostExecute(String result) {
            Log.e(TAG, "Response from server: " + result);

            // showing the server response in an alert dialog
            showAlert(result);
            super.onPostExecute(result);
            if (result != null) {
                dialog.dismiss();
                Toast.makeText(NoticePost.this, result, Toast.LENGTH_SHORT).show();
            } else
                Toast.makeText(NoticePost.this, "Try Again", Toast.LENGTH_SHORT).show();

//            Intent i = new Intent(MainActivity.this,LogIn.class);
//            startActivity(i);
//            Toast.makeText(getBaseContext(), result, Toast.LENGTH_LONG).show();
            Log.d("DownloadTextTask", result);
        }

        @SuppressWarnings("deprecation")
        private String uploadFile(String userId1) {
            String responseString = null;

            String folder = "";
            switch (fileExtention) {
                case "jpg":
                case "png":
                case "jpeg": {
                    folder = "1";
                }
                break;

                default: {
                    folder = "2";
                }
                break;
            }

            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(NOTICE_POST_UPLOAD);

            try {
                AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
                        new AndroidMultiPartEntity.ProgressListener() {

                            @Override
                            public void transferred(long num) {
                                publishProgress((int) ((num / (float) totalSize) * 100));
                            }
                        });

                // Adding file data to http body
                entity.addPart("check_type", new StringBody("true"));
                entity.addPart("folder_path", new StringBody(folder));
                entity.addPart("user_id", new StringBody(userIdS));
                entity.addPart("college_name", new StringBody(clgNameS));
                entity.addPart("msg_type", new StringBody(massageType));
                entity.addPart("msg_header", new StringBody(msgSub_et.getText().toString()));
                entity.addPart("msg_body", new StringBody(msgBody_et.getText().toString()));
                if(picBody != null) {
                    if(folder.equals("1")) {
                        entity.addPart("msg_image", picBody);
                    }
                    else if(folder.equals("2")) {
                        entity.addPart("msg_file", picBody);
                    }
                }

                totalSize = entity.getContentLength();
                httppost.setEntity(entity);

                // Making server call
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity r_entity = response.getEntity();

                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == 200) {
                    // Server response
                    responseString = EntityUtils.toString(r_entity);
                } else {
                    responseString = "Error occurred! Http Status Code: "
                            + statusCode;
                }

            } catch (ClientProtocolException e) {
                responseString = e.toString();
            } catch (IOException e) {
                responseString = e.toString();
            }

            Log.e("method used " + "no", String.valueOf(fileSize));
            return responseString;
        }

        private String uploadFileFTP(String userId) {

            try {
                URL url = new URL(NOTICE_POST_UPLOAD);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                //connection.connect();
                String reqHead = "Accept:application/json";
                connection.setRequestMethod("POST");
                connection.setRequestProperty("connection", "Keep-Alive" + reqHead);
                connection.setDoInput(true);
                connection.setDoOutput(true);
                connection.setUseCaches(false);
                //Header header = new Header();

                AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
                        new AndroidMultiPartEntity.ProgressListener() {

                            @Override
                            public void transferred(long num) {
                                publishProgress((int) ((num / (float) totalSize) * 100));
                            }
                        });

//                MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
                entity.addPart("check_type", new StringBody("false"));
                entity.addPart("user_id", new StringBody(userIdS));
                entity.addPart("college_name", new StringBody(clgNameS));
                entity.addPart("msg_header", new StringBody(msgSub_et.getText().toString()));
                entity.addPart("msg_body", new StringBody(msgBody_et.getText().toString()));
                entity.addPart("msg_file", new StringBody(fileName));
                connection.addRequestProperty("content-length", entity.getContentLength() + "");
                connection.addRequestProperty(entity.getContentType().getName(), entity.getContentType().getValue());

                BufferedOutputStream os = new BufferedOutputStream(connection.getOutputStream());

                entity.writeTo(connection.getOutputStream());
                os.close();
                Log.d("HITTING", "hitting url");
                connection.connect();

                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    String response = readStream(connection.getInputStream());

                    if (response.charAt(response.length() - 1) == '1') {
                        ftpUploading();
//                        runProgress();
//                        boolean status = false;
//                        status = ftpclient.ftpUploadFile(fileForUpload, fileName, "/", cntx);
//
//                        if (status) {
//                            Log.d(TAG, "Upload success");
//                            ftpclient.ftpDisconnect();
//                            dismissProgress();
//                        } else {
//                            Log.d(TAG, "Upload failed");
//                            ftpclient.ftpDisconnect();
//                            handler.sendEmptyMessage(-1);
//                            dismissProgress();
//                        }
                    } else {
                        response = "network error";
                        ftpclient.ftpDisconnect();
                    }

                    return response;
                }

            } catch (IOException e) {
                ftpclient.ftpDisconnect();
                e.printStackTrace();
            }
            catch (Exception e){
                e.printStackTrace();
            }


            Log.e("method used " + "ftp", String.valueOf(fileSize));
            return "fails";
        }
    }

    private void runProgress(){
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                pd = ProgressDialog.show(NoticePost.this, "", "Uploading...",
                        true, false);
            }
        });
    }

    private void dismissProgress(){
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (pd != null && pd.isShowing()) {
                    pd.dismiss();
                }
            }
        });

    }

    private void ftpUploading(){

        runProgress();
        new Thread(new Runnable() {
            public void run() {
                boolean status = false;
                status = ftpclient.ftpUploadFile(fileForUpload, fileName, "/", cntx);

                if (status) {
                    Log.d(TAG, "Upload success");
                    ftpclient.ftpDisconnect();
                    handler.sendEmptyMessage(2);
                } else {
                    Log.d(TAG, "Upload failed");
                    handler.sendEmptyMessage(-1);
                }
            }
        }).start();
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

    private int connectToFTPAddress() {
        switch (fileExtention) {
            case "jpg":
            case "png":
            case "jpeg": {
                final String host = "ftp.steinmetzils.com";
                final String username = "holymarymi@vchmgi.com";
                final String password = "DDiJk+p;LMMN";

                boolean status = false;
                status = ftpclient.ftpConnect(host, username, password, 21);
                if (status) {
                    Log.d(TAG, "Connection Success");
                    handler.sendEmptyMessage(0);
                    return 0;
                } else {
                    Log.d(TAG, "Connection failed");
                    handler.sendEmptyMessage(-1);
                    return 1;
                }
            }
            case "pdf":
            case "dox":
            case "docx":
            case "ppt":
            case "txt": {
                final String host = "ftp.steinmetzils.com";
                final String username = "holymarymf@vchmgi.com";
                final String password = "+d_A=atIC&7M";

                boolean status = false;
                status = ftpclient.ftpConnect(host, username, password, 21);
                if (status) {
                    Log.d(TAG, "Connection Success");
                    handler.sendEmptyMessage(0);
                    return 0;
                } else {
                    Log.d(TAG, "Connection failed");
                    handler.sendEmptyMessage(-1);
                    return 1;
                }
            }
            default: {
                final String host = "ftp.steinmetzils.com";
                final String username = "holymarymv@vchmgi.com";
                final String password = "v80Z_S;N7ZeN";

                boolean status = false;
                status = ftpclient.ftpConnect(host, username, password, 21);
                if (status) {
                    Log.d(TAG, "Connection Success");
                    handler.sendEmptyMessage(0);
                    return 0;
                } else {
                    Log.d(TAG, "Connection failed");
                    handler.sendEmptyMessage(-1);
                    return 1;
                }
            }
        }
    }

    /**
     * Method to show alert dialog
     * */
    private void showAlert(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
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

    private boolean isOnline(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnected();
    }

    private Handler handler = new Handler() {

        public void handleMessage(android.os.Message msg) {

            if (msg.what == 0) {
                //if connect
            } else if (msg.what == 1) {
                //not connect
                dismissProgress();
            }
            else if (msg.what == 2) {
                Toast.makeText(NoticePost.this, "Uploaded Successfully!",
                        Toast.LENGTH_LONG).show();
                dismissProgress();
            } else if (msg.what == 3) {
                Toast.makeText(NoticePost.this, "Disconnected Successfully!",
                        Toast.LENGTH_LONG).show();
                dismissProgress();
            } else {
                Toast.makeText(NoticePost.this, "Unable to Perform Action!",
                        Toast.LENGTH_LONG).show();
                dismissProgress();
            }

        }

    };

}
