package daviddoorn_twitter.saxion.nl.twitter;

import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.JsPromptResult;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.github.scribejava.apis.TwitterApi;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.OAuth1AccessToken;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth10aService;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import daviddoorn_twitter.saxion.nl.twitter.Controller.LocalDataController;
import daviddoorn_twitter.saxion.nl.twitter.Model.User;
import daviddoorn_twitter.saxion.nl.twitter.View.TweetsTab;

public class ProfileViewActivity extends AppCompatActivity {

    private Bundle extra;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_view);

        extra = getIntent().getExtras();

        if(extra != null) {
            try {
                user = new User(new JSONObject(extra.getString(TweetsTab.USER)));
                System.out.println(user.toJSON());
            } catch (JSONException e){
                e.printStackTrace();
            }

            ToggleButton followButton = (ToggleButton) findViewById(R.id.followButton);

            System.out.println(user.isFollowing());

            followButton.setChecked(user.isFollowing());

            followButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    new FollowTask().execute(isChecked);
                }
            });


            ImageView iv = (ImageView) findViewById(R.id.profilePictureView);

            Picasso.with(this).load(user.getImageLink().replaceAll("_normal","")).fit().into(iv);

            ((TextView) findViewById(R.id.titleView)).setText(user.getFullName()+" (@"+user.getHandle()+")");
            ((TextView) findViewById(R.id.description)).setText(user.getDescription());
        }
    }

    private class FollowTask extends AsyncTask<Boolean, Void, Void>{
        @Override
        protected Void doInBackground(Boolean... params) {
            String action;
            if(params[0]){
                action = "https://api.twitter.com/1.1/friendships/create.json";
            } else {
                action = "https://api.twitter.com/1.1/friendships/destroy.json";
            }
            OAuth10aService service = new ServiceBuilder()
                    .apiKey("EQspxEHyFIMuUzFE3nLDiFaGS")
                    .apiSecret("z7hnSMOBcf7FyWXmOt2svgMJCEEkxpUWyBvWWoGuyFOfio2hEm")
                    .callback("https://daviddoorn.nl/twittah/authOK")
                    .build(TwitterApi.instance());
            OAuth1AccessToken token = LocalDataController.getInstance(ProfileViewActivity.this).getToken();
            final OAuthRequest request = new OAuthRequest(Verb.POST, action, service);
            request.addParameter("user_id", String.valueOf(user.getUid()));
            service.signRequest(token, request);
            final Response response = request.send();
            try {
                System.out.println(response.getBody());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

}
