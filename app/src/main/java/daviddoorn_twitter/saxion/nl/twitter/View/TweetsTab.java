package daviddoorn_twitter.saxion.nl.twitter.View;

import android.content.Intent;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.github.scribejava.apis.TwitterApi;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.OAuth1AccessToken;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth10aService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import daviddoorn_twitter.saxion.nl.twitter.Controller.LocalDataController;
import daviddoorn_twitter.saxion.nl.twitter.MainActivity;
import daviddoorn_twitter.saxion.nl.twitter.Model.Tweet;
import daviddoorn_twitter.saxion.nl.twitter.Model.User;
import daviddoorn_twitter.saxion.nl.twitter.ProfileViewActivity;
import daviddoorn_twitter.saxion.nl.twitter.R;
import daviddoorn_twitter.saxion.nl.twitter.TweetListAdapter;
import daviddoorn_twitter.saxion.nl.twitter.TwitterLoginActivity;

/**
 * Created by thedj on 3-5-2017.
 */

public class TweetsTab extends Fragment {

    public final static String USER = "[USER INFO]";
    public TweetsTab() {
        super();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.tab_tweets, container, false);

        if(LocalDataController.getInstance(getActivity()).getToken()!=null){
            new GetUserTimeline().execute();
        }

        ListView tweetlist =  (ListView) view.findViewById(R.id.tweetList);

        tweetlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Tweet t =  (Tweet) parent.getAdapter().getItem(position);
                Intent intent = new Intent(getActivity(), ProfileViewActivity.class);
                intent.putExtra(USER, t.getUser().toJSON());
                startActivity(intent);
            }
        });
        return view;
    }

    private class GetUserTimeline extends AsyncTask<Void, Void, Void>{
        private ArrayList<Tweet> tweetsResponse = new ArrayList<Tweet>();
        @Override
        protected Void doInBackground(Void... params) {
            OAuth10aService service = new ServiceBuilder()
                    .apiKey("EQspxEHyFIMuUzFE3nLDiFaGS")
                    .apiSecret("z7hnSMOBcf7FyWXmOt2svgMJCEEkxpUWyBvWWoGuyFOfio2hEm")
                    .callback("https://daviddoorn.nl/twittah/authOK")
                    .build(TwitterApi.instance());
            OAuth1AccessToken token = LocalDataController.getInstance(getActivity()).getToken();
            final OAuthRequest request = new OAuthRequest(Verb.GET, "https://api.twitter.com/1.1/statuses/home_timeline.json", service);
            service.signRequest(token, request);
            final Response response = request.send();
            try {
                String longJson = response.getBody();
                System.out.println(longJson);
                JSONArray tweets = new JSONArray(longJson);
                for (int i = 0; i<tweets.length(); i++){
                    Tweet t = new Tweet(tweets.getJSONObject(i));
                    System.out.println(t.getUser()+":"+t.getContent());
                    tweetsResponse.add(t);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            System.out.println("Postexecute done");
            TweetListAdapter tweetListAdapter = new TweetListAdapter(getContext(), R.layout.tweet_list_item, tweetsResponse);
            ((ListView) getView().findViewById(R.id.tweetList)).setAdapter(tweetListAdapter);
            getView().findViewById(R.id.tweetList).invalidate();
            super.onPostExecute(aVoid);
        }
    }

}

