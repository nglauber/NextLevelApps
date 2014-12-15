package br.com.nglauber.nextlevelapps;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.wearable.activity.ConfirmationActivity;
import android.support.wearable.view.WatchViewStub;
import android.support.wearable.view.WearableListView;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.Asset;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.DataItemBuffer;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Wearable;

import java.io.InputStream;

public class MainWearActivity extends Activity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = MainWearActivity.class.getSimpleName();

    private WearableListView mListView;

    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_wear);
        final WatchViewStub mWatchViewStub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        mWatchViewStub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                Log.d(TAG, "onLayoutInflated");
                initLayout();
            }
        });
        initApi();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
        if (mGoogleApiClient != null) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.d(TAG, "onConnected: "+ mGoogleApiClient.isConnected());
        Wearable.DataApi.addListener(mGoogleApiClient, mDataListener);
        Wearable.MessageApi.addListener(mGoogleApiClient, mMessageListener);
        readData();
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d(TAG, "onConnectionSuspended");
        Wearable.DataApi.removeListener(mGoogleApiClient, mDataListener);
        Wearable.MessageApi.removeListener(mGoogleApiClient, mMessageListener);
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed");
    }

    // Private helper methods -------------------------------------------------
    private void initApi() {
        Log.d(TAG, "initApi");
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        mGoogleApiClient.connect();
    }

    private void initLayout() {
        Log.d(TAG, "initLayout");
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
                "Grid", "Confirmation", "Sucesso",
                "Outra opção 1", "Outra opção 2", "Outra opção 3",
                "Outra opção 4", "Outra opção 5"
        };

        mListView = (WearableListView) findViewById(R.id.listView);
        mListView.setAdapter(new MyListAdapter(this, items, images));
        mListView.setClickListener(new WearableListView.ClickListener() {
            @Override
            public void onClick(WearableListView.ViewHolder viewHolder) {
                final int position = viewHolder.getPosition();

                if (position == 0) {
                    startActivity(new Intent(MainWearActivity.this, GridActivity.class));
                } else if (position == 1) {
                    Intent it = new Intent(MainWearActivity.this, ConfirmationActivity.class);
                    it.putExtra(ConfirmationActivity.EXTRA_ANIMATION_TYPE, ConfirmationActivity.SUCCESS_ANIMATION);
                    startActivity(new Intent(MainWearActivity.this, ConfirmationActivity.class));
                } else if (position == 2) {
                    Intent it = new Intent(MainWearActivity.this, ConfirmationActivity.class);
                    it.putExtra(ConfirmationActivity.EXTRA_ANIMATION_TYPE, ConfirmationActivity.OPEN_ON_PHONE_ANIMATION);
                    startActivity(it);
                }
            }

            @Override
            public void onTopEmptyRegionClick() {

            }
        });
    }

    private void readData() {
        Log.d(TAG, "readData");
        Wearable.DataApi.getDataItems(mGoogleApiClient).setResultCallback(
                new ResultCallback<DataItemBuffer>() {
                    @Override
                    public void onResult(DataItemBuffer dataItems) {
                        Log.d(TAG, "getDataItems::onResult: " + dataItems);
                        for (int i = 0; i < dataItems.getCount(); i++) {
                            DataItem dataItem = dataItems.get(i);
                            updateUI(dataItem);
                        }
                    }
                });
    }

    private void updateUI(DataItem dataItem) {
        DataMapItem dataMapItem = DataMapItem.fromDataItem(dataItem);
        final String message = dataMapItem.getDataMap().getString("message");

        Asset asset = dataMapItem.getDataMap().getAsset("image");
        new AsyncTask<Asset, Void, Drawable >(){

            @Override
            protected Drawable doInBackground(Asset... params) {
                InputStream assetInputStream = Wearable.DataApi.getFdForAsset(
                        mGoogleApiClient, params[0]).await().getInputStream();

                return BitmapDrawable.createFromStream(assetInputStream, "bg");
            }

            @Override
            protected void onPostExecute(Drawable drawable) {
                super.onPostExecute(drawable);

                mListView.setBackground(drawable);
                Toast.makeText(MainWearActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        }.execute(asset);
    }

    // Listeners --------------------------------------------------------------
    private MessageApi.MessageListener mMessageListener = new MessageApi.MessageListener() {
        @Override
        public void onMessageReceived(MessageEvent messageEvent) {
            Log.d(TAG, "onMessageReceived");
            Toast.makeText(MainWearActivity.this,
                    new String(messageEvent.getData()), Toast.LENGTH_SHORT).show();
        }
    };

    private DataApi.DataListener mDataListener = new DataApi.DataListener() {
        @Override
        public void onDataChanged(DataEventBuffer dataEvents) {
            Log.d(TAG, "onDataChanged");
            for (DataEvent event : dataEvents) {
                Log.d(TAG, "event: " + event);
                if (event.getType() == DataEvent.TYPE_CHANGED &&
                        event.getDataItem().getUri().getPath().equals("/mydata")) {
                    updateUI(event.getDataItem());
                }
            }
        }
    };
}
