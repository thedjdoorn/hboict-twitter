package daviddoorn_twitter.saxion.nl.twitter;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.List;

import daviddoorn_twitter.saxion.nl.twitter.Model.Tweet;

/**
 * Created by thedj on 10-5-2017.
 */

public class TweetListAdapter extends ArrayAdapter<Tweet> {

    private List<Tweet> tweets;

    public TweetListAdapter(Context context, int resource, List<Tweet> objects) {
        super(context, resource, objects);
        tweets = objects;
    }


}
