package daviddoorn_twitter.saxion.nl.twitter;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Cache;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.concurrent.Executors;

import daviddoorn_twitter.saxion.nl.twitter.Model.User;

public class UserListAdapter extends ArrayAdapter<User> {

    private List<User> users;

    public UserListAdapter(Context context, int resource, List<User> objects) {
        super(context, resource, objects);
        users = objects;
    }


    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView==null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.user_list_item, parent, false);
        }

        User user = users.get(position);

        Picasso.with(getContext()).load(user.getImageLink().replaceAll("_normal", "")).into( (ImageView) convertView.findViewById(R.id.userAvatar));
        ((TextView) convertView.findViewById(R.id.fullName)).setText(user.getFullName());
        ((TextView) convertView.findViewById(R.id.username)).setText(user.getHandle());
        ((TextView) convertView.findViewById(R.id.followercount)).setText(user.getFollowerCount() + " followers");
        ((TextView) convertView.findViewById(R.id.followingCount)).setText("Following" + user.getFriendsCount());

        return convertView;
    }

    @Nullable
    @Override
    public User getItem(int position) {
        return users.get(position);
    }
}
