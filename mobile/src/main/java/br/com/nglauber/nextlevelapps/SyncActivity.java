package br.com.nglauber.nextlevelapps;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.Asset;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class SyncActivity extends ActionBarActivity
        implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    public static final String TAG = SyncActivity.class.getSimpleName();
    public static final int REQUEST_CODE_IMAGE = 0;
    public static final String PATH_MY_DATA = "/mydata";

    GoogleApiClient mGoogleApiClient;
    Node mCurrentNode;

    ImageView mImageView;
    EditText mEdtMessage;

    Bitmap mPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sync);

        mEdtMessage = (EditText)findViewById(R.id.editText);
        mImageView = (ImageView)findViewById(R.id.imageView);

        mPhoto = BitmapFactory.decodeResource(getResources(), R.drawable.gdg_logo);
        mImageView.setImageBitmap(mPhoto);

        initPlayServices();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mGoogleApiClient.disconnect();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_IMAGE && resultCode == RESULT_OK){
            try {
                if (mPhoto != null) {
                    mPhoto.recycle();
                }
                mPhoto = ImageUtil.decodeSampledBitmap(
                        this,
                        data.getData(),
                        getResources().getDimension(R.dimen.image_size),
                        getResources().getDimension(R.dimen.image_size));
                Log.d(TAG, "w:" + mPhoto.getWidth() + " h:" + mPhoto.getHeight());
                mImageView.setImageBitmap(mPhoto);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // GoogleApiClient.ConnectionCallbacks methods ----------------------------
    @Override
    public void onConnected(Bundle bundle) {
        Toast.makeText(this, R.string.connected, Toast.LENGTH_SHORT).show();
        initNode();
    }

    @Override
    public void onConnectionSuspended(int i) {
        mCurrentNode = null;
    }

    // GoogleApiClient.OnConnectionFailedListener -----------------------------
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Toast.makeText(this, R.string.fail_to_connect, Toast.LENGTH_SHORT).show();
    }

    // OnClick methods  -------------------------------------------------------
    public void pickImage(View View) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, REQUEST_CODE_IMAGE);
    }

    public void sendData(View v){
        if (mGoogleApiClient != null
                && mGoogleApiClient.isConnected()
                && mCurrentNode != null
                && mPhoto != null) {

            Wearable.MessageApi.sendMessage(
                    mGoogleApiClient,
                    mCurrentNode.getId(),
                    PATH_MY_DATA,
                    mEdtMessage.getText().toString().getBytes());

            final String s =  mEdtMessage.getText().toString();

            // TODO replace async task to another approach
            // Data are sent in background, so create a new thread
            new AsyncTask<Void, Void, PutDataRequest>(){
                @Override
                protected PutDataRequest doInBackground(Void... params) {
                    Asset asset = createAssetFromBitmap(mPhoto);

                    PutDataMapRequest putDataMapRequest = PutDataMapRequest.create(PATH_MY_DATA);
                    putDataMapRequest.getDataMap().putString("message", s);
                    putDataMapRequest.getDataMap().putAsset("image", asset);

                    PutDataRequest putDataRequest = putDataMapRequest.asPutDataRequest();
                    Wearable.DataApi.putDataItem(mGoogleApiClient, putDataRequest);
                    return putDataRequest;
                }
            }.execute();
        }
    }

    // Private methods --------------------------------------------------------
    private void initPlayServices() {
        mGoogleApiClient =
                new GoogleApiClient.Builder(this)
                        .addApi(Wearable.API)
                        .addConnectionCallbacks(this)
                        .addOnConnectionFailedListener(this)
                        .build();
    }

    private void initNode() {
        Wearable.NodeApi.getConnectedNodes(mGoogleApiClient).setResultCallback(
                new ResultCallback<NodeApi.GetConnectedNodesResult>() {
                    @Override
                    public void onResult(NodeApi.GetConnectedNodesResult result) {
                        if (result != null
                                && result.getNodes() != null
                                && result.getNodes().size() > 0) {
                            mCurrentNode = result.getNodes().get(0);
                        }
                    }
                });
    }

    private static Asset createAssetFromBitmap(Bitmap bitmap) {
        final ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteStream);
        return Asset.createFromBytes(byteStream.toByteArray());
    }
}
