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

public class TweetsTab extends Fragment {

    public TweetsTab() {
        super();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.tab_tweets, container, false);
    }
}
