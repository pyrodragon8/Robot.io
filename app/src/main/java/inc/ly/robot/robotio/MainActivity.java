package inc.ly.robot.robotio;

import android.support.v7.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.jmedeisis.bugstick.Joystick;
import com.jmedeisis.bugstick.JoystickListener;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    float deg, off;
    int x, y;
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    public enum Command {FORWARD, BACK, LEFT, RIGHT}


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        final Button button = (Button) findViewById(R.id.stick);
        Joystick joystick = (Joystick) findViewById(R.id.joystick);
        assert joystick != null;
        joystick.setJoystickListener(new JoystickListener() {
            @Override
            public void onDown() {
                //Log.d("JoyStickOP", "onDown: x = " + deg + ", y = " + off);
            }

            @Override
            public void onDrag(float degrees, float offset) {
                degrees = (float) ((Math.PI/180) * degrees);
                offset = (int)(offset * 100);
                Log.d("JoystickIn", "deg: " + degrees + " off: " + offset);
                if(degrees <= 90 && degrees > 0){
                    y = (int)(offset * Math.sin((double)degrees));
                    x = (int)(offset * Math.cos((double)degrees));
                    Log.d("JoystickOP", "Q1");
                } else if(degrees <= 180 && degrees > 90){
                    offset = 180 - offset;
                    y = (int)(offset * Math.sin((double)degrees));
                    x = (int)(offset * Math.cos((double)degrees));
                    x = x*-1;
                    Log.d("JoystickOP", "Q2dfadfasdfsadf");
                } else if(degrees >= -90 && degrees < 0){
                    offset = offset * -1;
                    y = (int)(offset * Math.sin((double)degrees));
                    x = (int)(offset * Math.cos((double)degrees));
                    y = y*-1;
                    x = x*-1;
                    Log.d("JoystickOP", "Q3");
                } else if(degrees >= -180 && degrees < -90){
                    offset = offset * -1;
                    offset = 180 - offset;
                    y = (int)(offset * Math.sin((double)degrees));
                    x = (int)(offset * Math.cos((double)degrees));
                    x = x*-1;
                    Log.d("JoystickOP", "Q4");
                }

                Log.d("JoyStickOP", "onDrag: x = " + x + ", y = " + y);

                //moveRobot(Command.FORWARD);

            }

            @Override
            public void onUp() {
                //Log.d("JoyStickOP", "onUp: x = " + deg + ", y = " + off);
                //Log.d("JoyStickOP", "onUp: ");
//              //  moveRobot(Command.FORWARD);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void moveRobot(Command command) {

        new MoveRobotTask().execute();
    }

    public class MoveRobotTask extends AsyncTask<Void, Void, Void> {
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
                Log.d(LOG_TAG, "Response returned :" + response.body());
            } catch (IOException e) {
                Log.d(LOG_TAG, "IOException occurred when executing http request");
            }
            return null;
        }

    }

}