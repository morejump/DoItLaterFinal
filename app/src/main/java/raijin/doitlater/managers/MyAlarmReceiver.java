package raijin.doitlater.managers;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import raijin.doitlater.R;
import raijin.doitlater.activities.MainActivity;

/**
 * Created by Qk Lahpita on 9/3/2016.
 */
public class MyAlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            NotificationManager mManager = (NotificationManager) context
                    .getSystemService(context.NOTIFICATION_SERVICE);

            Intent intent1 = new Intent(context, MainActivity.class);

            PendingIntent pendingIntent = PendingIntent.getActivity(context,
                    0, intent1, Intent.FLAG_ACTIVITY_NEW_TASK);

            Notification.Builder builder = new Notification.Builder(context);

            builder.setAutoCancel(true);
            builder.setContentTitle(intent.getStringExtra("title"));
            builder.setContentText(intent.getStringExtra("detail"));
            builder.setSmallIcon(R.mipmap.ic_launcher);
            builder.setContentIntent(pendingIntent);

            mManager.notify(0, builder.build());

        } catch (Exception e) {
            Toast.makeText(context, "There was an error somewhere, but we still received an alarm", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
            Log.d("abcd", "Failllllllll");
        }
    }
}
