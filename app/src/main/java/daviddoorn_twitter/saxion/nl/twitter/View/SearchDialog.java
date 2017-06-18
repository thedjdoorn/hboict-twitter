package daviddoorn_twitter.saxion.nl.twitter.View;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import daviddoorn_twitter.saxion.nl.twitter.MainActivity;
import daviddoorn_twitter.saxion.nl.twitter.R;
import daviddoorn_twitter.saxion.nl.twitter.SearchResultsActivity;


public class SearchDialog extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        final View view = inflater.inflate(R.layout.search_dialog, null);
        builder.setView(view)
                // Add action buttons
                .setPositiveButton("Search", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        EditText editText = (EditText) view.findViewById(R.id.editQuery);
                        Intent searchIntent = new Intent(getActivity(), SearchResultsActivity.class);
                        searchIntent.putExtra(MainActivity.SEARCH, editText.getText().toString());
                        startActivity(searchIntent);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        SearchDialog.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }
}
