package bo.edu.ucbcba.httpconnection;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import bo.edu.ucbcba.httpconnection.data.JobPostDbContract;
import bo.edu.ucbcba.httpconnection.data.JobPostDbHelper;

import static bo.edu.ucbcba.httpconnection.data.JobPostDbContract.*;

public class MainActivity extends AppCompatActivity {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private ArrayAdapter<String> arrayAdapter;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView)findViewById(R.id.works_list_view);
        ArrayList<String> arrayList = new ArrayList<>();
        arrayAdapter = new ArrayAdapter<String>(this, R.layout.job_post_element,
                                                R.id.content_text_view, arrayList);
        listView.setAdapter(arrayAdapter);
    }

    public void syncData(View view) {
        GetDataAsyncTask asyncTask = new GetDataAsyncTask();

        asyncTask.execute();
    }

    private class GetDataAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            // The URL To connect:
            // http://dipandroid-ucb.herokuapp.com/work_posts.json
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            Uri buildUri = Uri.parse("http://dipandroid-ucb.herokuapp.com").buildUpon()
                    .appendPath("work_posts.json").build();
            try {
                URL url = new URL(buildUri.toString());

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.addRequestProperty("Content-Type", "application/json");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuffer buffer = new StringBuffer();

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line).append("\n");
                }

                String clientInfoJSON = buffer.toString();
                Log.d(LOG_TAG, "JSON: " + clientInfoJSON);
                JSONArray jsonArray = new JSONArray(clientInfoJSON);
                int length = jsonArray.length();
                JobPostDbHelper dbHelper = new JobPostDbHelper(MainActivity.this);
                SQLiteDatabase db = dbHelper.getWritableDatabase();

                for (int i = 0; i < length; i++) {
                    /*
                     {"id":1,"title":"Something","description":"Something",
                          "posted_date":"03/10/2015","contacts":["1-391-882-2074","846-971-4852 x9658"]}
                    * */
                    JSONObject element = jsonArray.getJSONObject(i);
                    int id = element.getInt("id");
                    String title = element.getString("title");
                    String date = element.getString("posted_date");
                    String description = element.getString("description");

                    // Para llenar la base de datos, usamos la clase ContentValue
                    // Un objeto de esta clase tiene la referencia a los atributos
                    // Que vamos a insertar en la base de datos
                    ContentValues contentValues = new ContentValues();

                    contentValues.put(JobPost._ID, id);
                    contentValues.put(JobPost.TITLE_COLUMN, title);
                    contentValues.put(JobPost.POSTED_DATE_COLUMN, date);
                    contentValues.put(JobPost.DESCRIPTION_COLUMN, description);

                    db.insert(JobPost.TABLE_NAME, null, contentValues);
                }
            } catch (IOException e) {

            } catch (JSONException e) {

            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();

                    }
                }
            }
            return null;
        }
    }
}
