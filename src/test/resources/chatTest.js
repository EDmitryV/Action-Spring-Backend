const stompClient = new StompJs.Client({
    brokerURL: 'ws://localhost:8080/action-ws'
});



const baseSendToEndpoint = "/app/chat/";
var SendToEndpoint='';
const baseSubscribeEndpoint = '/topic/'
// '/topic/'+$("#chat_id").val()

stompClient.onConnect = (frame) => {
    setConnected(true);
    console.log('Connected: ' + frame);
    subscribeEndpoint= baseSubscribeEndpoint+ $("#chat_id").val();
    console.log("Subscribed to: "+ subscribeEndpoint);
    stompClient.subscribe(subscribeEndpoint, (msg) => {
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
    sendToEndpoint = baseSendToEndpoint +$("#chat_id").val();
    console.log("connecting to: " + sendToEndpoint);
    stompClient.activate();
}

/*
function connect(event) {
    var socket = new SockJS('http://localhost:8080/action-ws');
    stompClient = Stomp.over(socket);

    stompClient.connect({}, onConnected, onError);
}
*/

function disconnect() {
    stompClient.deactivate();
    setConnected(false);
    console.log("Disconnected");
}

function sendName() {
    console.log(JSON.stringify({'content': $("#name").val()}));
    console.log("Sended to: "+ sendToEndpoint);
    stompClient.publish({
        destination: sendToEndpoint,
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

