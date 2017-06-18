package daviddoorn_twitter.saxion.nl.twitter.Model;

import android.content.Entity;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by thedj on 10-5-2017.
 */

public class Tweet {
    private String id;
    private String content;
    private User user;
    private boolean favorited;

    private Tweet(){
        this.user = null;
        this.content = "";
        this.id = "-1";
        this.favorited = false;
    }

    public Tweet(User user, String content, String id, boolean favorited){
        this.user =  user;
        this.content = content;
        this.id = id;
        this.favorited = favorited;
    }

    public Tweet(JSONObject fromJSON){
        System.out.println(fromJSON);
        try {
            this.user = new User(fromJSON.getJSONObject("user"));
            this.content = fromJSON.getString("text");
            this.id = fromJSON.getString("id_str");
            this.favorited = fromJSON.getBoolean("favorited");
            System.out.println(favorited);
        } catch (JSONException e){
            System.out.println("JSON Error occurred: "+e.getMessage());
        }
    }

    public String getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public String getContent() {
        return content;
    }

    public boolean isFavorited(){
        return favorited;
    }

    public void setFavorited(boolean favorited) {
        this.favorited = favorited;
    }
}
