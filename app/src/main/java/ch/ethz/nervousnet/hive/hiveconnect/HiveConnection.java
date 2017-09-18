package ch.ethz.nervousnet.hive.hiveconnect;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.util.Log;
import android.widget.TextView;

public class HiveConnection {
    //Default Constants
    private final String PROJECT_ID = "defaultProject";
    private final String USER_ID = "defaultUser";
    private final String TASK_ID = "defaultTask";
    private final String SCHEME = "http";
    private final String HOST = "10.0.2.2";
    private final String PORT = "8080";
    //Variables
    private String project_id = "";
    private String user_id = "";
    private String task_id = "";
    private String scheme = "";
    private String host = "";
    private String port = "";

    public HiveConnection() {
        this.project_id = PROJECT_ID;
        this.user_id = USER_ID;
        this.task_id = TASK_ID;
        this.scheme = SCHEME;
        this.host = HOST;
        this.port = PORT;
    }

    public HiveConnection(String project_id, String user_id, String task_id, String scheme, String host, String port) {
        this.project_id = project_id;
        this.user_id = user_id;
        this.task_id = task_id;
        this.scheme = scheme;
        this.host = host;
        this.port = port;
    }

    public String getProject_id() {
        return project_id;
    }

    public void setProject_id(String project_id) {
        this.project_id = project_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getTask_id() {
        return task_id;
    }

    public void setTask_id(String task_id) {
        this.task_id = task_id;
    }

    public String getScheme() {
        return scheme;
    }

    public void setScheme(String scheme) {
        this.scheme = scheme;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    //Return current Client
    //This has to be changed by the
    public Client getClient() {
        Log.d("HiveTest", "create a new Client to make a connection to the hive server");
        return new Client(scheme, host, port);
    }
}