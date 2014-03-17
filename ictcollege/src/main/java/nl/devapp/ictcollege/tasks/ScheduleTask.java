package nl.devapp.ictcollege.tasks;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import nl.devapp.ictcollege.MainActivity;
import nl.devapp.ictcollege.R;
import nl.devapp.ictcollege.fragments.ScheduleFragment;
import nl.devapp.ictcollege.models.Schedule;

public class ScheduleTask extends AsyncTask<ScheduleFragment, Void, JsonObject> {

    private MainActivity mainActivity;
    private SharedPreferences preferences;
    private boolean forceSync = false;

    public ArrayList<String> hours = new ArrayList<String>();

    public ScheduleTask(MainActivity mainActivity, boolean forceSync) {
        this.mainActivity = mainActivity;
        this.forceSync = forceSync;

        this.preferences = mainActivity.getSharedPreferences("global", Context.MODE_PRIVATE);

        hours.add("-");
        hours.add("08:45");
        hours.add("09:35");
        hours.add("10:45");
        hours.add("11:35");
        hours.add("12:55");
        hours.add("13:45");
        hours.add("14:55");
        hours.add("15:45");
    }

    @Override
    public void onPostExecute(JsonObject results) {
        super.onPostExecute(results);

        if (results != null) {

            try {
                Log.d("ScheduleTask", "Starting parsing Rooster data....");

                JsonArray eventArray = results.getAsJsonArray("events");

                for (int i = 0; i < eventArray.size(); i++) {
                    JsonObject targetObject = eventArray.get(i).getAsJsonObject();

                    int eventId = targetObject.get("id").getAsInt();

                    String eventTo = "";

                    if (preferences.getInt("targetType", 0) == 1) {
                        eventTo = ((targetObject.get("teacher_name").isJsonNull()) ? "-" : targetObject.get("teacher_name").getAsString());
                    } else {
                        eventTo = ((targetObject.get("class_name").isJsonNull()) ? "-" : targetObject.get("class_name").getAsString());
                    }

                    String eventClassroom = targetObject.get("classroom_code").getAsString();
                    String eventSubject = targetObject.get("subject_abbreviation").getAsString();

                    Calendar startCalendar = isoToCalendar(targetObject.get("start").getAsString());
                    Calendar stopCalander = isoToCalendar(targetObject.get("end").getAsString());

                    int eventDay = startCalendar.get(Calendar.DAY_OF_WEEK);

                    long startMillis = startCalendar.getTimeInMillis();
                    long stopMillis = stopCalander.getTimeInMillis();
                    long currentMillis = System.currentTimeMillis();

                    Log.d("ScheduleTask", "Id: " + eventId + ", classroom: " + eventClassroom + ", subject: " + eventSubject + ", to: " + eventTo + ", day: " + eventDay);

                    Schedule schedule = new Schedule();

                    schedule.setWeekDay(eventDay);
                    schedule.setClassRoom(eventClassroom);
                    schedule.setHour(hours.indexOf(new SimpleDateFormat("HH:mm").format(startCalendar.getTime())));
                    schedule.setLesson(eventSubject);
                    schedule.setTo(eventTo);

                    mainActivity.scheduleArray.add(schedule);

                    if(startMillis < currentMillis && currentMillis < stopMillis) {
                        Log.d("ScheduleTask", "Including Progessbar!");

                        Schedule progressBar = new Schedule();

                        progressBar.setWeekDay(eventDay);
                        progressBar.setProgressbar(true);
                        progressBar.setMax(startMillis - stopMillis);
                        progressBar.setStart(startMillis);

                        mainActivity.scheduleArray.add(progressBar);
                    }
                }

                mainActivity.mViewPager.getAdapter().notifyDataSetChanged();

                Log.d("ScheduleTask", "Successfully parsed Rooster data.");
            } catch (Exception e) {
                Log.e("ScheduleTask", "Error while loading data..", e);
            }

        } else {
            Toast.makeText(mainActivity.getApplicationContext(), mainActivity.getResources().getString(
                    R.string.net_error_cant_read_content), Toast.LENGTH_LONG).show();
        }

        return;
    }

    @Override
    public JsonObject doInBackground(ScheduleFragment... arg) {
        try {
            mainActivity.setLoading(true);

            if (!mainActivity.cacheRoosterJsonFile.exists() || forceSync) {
                int departmentId = mainActivity.getResources().getInteger(R.integer.department_id);
                int schoolId = mainActivity.getResources().getInteger(R.integer.school_id);
                String apiUrl = mainActivity.getResources().getString(R.string.api_url);
                int targetType = preferences.getInt("targetType", 0);
                int targetId = preferences.getInt("targetId", 0);

                Calendar firstDay = (Calendar) Calendar.getInstance().clone();

                firstDay.add(Calendar.DAY_OF_WEEK, firstDay.getFirstDayOfWeek() - firstDay.get(Calendar.DAY_OF_WEEK));

                Calendar lastDay = (Calendar) firstDay.clone();

                lastDay.add(Calendar.DAY_OF_YEAR, 5);


                String requestUrl = apiUrl + "/schedule/view.json?department=" + departmentId + "&school=" + schoolId + "&" + ((targetType == 1) ? "class" : "teacher") + "=" + targetId + "&start=" + (firstDay.getTimeInMillis() / 1000) + "&end=" + (lastDay.getTimeInMillis() / 1000);

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

                    mainActivity.cacheRoosterJsonFile.createNewFile();

                    PrintWriter writer = new PrintWriter(mainActivity.cacheRoosterJsonFile.getAbsoluteFile());
                    writer.print(response);
                    writer.close();

                    writer = new PrintWriter(mainActivity.cacheRoosterTimeFile.getAbsoluteFile());
                    writer.print(System.currentTimeMillis());
                    writer.close();

                    mainActivity.setLoading(false);
                    return new JsonParser().parse(response).getAsJsonObject();
                } else {
                    throw new IllegalStateException("HTTP response while fetching for Target data (): " + requestConnection.getResponseCode());
                }
            } else {
                mainActivity.setLoading(false);
                return new JsonParser().parse(new BufferedReader(new FileReader(mainActivity.cacheRoosterJsonFile.getAbsoluteFile())).readLine()).getAsJsonObject();
            }
        } catch (Exception e) {
            mainActivity.setLoading(false);

            Log.e("ICT College", "Whut?", e);
            return null;
        }
    }

    private Calendar isoToCalendar(String iso8601string) throws ParseException {
        Calendar calendar = Calendar.getInstance();

        String s = iso8601string.replace("Z", "+00:00");
        try {
            s = s.substring(0, 22) + s.substring(23);  // to get rid of the ":"
        } catch (IndexOutOfBoundsException e) {
            throw new ParseException("Invalid length", 0);
        }
        Date date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").parse(s);
        calendar.setTime(date);
        return calendar;
    }
}
