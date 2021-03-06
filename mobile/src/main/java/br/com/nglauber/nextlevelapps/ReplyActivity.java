package br.com.nglauber.nextlevelapps;

import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.app.RemoteInput;
import android.support.v7.app.ActionBarActivity;
import android.widget.TextView;

public class ReplyActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reply);

        TextView textView = (TextView)findViewById(R.id.txtReply);

        Bundle remoteInput = RemoteInput.getResultsFromIntent(getIntent());
        if (remoteInput != null) {
            CharSequence resposta = remoteInput.getCharSequence(NotificationUtils.EXTRA_VOICE_REPLY);
            textView.setText(resposta);

            NotificationManagerCompat.from(this).cancel(NotificationUtils.NOTIFICATION_ID);
        }
    }
}
