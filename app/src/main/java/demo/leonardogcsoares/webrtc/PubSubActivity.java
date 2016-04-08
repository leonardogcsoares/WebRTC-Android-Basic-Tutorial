package demo.leonardogcsoares.webrtc;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import io.nats.client.Connection;
import io.nats.client.ConnectionFactory;
import io.nats.client.Message;
import io.nats.client.MessageHandler;


public class PubSubActivity extends AppCompatActivity {

    private static final String TAG = "PubSubActivity";
    private ConnectionFactory natsConnectionFactory;
    private Connection natsConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pub_sub);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        natsConnectionFactory = new ConnectionFactory("nats://demo.nats.io:4222");
    }

    @Override
    protected void onResume() {
        super.onResume();

        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    natsConnection = natsConnectionFactory.createConnection();

                    Log.d(TAG, "natsConnection: " + natsConnection.toString());
                } catch (IOException | TimeoutException e) {
                    e.printStackTrace();
                }

                natsConnection.subscribe("foo-leonardogcsoares93", new MessageHandler() {
                    @Override
                    public void onMessage(Message msg) {
                        Log.d(TAG, "Received a message: " + msg.getData());
                    }
                });

                natsConnection.publish("foo-leonardogcsoares93", "Hello World".getBytes());
            }
        };

        thread.start();


    }

    @Override
    protected void onPause() {
        super.onPause();
        if (natsConnection != null)
            natsConnection.close();
    }
}
