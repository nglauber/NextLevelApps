package br.com.nglauber.nextlevelapps;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.wearable.activity.ConfirmationActivity;
import android.support.wearable.view.WatchViewStub;
import android.support.wearable.view.WearableListView;

import java.io.IOException;

public class MainWearActivity extends Activity {

    private WearableListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_wear);
        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                initLayout();
            }
        });
    }

    public void initLayout() {
        int[] images = new int[]{
                android.R.drawable.ic_menu_gallery,
                android.R.drawable.ic_menu_agenda,
                android.R.drawable.ic_menu_call,
                android.R.drawable.ic_menu_camera,
                android.R.drawable.ic_menu_directions,
                android.R.drawable.ic_menu_compass,
                android.R.drawable.ic_menu_help,
                android.R.drawable.ic_menu_search
        };
        final String[] items = new String[]{
                "Galeria", "Agenda", "Ligar", "Camera", "Navegação", "Compasso", "Ajuda", "Busca"
        };

        mListView = (WearableListView) findViewById(R.id.listView);
        mListView.setAdapter(new MyListAdapter(this, items, images));
        mListView.setClickListener(new WearableListView.ClickListener() {
            @Override
            public void onClick(WearableListView.ViewHolder viewHolder) {
                final int position = viewHolder.getPosition();

                if (position == 0){
                    startActivity(new Intent(MainWearActivity.this, GridActivity.class));
                } else if (position == 1){
                    startActivity(new Intent(MainWearActivity.this, ConfirmationActivity.class));
                }
            }

            @Override
            public void onTopEmptyRegionClick() {

            }
        });
    }

    private void friboiNotification(){
        final Bitmap bitmap;
        try {
            bitmap = BitmapFactory.decodeStream(
                    getAssets().open("friboi.png"));
            PendingIntent pit = PendingIntent.getActivity(
                    this, 0,
                    new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.nglauber.com.br")),
                    0
            );

            NotificationCompat.BigPictureStyle bigPictureStyle =
                    new NotificationCompat.BigPictureStyle().bigPicture(bitmap);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(MainWearActivity.this)
                    .setContentTitle("É isso aí Dona Maria!")
                    .setContentText("E essa carne?")
                    .setStyle(bigPictureStyle)
                    .setContentIntent(pit)
                    .setSmallIcon(R.drawable.ic_launcher);

            NotificationManagerCompat.from(MainWearActivity.this).notify(1, builder.build());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
