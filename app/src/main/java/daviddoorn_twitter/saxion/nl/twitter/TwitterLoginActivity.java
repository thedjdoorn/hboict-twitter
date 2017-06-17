package daviddoorn_twitter.saxion.nl.twitter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.github.scribejava.apis.TwitterApi;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.OAuth1AccessToken;
import com.github.scribejava.core.model.OAuth1RequestToken;
import com.github.scribejava.core.oauth.OAuth10aService;
import com.wang.avi.AVLoadingIndicatorView;

import java.io.IOException;

public class TwitterLoginActivity extends AppCompatActivity {

    private OAuth10aService service;
    private OAuth1RequestToken requestToken;
    private String OAuthURL ="";
    private OAuth1AccessToken accessToken=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_twitter_login);

        WebView wv = ((WebView) findViewById(R.id.webview));
        wv.setWebViewClient(new WebViewClient(){

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                view.setVisibility(View.INVISIBLE);
                ((AVLoadingIndicatorView)findViewById(R.id.avi)).setVisibility(View.VISIBLE);
                ((AVLoadingIndicatorView)findViewById(R.id.avi)).show();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                view.setVisibility(View.VISIBLE);
                ((AVLoadingIndicatorView)findViewById(R.id.avi)).hide();
                ((AVLoadingIndicatorView)findViewById(R.id.avi)).setVisibility(View.INVISIBLE);
                System.out.println("forwarded to URL: "+url);
                if(url.startsWith("https://daviddoorn.nl/twittah/authOK")){
                        Intent goBackWithToken = new Intent();
                        Uri uri = Uri.parse(url);
                        String verifier = uri.getQueryParameter("oauth_verifier");
                        new getAuthToken().execute(verifier);
                        while (accessToken==null){

                        }
                        goBackWithToken.putExtra("token", accessToken.getToken());
                        goBackWithToken.putExtra("tokenSecret", accessToken.getTokenSecret());
                        setResult(Activity.RESULT_OK, goBackWithToken);
                        finish();
                }
            }
        });
        getRequestURL();
        while (OAuthURL==""){

        }
        wv.loadUrl(OAuthURL);
    }



    private void getRequestURL(){
        service = new ServiceBuilder()
                .apiKey("EQspxEHyFIMuUzFE3nLDiFaGS")
                .apiSecret("z7hnSMOBcf7FyWXmOt2svgMJCEEkxpUWyBvWWoGuyFOfio2hEm")
                .callback("https://daviddoorn.nl/twittah/authOK")
                .build(TwitterApi.instance());
       new AsyncAuthURL().execute();
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    private class AsyncAuthURL extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            try

            {
                requestToken = service.getRequestToken();
            } catch(
                    IOException e)

            {
                System.err.println("IO Exception thrown :" + e.getLocalizedMessage());
            }
            OAuthURL = service.getAuthorizationUrl(requestToken);
            return null;
        }
    }

    private class getAuthToken extends AsyncTask<String,Void,Void>{
        @Override
        protected Void doInBackground(String... params) {
            try {
            accessToken = service.getAccessToken(requestToken, params[0]);
            System.out.println("Auth Token: "+ accessToken.toString());
            } catch (IOException e){
                System.err.println("IO Exception thrown :" +e.getLocalizedMessage());
            }
            return null;
        }
    }

    private class loadActivity extends AsyncTask<Void,Void,Void> {

        @Override
        protected Void doInBackground(Void... params) {
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }

}
