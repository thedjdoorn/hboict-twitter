package daviddoorn_twitter.saxion.nl.twitter.View;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import org.json.JSONObject;

import java.util.ArrayList;

import daviddoorn_twitter.saxion.nl.twitter.Controller.LocalDataController;
import daviddoorn_twitter.saxion.nl.twitter.MainActivity;
import daviddoorn_twitter.saxion.nl.twitter.Model.Tweet;
import daviddoorn_twitter.saxion.nl.twitter.Model.User;
import daviddoorn_twitter.saxion.nl.twitter.ProfileViewActivity;
import daviddoorn_twitter.saxion.nl.twitter.R;
import daviddoorn_twitter.saxion.nl.twitter.TweetListAdapter;
import daviddoorn_twitter.saxion.nl.twitter.UserListAdapter;

/**
 * Created by thedj on 17-6-2017.
 */

public class UsersResultTab extends Fragment {

    private Bundle extra;
    private String query;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_search_users, container, false);

        extra = getActivity().getIntent().getExtras();

        if(extra != null){
            query = extra.getString(MainActivity.SEARCH);
        }
        new SearchTask().execute(query);

        ListView userlist =  (ListView) view.findViewById(R.id.userresultslist);

        userlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                User u =  (User) parent.getAdapter().getItem(position);
                Intent intent = new Intent(getActivity(), ProfileViewActivity.class);
                intent.putExtra(TweetsTab.USER, u.toJSON());
                startActivity(intent);
            }
        });

        return view;
    }


    private class SearchTask extends AsyncTask<String , Void , Void> {
        private ArrayList<User> usersResponse = new ArrayList<User>();
        @Override
        protected Void doInBackground(String... params) {
            OAuth10aService service = new ServiceBuilder()
                    .apiKey("EQspxEHyFIMuUzFE3nLDiFaGS")
                    .apiSecret("z7hnSMOBcf7FyWXmOt2svgMJCEEkxpUWyBvWWoGuyFOfio2hEm")
                    .callback("https://daviddoorn.nl/twittah/authOK")
                    .build(TwitterApi.instance());
            OAuth1AccessToken token = LocalDataController.getInstance(getContext()).getToken();
            final OAuthRequest request = new OAuthRequest(Verb.GET, "https://api.twitter.com/1.1/users/search.json", service);
            request.addParameter("q", params[0]);
            service.signRequest(token, request);
            final Response response = request.send();
            try {
                JSONArray users = new JSONArray(response.getBody());
                for (int i = 0; i<users.length(); i++){
                    User u = new User(users.getJSONObject(i));
                    System.out.println(u.toJSON());
                    usersResponse.add(u);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            UserListAdapter userListAdapter = new UserListAdapter(getContext() , R.layout.user_list_item, usersResponse);
            ((ListView) getActivity().findViewById(R.id.userresultslist)).setAdapter(userListAdapter);
            getActivity().findViewById(R.id.userresultslist).invalidate();
            super.onPostExecute(aVoid);
        }
    }
}
