package inc.ly.robot.robotio;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import com.jmedeisis.bugstick.Joystick;
import com.jmedeisis.bugstick.JoystickListener;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

import okhttp3.MediaType;

public class MainActivity extends AppCompatActivity {
    float deg, off, xdir = 127, ydir = 127;
    int x, y;
    byte[] control = new byte[]{(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00};
    private Socket client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        final Button button = (Button) findViewById(R.id.stick);
        Joystick joystick = (Joystick) findViewById(R.id.joystick);
        assert joystick != null;
        new MoveRobotTask().start();


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
                y = (int)(offset * Math.sin((double)degrees));
                x = (int)(offset * Math.cos((double)degrees));

                xdir = (int)((x/100.0)*127);
                ydir = (int)((y/100.0)*127);
                xdir += 128;
                ydir += 128;

                Log.d("JoyStickOP", "onDrag: x = " + x + ", y = " + y);
                Log.d("JoyStickOP", "onDrag: xdir = " + xdir + ", ydir = " + ydir);

            }

            @Override
            public void onUp() {
                xdir = 127;
                ydir = 127;
                Log.d("JoyStickOP", "onUp: x = " + x + ", y = " + y);
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

    @Override
    protected void onDestroy() {
        try {
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }

    public class MoveRobotTask extends Thread {
        private final String LOG_TAG = MoveRobotTask.class.getSimpleName();

        @Override
        public void run() {
            try {
                client = new Socket("10.140.148.37", 1100);
                while (client != null && client.isConnected()) {
                    control[4] = (byte) 0x01;
                    control[2] = (byte) xdir;
                    control[3] = (byte) ydir;
                    OutputStream output;
                    output = (client.getOutputStream());
                    output.write(control);
                    Thread.sleep(10);
                }
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    }
}