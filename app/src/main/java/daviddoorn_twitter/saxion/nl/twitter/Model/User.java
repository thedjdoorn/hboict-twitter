package daviddoorn_twitter.saxion.nl.twitter.Model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by thedj on 10-5-2017.
 */

public class User {

    private int uid;
    private int followerCount;
    private int friendsCount;
    private String handle;
    private String fullName;
    private String imageLink;
    private String description;
    private boolean following;

    public User(JSONObject jsonObject){
        try {
            this.uid=jsonObject.getInt("id");
            this.handle=jsonObject.getString("screen_name");
            this.fullName=jsonObject.getString("name");
            this.imageLink=jsonObject.getString("profile_image_url_https");
            this.description = jsonObject.getString("description");
            this.followerCount = jsonObject.getInt("followers_count");
            this.friendsCount = jsonObject.getInt("friends_count");
            this.following = jsonObject.getBoolean("following");

        } catch (JSONException e){
            System.out.println("JSON error: "+e.getMessage());
        }
    }

    public User(int uid, String handle, String fullName, String imageLink, int followerCount, int friendsCount, boolean following){
        this.uid = uid;
        this.handle = handle;
        this.fullName = fullName;
        this.imageLink = imageLink;
        this.followerCount = followerCount;
        this.friendsCount = friendsCount;
        this.following = following;
    }

    public String getFullName() {
        return fullName;
    }

    public String getHandle() {
        return handle;
    }

    public String getImageLink() {
        return imageLink;
    }

    public String getDescription() {
        return description;
    }

    public int getFollowerCount() {
        return followerCount;
    }

    public int getFriendsCount() {
        return friendsCount;
    }

    public boolean isFollowing() {
        return following;
    }

    public String toJSON(){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", uid);
            jsonObject.put("screen_name", handle);
            jsonObject.put("name", fullName);
            jsonObject.put("profile_image_url_https", imageLink);
            jsonObject.put("description", description);
        } catch (JSONException e){
            System.out.println("JSON Error");
        }
        return jsonObject.toString();
    }
}
