package daviddoorn_twitter.saxion.nl.twitter.View;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import org.json.JSONObject;

import java.io.IOException;

import daviddoorn_twitter.saxion.nl.twitter.Controller.LocalDataController;
import daviddoorn_twitter.saxion.nl.twitter.MainActivity;
import daviddoorn_twitter.saxion.nl.twitter.Model.User;
import daviddoorn_twitter.saxion.nl.twitter.R;
import daviddoorn_twitter.saxion.nl.twitter.TwitterLoginActivity;

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
        if(LocalDataController.getInstance(getActivity()).getToken()!=null) {
            new GetUserInfo().execute();
        }

        return rootview;
    }

    private class GetUserInfo extends AsyncTask<Void, Void, Void>{
        private User user;
        @Override
        protected Void doInBackground(Void... params) {
           OAuth10aService service = new ServiceBuilder()
                    .apiKey("EQspxEHyFIMuUzFE3nLDiFaGS")
                    .apiSecret("z7hnSMOBcf7FyWXmOt2svgMJCEEkxpUWyBvWWoGuyFOfio2hEm")
                    .callback("https://daviddoorn.nl/twittah/authOK")
                    .build(TwitterApi.instance());
            OAuth1AccessToken token = LocalDataController.getInstance(getActivity()).getToken();
            final OAuthRequest request = new OAuthRequest(Verb.GET, "https://api.twitter.com/1.1/account/verify_credentials.json", service);
            service.signRequest(token, request);
            final Response response = request.send();
            try {
                user = new User(new JSONObject(response.getBody()));
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            ImageView iv = (ImageView) getView().findViewById(R.id.profilePictureView);
            Picasso.Builder builder = new Picasso.Builder(getActivity());
            builder.listener(new Picasso.Listener() {
                @Override
                public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                    exception.printStackTrace();
                }
            });

            builder.build().load(user.getImageLink()).fit().into(iv);

            ((TextView) getView().findViewById(R.id.titleView)).setText(user.getFullName()+" (@"+user.getHandle()+")");
            ((TextView) getView().findViewById(R.id.description)).setText(user.getDescription());

            super.onPostExecute(aVoid);
        }
    }

}
