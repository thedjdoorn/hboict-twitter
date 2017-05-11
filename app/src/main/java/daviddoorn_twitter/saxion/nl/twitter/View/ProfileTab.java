package daviddoorn_twitter.saxion.nl.twitter.View;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import daviddoorn_twitter.saxion.nl.twitter.R;

/**
 * Created by thedj on 3-5-2017.
 */

public class ProfileTab extends Fragment {

    public ProfileTab(){
        super();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootview =  inflater.inflate(R.layout.tab_profile, container, false);
        return rootview;
    }
}
