
//JavaScript의 strict mode 를 활성화, 코드를 엄격하게 검사 하여오류를 줄임
'use strict';

//document.write("<script src='jquery-3.6.1.js'></script>")
document.write("<script\n" +
    "  src=\"https://code.jquery.com/jquery-3.6.1.min.js\"\n" +
    "  integrity=\"sha256-o88AwQnZB+VDvE9tvIXrMQaPlFFSUTR+nldQm1LuPXQ=\"\n" +
    "  crossorigin=\"anonymous\"></script>")

var userNamePage= document.querySelector('#userName-page');
var chatPage = document.querySelector('#chat-page');
var userNameForm = document.querySelector('#userNameForm');
var messageForm = document.querySelector('#messageForm');
var messageInput = document.querySelector('#message');
var messageArea = document.querySelector('#messageArea');
var connectingElement = document.querySelector('.connecting');

var stompClient = null;
var userName = null;

var colors = [
    '#2196F3', '#32c787', '#00BCD4', '#ff5652',
    '#ffc107', '#ff85af', '#FF9800', '#39bbb0'
];

//roomId 파라미터 가져오기 roomId 채팅방의 식별자로 사용
const url = new URL(location.href).searchParams;
const roomId = url.get('roomId');


/*
    connect 는 사용자 이름을 입력하고 서버에 웹 소켓 연결을 설정하는함수
    사용자 이름을 입력후 중복확인 수행, 웹소켓 연결을 설정,
    서버에 사용자의 입장을 아리는 메시지 전송
 */
function connect(event) {
    userName = document.querySelector('#name').value.trim();

    //userName 중복확인
    isDuplicateName();
    // userNamePage 에 hidden 속성 추가해서, chatPage 나오게함
    userNamePage.classList.add('hidden');
    chatPage.classList.remove('hidden');

    //연결할려고 하는 Socket 의 endPoint
    var socket = new SockJS('/ws-stomp');
    stompClient = Stomp.over(socket);

    stompClient.connect({}, onConnected, onError);

    event.preventDefault();
}

/*
    onConnected()
    웹 소켓 연결에 성공 했을때 호출는 함수
    웹 소켓을 통해 채팅 메시지를 받고, 사용자의 입장을
    서버에 알리는 메시지를 전송
 */
function onConnected() {

    //sub 할 url => /sub/chat/room/rommId 로 함
    stompClient.subscribe('/sub/chat/room/' +
        roomId,onMessageReceived);
    //서버에 userName 을 가진 유저가 들어왔다는 것을알리고
    // ,,,/pub/chat/enterUser 로 메시지를 보냄
    stompClient.send("/pub/chat/enterUser", {},
        JSON.stringify({
            "roomId": roomId,
            sender: userName,
            type: "ENTER",
        }));
    connectingElement.classList.add('hidden');
}
/*
    isDuplicateName()는 사용자 이름 중복되는지 확인, AJAX 요청
    을 보내는 함수 ,서버에서 사용자 이름검증, 중복되니 않으면
    그대로 사용하고 준복된경우 수정된 사용자이름 받아옴
 */
function isDuplicateName() {

    $.ajax({
        type: "GET",
        url: "/chat/duplicateName",
        data: {
            "userName": userName,
            "roomId": roomId
        },
        success: function (data) {
            console.log("함수 동작 확인 :" + data);
            userName = data;
        }
    });
}

/*
    getUserLiset() 채팅방 사용자 목록을 서버에서 가져
    와서 화면에 표시
 */
function getUserLiset() {
    const $list = $("#list");

    $.ajax({
        type: "GET",
        url: "/chat/userList",
        data: {
            "roomId": roomId
        },
        success: function (data) {
            var users = "";
            for (let i = 0; i < data.length; i++) {
                users += "<li class='dromdown-item'>" + data[i] + "</li>"
            }
            $list.html(users);
        }

    });
}
function onError() {
    connectingElement.textContent = 'Could not connect to WebSocket server. Please refresh this page to try again!';
    connectingElement.style.color = 'red';
}
//메시지 전송때는 JSON 형식을 메시지를 전달한다.
function sendMessage(event) {
    var messageContent = messageInput.value.trim();

    if (messageContent && stompClient) {
        var chatMessage = {
            "roomId": roomId,
            sender: userName,
            message: messageInput.value,
            type: 'TALK'
        };
        stompClient.send("/pub/chat/sendMessage", {}, JSON.stringify(chatMessage));
        messageInput.value = '';
    }
    event.preventDefault();
}


/*
    onMessageReceived() 서버에서 채팅 메시지를 받았을때 호출
    되는 함수로, 메시지의 종류에 따라 화면에 표시 방법을 다르게 처리
    각종 이벤트 처리
 */
function onMessageReceived(payload) {
    var chat = JSON.parse(payload.body);
    var messageElement = document.createElement('li');

    if (chat.type === 'ENTER') {
        //chat type 이 ENTER 이면
        messageElement.classList.add('event-message');
        chat.content = chat.sender + chat.message;
        getUserLiset();
    }else if (chat.type === 'LEAVE') {
        //chat type 이 LEAVE 이면
        messageElement.classList.add('event-message');
        chat.content = chat.sender + chat.message;
        getUserLiset();
    } else {
        //chat type 이 TALK 이면
        messageElement.classList.add('event-message');

        var avatarElement = document.createElement('i');
        var avartarText = document.createTextNode(chat.sender[0]);
        avatarElement.appendChild(avartarText);
        avatarElement.style['background-color'] = getAvatarColor(chat.sender);

        messageElement.appendChild(avatarElement);

        var userNameElement = document.createElement('span');
        var userNameText = document.createTextNode(chat.sender);

        userNameElement.appendChild(userNameText);
        messageElement.appendChild(userNameElement);
    }
    var textElement = document.createElement('p');
    var messageText = document.createTextNode(chat.message);
    textElement.appendChild(messageText);

    messageElement.appendChild(textElement);

    messageArea.appendChild(messageElement);
    messageArea.scrollTop = messageArea.scrollHeight;

}

/*
    getAvatarColor() 는 사용자 이름을 기반으로 사용자
    아바타(프로필사진) 의 배경색을 결정하는 함수
 */
function getAvatarColor(messageSender) {
    var hash = 0;
    for (var i = 0; i < messageSender.length; i++) {
        hash = 31 * hash + messageSender.charCodeAt(i);
    }
    var index = Math.abs(hash % colors.length);

    return colors[index];
}

/*
    이벤트 리스너(addEventListener)를 사용하여
    사용자 이름 입력 폼(userNameForm) 및 메시지 입력
    폼(messageForm)에서 제출 이벤트가 발생할 때
    각각 connect 및 sendMessage 함수를 호출하도록 설정
 */
userNameForm.addEventListener('submit', connect, true);
messageForm.addEventListener('submit', sendMessage, true);
