package br.com.nglauber.nextlevelapps;

import android.app.Activity;
import android.os.Bundle;
import android.support.wearable.view.GridViewPager;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;


public class GridActivity extends Activity {

    GridViewPager mGridViewPager;
    List<Album> mAlbumList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid);

        mAlbumList = new ArrayList<Album>();
        mAlbumList.add(new Album("capa_01.jpg", "Revolta dos Dândis", 1987));
        mAlbumList.add(new Album("capa_02.jpg", "Alívio imediato", 1989));
        mAlbumList.add(new Album("capa_03.jpg", "O Papa é pop", 1990));
        mAlbumList.add(new Album("capa_04.jpg", "Várias variáveis", 1991));
        mAlbumList.add(new Album("capa_05.jpg", "Acústico MTV", 2004));

        mGridViewPager = (GridViewPager)findViewById(R.id.pager);
        mGridViewPager.setAdapter(new MyGridAdapter(getFragmentManager(), mAlbumList));
    }
}
