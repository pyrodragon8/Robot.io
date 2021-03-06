package inc.ly.robot.robotio;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.jmedeisis.bugstick.Joystick;
import com.jmedeisis.bugstick.JoystickListener;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    float deg, off, xdir = 127, ydir = 127, xrot = 127;
    int x, y, port = 1110, rot;
    byte[] control = new byte[]{(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00};
    private Socket client;
    private Joystick joystick, joyLeft, joyRight;
    String ipAddress = "";
    Button button, buttonL, buttonR;
    boolean Connected = false;
    private ImageView imageView;
    private Handler handler = new Handler();
    Thread t;
    Thread imageLoader = new Thread() {
        @Override
        public void run() {
            while(!isInterrupted()) {
                final Drawable image = loadImageFromWebOperations("http://10.140.126.90:8080/image");
                Log.d("MainActivity", "Loading image");
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        imageView.setImageDrawable(image);
                    }
                });
            }
        }
    };


    public static Drawable loadImageFromWebOperations(String url) {
        try {
            InputStream is = (InputStream) new URL(url).getContent();
            Drawable d = Drawable.createFromStream(is, "src name");
            return d;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);

        t = new MoveRobotTask();
        t.start();

        if(getResources().getConfiguration().orientation== Configuration.ORIENTATION_LANDSCAPE) {
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getSupportActionBar().hide();
        }

        setContentView(R.layout.activity_main_screen);
        imageView = (ImageView) findViewById(R.id.camera_image);
        imageLoader.start();



        if(getResources().getConfiguration().orientation== Configuration.ORIENTATION_LANDSCAPE) {

            buttonL = (Button) findViewById(R.id.stickleft);
            buttonR = (Button) findViewById(R.id.stickright);
            joyLeft = (Joystick) findViewById(R.id.joystickleft);
            joyRight = (Joystick) findViewById(R.id.joystickright);
            assert joyLeft != null;
            assert joyRight != null;
            new MoveRobotTask().start();

            setLandscapeJoystickListenerL();
            setLandscapeJoystickListenerR();
        }
        else{
            button = (Button) findViewById(R.id.stick);
            joystick = (Joystick) findViewById(R.id.joystick);

            assert joystick != null;


            setPortraitJoystickListener();
        }
    }



    public void setPortraitJoystickListener(){
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

                Log.d("JoyStickOP", Connected+"onDrag: x = " + x + ", y = " + y);
                Log.d("JoyStickOP", Connected+"onDrag: xdir = " + xdir + ", ydir = " + ydir);

            }

            @Override
            public void onUp() {
                xdir = 127;
                ydir = 127;
                Log.d("JoyStickOP", Connected+"onUp: x = " + x + ", y = " + y);
            }
        });

        SetJoystickStatus(joystick);
    }
    public void setLandscapeJoystickListenerL(){
        joyLeft.setJoystickListener(new JoystickListener() {
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


                Log.d("JoyStickOP", Connected+"onDrag: x = " + x + ", y = " + y);
                Log.d("JoyStickOP", Connected+"onDrag: xdir = " + xdir + ", ydir = " + ydir);

            }

            @Override
            public void onUp() {
                xdir = 127;
                ydir = 127;
                Log.d("JoyStickOP", Connected+"onUp: x = " + x + ", y = " + y);
            }
        });

        SetJoystickStatus(joyLeft);
    }
    public void setLandscapeJoystickListenerR(){
        joyRight.setJoystickListener(new JoystickListener() {
            @Override
            public void onDown() {

            }

            @Override
            public void onDrag(float degrees, float offset) {
                degrees = (float) ((Math.PI/180) * degrees);
                offset = (int)(offset * 100);
                rot = (int)(offset * Math.cos((double)degrees));

                xrot = (int)((rot/100.0)*127);
                xrot += 128;

                Log.d("JoystickRL", "rot: " + rot + " xrot: " + xrot);

            }

            @Override
            public void onUp() {
                xrot = 127;
                Log.d("JoyStickOP", Connected+"onUp: x = " + x + ", y = " + y);
            }
        });

        SetJoystickStatus(joyRight);
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
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void SetJoystickStatus(Joystick joy){
        if(Connected){
            joy.setBackgroundDrawable(getResources().getDrawable(R.drawable.connected_circle));
        }
        else{
            joy.setBackgroundDrawable(getResources().getDrawable(R.drawable.center_circle));
        }

    }
    @Override
    protected void onDestroy() {
        try {
            if(client!=null) {
                client.close();
                imageLoader.interrupt();
            }
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
                SharedPreferences sharedPref = MainActivity.this.getSharedPreferences("test", Context.MODE_PRIVATE);
                port = Integer.parseInt(sharedPref.getString(getString(R.string.pref_port_key), getString(R.string.pref_port_default)).trim());
                ipAddress = sharedPref.getString(getString(R.string.pref_url_key), getString(R.string.pref_url_default)).trim();
                client = new Socket(ipAddress, port);
                if(client.isConnected()) {
                    Connected = true;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(getResources().getConfiguration().orientation== Configuration.ORIENTATION_LANDSCAPE) {
                                SetJoystickStatus(joyLeft);
                                SetJoystickStatus(joyRight);
                            }
                            else {SetJoystickStatus(joystick);}
                        }
                    });
                }
                OutputStream output;
                output = (client.getOutputStream());
                Connected = true;
                while (client != null && client.isConnected()) {
                    control[0] = (byte) xrot;
                    control[4] = (byte) 0x01;
                    control[2] = (byte) xdir;
                    control[3] = (byte) ydir;
                    output.write(control);
                    Thread.sleep(10);
                }
                Connected = false;
            } catch(Exception e) {
                e.printStackTrace();
            }

        }




    }


}