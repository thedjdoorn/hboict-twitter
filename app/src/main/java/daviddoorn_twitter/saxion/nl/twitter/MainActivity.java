package daviddoorn_twitter.saxion.nl.twitter;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.github.scribejava.core.model.OAuth1AccessToken;

import daviddoorn_twitter.saxion.nl.twitter.Controller.LocalDataController;
import daviddoorn_twitter.saxion.nl.twitter.View.NewTweetDialog;
import daviddoorn_twitter.saxion.nl.twitter.View.ProfileTab;
import daviddoorn_twitter.saxion.nl.twitter.View.SearchDialog;
import daviddoorn_twitter.saxion.nl.twitter.View.FriendsTab;
import daviddoorn_twitter.saxion.nl.twitter.View.TweetsTab;

public class MainActivity extends AppCompatActivity {

    public static final int AUTH_REQUEST_CODE = 1;
    public final static String SEARCH = "[SEARCH QUERY]";

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(LocalDataController.getInstance(this).getToken() ==null){
            Intent browserIntent = new Intent(MainActivity.this, TwitterLoginActivity.class);
            startActivityForResult(browserIntent, AUTH_REQUEST_CODE);
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

      FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
      fab.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              DialogFragment dialog = new NewTweetDialog();
              dialog.show(getFragmentManager(), "New Tweet");
          }
      });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if(LocalDataController.getInstance(this).getToken()!= null){
            getMenuInflater().inflate(R.menu.menu_main_loggedin, menu);
        } else{
            getMenuInflater().inflate(R.menu.menu_main, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id){
            case R.id.loginItem:
                Intent browserIntent = new Intent(MainActivity.this, TwitterLoginActivity.class);
                startActivityForResult(browserIntent, AUTH_REQUEST_CODE);
                return true;

            case R.id.logoutItem:
                LocalDataController.getInstance(this).logout();
                Intent i  = new Intent(MainActivity.this, MainActivity.class);
                startActivity(i);
                return true;

            case R.id.searchitem:
                DialogFragment dialogFragment = new SearchDialog();
                dialogFragment.show(getFragmentManager(), "Search");
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == AUTH_REQUEST_CODE){
            if(resultCode== Activity.RESULT_OK){
                OAuth1AccessToken oAuth1AccessToken = new OAuth1AccessToken(data.getStringExtra("token"), data.getStringExtra("tokenSecret"));
                LocalDataController.getInstance(this).putToken(oAuth1AccessToken);
                invalidateOptionsMenu();
                findViewById(R.id.tabs).invalidate();
            }
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if(position == 0){
                return new TweetsTab();
            } else if(position == 1){
                return new ProfileTab();
            } else {
                return new FriendsTab();
            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Tweets";
                case 1:
                    return "Profile";
                case 2:
                    return "Contacts";
            }
            return null;
        }
    }
}
