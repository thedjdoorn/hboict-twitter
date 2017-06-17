package daviddoorn_twitter.saxion.nl.twitter.Model;

import android.content.Entity;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by thedj on 10-5-2017.
 */

public class Tweet {
    private int id;
    private String content;
    private User user;
    private Entity[] entities;

    public Tweet(){
        this.user = null;
        this.content = "";
        this.id = -1;
    }

    public Tweet(User user, String content, int id){
        this.user =  user;
        this.content = content;
        this.id = id;
    }

    public Tweet(JSONObject fromJSON){
        this();
        try {
            this.user = new User(fromJSON.getJSONObject("user"));
            this.content = fromJSON.getString("text");
            this.id = fromJSON.getInt("id");
        } catch (JSONException e){
            System.out.println("JSON Error occurred");
        }
    }

    public User getUser() {
        return user;
    }

    public String getContent() {
        return content;
    }

    public Entity[] getEntities() {
        return entities;
    }
}
