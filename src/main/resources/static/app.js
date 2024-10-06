document.addEventListener('DOMContentLoaded', (event) => {
    const stompClient = new StompJs.Client({
        brokerURL: 'ws://localhost:8081/tic-tac-toe-websocket'
    });
    let board = Array(9).fill(null); // Начальное состояние игрового поля
    let currentPlayer = 'X'; // Первый игрок
    let message = document.getElementById('message'); // Элемент для вывода сообщений
    const isPvP = false;
    let radios = document.querySelectorAll('input[type="radio"]');
    let button = document.querySelector('#start');
    let buttonReset = document.querySelector('#reset');

    let urlForPublish = '/app/play/PvP';
    let urlForSubscribe = '/topic/games/PvP';

    stompClient.onConnect = (frame) => {
        console.log('Connected: ' + frame);
        stompClient.subscribe(urlForSubscribe, (gameState) => {

            const state = JSON.parse(gameState.body);
            console.log('stompClient.state: ' + state);

            board = state.board;
            currentPlayer = state.currentPlayer;
            render(); // обновляем отображение
            checkWinner(state); // проверяем есть ли победитель
        });

        console.log('Connected CHAT: ' + frame);
        stompClient.subscribe('/topic/greetings', (greeting) => {
            showGreeting(JSON.parse(greeting.body).content);
        });
    };

    stompClient.onWebSocketError = (error) => {
        console.error('Error with websocket', error);
    };

    stompClient.onStompError = (frame) => {
        console.error('Broker reported error: ' + frame.headers['message']);
        console.error('Additional details: ' + frame.body);
    };

    button.addEventListener('click', function() {
        for (let radio of radios) {
            if (radio.checked) {
                console.log();
                if (radio.value === 'PvP') {
                    urlForPublish = '/app/play/PvP';
                    urlForSubscribe = '/topic/games/PvP';
                }
                else {
                    urlForPublish = '/app/play/PvE';
                    urlForSubscribe = '/topic/games/PvE';
                }
            }
        }
        start();
    });

    buttonReset.addEventListener('click', function (){

        stompClient.publish({
            destination: '/app/play/clear'
        });
        render();
    })


    function start() {
        console.log('Try to start: ');

        console.log('stompClient.activate: urlForPublish - ', urlForPublish, "; urlForSubscribe - ", urlForSubscribe);
        stompClient.activate();
        message.innerText = "Да начнутся голодные игры!)))";
    }

    const handleClick = (index) => {
        if (board[index] || checkWinner(board)) {
            return;
        }

        // Создаем объект хода и отправляем его на сервер
        const move = {
            index: index,
            player: currentPlayer,
        };

        stompClient.publish({
            destination: urlForPublish,
            body: JSON.stringify(move),
        });

        board[index] = currentPlayer; // Обновляем доску локально
        render();
        checkWinner({'winner': null}); // Проверка на победителя после хода
    };

    const render = () => {
        const gameboard = document.getElementById('gameboard');
        gameboard.innerHTML = '';
        console.log("render - currentPlayer: " , currentPlayer, " board: ", board)
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
            message.innerText = `${state.winner} выиграл!`;
        } else if (board.every(cell => cell)) {
            message.innerText = "Ничья!";
        }
    };

    render(); // обновляем отображение

    let buttonMessage = document.querySelector('#send_message');

    buttonMessage.addEventListener('click', function() {
        stompClient.publish({
            destination: "/app/message",
            body: JSON.stringify(
                {
                    'name': $("#name-chat").val(),
                    'message': $("#message-chat").val()
                }
            )
        });
    })

    function showGreeting(message) {
        $("#greetings").append("<tr><td>" + message + "</td></tr>");
    }
});