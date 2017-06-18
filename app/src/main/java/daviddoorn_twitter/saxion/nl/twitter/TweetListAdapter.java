package daviddoorn_twitter.saxion.nl.twitter;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.scribejava.apis.TwitterApi;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.OAuth1AccessToken;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth10aService;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Exchanger;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import daviddoorn_twitter.saxion.nl.twitter.Controller.LocalDataController;
import daviddoorn_twitter.saxion.nl.twitter.Model.Tweet;

public class TweetListAdapter extends ArrayAdapter<Tweet> {

    private List<Tweet> tweets;
    private static Picasso instance;

    public TweetListAdapter(Context context, int resource, List<Tweet> objects) {
        super(context, resource, objects);
        tweets = objects;
    }


    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView==null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.tweet_list_item, parent, false);
        }

        final Tweet tweet = tweets.get(position);

        final Button favoriteButton = (Button) convertView.findViewById(R.id.favoriteButton);
        if(tweet.isFavorited()){
            favoriteButton.setBackground(getContext().getDrawable(R.drawable.ic_favorite));
        }

        favoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!tweet.isFavorited()){
                  AsyncTask favoriteAction = new FavoriteAction().execute(tweet.getId());
                    try {
                        while (!(Boolean) favoriteAction.get(1, TimeUnit.SECONDS)){}
                        favoriteButton.setBackground(getContext().getDrawable(R.drawable.ic_favorite));
                        tweet.setFavorited(true);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    AsyncTask unfavoriteAction = new UnfavoriteAction().execute(tweet.getId());
                    try {
                        while (!(Boolean) unfavoriteAction.get(1,TimeUnit.SECONDS)){}
                        favoriteButton.setBackground(getContext().getDrawable(R.drawable.ic_favorite_border));
                        tweet.setFavorited(false);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        Picasso.with(getContext()).load(tweet.getUser().getImageLink().replaceAll("_normal","")).fit().into( (ImageView) convertView.findViewById(R.id.userAvatar));
        ((TextView) convertView.findViewById(R.id.userText)).setText(tweet.getUser().getFullName() + " ("+tweet.getUser().getHandle()+")");
        ((TextView) convertView.findViewById(R.id.tweetText)).setText(tweet.getContent());

        return convertView;
    }

    @Nullable
    @Override
    public Tweet getItem(int position) {
        return tweets.get(position);
    }

    private class FavoriteAction extends AsyncTask<String, Void, Boolean>{
        @Override
        protected Boolean doInBackground(String... params) {
            OAuth10aService service = new ServiceBuilder()
                    .apiKey("EQspxEHyFIMuUzFE3nLDiFaGS")
                    .apiSecret("z7hnSMOBcf7FyWXmOt2svgMJCEEkxpUWyBvWWoGuyFOfio2hEm")
                    .callback("https://daviddoorn.nl/twittah/authOK")
                    .build(TwitterApi.instance());
            OAuth1AccessToken token = LocalDataController.getInstance(TweetListAdapter.this.getContext()).getToken();
            final OAuthRequest request = new OAuthRequest(Verb.POST, "https://api.twitter.com/1.1/favorites/create.json", service);
            request.addParameter("id", params[0]);
            service.signRequest(token, request);
            System.out.println(request.getUrl());
            final Response response = request.send();
            try {
                System.out.println(response.getBody());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return true;
        }
    }

    private class UnfavoriteAction extends AsyncTask<String, Void, Boolean>{
        @Override
        protected Boolean doInBackground(String... params) {
            OAuth10aService service = new ServiceBuilder()
                    .apiKey("EQspxEHyFIMuUzFE3nLDiFaGS")
                    .apiSecret("z7hnSMOBcf7FyWXmOt2svgMJCEEkxpUWyBvWWoGuyFOfio2hEm")
                    .callback("https://daviddoorn.nl/twittah/authOK")
                    .build(TwitterApi.instance());
            OAuth1AccessToken token = LocalDataController.getInstance(TweetListAdapter.this.getContext()).getToken();
            final OAuthRequest request = new OAuthRequest(Verb.POST, "https://api.twitter.com/1.1/favorites/destroy.json", service);
            request.addParameter("id", params[0]);
            service.signRequest(token, request);
            final Response response = request.send();
            try {
                System.out.println(response.getBody());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return true;
        }
    }

}
