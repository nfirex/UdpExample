package ru.nfirex.udp.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import ru.nfirex.udp.R;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_start_receiver:
                final Intent startIntent = new Intent(this, ReceiverIntentService.class)
                        .putExtra(ReceiverIntentService.EXTRA_STATUS, ReceiverIntentService.STATUS_START);

                startService(startIntent);
                return true;

            case R.id.action_stop_receiver:
                final Intent stopIntent = new Intent(this, ReceiverIntentService.class)
                        .putExtra(ReceiverIntentService.EXTRA_STATUS, ReceiverIntentService.STATUS_STOP);

                startService(stopIntent);
                return true;

            case R.id.action_send:
                final Intent sendIntent = new Intent(this, SenderIntentService.class)
                        .putExtra(SenderIntentService.EXTRA_DATA, "1234567890");

                startService(sendIntent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}