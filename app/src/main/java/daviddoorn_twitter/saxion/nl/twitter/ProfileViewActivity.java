package daviddoorn_twitter.saxion.nl.twitter;

import android.media.Image;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.JsPromptResult;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

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
            } catch (JSONException e){
                e.printStackTrace();
            }

            ImageView iv = (ImageView) findViewById(R.id.profilePictureView);
            Picasso.Builder builder = new Picasso.Builder(getApplicationContext());
            builder.listener(new Picasso.Listener() {
                @Override
                public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                    exception.printStackTrace();
                }
            });

            builder.build().load(user.getImageLink()).fit().into(iv);

            ((TextView) findViewById(R.id.titleView)).setText(user.getFullName()+" (@"+user.getHandle()+")");
            ((TextView) findViewById(R.id.description)).setText(user.getDescription());
        }
    }

}
