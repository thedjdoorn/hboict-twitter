package daviddoorn_twitter.saxion.nl.twitter;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Cache;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.List;
import java.util.concurrent.Executors;

import daviddoorn_twitter.saxion.nl.twitter.Model.Tweet;

public class TweetListAdapter extends ArrayAdapter<Tweet> {

    private List<Tweet> tweets;
    private static Picasso instance;

    public TweetListAdapter(Context context, int resource, List<Tweet> objects) {
        super(context, resource, objects);
        tweets = objects;
    }

    public static Picasso getSharedInstance(Context context)
    {
        if(instance == null)
        {
            instance = new Picasso.Builder(context).executor(Executors.newSingleThreadExecutor()).memoryCache(Cache.NONE).indicatorsEnabled(true).build();
        }
        return instance;
    }


    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView==null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.tweet_list_item, parent, false);
        }

        Tweet tweet = tweets.get(position);

        Picasso.Builder builder = new Picasso.Builder(convertView.getContext());
        builder.listener(new Picasso.Listener()
        {
            @Override
            public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception)
            {
                exception.printStackTrace();
            }
        });

        builder.build().load(tweet.getUser().getImageLink()).fit().into( (ImageView) convertView.findViewById(R.id.userAvatar));
        ((TextView) convertView.findViewById(R.id.userText)).setText(tweet.getUser().getFullName() + " ("+tweet.getUser().getHandle()+")");
        ((TextView) convertView.findViewById(R.id.tweetText)).setText(tweet.getContent());

        return convertView;
    }

    @Nullable
    @Override
    public Tweet getItem(int position) {
        return tweets.get(position);
    }
}
