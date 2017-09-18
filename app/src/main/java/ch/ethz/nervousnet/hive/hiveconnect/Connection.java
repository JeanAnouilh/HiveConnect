package ch.ethz.nervousnet.hive.hiveconnect;

import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Dario on 28.08.17.
 */

public class Connection {
    //Variables
    private BufferedReader reader = null;
    private final int TIMEOUT = 5000;
    private String domain = "";

    //Constructor
    public Connection(String domain) {
        this.domain = domain;
    }

    //Connection function and classes
    public String httpGet(URL url) {
        HttpURLConnection urlConnection = null;
        try {
            System.out.printf("test");

            //Set urlConnection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setConnectTimeout(TIMEOUT);
            urlConnection.setReadTimeout(TIMEOUT);

            //connect
            urlConnection.connect();

            //Return response as String
            return urlConnectionToString(urlConnection);
        } catch (IOException e) {
            Log.e("PlaceholderFragment", "Error ", e);
            // If the code didn't successfully get the weather data, there's no point in attemping
            // to parse it.
            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e("PlaceholderFragment", "Error closing stream", e);
                }
            }
        }
    }

    public String httpGetWithUser(URL url, String userId, String projectId) {
        HttpURLConnection urlConnection = null;
        try {
            //Set urlConnection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setRequestProperty("Cookie",projectId + "_user_id=" + userId);
            urlConnection.setConnectTimeout(TIMEOUT);
            urlConnection.setReadTimeout(TIMEOUT);

            //connect
            urlConnection.connect();

            //Return response as String
            return urlConnectionToString(urlConnection);
        } catch (IOException e) {
            Log.e("PlaceholderFragment", "Error ", e);
            // If the code didn't successfully get the weather data, there's no point in attemping
            // to parse it.
            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e("PlaceholderFragment", "Error closing stream", e);
                }
            }
        }
    }

    public String httpPost(URL url, JSONObject jObject) {
        HttpURLConnection urlConnection = null;
        try {
            //Set urlConnection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setConnectTimeout(TIMEOUT);
            urlConnection.setReadTimeout(TIMEOUT);
            urlConnection.setDoOutput(true);
            urlConnection.setRequestProperty("content-type", "application/json");

            //connect
            urlConnection.connect();

            //Send Post Data
            OutputStreamWriter out = new OutputStreamWriter(urlConnection.getOutputStream());
            out.write(jObject.toString());
            out.flush();
            out.close();

            //Return response as String
            return urlConnectionToString(urlConnection);
        } catch (IOException e) {
            Log.e("PlaceholderFragment", "Error ", e);
            // If the code didn't successfully get the weather data, there's no point in attemping
            // to parse it.
            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e("PlaceholderFragment", "Error closing stream", e);
                }
            }
        }
    }

    public String httpPostWithUser(URL url, JSONObject jObject, String userId, String projectId) {
        HttpURLConnection urlConnection = null;
        try {
            //Set urlConnection
            System.out.println(url.toString());

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("cookie", projectId + "_user_id=" + userId);
            urlConnection.setRequestProperty("content-type", "application/json");
            urlConnection.setInstanceFollowRedirects(false);
            urlConnection.setConnectTimeout(TIMEOUT);
            urlConnection.setReadTimeout(TIMEOUT);
            urlConnection.setDoOutput(true);

            //connect
            urlConnection.connect();

            //Send Post Data
            OutputStreamWriter out = new OutputStreamWriter(urlConnection.getOutputStream());
            out.write(jObject.toString());
            out.flush();
            out.close();

            //Return response as String
            return urlConnectionToString(urlConnection);
        } catch (IOException e) {
            Log.e("PlaceholderFragment", "Error ", e);
            // If the code didn't successfully get the weather data, there's no point in attemping
            // to parse it.
            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e("PlaceholderFragment", "Error closing stream", e);
                }
            }
        }
    }

    private String urlConnectionToString(HttpURLConnection urlConnection) {
        try {
            if(urlConnection.getResponseCode() == 200) {
                InputStream inputStream = urlConnection.getInputStream();
                StringBuilder buffer = new StringBuilder();
                if (inputStream == null) {
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line).append("\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }

                return buffer.toString();
            } else {
                return urlConnection.getResponseCode() + ": " + urlConnection.getResponseMessage();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "ERROR: Couldn't get a valid URLRespnse";
    }

    //
    //    ---- Request formatting
    //
    private String formatQuery(HiveTypes.Params params) {
        if (params == null) return "";
        String query = "";
        query = appendParam(query, "from", params.from);
        query = appendParam(query, "size", params.size);
        query = appendParam(query, "sortBy", params.sortBy);
        query = appendParam(query, "sortDir", params.sortDir);
        query = appendParam(query, "task", params.task);
        query = appendParam(query, "state", params.state);
        query = appendParam(query, "verified", params.verified);
        return query;
    }

    private String appendParam(String query, String name, String value) {
        if (value.length() > 0) {
            if (query.length() == 0) {
                query = "?" + name + "=" + value;
            } else {
                query += "&" + name + "=" + value;
            }
        }
        return query;
    }
}
