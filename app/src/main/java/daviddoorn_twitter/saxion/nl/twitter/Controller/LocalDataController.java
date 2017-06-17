package daviddoorn_twitter.saxion.nl.twitter.Controller;

import android.content.Context;
import android.content.SharedPreferences;

import com.github.scribejava.core.model.OAuth1AccessToken;

public class LocalDataController {

    public static final String PREFS_NAME = "MyPreferences";

    private Context c;
    private SharedPreferences settings;
    static LocalDataController instance;

    public LocalDataController(Context c){
        this.c = c;
        settings = c.getSharedPreferences(PREFS_NAME, 0);
    }

    public static LocalDataController getInstance(Context c) {
        if(instance ==null) {
            instance = new LocalDataController(c);
        }
        return instance;
    }

    public OAuth1AccessToken getToken(){
        if(settings.contains("OAuthToken") && settings.contains("OAuthSecret")){
            return new OAuth1AccessToken(settings.getString("OAuthToken", ""),settings.getString("OAuthSecret",""));
        } else return null;
    }

    public void putToken(String token, String secret){
        settings.edit().putString("OAuthToken", token).putString("OAuthSecret", secret).apply();
    }

    public void putToken(OAuth1AccessToken accessToken){
        putToken(accessToken.getToken(), accessToken.getTokenSecret());
    }

    public void logout(){
        settings.edit().remove("OAuthToken").remove("OAuthSecret").apply();
    }
}
