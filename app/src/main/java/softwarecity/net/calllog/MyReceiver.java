package softwarecity.net.calllog;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import static java.lang.Thread.sleep;

public class MyReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        WakeLocker.acquire(context);
        Thread t = new Thread(new Runnable() {
            public void run() {
                try {
                    MainActivity.mainActivity.allFilds();
                    Log.e("sssddsdssssasaddasd", "onReceive: " );

                    sleep(1000*10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    Log.e("sssdddsdsdssdasd", "onReceive: " );

                }

            }
        });
        t.start();
//        MainActivity.mainActivity.allFilds();
//            Toast.makeText(context.getApplicationContext(), "Alarm Manager just ran 12", Toast.LENGTH_LONG).show();
            Log.e("sssdddasd", "onReceive: " );


//        }

    }


}

