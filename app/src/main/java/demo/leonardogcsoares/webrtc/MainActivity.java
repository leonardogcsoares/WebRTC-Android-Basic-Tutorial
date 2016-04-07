package demo.leonardogcsoares.webrtc;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;


import org.webrtc.DataChannel;
import org.webrtc.IceCandidate;
import org.webrtc.MediaConstraints;
import org.webrtc.MediaStream;
import org.webrtc.PeerConnection;
import org.webrtc.PeerConnectionFactory;
import org.webrtc.SdpObserver;
import org.webrtc.SessionDescription;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String LOCAL_MEDIA_STRING_ID = "Any string here";

    private static final String localDescription = "local";
    private static final String remoteDescription = "remote";
    private static final String TAG = "MainActivity";

    private PeerConnection localPeerConnection;
    private PeerConnection remotePeerConnection;

    private LocalPCObserver localPCObserver;
    private RemotePCObserver remotePCObserver;

    private LocalDataChannelObserver localDataChannelObserver;
    private RemoteDataChannelObserver remoteDataChannelObserver;

    private DataChannel sendChannel;
    private DataChannel receiveChannel;


    private List<PeerConnection.IceServer> iceServerList = new ArrayList<>();


    private SdpObserver localSdpObserver = new SdpObserver() {
        @Override
        public void onCreateSuccess(SessionDescription sessionDescription) {
            Log.d(TAG, "localPeerConnection onCreateSuccess type: " + sessionDescription.type.toString());

            Log.d("TAG", "Session descrpt. size: " + sessionDescription.description.length());
            if (sessionDescription.type == SessionDescription.Type.OFFER) {

                localPeerConnection.setLocalDescription(localSdpObserver, sessionDescription);
                remotePeerConnection.setRemoteDescription(remoteSdpObserver, sessionDescription);
                Log.d(TAG, String.valueOf("localSdpObserver is same session description: " + localPeerConnection.getLocalDescription().description.equals(remotePeerConnection.getRemoteDescription().description)));
            }
        }

        @Override
        public void onSetSuccess() {

            Log.d(TAG, "localPeerConnection onSetSuccess");

        }

        @Override
        public void onCreateFailure(String s) {
            Log.d(TAG, "localPeerConnection onCreateFailure: " + s);
        }

        @Override
        public void onSetFailure(String s) {
            Log.d(TAG, "localPeerConnection onSetFailure: " + s);
        }
    };
    private SdpObserver remoteSdpObserver = new SdpObserver() {

        @Override
        public void onCreateSuccess(SessionDescription sessionDescription) {
            Log.d(TAG, "remotePeerConnection onCreateSuccess type: " + sessionDescription.type.toString());
            final String desc = sessionDescription.description;

            if (sessionDescription.type == SessionDescription.Type.ANSWER) {


                localPeerConnection.setRemoteDescription(localSdpObserver, sessionDescription);
                Log.d(TAG, String.valueOf(localPeerConnection.signalingState()));
                remotePeerConnection.setLocalDescription(remoteSdpObserver, sessionDescription);

                Log.d(TAG, String.valueOf("remoteSdpObserver is same session description: " + localPeerConnection.getRemoteDescription().description.equals(remotePeerConnection.getLocalDescription().description)));




//                byte[] b = "Some string to send over WebRTC".getBytes(Charset.forName("UTF-8"));
//                ByteBuffer byteBuffer = ByteBuffer.allocate(16384);
//                byteBuffer.put(b);
//                DataChannel.Buffer buffer = new DataChannel.Buffer(byteBuffer, false);
//                sendChannel.send(buffer);
//                Log.d(TAG, "Sent string over Data Channel");
//
//                Log.d(TAG, "Data Channel Remote: " + sendChannel.state());
            }
        }

        @Override
        public void onSetSuccess() {
            Log.d(TAG, "remotePeerConnection onSetSuccess");
        }

        @Override
        public void onCreateFailure(String s) {
            Log.d(TAG, "remotePeerConnection onCreateFailure: " + s);
        }

        @Override
        public void onSetFailure(String s) {
            Log.d(TAG, "remotePeerConnection onSetFailure: " + s);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(TAG, "init MainActivity onCreate");

        Log.d(TAG, PeerConnectionFactory.initializeAndroidGlobals(getApplicationContext(), true, true, true)
                ? "Success initAndroidGlobals" : "Failed initAndroidGlobals");


        PeerConnectionFactory peerConnectionFactory = new PeerConnectionFactory();


        MediaConstraints mediaConstraints = new MediaConstraints();
//        mediaConstraints.optional.add(new MediaConstraints.KeyValuePair("DtlsSrtpKeyAgreement", "true"));
        mediaConstraints.optional.add(new MediaConstraints.KeyValuePair("RtpDataChannels", "true"));


        Log.d(TAG, "has yet to create local and remote peerConnection");

        localPCObserver = new LocalPCObserver();
        localPeerConnection = peerConnectionFactory.createPeerConnection(
                iceServerList,
                mediaConstraints,
                localPCObserver
        );
        Log.d(TAG, "creating the data channel");
        localDataChannelObserver = new LocalDataChannelObserver();
        remoteDataChannelObserver = new RemoteDataChannelObserver();

        remotePCObserver = new RemotePCObserver();
        remotePeerConnection = peerConnectionFactory.createPeerConnection(
                iceServerList,
                mediaConstraints,
                remotePCObserver
        );


        Log.d(TAG, "initialized both local and remote peerConnections");
        localPeerConnection.createOffer(localSdpObserver, mediaConstraints);

    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.d(TAG, "Answer created");
        remotePeerConnection.createAnswer(remoteSdpObserver, new MediaConstraints());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (sendChannel != null) {
            sendChannel.close();
            sendChannel.unregisterObserver();
        }

        if (receiveChannel != null) {
            receiveChannel.close();
            receiveChannel.unregisterObserver();
        }

        if (localPeerConnection != null)
            localPeerConnection.close();

        if (remotePeerConnection != null)
            remotePeerConnection.close();

    }

    private class DCObserver implements DataChannel.Observer {

        @Override
        public void onBufferedAmountChange(long l) {

        }

        @Override
        public void onStateChange() {
            Log.d(TAG, "onStateChanged");

        }

        @Override
        public void onMessage(DataChannel.Buffer buffer) {

        }
    }


    public class LocalPCObserver implements PeerConnection.Observer {

//        private static final String TAG = "LocalPCObserver";

        /////// PeerConnection Observer Implementation ///////
        @Override
        public void onSignalingChange(PeerConnection.SignalingState signalingState) {
            Log.d(TAG, "local onSignalingChange: " + signalingState.toString());
        }

        @Override
        public void onIceConnectionChange(PeerConnection.IceConnectionState iceConnectionState) {
            Log.d(TAG, "local onIceConnectionChange: " + iceConnectionState.toString());
        }

        @Override
        public void onIceConnectionReceivingChange(boolean b) {
            Log.d(TAG, "local onIceConnectionReceivingChange: " + String.valueOf(b));
        }

        @Override
        public void onIceGatheringChange(PeerConnection.IceGatheringState iceGatheringState) {
            Log.d(TAG, "local onIceGatheringChange: " + iceGatheringState.toString());
        }

        @Override
        public void onIceCandidate(IceCandidate iceCandidate) {
            Log.d(TAG, "local onIceCandidate: " + iceCandidate.toString());
        }

        @Override
        public void onAddStream(MediaStream mediaStream) {

        }

        @Override
        public void onRemoveStream(MediaStream mediaStream) {

        }

        @Override
        public void onDataChannel(DataChannel dataChannel) {
            Log.d(TAG, "local onDataChannel called");

        }

        @Override
        public void onRenegotiationNeeded() {
            Log.d(TAG, "local onRenegotiationNeeded");
        }

    }

    public class RemotePCObserver implements PeerConnection.Observer{

        /////// PeerConnection Observer Implementation ///////

//        private static final String TAG = "RemotePCObserver";

        @Override
        public void onSignalingChange(PeerConnection.SignalingState signalingState) {
            Log.d(TAG, "remote onSignalingChange: " + signalingState.toString());
        }

        @Override
        public void onIceConnectionChange(PeerConnection.IceConnectionState iceConnectionState) {
            Log.d(TAG, "remote onIceConnectionChange: " + iceConnectionState.toString());
        }

        @Override
        public void onIceConnectionReceivingChange(boolean b) {
            Log.d(TAG, "remote onIceConnectionReceivingChange: " + String.valueOf(b));
        }

        @Override
        public void onIceGatheringChange(PeerConnection.IceGatheringState iceGatheringState) {
            Log.d(TAG, "remote onIceGatheringChange: " + iceGatheringState.toString());
        }

        @Override
        public void onIceCandidate(IceCandidate iceCandidate) {
            Log.d(TAG, "remote onIceCandidate: " + iceCandidate.toString());
        }

        @Override
        public void onAddStream(MediaStream mediaStream) {

        }

        @Override
        public void onRemoveStream(MediaStream mediaStream) {

        }

        @Override
        public void onDataChannel(DataChannel dataChannel) {
            Log.d(TAG, "remote onDataChannel called Local");
            receiveChannel = dataChannel;
            receiveChannel.registerObserver(new DCObserver());

        }

        @Override
        public void onRenegotiationNeeded() {
            Log.d(TAG, "remote onRenegotiationNeeded");
        }
    }

    private class LocalDataChannelObserver implements DataChannel.Observer {

        @Override
        public void onBufferedAmountChange(long l) {

        }

        @Override
        public void onStateChange() {
            Log.d(TAG, "localDataChannelObserver state changed" + sendChannel.state().name());
        }

        @Override
        public void onMessage(DataChannel.Buffer buffer) {

        }
    }

    private class RemoteDataChannelObserver implements DataChannel.Observer {

        @Override
        public void onBufferedAmountChange(long l) {

        }

        @Override
        public void onStateChange() {

        }

        @Override
        public void onMessage(DataChannel.Buffer buffer) {

        }
    }


}
