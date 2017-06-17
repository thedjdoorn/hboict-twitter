package daviddoorn_twitter.saxion.nl.twitter.View;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.github.scribejava.apis.TwitterApi;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.OAuth1AccessToken;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth10aService;

import daviddoorn_twitter.saxion.nl.twitter.Controller.LocalDataController;
import daviddoorn_twitter.saxion.nl.twitter.R;


public class NewTweetDialog extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        final View view = inflater.inflate(R.layout.new_tweet, null);
        builder.setView(view)
                // Add action buttons
                .setPositiveButton("Tweet", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        EditText editText = (EditText) view.findViewById(R.id.editTweet);
                        new SendTweetTask().execute(editText.getText().toString());
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        NewTweetDialog.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }


   private class SendTweetTask extends AsyncTask<String, Void, Void>{
        @Override
        protected Void doInBackground(String... params) {
            OAuth10aService service = new ServiceBuilder()
                    .apiKey("EQspxEHyFIMuUzFE3nLDiFaGS")
                    .apiSecret("z7hnSMOBcf7FyWXmOt2svgMJCEEkxpUWyBvWWoGuyFOfio2hEm")
                    .callback("https://daviddoorn.nl/twittah/authOK")
                    .build(TwitterApi.instance());
            OAuth1AccessToken token = LocalDataController.getInstance(getActivity()).getToken();
            System.out.println(LocalDataController.getInstance(getActivity()).getToken());
            final OAuthRequest request = new OAuthRequest(Verb.POST, "https://api.twitter.com/1.1/statuses/update.json", service);
            request.addParameter("status", params[0]);
            service.signRequest(token, request);
            final Response r = request.send();
            try {
                System.out.println(r.getCode() + "-" + r.getMessage() + r.getBody());
            } catch (Exception e){
                System.out.println("nay");
            }
            return null;
        }
    }
}
