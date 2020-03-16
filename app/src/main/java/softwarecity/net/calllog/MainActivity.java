package softwarecity.net.calllog;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.CallLog;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import softwarecity.net.calllog.api.ApiServers;
import softwarecity.net.calllog.remote.DataManagerImpl;
import softwarecity.net.calllog.remote.RetrofitCallback;

import static android.widget.Toast.LENGTH_LONG;

public class MainActivity extends AppCompatActivity {
    AlarmManager alarmManager;
    PendingIntent pi ;
    private DataManagerImpl dataManager;
    StringBuffer sb;
    String dataa;
    static MainActivity mainActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dataManager = new DataManagerImpl();
        mainActivity = this;

        if(ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.READ_CALL_LOG)){
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.READ_CALL_LOG} , 1);
            }else {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.READ_CALL_LOG} , 1);
            }
        }
//        startService ();
//        go();
        settingAlarm();
        startAlarm();

    }



    private void settingAlarm() {

        Intent  intent = new Intent(this , MyReceiver.class);

        pi = PendingIntent.getBroadcast(this , 0 , intent , 0);

        alarmManager = (AlarmManager) this.getSystemService(ALARM_SERVICE);

    }

    private void startAlarm() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(),  1000*60 , pi);
            Toast.makeText(this , "done1" , Toast.LENGTH_LONG).show();
        } else {
            alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME, System.currentTimeMillis(),  1000*60 , pi);
            Toast.makeText(this , "done2" , Toast.LENGTH_LONG).show();
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode,  String[] permissions,  int[] grantResults) {
        switch (requestCode){
            case  1 : {
                if (grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    if (ContextCompat.checkSelfPermission(MainActivity.this,
                            Manifest.permission.READ_CALL_LOG) == PackageManager.PERMISSION_GRANTED){
                        Toast.makeText(this , "Permission granted " , LENGTH_LONG).show();
                         dataa = getCallDetails1();

                    }
                }else {
                    Toast.makeText(this , "No Permission granted " , LENGTH_LONG).show();
                }
                return;
            }
        }

    }


    public String getCallDetails1() {

        sb = new StringBuffer();
        String strOrder = android.provider.CallLog.Calls.DATE + " DESC";

        Date c = Calendar.getInstance().getTime();
        DateFormat simpleDateFormat = new java.text.SimpleDateFormat("yyyy-MMM-dd");
//        DateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MMM-dd");
        String formattedDate = simpleDateFormat.format(c);
        Log.e("date" , "ssss" + formattedDate);
        Date newDate = new Date(c.getTime() - 172800000); // 7 * 24 * 60 * 60 * 1000
        Calendar calender = Calendar.getInstance();
        calender.setTime(newDate);
        String fromDate = String.valueOf(calender.getTimeInMillis());

        calender.setTime(c);
        String toDate = String.valueOf(calender.getTimeInMillis());

        String[] whereValue = {fromDate,toDate};
        Cursor managedCursor = managedQuery( CallLog.Calls.CONTENT_URI,null, android.provider.CallLog.Calls.DATE + " BETWEEN ? AND ?", whereValue, strOrder);
        int number = managedCursor.getColumnIndex( CallLog.Calls.NUMBER );
        int type = managedCursor.getColumnIndex( CallLog.Calls.TYPE );
        int date = managedCursor.getColumnIndex( CallLog.Calls.DATE);
        int duration = managedCursor.getColumnIndex( CallLog.Calls.DURATION);
//        sb.append( "Call Details :");
        while ( managedCursor.moveToNext() ) {
            String phNumber = managedCursor.getString( number );
            String callType = managedCursor.getString( type );
            String callDate = managedCursor.getString( date );
            Date callDayTime = new Date(Long.valueOf(callDate));
            String callDuration = managedCursor.getString( duration );
            String dir = null;
            int dircode = Integer.parseInt( callType );
            switch( dircode ) {
                case CallLog.Calls.OUTGOING_TYPE:
                    dir = "OUTGOING";
                    break;

                case CallLog.Calls.INCOMING_TYPE:
                    dir = "INCOMING";
                    break;

                case CallLog.Calls.MISSED_TYPE:
                    dir = "MISSED";
                    break;
            }
            String android_id = Settings.Secure.getString(this.getContentResolver(),
                    Settings.Secure.ANDROID_ID);
            sb.append( "{'Phone Number':'"+phNumber+"','Call Type':'"+dir+"','Call Date':'"+callDayTime+"','Phone ID':'"+ android_id+"','Call duration in sec':'"+callDuration+"},");

            Log.e("hfhfhfhfasasasaf", android_id);
        }
        managedCursor.close();

        return  sb.toString();
    }



    public String allFilds() {

        String data = sb.toString();
        Log.e("kiokio", "allFilds: "+data );
        callLogs(data);
        return data;
    }





     void callLogs(String data) {

        dataManager.callLogs(new RetrofitCallback() {
            @Override
            public void onSuccess(Object response) {
                CallLogs callLogs = (CallLogs) response;
                if (callLogs.getStatus().equals("success")){
                    Log.e("sassasasasasaas", "onSuccess: " );
                }
            }

            @Override
            public void onError(Throwable throwable) {
                Log.e("sasas", "ererro: " );

            }

            @Override
            public void onErrorCode(Response<Object> response) {
                Log.e("sasas", "sdsdsdsds: " );

            }
        },data);
    }
//    private void callLogs(String data) {
//
//        apiServers.callLogs(data).enqueue(new Callback<CallLogs>() {
//
//            @Override
//            public void onResponse(Call<CallLogs> call, Response<CallLogs> response) {
//                try {
//                    if (response.body().getStatus().equals("success") ) {
//                        Log.e("asasasas", "onReszxzxzxzponse: " );
//
//                        Toast.makeText(MainActivity.this,"done", Toast.LENGTH_SHORT).show();
//
//                    } else {
//                        Toast.makeText(MainActivity.this,"done1", Toast.LENGTH_SHORT).show();
//                        Log.e("asasasas", "onReszxzxzxzponse: " );
//
//                    }
//
//                } catch (Exception e) {
//                    Toast.makeText(MainActivity.this,"done3", Toast.LENGTH_SHORT).show();
//                    Log.e("asasasas", "onReszxzxzxzponse: " );
//
//                }
//            }
//
//            @Override
//            public void onFailure(Call<CallLogs> call, Throwable t) {
//                Toast.makeText(MainActivity.this,"done4", Toast.LENGTH_SHORT).show();
//
//            }
//        });
//    }

//    public void startService (){
//        startService(new Intent(getBaseContext() , BackgroundService.class));
//        settingAlarm();
//        startAlarm();
//    }
    public void go(){
    settingAlarm();
    startAlarm();
    }
}
