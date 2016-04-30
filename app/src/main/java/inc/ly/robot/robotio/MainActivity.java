package inc.ly.robot.robotio;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.jmedeisis.bugstick.Joystick;
import com.jmedeisis.bugstick.JoystickListener;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);


        Joystick joystick = (Joystick) findViewById(R.id.joystick);
        joystick.setJoystickListener(new JoystickListener() {
            @Override
            public void onDown() {
                Log.d("JoyStickOP", "onDown: ");
            }

            @Override
            public void onDrag(float degrees, float offset) {
                Log.d("JoyStickOP", "onDrag: ");
            }

            @Override
            public void onUp() {
                Log.d("JoyStickOP", "onUp: ");
            }
        });





    }





}
