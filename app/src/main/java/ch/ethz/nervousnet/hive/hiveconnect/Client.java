package ch.ethz.nervousnet.hive.hiveconnect;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

/**
 * Created by Dario on 15.07.17.
 */

public class Client {
    //Variables
    private String scheme;
    private String host;
    private String port;
    private String domain;
    private HttpGet httpGet;
    private HttpGetWithUser httpGetWithUser;
    private HttpPost httpPost;
    private HttpPostWithUser httpPostWithUser;
    private Connection connection;

    //Constructors
    public Client(String scheme, String host, String port) {
        this.scheme = scheme;
        this.host = host;
        this.port = port;
        if (port.length() > 0) this.domain = scheme + "://" + host + ":" + port;
        else this.domain = scheme + "://" + host;
        initializeHttpFunctions();
    }

    //Initialize Http Functions
    private void initializeHttpFunctions() {
        connection = new Connection(domain);
        httpGet = new HttpGet();
        httpGetWithUser = new HttpGetWithUser();
        httpPost = new HttpPost();
        httpPostWithUser = new HttpPostWithUser();
    }

    //Getter
    public String getScheme() {
        return scheme;
    }

    public String getHost() {
        return host;
    }

    public String getPort() {
        return port;
    }

    public String getDomain() {
        return domain;
    }

    //Setter
    public void setScheme(String scheme) {
        this.scheme = scheme;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    //
    //    ---- Admin interface
    //

    //
    //    Setup
    //

    String adminRoot(String projectId, String taskId) {
        httpGet.setHiveEndpoint("/");
        String serverResponse = "";
        try {
            serverResponse = httpGet.execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return serverResponse;
    }

    // AdminSetup -- finds or creates an unfinished task assignment for the current user
    // /admin/setup/{DELETE_MY_DATABASE} [put]
    String adminSetup(Boolean resetDb, JSONObject project, JSONArray tasks, JSONArray assets) {
        if(resetDb) httpPost.setHiveEndpoint("/admin/setup/YES_I_AM_SURE");
        else httpPost.setHiveEndpoint("/admin/setup");
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("Project", project);
            jsonObject.put("Tasks", tasks);
            jsonObject.put("Assets", assets);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        httpPost.setJsonObject(jsonObject);
        String serverResponse = "";
        try {
            serverResponse = httpPost.execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return serverResponse;
    }

    //
    //    Projects
    //

    // AdminProjects -- returns a paginated list of projects in Hive
    // /admin/projects [get]
    String adminProjects(String projectId, String taskId) {
        httpGet.setHiveEndpoint("/admin/projects");
        String serverResponse = "";
        try {
            serverResponse = httpGet.execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return serverResponse;
    }

    // AdminProject -- returns a project by ID
    // /admin/projects/{project_id} [get]
    String adminProject(String projectId, String taskId) {
        httpGet.setHiveEndpoint("/admin/projects/" + projectId);
        String serverResponse = "";
        try {
            serverResponse = httpGet.execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return serverResponse;
    }

    // AdminCreateProject -- creates or updates a project
    // /admin/projects/{project_id} [post]
    String adminCreateProject(JSONObject project, String projectId) {
        httpPost.setHiveEndpoint("/admin/projects/" + projectId);
        httpPost.setJsonObject(project);
        String serverResponse = "";
        try {
            serverResponse = httpPost.execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return serverResponse;
    }

    //
    //    Tasks
    //

    // AdminTasks -- returns a paginated list of tasks in a project
    // /admin/projects/{project_id}/tasks [get]
    String adminTasks(String projectId, String taskId) {
        httpGet.setHiveEndpoint("/admin/projects/" + projectId + "/tasks/");
        String serverResponse = "";
        try {
            serverResponse = httpGet.execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return serverResponse;
    }

    // AdminTask -- returns info for a single task by ID
    // /admin/projects/{project_id}/tasks/{task_id} [get]
    String adminTask(String projectId, String taskId) {
        httpGet.setHiveEndpoint("/admin/projects/" + projectId + "/tasks/" + taskId);
        String serverResponse = "";
        try {
            serverResponse = httpGet.execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return serverResponse;
    }

    // AdminCreateTasks -- creates or updates tasks in a project
    // /admin/projects/{project_id}/tasks [post]
    String adminCreateTasks(JSONArray tasks, String projectId) {
        httpPost.setHiveEndpoint("/admin/projects/" + projectId + "/tasks");
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("Tasks", tasks);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        httpPost.setJsonObject(jsonObject);
        String serverResponse = "";
        try {
            serverResponse = httpPost.execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return serverResponse;
    }

    // AdminCreateTask -- creates or updates a task in a project
    // /admin/projects/{project_id}/tasks/{task_id} [post]
    String adminCreateTask(JSONObject task, String projectId, String taskId) {
        httpPost.setHiveEndpoint("/admin/projects/" + projectId + "/tasks/" + taskId);
        httpPost.setJsonObject(task);
        String serverResponse = "";
        try {
            serverResponse = httpPost.execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return serverResponse;
    }

    // AdminCompleteTask -- updates assets matching task CompletionCriteria with SubmittedData
    // /admin/projects/{project_id}/tasks/{task_id}/complete [get]
    String adminCompleteTask(String projectId, String taskId) {
        httpGet.setHiveEndpoint("/admin/projects/" + projectId + "/tasks/" + taskId + "/complete");
        String serverResponse = "";
        try {
            serverResponse = httpGet.execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return serverResponse;
    }

    // AdminDisableTask -- makes a task unavailable for assignment by disabling it
    // /admin/projects/{project_id}/tasks/{task_id}/disable [get]
    String adminDisableTask(String projectId, String taskId) {
        httpGet.setHiveEndpoint("/admin/projects/" + projectId + "/tasks/" + taskId + "/disable");
        String serverResponse = "";
        try {
            serverResponse = httpGet.execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return serverResponse;
    }

    // AdminEnableTask -- makes a task available for assignment by enabling it
    // /admin/projects/{project_id}/tasks/{task_id}/enable [get]
    String adminEnableTask(String projectId, String taskId) {
        httpGet.setHiveEndpoint("/admin/projects/" + projectId + "/tasks/" + taskId + "/enable");
        String serverResponse = "";
        try {
            serverResponse = httpGet.execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return serverResponse;
    }

    //
    //    Assets
    //

    // AdminAssets -- returns a paginated list of assets in a project
    // /admin/projects/{project_id}/assets [get]
    String adminAssets(String projectId) {
        httpGet.setHiveEndpoint("/admin/projects/" + projectId + "/assets");
        String serverResponse = "";
        try {
            serverResponse = httpGet.execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return serverResponse;
    }

    // AdminAsset -- retrieves a single project asset defined by an id
    // /admin/projects/{project_id}/assets/{asset_id} [get]
    String adminAsset(String projectId, String assetId) {
        httpGet.setHiveEndpoint("/admin/projects/" + projectId + "/assets/" + assetId);
        String serverResponse = "";
        try {
            serverResponse = httpGet.execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return serverResponse;
    }

    // AdminCreateAssets -- creates assets in a project
    // /admin/projects/{project_id}/assets [post]
    String adminCreateAssets(JSONArray assets, String projectId) {
        httpPost.setHiveEndpoint("/admin/projects/" + projectId + "/assets");
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("Assets", assets);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        httpPost.setJsonObject(jsonObject);
        String serverResponse = "";
        try {
            serverResponse = httpPost.execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return serverResponse;
    }

    //
    //    Users
    //

    // AdminUsers -- returns a paginated list of users in a project
    // /admin/projects/{project_id}/users [get]
    String adminUsers(String projectId) {
        httpGet.setHiveEndpoint("/admin/projects/" + projectId + "/users/");
        String serverResponse = "";
        try {
            serverResponse = httpGet.execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return serverResponse;
    }

    // AdminUser -- returns a single user in a project by ID
    // /admin/projects/{project_id}/users/{user_id} [get]
    String adminUser(String projectId, String userId) {
        httpGet.setHiveEndpoint("/admin/projects/" + projectId + "/users/" + userId);
        String serverResponse = "";
        try {
            serverResponse = httpGet.execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return serverResponse;
    }

    //
    //    Assignments
    //

    // AdminAssignments -- returns a paginated list of assignments in a task
    // /admin/projects/{project_id}/assignments [get]
    String adminAssignments(String projectId) {
        httpGet.setHiveEndpoint("/admin/projects/" + projectId + "/assignments/");
        String serverResponse = "";
        try {
            serverResponse = httpGet.execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return serverResponse;
    }

    //
    //    ---- User interface
    //

    //
    //    Projects
    //

    // Project -- returns a project by ID
    // /projects/{project_id} [get]
    String project(String projectId) {
        httpGet.setHiveEndpoint("/projects/" + projectId);
        String serverResponse = "";
        try {
            serverResponse = httpGet.execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return serverResponse;
    }

    //
    //    Tasks
    //

    // Tasks -- returns a paginated list of tasks in a project
    // /projects/{project_id}/tasks [get]
    String tasks(String projectId) {
        httpGet.setHiveEndpoint("/projects/" + projectId + "/tasks/");
        String test = "";
        try {
            test = httpGet.execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return test;
    }

    // Task -- returns public info for a single task by ID
    // /projects/{project_id}/tasks/{task_id} [get]
    String task(String projectId, String taskId) {
        httpGet.setHiveEndpoint("/projects/" + projectId + "/tasks/" + taskId);
        String serverResponse = "";
        try {
            serverResponse = httpGet.execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return serverResponse;
    }

    //
    //    Assets
    //

    // Asset -- returns public info for a single asset by ID
    // /projects/{project_id}/assets/{asset_id} [get]
    String asset(String projectId, String taskId) {
        httpGet.setHiveEndpoint("/projects/" + projectId + "/assets/" + taskId);
        String serverResponse = "";
        try {
            serverResponse = httpGet.execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return serverResponse;
    }

    //
    //    Users
    //

    // User -- returns info for the current user, creating a matching record if none found
    // /projects/{project_id}/user [get]
    String user(String projectId, String userId) {
        httpGetWithUser.setHiveEndpoint("/projects/" + projectId + "/user");
        httpGetWithUser.setUserId(userId);

        httpGetWithUser.setProjectId(projectId);
        String serverResponse = "";
        try {
            serverResponse = httpGetWithUser.execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return serverResponse;
    }

    // CreateUser -- creates a user in a project
    // /projects/{project_id}/user [post]
    String createUser(String projectId, JSONObject user) {
        httpPost.setHiveEndpoint("/projects/" + projectId + "/user");
        httpPost.setJsonObject(user);
        String serverResponse = "";
        try {
            serverResponse = httpPost.execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return serverResponse;
    }

    // ExternalUser -- finds or creates a user by external ID
    // /projects/{project_id}/user/external/{connect} [post]
    String externalUser(String projectId, JSONObject user) {
        httpPost.setHiveEndpoint("/projects/" + projectId + "/user/external");
        httpPost.setJsonObject(user);
        String serverResponse = "";
        try {
            serverResponse = httpPost.execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return serverResponse;
    }

    // Favorites -- returns a paginated list of favorited assets for the current user
    // /projects/{project_id}/user/favorites [get]
    String favorites(String projectId, String userId) {
        httpGetWithUser.setHiveEndpoint("/projects/" + projectId + "/user/favorites");
        httpGetWithUser.setUserId(userId);
        httpGetWithUser.setProjectId(projectId);
        String serverResponse = "";
        try {
            serverResponse = httpGetWithUser.execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return serverResponse;
    }

    // Favorite -- toggles favoriting on an asset for the current user
    // /projects/{project_id}/assets/{asset_id}/favorite [get]
    String favorite(String projectId, String userId, String assetId) {
        httpGetWithUser.setHiveEndpoint("/projects/" + projectId + "/assets/" + assetId + "/favorite");
        httpGetWithUser.setUserId(userId);
        httpGetWithUser.setProjectId(projectId);
        String serverResponse = "";
        try {
            serverResponse = httpGetWithUser.execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return serverResponse;
    }

    //
    //    Assignments
    //

    // Assignment -- returns public info for a single assignment by ID
    // /projects/{project_id}/assignments/{assignment_id} [get]
    String assignment(String projectId, String assignmentId) {
        httpGet.setHiveEndpoint("/projects/" + projectId + "/assignments/" + assignmentId);
        String serverResponse = "";
        try {
            serverResponse = httpGet.execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return serverResponse;
    }

    // AssignAsset -- finds or creates an unfinished assignment for the given asset, task and current user
    // /projects/{project_id}/tasks/{task_id}/assets/{asset_id}/assignments [get]
    String assignAsset(String projectId, String taskId, String assetId, String userId) {
        httpGetWithUser.setHiveEndpoint("/projects/" + projectId + "/tasks/" + taskId + "/assets/" + assetId + "/assignments");
        httpGetWithUser.setUserId(userId);
        httpGetWithUser.setProjectId(projectId);
        String serverResponse = "";
        try {
            serverResponse = httpGetWithUser.execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return serverResponse;
    }

    // UserAssignment -- finds or creates an unfinished task assignment for the current user
    // /projects/{project_id}/tasks/{task_id}/assignments [get]
    String userAssignment(String projectId, String userId, String taskId) {
        httpGetWithUser.setHiveEndpoint("/projects/" + projectId + "/tasks/" + taskId + "/assignments");
        httpGetWithUser.setUserId(userId);
        httpGetWithUser.setProjectId(projectId);
        String serverResponse = "";
        try {
            serverResponse = httpGetWithUser.execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return serverResponse;
    }

    // UserCreateAssignment -- finishes a task assignment & assigns a new one for the current user
    // /projects/{project_id}/tasks/{task_id}/assignments [post]
    // CreateUser -- creates a user in a project
    // /projects/{project_id}/user [post]
    String userCreateAssignment(String projectId, JSONObject user, String taskId, String userId) {
        httpPostWithUser.setHiveEndpoint("/projects/" + projectId + "/tasks/" + taskId + "/assignments");
        httpPostWithUser.setJsonObject(user);
        httpPostWithUser.setUserId(userId);
        httpPostWithUser.setProjectId(projectId);
        String serverResponse = "";
        try {
            serverResponse = httpPostWithUser.execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return serverResponse;
    }

    public class HttpGet extends AsyncTask<Void, Void, String> {
        //Class Variables
        private String hiveEndpoint = "";

        @Override
        protected String doInBackground(Void... params) {
            URL url = null;
            try {
                // Construct the URL for the Nervousnet query
                url = new URL(domain + hiveEndpoint);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            return connection.httpGet(url);
        }

        //Setter

        public void setHiveEndpoint(String hiveEndpoint) {
            this.hiveEndpoint = hiveEndpoint;
        }
    }

    public class HttpGetWithUser extends AsyncTask<Void, Void, String> {
        //Class Variables
        private String hiveEndpoint = "";
        private String userId = "";
        private String projectId = "";

        @Override
        protected String doInBackground(Void... params) {
            URL url = null;
            try {
                // Construct the URL for the Nervousnet Hive query
                url = new URL(domain + hiveEndpoint);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            return connection.httpGetWithUser(url, userId, projectId);
        }

        //Setter
        public void setHiveEndpoint(String hiveEndpoint) {
            this.hiveEndpoint = hiveEndpoint;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public void setProjectId(String projectId) {
            this.projectId = projectId;
        }
    }

    public class HttpPost extends AsyncTask<Void, Void, String> {
        //Class Variables
        private String hiveEndpoint = "";
        private JSONObject jsonObject;

        @Override
        protected String doInBackground(Void... params) {
            URL url = null;
            try {
                // Construct the URL for the Nervousnet query
                url = new URL(domain + hiveEndpoint);
            } catch (IOException e) {
                Log.e("PlaceholderFragment", "Error ", e);
                return null;
            }
            // Create the request to Hive, and open the connection
            return connection.httpPost(url, jsonObject);
        }

        //Setter
        public void setHiveEndpoint(String hiveEndpoint) {
            this.hiveEndpoint = hiveEndpoint;
        }

        public void setJsonObject(JSONObject jsonObject) {
            this.jsonObject = jsonObject;
        }
    }

    public class HttpPostWithUser extends AsyncTask<Void, Void, String> {
        //Class Variables
        private String hiveEndpoint = "";
        private String userId = "";
        private String projectId = "";
        private JSONObject jsonObject;

        @Override
        protected String doInBackground(Void... params) {
            URL url = null;
            try {
                // Construct the URL for the Nervousnet query
                url = new URL(domain + hiveEndpoint);
            } catch (IOException e) {
                Log.e("PlaceholderFragment", "Error ", e);
                return null;
            }
            // Create the request to Hive, and open the connection
            return connection.httpPostWithUser(url, jsonObject, userId, projectId);
        }

        //Setter
        public void setHiveEndpoint(String hiveEndpoint) {
            this.hiveEndpoint = hiveEndpoint;
        }

        public void setJsonObject(JSONObject jsonObject) {
            this.jsonObject = jsonObject;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public void setProjectId(String projectId) {
            this.projectId = projectId;
        }
    }
}
