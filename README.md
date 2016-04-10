# WebRTC Android Basic Tutorial
>**Using the DataChannel to communicate between two peers on one Android device**

As I was searching for tutorials using WebRTC on native Android I found many that either taught you to use a specific SDK or taught 
you only the basics of Video/Audio chat clients. What I really wanted was a tutorial that taught you the basics of using the DataChannel on Android to transmit data between peers. Since I found none, I decided to make one.

Following this tutorial or looking through the code you will learn how to use the PeerConnection API to create two peers inside an Android and exchange data between using the DataChannel. This will thus give you the basics to implement more complicated and interesting applications.

In no way do I intend in this tutorial, to teach you the basics of how WebRTC works. That information can be acquire from these links:
>**Learning about WebRTC**
- [Getting Started with WebRTC - Sam Dutton](https://www.google.com.br/url?sa=t&rct=j&q=&esrc=s&source=web&cd=1&cad=rja&uact=8&ved=0ahUKEwjbgbOLhIPMAhXBiZAKHZ1FA5EQFggrMAA&url=http%3A%2F%2Fwww.html5rocks.com%2Fen%2Ftutorials%2Fwebrtc%2Fbasics%2F&usg=AFQjCNF-Cvvqsgt-nyHOSYclhUhG7NuCng&sig2=pVTzGWz0k51V-GlzK1LVfQ&bvm=bv.119028448,d.Y2I)
- [WebRTC in the real world: STUN, TURN and signaling] (http://www.html5rocks.com/en/tutorials/webrtc/infrastructure/)

>**Samples used as basis for this tutorial**

- [PeerConnection Sample](https://webrtc.github.io/samples/src/content/peerconnection/pc1/)
- [Generate and Transfer Data](http://webrtc.github.io/samples/src/content/datachannel/datatransfer/)

###Getting Started
Before developing Android apps that use native WebRTC you need the compiled code. [WebRTC.org](https://webrtc.org/native-code/android/) offers
a barebones guide to obtaining the compiled Java code. But a simpler and faster way to get this library is to use the shortcut offered by [io.pristine](http://mvnrepository.com/artifact/io.pristine/libjingle).
This is done by placing the following inside the `build.gradle` of the app.
```
compile 'io.pristine:libjingle:_version_@aar'
```
Where `_version_` represents the current version of the library. The current working version is `11139`. (04/09/2016)
  
###How it works

As you start the app the first steps it takes is creating the PeerConnection between the simulated Local Peer and Remote Peer. All the steps taken are logged using the `log.d()` function to show the steps 

[App Screen Image](https://github.com/leonardogcsoares/WebRTC-Android-Basic-Tutorial/blob/master/Screen.jpg)

Given that both the Local and Remote peers are on the same mobile device, it is was not necessary to implement a signaling mecanism to exchange the `Session Description Protocol` between peers. If interested in signaling mecanisms, take a look at [NATS.io](http://nats.io/)

As soon as the PeerConnection is established, the DataChannel for each peer is created and "connected". Thus possibilitating the exchange of messages as shown on the app screen.

