function trace(arg) {
  var now = (window.performance.now() / 1000).toFixed(3);
  console.log(now + ': ', arg);
}

var startButton = document.getElementById('startButton');
var callButton = document.getElementById('callButton');
var hangupButton = document.getElementById('hangupButton');
var id_input = document.getElementById("id-input");

var channelOpenTime,
  initiator;

startButton.onclick = start;
callButton.onclick = call;
hangupButton.onclick = hangup;

var startTime;
var localVideo = document.getElementById('localVideo');
var remoteVideo = document.getElementById('remoteVideo');

localVideo.addEventListener('loadedmetadata', function () {
  trace('Local video videoWidth: ' + this.videoWidth + 'px,  videoHeight: ' + this.videoHeight + 'px');
});

remoteVideo.addEventListener('loadedmetadata', function () {
  trace('Remote video videoWidth: ' + this.videoWidth + 'px,  videoHeight: ' + this.videoHeight + 'px');
});

remoteVideo.onresize = function () {
  trace('Remote video size changed to ' + remoteVideo.videoWidth + 'x' + remoteVideo.videoHeight);
  // We'll use the first onsize callback as an indication that video has started
  // playing out.
  if (startTime) {
    var elapsedTime = window
      .performance
      .now() - startTime;
    trace('Setup time: ' + elapsedTime.toFixed(3) + 'ms');
    startTime = null;
  }
};

var localStream,
  remoteStream;
var pc1;
var pc2;
var offerOptions = {
  offerToReceiveAudio: 1,
  offerToReceiveVideo: 1
};

var servers = {
  iceServers: [
    { // Information about ICE servers - Use your own!
      urls: "stun:stun.l.google.com:19302", // A TURN server
      // username: "webrtc", credential: "turnserver"
    }
  ]
};

function
getName(pc) {
  return (pc === pc1)
    ? 'pc1'
    : 'pc2';
}

function
getOtherPc(pc) {
  return (pc === pc1)
    ? pc2
    : pc1;
}

function
start() {
  openChannel();
  pc1 = new RTCPeerConnection(servers);
  pc1.onicecandidate = function (event) {
    trace("状态改变触发:");
    trace(event);
    if (event.candidate) {
      socket.send(JSON.stringify({
        option: "message",
        data: {
          type: "candidate",
          data: event.candidate
        }
      }));
    }
   // pc1.addIceCandidate(event.candidate);
    // pc1   .addIceCandidate(event.candidate)   .then(function () {
    // onAddIceCandidateSuccess(pc1);   }, function (err) {
    // onAddIceCandidateError(pc1, err);   });
    trace(getName(pc1) + ' ICE candidate: \n' + (event.candidate
      ? event.candidate.candidate
      : '(null)'));
  };

  pc1.oniceconnectionstatechange = function (e) {
    trace("状态已改变");
    onIceStateChange(pc1, e);
  };

  trace('Requesting local stream');
  // startButton.disabled = true;
  navigator
    .mediaDevices
    .getUserMedia({audio: true, video: true})
    .then(function (stream) {
      trace('Received local stream');
      console.log(stream);
      //var url = URL.createObjectURL(stream); localVideo.src = url;
      localVideo.srcObject = stream;
      localStream = stream;
      pc1.onaddstream = function (e) {
        console.log(e);

        remoteVideo.srcObject = e.stream;
        remoteStream = e.stream;
      };
      pc1.addStream(localStream);
    })
    .catch(function (e) {
      alert('getUserMedia() error: ' + e.name);
    });
}

function
call() {
  var who_input = document.getElementById("who");
  var whoid = who_input.value;

  // pc2.oniceconnectionstatechange = function (e) {   trace("pc2 onicecandidate
  // state change");   onIceStateChange(pc2, e); };

  trace('Added local stream to pc1');
  trace('pc1 createOffer start');
  pc1
    .createOffer(offerOptions)
    .then(function (desc) {
      trace('pc1 setLocalDescription start');

      // console.log("发送", JSON.stringify({option: "call", to: whoid}))
      // socket.send(JSON.stringify({option: "call", to: whoid}));

      pc1
        .setLocalDescription(desc)
        .then(function () {
          onSetLocalSuccess(pc1);
          console.log(desc);
          socket.send(JSON.stringify({option: 'call', to: whoid, data: pc1.localDescription}))
        }, onSetSessionDescriptionError);
    }, onCreateSessionDescriptionError);
}

function
onCreateSessionDescriptionError(error) {
  trace('Failed to create session description: ' + error.toString());
}

function
onIceCandidate(pc, event) {}

function
onAddIceCandidateSuccess(pc) {
  trace(getName(pc) + ' addIceCandidate success');
}

function
onAddIceCandidateError(pc, error) {
  trace(getName(pc) + ' failed to add ICE Candidate: ' + error.toString());
}

function
onIceStateChange(pc, event) {
  if (pc) {
    trace(getName(pc) + ' ICE state: ' + pc.iceConnectionState);
    console.log('ICE state change event: ', event);
  }
}

function
hangup() {
  trace('Ending call');
  pc1.close();
  // pc2.close();
  pc1 = null;
  //pc2 = null; hangupButton.disabled = true; callButton.disabled = false;
}

function openChannel() {
  var id = id_input.value;
  //socket = new WebSocket("ws://192.168.10.106:8080/ws/chat/" + id);
  socket = new WebSocket("ws://60.205.217.40/RTC_Demo/ws/chat/" + id);
  socket.onopen = onChannelOpened;
  socket.onmessage = onChannelMessage;
  socket.onclose = onChannelClosed;
  socket.onerror = onChannelError;

}

// websocket打开
function
onChannelOpened() {
  console.log("websocket打开");
  channelOpenTime = new Date();
  channelReady = true;
  if (initiator) 
    maybeStart();
  }

// websocket收到消息
function
onChannelMessage(message) {
  console.log("收到信息 : " + message.data);
  processSignalingMessage(message.data); //建立视频连接
}

// 处理消息
function
processSignalingMessage(message) {
  var msg = JSON.parse(message);
  if (msg.option === "agree") {
    trace('对方已同意通话');
    pc1.setRemoteDescription(new RTCSessionDescription(msg.data))
  } else if (msg.option === "bye") {
    onRemoteClose();
  } else if (msg.option === "nowaiting") {
    onRemoteClose();
    setNotice("对方已离开！");
  } else if (msg.option === "messsage") {
    if (msg.data.type === "candidate") {
      trace('收到验证消息：', msg.data);
      var candidate = new RTCIceCandidate(msg.data.data);
      pc1.addIceCandidate(candidate);
    }
  } else if (msg.option == "requestLink") {

    pc1
      .setRemoteDescription(new RTCSessionDescription(msg.data))
      .then(function () {
        trace("set remote Description success");
      }, onSetSessionDescriptionError);
    trace('收到通话请求');
    // Since the 'remote' side has no media stream we need to pass in the right
    // constraints in order for it to accept the incoming offer of audio and video.
    pc1
      .createAnswer(pc1.remoteDescription)
      .then(function (desc) {
        trace('Answer from pc2:\n' + desc.sdp);
        trace('pc2 setLocalDescription start');
        // pc2   .setLocalDescription(desc)   .then(function () {
        // onSetLocalSuccess(pc2);   }, onSetSessionDescriptionError);

        pc1
          .setLocalDescription(desc)
          .then(function () {
            console.log("回答通话请求");
            socket.send(JSON.stringify({option: "answer", request: "ok", data: pc1.localDescription}));
          })

      }, onCreateSessionDescriptionError);

    // socket.send(JSON.stringify({option: "answer", request: "sorry", requestid:
    // msg.requestid}));
  }
}

// websocket异常
function
onChannelError(event) {
  console.log("websocket异常 ： " + event);

  //alert("websocket异常");
}

// websocket关闭
function
onChannelClosed() {
  console.log("websocket关闭");
  if (!channelOpenTime) {
    channelOpenTime = new Date();
  }
  channelCloseTime = new Date();
  //openChannel();
}

function
onSetLocalSuccess(pc) {
  trace(getName(pc) + ' setLocalDescription complete');
}

function
onSetRemoteSuccess(pc) {
  trace(getName(pc) + ' setRemoteDescription complete');
}

function
onSetSessionDescriptionError(error) {
  trace('Failed to set session description: ' + error.toString());
}
