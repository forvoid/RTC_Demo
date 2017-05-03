function trace(arg) {
  var now = (window.performance.now() / 1000).toFixed(3);
  console.log(now + ': ', arg);
}

var startButton = document.getElementById('startButton');
var callButton = document.getElementById('callButton');
var hangupButton = document.getElementById('hangupButton');
var id_input = document.getElementById("id-input");


var channelOpenTime, initiator;


startButton.onclick = start;
callButton.onclick = call;
hangupButton.onclick = hangup;

var startTime;
var localVideo = document.getElementById('localVideo');
var remoteVideo = document.getElementById('remoteVideo');

localVideo.addEventListener('loadedmetadata', function () {
  trace('Local video videoWidth: ' + this.videoWidth +
    'px,  videoHeight: ' + this.videoHeight + 'px');
});

remoteVideo.addEventListener('loadedmetadata', function () {
  trace('Remote video videoWidth: ' + this.videoWidth +
    'px,  videoHeight: ' + this.videoHeight + 'px');
});

remoteVideo.onresize = function () {
  trace('Remote video size changed to ' +
    remoteVideo.videoWidth + 'x' + remoteVideo.videoHeight);
  // We'll use the first onsize callback as an indication that video has started
  // playing out.
  if (startTime) {
    var elapsedTime = window.performance.now() - startTime;
    trace('Setup time: ' + elapsedTime.toFixed(3) + 'ms');
    startTime = null;
  }
};

var localStream;
var pc1;
var pc2;
var offerOptions = {
  offerToReceiveAudio: 1,
  offerToReceiveVideo: 1
};


var servers = { 
  iceServers: [     // Information about ICE servers - Use your own!
  {
    url: "stun:stun.l.google.com:19302",  // A TURN server
    // username: "webrtc",
    // credential: "turnserver"
  }
]};


function getName(pc) {
  return (pc === pc1) ? 'pc1' : 'pc2';
}

function getOtherPc(pc) {
  return (pc === pc1) ? pc2 : pc1;
}

function gotStream(stream) {
  trace('Received local stream');
  console.log(stream);
  //var url = URL.createObjectURL(stream);
  //localVideo.src = url;
  localVideo.srcObject = stream;
  localStream = stream;

}

function start() {

  openChannel();

  pc1 = new RTCPeerConnection(servers);


  trace('Requesting local stream');
  // startButton.disabled = true;
  navigator.mediaDevices.getUserMedia({
      audio: true,
      video: true
    })
    .then(gotStream)
    .catch(function (e) {
      alert('getUserMedia() error: ' + e.name);
    });
}

function call() {

  var who_input = document.getElementById("who");
  var whoid = who_input.value;

  socket.send(JSON.stringify({
    option: "call",
    id: whoid
  }));

  

  //callButton.disabled = true;
  //hangupButton.disabled = false;
  trace('Starting call');
  //startTime = window.performance.now();
 // var videoTracks = localStream.getVideoTracks();
 // var audioTracks = localStream.getAudioTracks();
  // if (videoTracks.length > 0) {
  //   trace('Using video device: ' + videoTracks[0].label);
  // }
  // if (audioTracks.length > 0) {
  //   trace('Using audio device: ' + audioTracks[0].label);
  // }

  pc1 = new RTCPeerConnection(servers);
  trace('Created local peer connection object pc1');
  pc1.onicecandidate = function (e) {
    trace("pc1 onicecandidate");
    onIceCandidate(pc1, e);
  };
  pc2 = new RTCPeerConnection(servers);
  //trace('Created remote peer connection object pc2');
  pc2.onicecandidate = function (e) {
    trace("pc2 onicecandidate");
    onIceCandidate(pc2, e);
  };
  pc1.oniceconnectionstatechange = function (e) {

    trace("pc1 onicecandidate state change");
    onIceStateChange(pc1, e);
  };
  pc2.oniceconnectionstatechange = function (e) {
    trace("pc2 onicecandidate state change");
    onIceStateChange(pc2, e);
  };
  pc2.onaddstream = gotRemoteStream;

  pc1.addStream(localStream);
  trace('Added local stream to pc1');

  trace('pc1 createOffer start');
  pc1.createOffer(
    offerOptions
  ).then(
    onCreateOfferSuccess,
    onCreateSessionDescriptionError
  );
}

function onCreateSessionDescriptionError(error) {
  trace('Failed to create session description: ' + error.toString());
}

function onCreateOfferSuccess(desc) {
  //trace('Offer from pc1\n' + desc.sdp);
  trace('pc1 setLocalDescription start');
  pc1.setLocalDescription(desc).then(
    function () {
      onSetLocalSuccess(pc1);
    },
    onSetSessionDescriptionError
  );
  trace('pc2 setRemoteDescription start');
  pc2.setRemoteDescription(desc).then(
    function () {
      onSetRemoteSuccess(pc2);
    },
    onSetSessionDescriptionError
  );
  trace('pc2 createAnswer start');
  // Since the 'remote' side has no media stream we need
  // to pass in the right constraints in order for it to
  // accept the incoming offer of audio and video.
  pc2.createAnswer().then(
    onCreateAnswerSuccess,
    onCreateSessionDescriptionError
  );
}

function onSetLocalSuccess(pc) {
  trace(getName(pc) + ' setLocalDescription complete');
}

function onSetRemoteSuccess(pc) {
  trace(getName(pc) + ' setRemoteDescription complete');
}

function onSetSessionDescriptionError(error) {
  trace('Failed to set session description: ' + error.toString());
}

function gotRemoteStream(e) {
  remoteVideo.srcObject = e.stream;
  trace('pc2 received remote stream');
}

function onCreateAnswerSuccess(desc) {
  trace('Answer from pc2:\n' + desc.sdp);
  trace('pc2 setLocalDescription start');
  pc2.setLocalDescription(desc).then(
    function () {
      onSetLocalSuccess(pc2);
    },
    onSetSessionDescriptionError
  );
  trace('pc1 setRemoteDescription start');
  pc1.setRemoteDescription(desc).then(
    function () {
      onSetRemoteSuccess(pc1);
    },
    onSetSessionDescriptionError
  );
}

function onIceCandidate(pc, event) {
  getOtherPc(pc).addIceCandidate(event.candidate)
    .then(
      function () {
        onAddIceCandidateSuccess(pc);
      },
      function (err) {
        onAddIceCandidateError(pc, err);
      }
    );
  trace(getName(pc) + ' ICE candidate: \n' + (event.candidate ?
    event.candidate.candidate : '(null)'));
}

function onAddIceCandidateSuccess(pc) {
  trace(getName(pc) + ' addIceCandidate success');
}

function onAddIceCandidateError(pc, error) {
  trace(getName(pc) + ' failed to add ICE Candidate: ' + error.toString());
}

function onIceStateChange(pc, event) {
  if (pc) {
    trace(getName(pc) + ' ICE state: ' + pc.iceConnectionState);
    console.log('ICE state change event: ', event);
  }
}

function hangup() {
  trace('Ending call');
  pc1.close();
  pc2.close();
  pc1 = null;
  pc2 = null;
  //hangupButton.disabled = true;
  //callButton.disabled = false;
}




function openChannel() {
  var id = id_input.value;
  socket = new WebSocket("ws://60.205.217.40/RTC_Demo/ws/chat/" + id);

  socket.onopen = onChannelOpened;
  socket.onmessage = onChannelMessage;
  socket.onclose = onChannelClosed;
  socket.onerror = onChannelError;

}


// websocket打开
function onChannelOpened() {
  console.log("websocket打开");

  channelOpenTime = new Date();
  channelReady = true;
  if (initiator)
    maybeStart();
}

// websocket收到消息
function onChannelMessage(message) {
  console.log("收到信息 : " + message.data);
  processSignalingMessage(message.data); //建立视频连接
}

// 处理消息
function processSignalingMessage(message) {
  var msg = JSON.parse(message);
  if (msg.option === "offer") {
    if (!initiator && !started)
      maybeStart();
    pc.setRemoteDescription(new RTCSessionDescription(msg));
    doAnswer();
  } else if (msg.option === "answer" && started) {
    pc.setRemoteDescription(new RTCSessionDescription(msg));
  } else if (msg.option === "candidate" && started) {
    var candidate = new RTCIceCandidate({
      sdpMLineIndex: msg.label,
      candidate: msg.candidate
    });
    pc.addIceCandidate(candidate);
  } else if (msg.option === "bye" && started) {
    onRemoteClose();
  } else if (msg.option === "nowaiting") {
    onRemoteClose();
    setNotice("对方已离开！");
  }
  if(msg.option =="requestLink"){
    console.log("requestlink")
    socket.send(JSON.stringify({option:"answer",request:"ok",requestid:msg.requestid}));


  }
}

// websocket异常
function onChannelError(event) {
  console.log("websocket异常 ： " + event);

  //alert("websocket异常");
}

// websocket关闭
function onChannelClosed() {
  console.log("websocket关闭");

  if (!channelOpenTime) {
    channelOpenTime = new Date();
  }
  channelCloseTime = new Date();
  //openChannel();
}