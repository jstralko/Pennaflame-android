package com.pennaflame.app;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

public class MTIStatementActivity extends PennaFlameBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new MTIStatementFragment())
                    .commit();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        /*
         * XXX: why do I have to add this here.
         * Without this check hitting the back button
         * when drawer is open it goes to the previous
         * activity.
         */
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            if (isDrawerOpen()) {
                closeDrawer();
                return true;
            }
        }

        return super.onKeyDown(keyCode, event);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class MTIStatementFragment extends Fragment {

        public MTIStatementFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_mtistatment, container, false);
            WebView wv = (WebView) rootView.findViewById(R.id.mtiWebView);
            wv.getSettings().setBuiltInZoomControls(true);
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
                wv.getSettings().setDisplayZoomControls(false);
            }
            wv.loadUrl("file:///android_asset/mtistatement.html");
            return rootView;
        }
    }

}
