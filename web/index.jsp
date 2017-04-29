<%--
  Created by IntelliJ IDEA.
  User: forvoid
  Date: 4/18/2017
  Time: 4:27 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
  <title>Testing websockets</title>
</head>
<body>
<div>
  <input type="submit" value="Start" onclick="start()" />
</div>
<div id="messages"></div>
<script type="text/javascript">
    var webSocket;
    if ('WebSocket' in window) {
        webSocket = new WebSocket("ws://localhost:8080/ws/websocket");
    } else if ('MozWebSocket' in window) {
        webSocket = new MozWebSocket("ws://localhost:8080/websocket");
    } else {
        alert("js");
        webSocket = new SockJS("http://localhost:8080/Origami/sockjs/webSocketServer");
    }

    webSocket.onerror = function(event) {
        onError(event)
    };

    webSocket.onopen = function(event) {
        onOpen(event)
    };

    webSocket.onmessage = function(event) {
        onMessage(event)
    };

    function onMessage(event) {
        document.getElementById('messages').innerHTML
            += '<br />' + event.data;
    }

    function onOpen(event) {
        document.getElementById('messages').innerHTML
            = 'Connection established';
    }

    function onError(event) {
        //console.log(event);
        //console.log(event.data);
        alert(event.data);
    }

    function start() {
        webSocket.send('hello');
        return false;
    }
</script>
</body>
</html>