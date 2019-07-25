package ysn.com.jpushdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import ysn.com.jpushdemo.model.event.TextEvent;

public class MainActivity extends AppCompatActivity {

    private StringBuilder msg = new StringBuilder();

    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.mian_activity_text);

        msg.append(textView.getText().toString());
    }

    @Override
    protected void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onTextEvent(TextEvent event) {
        msg.append("\n");
        msg.append(event.text);
        if (textView != null) {
            textView.setText(msg.toString());
        }
    }
}
