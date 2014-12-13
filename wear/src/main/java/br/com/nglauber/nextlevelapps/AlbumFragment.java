package br.com.nglauber.nextlevelapps;


import android.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.io.IOException;


public class AlbumFragment extends Fragment {

    private static final String EXTRA_ALBUM = "album";

    private Album mAlbum;

    public static AlbumFragment newInstance(Album album){
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_ALBUM, album);

        AlbumFragment f = new AlbumFragment();
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAlbum = (Album)getArguments().getSerializable(EXTRA_ALBUM);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Context context = getActivity();
        Bitmap cover;
        try {
            cover = BitmapFactory.decodeStream(context.getAssets().open(mAlbum.capa));

        } catch (IOException e) {
            cover = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher);
            e.printStackTrace();
        }

        ImageView imgCover = new ImageView(context);
        imgCover.setImageBitmap(cover);
        return imgCover;
    }
}
