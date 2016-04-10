//package demo.leonardogcsoares.webrtc;
//
//import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.Toolbar;
//import android.util.Log;
//
//import java.io.IOException;
//import java.util.concurrent.TimeoutException;
//
//import io.nats.client.Connection;
//import io.nats.client.ConnectionFactory;
//import io.nats.client.Message;
//import io.nats.client.MessageHandler;
//import soares.leonardo.com.greta.signaling.Connected;
//import soares.leonardo.com.greta.signaling.Greta;
//import soares.leonardo.com.greta.signaling.Subscriber;
//
//
//public class PubSubActivity extends AppCompatActivity {
//
//    private static final String TAG = "PubSubActivity";
//    int i=0;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_pub_sub);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//
//        Greta.initializeGretaConnection("someRandomToken");
//
      //  Greta.Signaling.connected(new Connected() {
       //
      //      @Override
      //      public void onConnection() {
      //          Log.d(TAG, "Greta Signaling finished connecting");
      //          Greta.Signaling.subscribe("channel", new Subscriber() {
      //              @Override
      //              public void messageReceived(String message) {
       //
      //                  Log.d(TAG, "Subscribe message received: " + message);
      //              }
      //          });
       //
      //          Greta.Signaling.publish("channel", "This is a new publish message");
      //      }
      //  });
//
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//
////        Greta.Signaling.subscribe("channel", new Subscriber() {
////            @Override
////            public void messageReceived(String message) {
////                Log.d(TAG, "Subscribe message received: " + message);
////            }
////        });
////
////        Greta.Signaling.publish("channel", "This is a publish message");
//
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//    }
//}
