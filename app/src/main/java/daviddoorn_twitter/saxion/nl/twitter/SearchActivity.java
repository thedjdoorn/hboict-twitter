package daviddoorn_twitter.saxion.nl.twitter;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import daviddoorn_twitter.saxion.nl.twitter.Model.Tweet;

public class SearchActivity extends AppCompatActivity {

    private Bundle extra;
    private String query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        extra = getIntent().getExtras();
        if(extra != null){
            query = extra.getString(MainActivity.SEARCH);
            new SearchTask().execute(query);
        }
    }

    private class SearchTask extends AsyncTask<String , Void , Void>{
        private ArrayList<Tweet> tweetsResponse = new ArrayList<Tweet>();
        @Override
        protected Void doInBackground(String... params) {
            OAuth10aService service = new ServiceBuilder()
                    .apiKey("EQspxEHyFIMuUzFE3nLDiFaGS")
                    .apiSecret("z7hnSMOBcf7FyWXmOt2svgMJCEEkxpUWyBvWWoGuyFOfio2hEm")
                    .callback("https://daviddoorn.nl/twittah/authOK")
                    .build(TwitterApi.instance());
            OAuth1AccessToken token = LocalDataController.getInstance(SearchActivity.this).getToken();
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
            TweetListAdapter tweetListAdapter = new TweetListAdapter(SearchActivity.this , R.layout.tweet_list_item, tweetsResponse);
            ((ListView) findViewById(R.id.resultslist)).setAdapter(tweetListAdapter);
            findViewById(R.id.resultslist).invalidate();
            super.onPostExecute(aVoid);
        }
    }
}
