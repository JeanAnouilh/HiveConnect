package ch.ethz.nervousnet.hive.hiveconnect;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;

public class MainActivity extends AppCompatActivity {
    //App-UI
    //important variables for App-UI
    TextView tvConnectionJson;
    Button btnSetUserInfo;
    Button btnGetUserInfo;
    Button btnGetUserAssignment;
    Button btnReturnUserAssignment;

    //Constants
    final String PROJECT_ID = "basic";
    final String USER_ID = "dario";
    final String TASK_ID = "basic-record";
    final String SCHEME = "http";
    final String HOST = "10.0.2.2";
    final String PORT = "8080";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        tvConnectionJson = (TextView) findViewById(R.id.tv_connection_json);
        btnSetUserInfo = (Button) findViewById(R.id.btn_set_user_info);
        btnGetUserInfo = (Button) findViewById(R.id.btn_get_user_info);
        btnGetUserAssignment = (Button) findViewById(R.id.btn_get_user_assignment);
        btnReturnUserAssignment = (Button) findViewById(R.id.btn_return_user_assignment);
        final HiveConnection HIVECONNECTION = new HiveConnection(PROJECT_ID, USER_ID, TASK_ID, SCHEME, HOST, PORT);

        btnSetUserInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // creates JSON user File
                Log.d("HiveTest", "PressSetUserInfo");
                JSONObject jObject = new JSONObject();
                try {
                    jObject.put("Id", "dario");
                    jObject.put("Name", "Dario Leuchtmann");
                    jObject.put("Email", "ldario@student.ethz.ch");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //new CreateUser().execute();
                Client client = HIVECONNECTION.getClient();
                String response = client.createUser(PROJECT_ID,jObject);
                tvConnectionJson.setText(response);
                Log.d("HiveTest", response);
            }
        });
        btnGetUserInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("HiveTest", "PressGetUserInfo");
                Client client = HIVECONNECTION.getClient();
                String response = client.user(PROJECT_ID, USER_ID);
                tvConnectionJson.setText(response);
                Log.d("HiveTest", response);
            }
        });
        btnGetUserAssignment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("HiveTest", "PressGetUserAssignment");
                Client client = HIVECONNECTION.getClient();
                String response = client.userAssignment(PROJECT_ID,USER_ID,TASK_ID);
                tvConnectionJson.setText(response);
                Log.d("HiveTest", response);
            }
        });
        btnReturnUserAssignment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // creates JSON user File
                Log.d("HiveTest", "PressReturnUserAssignment");
                JSONObject assignment = null;
                try {
                    assignment = new JSONObject((String) tvConnectionJson.getText().toString());
                    assignment.put("State", "finished");
                    record = new JSONObject();
                    record.put("end", "2016-06-01T12:10:00Z");
                    record.put("sensors", new String[]{"accelerometer"});
                    record.put("start", "2016-06-01T12:00:00Z");
                    record.put("step", 1000);
                    record.put("accelerometer", new double[]{0.0, 1.0, 0.0, -1.0, 0.0, 1.0, 0.0});
                    assignment.put("record",record);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //new CreateUser().execute();
                Client client = HIVECONNECTION.getClient();
                String response = client.userCreateAssignment(PROJECT_ID,assignment,TASK_ID,USER_ID);
                tvConnectionJson.setText(response);
                Log.d("HiveTest", response);
            }
        });
    }



    BufferedReader reader = null;

    //JSON Files
    //TODO get them as Parameters
    JSONObject project = new JSONObject();
    JSONArray tasks = new JSONArray();
    JSONObject task = new JSONObject();
    JSONArray assets = new JSONArray();
    JSONObject asset = new JSONObject();
    JSONObject assignmentCriteria = new JSONObject();
    JSONObject completionCriteria = new JSONObject();
    JSONObject submittedData = new JSONObject();
    JSONObject record = new JSONObject();
    JSONObject recordMetadata = new JSONObject();
    JSONObject metadata = new JSONObject();
    String[] sensors = {"accelerometer"};
    private void CreateJsonFiles() {
        //SubmittedData
        try {
            submittedData.put("record",record);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //AssignmentCriteria
        try {
            assignmentCriteria.put("SubmittedData",submittedData);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //CompletionCriteria
        try {
            completionCriteria.put("Total",100);
            completionCriteria.put("Matching",100);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //Project
        try {
            project.put("Id","basic");
            project.put("Name", "Basic");
            project.put("Description", "Basic Nervousnet data collection");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //Task
        try {
            task.put("Name", "record");
            task.put("Description", "Recording data for specified sensors and time interval");
            task.put("CurrentState", "available");
            task.put("AssignmentCriteria", assignmentCriteria);
            task.put("CompletionCriteria", completionCriteria);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //Tasks
        tasks.put(task);
        //RecordMetadata
        try {
            recordMetadata.put("sensors",sensors);
            recordMetadata.put("start", "2016-06-01T12:00:00Z");
            recordMetadata.put("end", "2016-06-01T12:10:00Z");
            recordMetadata.put("step", 1000);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //Metadata
        try {
            metadata.put("record",recordMetadata);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //Asset
        try {
            asset.put("Name","Data Set 1");
            asset.put("Url", "http://erdw.ethz.ch/nervousnet/basic/intro.html");
            asset.put("Metadata", metadata);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //Assets
        assets.put(asset);
    }
}