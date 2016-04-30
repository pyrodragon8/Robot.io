package inc.ly.robot.robotio;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.jmedeisis.bugstick.Joystick;
import com.jmedeisis.bugstick.JoystickListener;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    float degrees, offset;


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
                Log.d("JoyStickOP", "onDown: x = "+degrees+", y = "+offset);
            }

            @Override
            public void onDrag(float deg, float off) {
                degrees=deg;
                offset=off;

                Log.d("JoyStickOP", "onDrag: x = "+degrees+", y = "+offset);
            }

            @Override
            public void onUp() {
                Log.d("JoyStickOP", "onUp: x = "+degrees+", y = "+offset);
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

}

