const stompClient = new StompJs.Client({
    brokerURL: 'ws://localhost:8080/action-ws'
});


stompClient.onConnect = (frame) => {
    setConnected(true);
    console.log('Connected: ' + frame);
    console.log("Subscribed to: "+ '/topic/'+$("#chat_id").val())
    stompClient.subscribe('/topic/'+$("#chat_id").val(), (msg) => {
        console.log(JSON.parse(msg.body));
        showMessage(JSON.parse(msg.body).content);
    });
};


stompClient.onWebSocketError = (error) => {
    console.error('Error with websocket', error);
};

stompClient.onStompError = (frame) => {
    console.error('Broker reported error: ' + frame.headers['message']);
    console.error('Additional details: ' + frame.body);
};

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#conversation").show();
    }
    else {
        $("#conversation").hide();
    }
    $("#messages").html("");
}

function connect() {
    console.log("connecting to");
    console.log('/chat/' + $("#chat_id").val());
    stompClient.activate();
}

function disconnect() {
    stompClient.deactivate();
    setConnected(false);
    console.log("Disconnected");
}

function sendName() {
    console.log(JSON.stringify({'content': $("#name").val()}));
    console.log("Sended to: "+ "/app/chat/"+$("#chat_id").val());
    stompClient.publish({
        destination: "/app/chat/"+$("#chat_id").val(),
        body: JSON.stringify({'content': $("#name").val()})
    });
}

function showMessage(message) {
    console.log("Messages");
    $("#messages").append("<tr><td>" + message + "</td></tr>");
}

$(function () {
    $("form").on('submit', (e) => e.preventDefault());
    $( "#connect" ).click(() => connect());
    $( "#disconnect" ).click(() => disconnect());
    $( "#send" ).click(() => sendName());

});

