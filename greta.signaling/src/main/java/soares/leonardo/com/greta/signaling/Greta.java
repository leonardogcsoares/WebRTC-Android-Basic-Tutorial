package soares.leonardo.com.greta.signaling;

import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.TimeoutException;

import io.nats.client.Connection;
import io.nats.client.ConnectionFactory;
import io.nats.client.Message;
import io.nats.client.MessageHandler;

/**
 * Created by leonardogcsoares on 4/8/2016.
 */
public class Greta {

    private static final String TAG = "Greta";

    private static Greta mGreta = new Greta();
    private static String mToken = null;

    private Greta() {}

    public static void initializeGretaConnection(String token) {
        if (!token.isEmpty())
            mToken = token;


    }

    public static class Signaling {

        private static final String TAG = "GretaSignaling";

        private static ConnectionFactory natsConnectionFactory;
        private static Connection natsConnection;
        private static boolean mIsConnected = false;

        public static void connected(final Connected connected) {

            natsConnectionFactory = new ConnectionFactory("nats://demo.nats.io:4222");

            Thread signalingThread = new Thread() {
                @Override
                public void run() {
                    try {
                        natsConnection = natsConnectionFactory.createConnection();
                        mIsConnected = true;
                        connected.onConnection();

                    } catch (TimeoutException | IOException e) {
                        e.printStackTrace();
                    }
                }
            };

            signalingThread.start();
        }

        public static void subscribe(String channel, final Subscriber subscriber) {

            natsConnection.subscribe(mToken + channel, new MessageHandler() {
                @Override
                public void onMessage(Message msg) {
                    subscriber.messageReceived(Arrays.toString(msg.getData()));
                }
            });
        }

        public static void publish(String channel, String message) {
            natsConnection.publish(mToken+channel, message.getBytes());

        }

    }

}
