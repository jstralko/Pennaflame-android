package com.pennaflame.app;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

public class ContactActivity extends PennaFlameBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new ContactFragment())
                    .commit();
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class ContactFragment extends Fragment {

        private Button callButton;
        private Button emailButton;
        private Button websiteButton;
        private TextView contactInfoTextView;

        public ContactFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_contact, container, false);
            callButton = (Button) rootView.findViewById(R.id.callButton);
            callButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent callIntent = new Intent(Intent.ACTION_DIAL)
                            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    callIntent.setData(Uri.parse("tel:7244528750"));
                    getActivity().startActivity(callIntent);
                }
            });

            emailButton = (Button) rootView.findViewById(R.id.emailButton);
            emailButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("text/html");
                    intent.putExtra(Intent.EXTRA_EMAIL, "brucec@pennaflame.com");
                    intent.putExtra(Intent.EXTRA_SUBJECT, "");

                    startActivity(Intent.createChooser(intent, "Send Email"));
                }
            });

            websiteButton = (Button) rootView.findViewById(R.id.websiteButton);
            websiteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), WebsiteActivity.class);
                    startActivity(intent);
                }
            });

            contactInfoTextView = (TextView) rootView.findViewById(R.id.contactInfoTextView);
            contactInfoTextView.setText("Penna Flame Industries\n1856 State Route 588\nZelienople, PA 16063-3902");

            return rootView;
        }
    }
}
