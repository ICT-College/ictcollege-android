package nl.devapp.ictcollege.tasks;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import nl.devapp.ictcollege.R;
import nl.devapp.ictcollege.fragments.SelectFragment;

public class SelectTask extends AsyncTask<SelectFragment, Void, JsonObject> {

    private SelectFragment fragment;
    private int targetType;

    public SelectTask(SelectFragment fragment, int targetType) {
        this.targetType = targetType;
        this.fragment = fragment;
    }

    @Override
    public void onPostExecute(JsonObject results) {
        super.onPostExecute(results);

        if (results != null) {
            Log.d("SelectTask", "Starting parsing Target data for " + targetType + "....");
            Log.d("SelectTask", results.getAsJsonArray(((targetType == 1) ? "classes" : "teachers")).toString());

            JsonArray targetArray = results.getAsJsonArray(((targetType == 1) ? "classes" : "teachers"));

            for (int i = 0; i < targetArray.size(); i++) {
                JsonObject targetObject = targetArray.get(i).getAsJsonObject().get(((targetType == 1) ? "AClass" : "Teacher")).getAsJsonObject();

                String targetName = targetObject.get("name").getAsString();
                int targetId = targetObject.get("id").getAsInt();

                Log.d("SelectTask", "Name: " + targetName + ", id: " + targetId);

                fragment.targetListValues.add(targetName);
                fragment.targetListIds.add(targetId);
            }

            fragment.targetListAdapter.notifyDataSetChanged();

            Log.d("SelectTask", "Successfully parsed Target data for " + targetType + ".");
        } else {
            if(fragment.getActivity() != null) {
                Toast.makeText(fragment.getActivity().getApplicationContext(), fragment.getResources().getString(
                        R.string.net_error_cant_read_content), Toast.LENGTH_LONG).show();
            }
        }

        return;
    }

    @Override
    public JsonObject doInBackground(SelectFragment... arg0) {
        try {
            if (fragment.isDetached()) {
                return null;
            }

            fragment.setLoading(true);

            int departmentId = fragment.getResources().getInteger(R.integer.department_id);
            int schoolId = fragment.getResources().getInteger(R.integer.school_id);
            String apiUrl = fragment.getResources().getString(R.string.api_url);

            String requestUrl = apiUrl + "/" + ((targetType == 1) ? "class" : "teacher") + ".json?department=" + departmentId + "&school=" + schoolId;

            HttpURLConnection requestConnection = (HttpURLConnection) new URL(requestUrl).openConnection();
            requestConnection.setRequestProperty("Content-Type", "application/json");
            requestConnection.setRequestProperty("Accept", "application/json");
            requestConnection.setUseCaches(false);
            requestConnection.setRequestMethod("GET");
            requestConnection.connect();

            if (requestConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {

                BufferedInputStream connectionInput = new BufferedInputStream(
                        requestConnection.getInputStream());
                BufferedReader connectionReader = new BufferedReader(new InputStreamReader(connectionInput));

                String line;
                String response = "";

                while ((line = connectionReader.readLine()) != null) {
                    response += line;
                }

                connectionInput.close();
                connectionReader.close();
                requestConnection.disconnect();

                Log.d("ICT College", response);

                fragment.setLoading(false);

                return new JsonParser().parse(response).getAsJsonObject();
            } else {
                throw new IllegalStateException("HTTP response while fetching for Target data (" + targetType + "): " + requestConnection.getResponseCode());
            }
        } catch (Exception e) {
            Log.e("ICT College", "Whut?", e);
            return null;
        }
    }

}