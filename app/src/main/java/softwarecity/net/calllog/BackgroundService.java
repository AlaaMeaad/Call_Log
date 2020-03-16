package softwarecity.net.calllog;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class BackgroundService extends Service {


    @Override
    public void onCreate() {
        Toast.makeText(this, "start" , Toast.LENGTH_SHORT).show();
        Log.e("kiokiasasaso", "allFilds:jghjvjgv " );

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Toast.makeText(this, "startasa" , Toast.LENGTH_SHORT).show();

        Log.e("kiokiasasaso", "allFilds:jghjvjgv " );

        return START_STICKY;
    }
}
