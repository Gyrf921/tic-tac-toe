document.addEventListener('DOMContentLoaded', (event) => {
    const stompClient = new StompJs.Client({
        brokerURL: 'ws://localhost:8081/tic-tac-toe-websocket'
    });
    let urlForPublish = '', urlForSubscribe = '';
    let board = Array(9).fill(null); // Начальное состояние игрового поля
    let currentPlayer = 'X'; // Первый игрок
    let radios = document.querySelectorAll('input[type="radio"]');
    let sessionUUID = document.getElementById('session-uuid');
    let messageTxt = document.getElementById('message');

    stompClient.onConnect = (frame) => {
        console.log('Connected: ' + frame);
        stompClient.subscribe(urlForSubscribe, (gameState) => {
            console.log('stompClient.subscribe:' + urlForSubscribe);
            const state = JSON.parse(gameState.body);
            board = state.board;
            currentPlayer = state.currentPlayer;
            render();
            checkWinner(state);
        });

        console.log('stompClient.subscribe chat: /topic/play/' + sessionUUID.value + '/chat');
        stompClient.subscribe('/topic/play/' + sessionUUID.value + '/chat', (chatMessage) => {
            showChatMessage(JSON.parse(chatMessage.body).content);
        });
    };
    stompClient.onWebSocketError = (error) => {
        console.error('Error with websocket', error);
    };
    stompClient.onStompError = (frame) => {
        console.error('Broker reported error: ' + frame.headers['message']);
        console.error('Additional details: ' + frame.body);
    };

    $(function () {
        $("#start").click(() => startGame());
        $("#reset").click(() => resetGame());
        $("#send_message").click(() => sendMessage());
    });

    function sendMessage() {
        stompClient.publish({
            destination: "/app/play/" + sessionUUID.value + "/chat",
            body: JSON.stringify(
                {
                    'name': $("#name-chat").val(),
                    'message': $("#message-chat").val()
                }
            )
        });
    }

    function resetGame() {
        stompClient.publish({
            destination: '/app/play/' + sessionUUID.value + '/clear'
        });
        render();
    }

    function startGame() {

        let uuidRandom = crypto.randomUUID();

        for (let radio of radios) {
            if (radio.checked) {
                if (radio.value === 'PvP') {
                    if (sessionUUID.value === '' || sessionUUID.value === undefined) {
                        sessionUUID.value = uuidRandom;
                        alert("Id для игры с другом: " + sessionUUID.value);
                    }
                    urlForPublish += '/app/play/' + sessionUUID.value + '/opponent/person';
                    urlForSubscribe += '/topic/play/' + sessionUUID.value + '/opponent/person';
                } else {
                    if (sessionUUID.value === '' || sessionUUID.value === undefined) {
                        sessionUUID.value = uuidRandom;
                    }
                    urlForPublish += '/app/play/' + sessionUUID.value + '/opponent/computer';
                    urlForSubscribe += '/topic/play/' + sessionUUID.value +'/opponent/computer';
                }
            }
        }
        console.log('stompClient.activate: urlForPublish - ', urlForPublish, "; urlForSubscribe - ", urlForSubscribe);
        stompClient.activate();
        messageTxt.innerHTML = "Да начнутся голодные игры!)))";
    }

    const handleClick = (index) => {
        if (board[index] || checkWinner(board)) {
            return;
        }

        const move = {
            index: index,
            player: currentPlayer,
        };

        console.log("handleClick: " + urlForPublish + "; move: " + move)

        stompClient.publish({
            destination: urlForPublish,
            body: JSON.stringify(move),
        });

        board[index] = currentPlayer;
        render();
        checkWinner({'winner': null});
    };

    const render = () => {
        const gameboard = document.getElementById('gameboard');
        gameboard.innerHTML = '';
        console.log("render - currentPlayer: ", currentPlayer, " board: ", board)
        board.forEach((cell, index) => {
            const cellElement = document.createElement('div');
            cellElement.classList.add('cell');
            cellElement.innerText = cell ? cell : '';
            cellElement.addEventListener('click', () => handleClick(index));
            gameboard.appendChild(cellElement);
        });
    };

    const checkWinner = (state) => {
        if (state.winner) {
            messageTxt.innerHTML = `${state.winner} выиграл!`;
        } else if (board.every(cell => cell)) {
            messageTxt.innerHTML = "Ничья!";
        }
    };

    function showChatMessage(message) {
        $("#greetings").append("<tr><td>" + message + "</td></tr>");
    }

    render(); // обновляем отображение
});