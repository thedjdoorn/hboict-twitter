package daviddoorn_twitter.saxion.nl.twitter.View;

import android.support.v4.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.github.scribejava.apis.TwitterApi;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.OAuth1AccessToken;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth10aService;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import daviddoorn_twitter.saxion.nl.twitter.Controller.LocalDataController;
import daviddoorn_twitter.saxion.nl.twitter.MainActivity;
import daviddoorn_twitter.saxion.nl.twitter.Model.Tweet;
import daviddoorn_twitter.saxion.nl.twitter.R;
import daviddoorn_twitter.saxion.nl.twitter.TweetListAdapter;

/**
 * Created by thedj on 17-6-2017.
 */

public class TweetsResultTab extends Fragment {

    private Bundle extra;
    private String query;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_search_results, container, false);

        extra = getActivity().getIntent().getExtras();

        if(extra != null){
            query = extra.getString(MainActivity.SEARCH);
        }
        new SearchTask().execute(query);
        return view;
    }


    private class SearchTask extends AsyncTask<String , Void , Void> {
        private ArrayList<Tweet> tweetsResponse = new ArrayList<Tweet>();
        @Override
        protected Void doInBackground(String... params) {
            OAuth10aService service = new ServiceBuilder()
                    .apiKey("EQspxEHyFIMuUzFE3nLDiFaGS")
                    .apiSecret("z7hnSMOBcf7FyWXmOt2svgMJCEEkxpUWyBvWWoGuyFOfio2hEm")
                    .callback("https://daviddoorn.nl/twittah/authOK")
                    .build(TwitterApi.instance());
            OAuth1AccessToken token = LocalDataController.getInstance(getContext()).getToken();
            final OAuthRequest request = new OAuthRequest(Verb.GET, "https://api.twitter.com/1.1/search/tweets.json", service);
            request.addParameter("q", params[0]);
            service.signRequest(token, request);
            final Response response = request.send();
            try {
                JSONObject longJson = new JSONObject(response.getBody());
                System.out.println(longJson.getJSONArray("statuses"));
                JSONArray tweets = longJson.getJSONArray("statuses");
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
            TweetListAdapter tweetListAdapter = new TweetListAdapter(getContext() , R.layout.tweet_list_item, tweetsResponse);
            ((ListView) getActivity().findViewById(R.id.resultslist)).setAdapter(tweetListAdapter);
            getActivity().findViewById(R.id.resultslist).invalidate();
            super.onPostExecute(aVoid);
        }
    }
}
