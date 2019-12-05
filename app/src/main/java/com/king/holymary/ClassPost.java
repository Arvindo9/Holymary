package com.king.holymary;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
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
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;

import static com.king.holymary.constant.Config.CLASS_POST;
import static com.king.holymary.constant.InitialInfo.BRANCH_STREAM_NAME;
import static com.king.holymary.constant.InitialInfo.CLASS_SECTION;
import static com.king.holymary.constant.InitialInfo.YEAR_SEM;

/**
 * Created by Arvindo on 06-04-2017.
 * Company KinG
 * email at support@towardtheinfinity.com
 */

public class ClassPost extends Activity implements View.OnClickListener{

    private ProgressBar progressBar;
    private long totalSize = 0;
    private TextView txtPercentage;
    private long fileSize;
    private static final int SELECT_FILE = 1;
    private String fileName;
    private FileBody picBody;
    public static final String TAG = "MY MESSAGE";
    private File fileForUpload;
    private String fileExtention = "";
    private String userIdS = "", clgNameS = "", clgDeptS = "", clgBranchS = "";
    private String msgHeadS, msgBodyS, msgFileS, msgPicS;
    private String yeadS, sectionS;
    private DataBaseAwake_1 db;
    private EditText messageHeaderE, messageBodyE;
    private Spinner branchSp, yearSp, secSp;
    private Button uploadDate;
    private Button sendingData;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.class_post);

        messageHeaderE = (EditText) findViewById(R.id.subject);
        messageBodyE = (EditText) findViewById(R.id.message);
        branchSp = (Spinner) findViewById(R.id.branch_c);
        yearSp = (Spinner) findViewById(R.id.year_c);
        secSp = (Spinner) findViewById(R.id.sec_c);


        uploadDate = (Button) findViewById(R.id.uploadData);
        sendingData = (Button) findViewById(R.id.post_data);

        uploadDate.setOnClickListener(this);
        sendingData.setOnClickListener(this);

        db = new DataBaseAwake_1(ClassPost.this);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        txtPercentage = (TextView) findViewById(R.id.txtPercentage);

        final ArrayAdapter<String> typesArrayAdapter = new ArrayAdapter<String>(getApplicationContext(),
                R.layout.marital_item, BRANCH_STREAM_NAME) {
            @Override
            public boolean isEnabled(int position) {
                return position != 0;
            }

            @Override
            public View getDropDownView(int position, View convertView,
                                        @NonNull ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (position == 0) {
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        typesArrayAdapter.setDropDownViewResource(R.layout.marital_item);
        branchSp.setAdapter(typesArrayAdapter);
        branchSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case 1: clgBranchS = "CSE";
                        break;
                    case 2: clgBranchS = "ECE";
                        break;
                    case 3: clgBranchS = "EEE";
                        break;
                    case 4: clgBranchS = "CIVIL";
                        break;
                    case 5: clgBranchS = "MEC";
                        break;
                    case 6:clgBranchS = "MINING";
                        break;
                    default:clgBranchS = "MINING";
                        break;
                }
//                tspinner = adapterView.getItemAtPosition(i).toString();
//                tspinner = type.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(getApplicationContext(), "Please Select Type", Toast.LENGTH_SHORT).show();
            }
        });
        //---------------------------------\
        final ArrayAdapter<String> typesArrayAdapterY = new ArrayAdapter<String>(getApplicationContext(),
                R.layout.marital_item, YEAR_SEM) {
            @Override
            public boolean isEnabled(int position) {
                return position != 0;
            }

            @Override
            public View getDropDownView(int position, View convertView,
                                        @NonNull ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (position == 0) {
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        typesArrayAdapterY.setDropDownViewResource(R.layout.marital_item);
        yearSp.setAdapter(typesArrayAdapterY);
        yearSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case 1: yeadS = "1 year";
                        break;
                    case 2: yeadS = "2 year";
                        break;
                    case 3: yeadS = "3 year";
                        break;
                    case 4: yeadS = "4 year";
                        break;
                    default:yeadS = "1 year";
                        break;
                }
//                tspinner = adapterView.getItemAtPosition(i).toString();
//                tspinner = type.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(getApplicationContext(), "Please Select Type", Toast.LENGTH_SHORT).show();
            }
        });
        //-----------------------------------------
        final ArrayAdapter<String> typesArrayAdapterSec = new ArrayAdapter<String>(getApplicationContext(),
                R.layout.marital_item, CLASS_SECTION) {
            @Override
            public boolean isEnabled(int position) {
                return position != 0;
            }

            @Override
            public View getDropDownView(int position, View convertView,
                                        @NonNull ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (position == 0) {
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        typesArrayAdapterSec.setDropDownViewResource(R.layout.marital_item);
        secSp.setAdapter(typesArrayAdapterSec);
        secSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case 1: sectionS = "A";
                        break;
                    case 2: sectionS = "B";
                        break;
                    case 3: sectionS = "C";
                        break;
                    case 4: sectionS = "D";
                        break;
                    default:sectionS = "E";
                        break;
                }
//                tspinner = adapterView.getItemAtPosition(i).toString();
//                tspinner = type.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(getApplicationContext(), "Please Select Type", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        try {
            userIdS = db.getTrueUserId();
            String[] data = db.getUserDetails(userIdS);
            if (data[3].equals("1")){
                clgNameS = data[0];
                clgDeptS = data[1];
//                clgBranchS = data[2];
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void onClick(View view) {
        switch (view.getId()){
            case R.id.uploadData:
                showFileChooser();
                break;

            case R.id.post_data:
                try {
                    String userType = db.userType();
                    if (userType.equals("2") && db.checkStatus()){
                        new ClassPost.SubmitToServer().execute(db.getTrueUserId());
                    }
                    else {
                        ClassPost.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(ClassPost.this, "Sorry! you can't be post", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                break;
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
                InputStream inputStream = ClassPost.this.getContentResolver().openInputStream(u);
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
                    ContentResolver cR = getContentResolver();
                    MimeTypeMap mime = MimeTypeMap.getSingleton();
                    String type = mime.getExtensionFromMimeType(cR.getType(u));
                    outFileName = fileName + "." + type;
                    fileName = outFileName;
                }
                else{
                    outFileName = fileName;
                }

                ContextWrapper cw = new ContextWrapper(getApplicationContext());
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

                ClassPost.this.runOnUiThread(new Runnable() {
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

    //-----------------------------------------------------------------------------------

    private class SubmitToServer extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {
            // setting progress bar to zero

            msgBodyS = messageBodyE.getText().toString();
            msgHeadS = messageHeaderE.getText().toString();

            progressBar.setProgress(0);
            super.onPreExecute();

            ClassPost.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(ClassPost.this, "dept: " + clgDeptS + "\nclg: " + clgNameS,
                            Toast.LENGTH_SHORT).show();
                }
            });
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
        protected String doInBackground(String... url) {
//            return sendData(url[0]);
            return uploadFile(url[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            Log.e(TAG, "Response from server: " + result);

            // showing the server response in an alert dialog
            showAlert(result);
            super.onPostExecute(result);
            if (result != null) {
                Toast.makeText(ClassPost.this, result, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(ClassPost.this, "Try Again", Toast.LENGTH_SHORT).show();
            }

            Log.d("DownloadTextTask", result);
        }

        @SuppressWarnings("deprecation")
        private String uploadFile(String userID) {
            String responseString = null;

            String folder = "";
            switch (fileExtention) {
                case "jpg":
                case "png":
                case "jpeg": {
                    folder = "1";
                }
                break;
                case "pdf":
                case "dox":
                case "docx":
                case "ppt":
                case "txt": {
                    folder = "2";
                }
                break;

                default: {
                    folder = "4";
                }
                break;
            }

            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(CLASS_POST);

            try {
                AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
                        new AndroidMultiPartEntity.ProgressListener() {
                            @Override
                            public void transferred(long num) {
                                publishProgress((int) ((num / (float) totalSize) * 100));
                            }
                        });

                // Adding file data to http body
                entity.addPart("folder_path", new StringBody(folder));
                entity.addPart("user_id", new StringBody(userIdS));
                entity.addPart("college_name", new StringBody(clgNameS));
                entity.addPart("branch_name", new StringBody(clgBranchS));
                entity.addPart("department_name", new StringBody(clgDeptS));
                entity.addPart("current_year", new StringBody(yeadS));
//                entity.addPart("current_semester", new StringBody(semS));
                entity.addPart("current_section", new StringBody(sectionS));
                entity.addPart("msg_header", new StringBody(msgHeadS));
                entity.addPart("msg_body", new StringBody(msgBodyS));
                if (!folder.equals("4")) {
                    entity.addPart("msg_file", picBody);
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
    }

    /**
     * Method to show alert dialog
     * */
    private void showAlert(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ClassPost.this);
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
}
