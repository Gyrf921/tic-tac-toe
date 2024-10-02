document.addEventListener('DOMContentLoaded', (event) => {
    const stompClient = new StompJs.Client({
        brokerURL: 'ws://localhost:8081/tic-tac-toe-websocket'
    });
    let board = Array(9).fill(null); // Начальное состояние игрового поля
    let currentPlayer = 'X'; // Первый игрок
    let message = document.getElementById('message'); // Элемент для вывода сообщений

    stompClient.onConnect = (frame) => {
        console.log('Connected: ' + frame);
        // Подписка на общий топик для обновлений игры
        stompClient.subscribe('/topic/games', (gameState) => {
            const state = JSON.parse(gameState.body);
            board = state.board;
            currentPlayer = state.currentPlayer;
            render(); // обновляем отображение
            checkWinner(state); // проверяем есть ли победитель
        });
    };

    stompClient.onWebSocketError = (error) => {
        console.error('Error with websocket', error);
    };

    stompClient.onStompError = (frame) => {
        console.error('Broker reported error: ' + frame.headers['message']);
        console.error('Additional details: ' + frame.body);
    };
    stompClient.activate();

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
            destination: '/app/play',
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
});