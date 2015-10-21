package uk.co.edwardiii.rugbybins

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

class RugbyBinsActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hello_layout);
    }
    
    @Override
    public void onStart() {
        super.onStart();
        TextView textView = (TextView) findViewById(R.id.text_view);
        //def council = new Council()
        //def binCollections = council.getBinCollectionsFor('Temple Street');

        textView.setText("Hello world!");
    }

} 

