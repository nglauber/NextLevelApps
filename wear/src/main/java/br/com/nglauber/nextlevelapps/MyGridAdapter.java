package br.com.nglauber.nextlevelapps;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.wearable.view.CardFragment;
import android.support.wearable.view.FragmentGridPagerAdapter;

import java.util.List;

public class MyGridAdapter extends FragmentGridPagerAdapter{

    List<Album> mAlbums;

    public MyGridAdapter(FragmentManager fm, List<Album> albumList) {
        super(fm);
        mAlbums = albumList;
    }

    @Override
    public Fragment getFragment(int row, int column) {
        Album album = mAlbums.get(row);
        if (column == 0) {
            return AlbumFragment.newInstance(album);
        } else {
            return CardFragment.create(String.valueOf(album.ano), album.titulo);
        }
    }

    @Override
    public int getRowCount() {
        return 5;
    }

    @Override
    public int getColumnCount(int row) {
        return 2;
    }
}
