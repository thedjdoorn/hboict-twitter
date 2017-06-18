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
import daviddoorn_twitter.saxion.nl.twitter.UserListAdapter;

/**
 * Created by thedj on 3-5-2017.
 */

public class FriendsTab extends Fragment {

    public final static String USER = "[USER INFO]";
    public FriendsTab() {
        super();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.tab_contacts, container, false);

        if(LocalDataController.getInstance(getActivity()).getToken()!=null){
            new GetFollowers().execute();
            new GetFriends().execute();
        }

        ListView followersList =  (ListView) view.findViewById(R.id.followersList);
        ListView friendsList =  (ListView) view.findViewById(R.id.followingList);

        followersList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                User u =  (User) parent.getAdapter().getItem(position);
                Intent intent = new Intent(getActivity(), ProfileViewActivity.class);
                intent.putExtra(USER, u.toJSON());
                startActivity(intent);
            }
        });

        friendsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                User u =  (User) parent.getAdapter().getItem(position);
                Intent intent = new Intent(getActivity(), ProfileViewActivity.class);
                intent.putExtra(USER, u.toJSON());
                startActivity(intent);
            }
        });
        return view;
    }

    private class GetFollowers extends AsyncTask<Void, Void, Void>{
        private ArrayList<User> followersList = new ArrayList<User>();
        @Override
        protected Void doInBackground(Void... params) {
            OAuth10aService service = new ServiceBuilder()
                    .apiKey("EQspxEHyFIMuUzFE3nLDiFaGS")
                    .apiSecret("z7hnSMOBcf7FyWXmOt2svgMJCEEkxpUWyBvWWoGuyFOfio2hEm")
                    .callback("https://daviddoorn.nl/twittah/authOK")
                    .build(TwitterApi.instance());
            OAuth1AccessToken token = LocalDataController.getInstance(getActivity()).getToken();
            final OAuthRequest request = new OAuthRequest(Verb.GET, "https://api.twitter.com/1.1/followers/list.json", service);
            request.addParameter("count", "200");
            service.signRequest(token, request);
            final Response response = request.send();
            try {
                JSONObject longJson = new JSONObject(response.getBody());
                System.out.println(longJson);
                JSONArray users = longJson.getJSONArray("users");
                for (int i = 0; i<users.length(); i++){
                    User u = new User(users.getJSONObject(i));
                    followersList.add(u);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            System.out.println("Postexecute done");
            UserListAdapter userListAdapter = new UserListAdapter(getContext(), R.layout.user_list_item, followersList);
            ((ListView) getView().findViewById(R.id.followersList)).setAdapter(userListAdapter);
            getView().findViewById(R.id.followersList).invalidate();
            super.onPostExecute(aVoid);
        }
    }

    private class GetFriends extends AsyncTask<Void, Void, Void>{
        private ArrayList<User> friendsList = new ArrayList<User>();
        @Override
        protected Void doInBackground(Void... params) {
            OAuth10aService service = new ServiceBuilder()
                    .apiKey("EQspxEHyFIMuUzFE3nLDiFaGS")
                    .apiSecret("z7hnSMOBcf7FyWXmOt2svgMJCEEkxpUWyBvWWoGuyFOfio2hEm")
                    .callback("https://daviddoorn.nl/twittah/authOK")
                    .build(TwitterApi.instance());
            OAuth1AccessToken token = LocalDataController.getInstance(getActivity()).getToken();
            final OAuthRequest request = new OAuthRequest(Verb.GET, "https://api.twitter.com/1.1/friends/list.json", service);
            request.addParameter("count", "200");
            service.signRequest(token, request);
            final Response response = request.send();
            try {
                JSONObject longJson = new JSONObject(response.getBody());
                System.out.println(longJson);
                JSONArray users = longJson.getJSONArray("users");
                for (int i = 0; i<users.length(); i++){
                    User u = new User(users.getJSONObject(i));
                    System.out.println(users.getJSONObject(i));
                    friendsList.add(u);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            System.out.println("Postexecute done");
            UserListAdapter userListAdapter = new UserListAdapter(getContext(), R.layout.user_list_item, friendsList);
            ((ListView) getView().findViewById(R.id.followingList)).setAdapter(userListAdapter);
            getView().findViewById(R.id.followingList).invalidate();
            super.onPostExecute(aVoid);
        }
    }

}


