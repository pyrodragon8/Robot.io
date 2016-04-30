package inc.ly.robot.robotio;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by HarmanS on 30/04/2016.
 */
public class MoveRobotTask extends AsyncTask<Void, Void, Void> {
        private static final MediaType JSON
                = MediaType.parse("application/json; charset=utf-8");
        private final String LOG_TAG = MoveRobotTask.class.getSimpleName();

        @Override
        protected Void doInBackground(Void... voids) {
            Log.d(LOG_TAG, "calling move robot");
            RequestBody body = RequestBody.create(JSON, "{LEFT: 100, RIGHT: 100}");
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url("http://10.140.126.90:8080/").post(body)
                    .build();

            try {
                Response response = client.newCall(request).execute();
                Log.d(LOG_TAG, "Respone returned :" + response.body());
            } catch (IOException e) {
                Log.d(LOG_TAG, "IOException occurred when executing http request");
            }

            return null;
        }
    }
