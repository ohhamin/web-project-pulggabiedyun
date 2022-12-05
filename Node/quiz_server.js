const express = require("express");
const http = require("http");
const cors = require("cors");
const axios = require("axios");
const _ = require("lodash");

const app = express();
const server = http.createServer(app);
const BASE_URL = "https://k7e203.p.ssafy.io";

app.use(cors());

const io = require("socket.io")(server, {
  cors: {
    origin: "*",
    methods: ["GET", "POST"],
  },
});

server.listen(3001, () => {
  console.log("Listening at port 3001...");
});

let wordQuizs = [];
let consonantQuizs = [];
let characterQuizs = [];
let players = {};
let roomNum = 0;
let rooms = [];

axios({
  url: BASE_URL + "/api/v1/game/words/auth/51",
  method: "GET",
})
  .then((res) => {
    wordQuizs = res.data;
  })
  .catch((err) => {
    console.log(err);
  });

axios({
  url: BASE_URL + "/api/v1/game/consonant/auth/50",
  method: "GET",
})
  .then((res) => {
    consonantQuizs = res.data;
  })
  .catch((err) => {
    console.log(err);
  });

axios({
  url: BASE_URL + "/api/v1/game/characterquiz/auth/30",
  method: "GET",
})
  .then((res) => {
    characterQuizs = res.data;
  })
  .catch((err) => {
    console.log(err);
  });

try {
  io.on("connection", (socket) => {
    console.log(`User ${socket.id} connected`);
    players[socket.id] = { id: socket.id, object: {}, room: -1 };

    socket.on("disconnect", (reason) => {
      console.log(`User ${socket.id} disconnected`);

      let roomInfo = rooms.find(
        (room) => room.roomId == players[socket.id].room
      );
      if (roomInfo) {
        roomInfo.currentUser = roomInfo.currentUser.filter(
          (user) => user.socketId !== socket.id
        );

        io.to(`${socket.id}`).emit("deleteMsg");
        let payload = [roomInfo, players[socket.id].object];

        if (roomInfo.currentUser.length > 0) {
          for (user of roomInfo.currentUser) {
            io.to(`${user.socketId}`).emit("leaveRoomOK", payload);
          }
        } else {
          rooms = rooms.filter((room) => room.roomId !== roomInfo.roomId);
        }
      }

      delete players[socket.id];
    });

    if (players[socket.id].object) {
      socket.on("sendNickname", (userInfo) => {
        players[socket.id].object = userInfo;
        console.log(
          `${socket.id}, ${
            players[socket.id].object.nickname
          }님이 퀴즈에 입장했습니다`
        );
        socket.emit("sendSocketId", socket.id);
      });
    }

    setInterval(() => {
      socket.emit("sendRooms", rooms);
    }, 500);

    socket.on("createRoom", (data) => {
      socket.join(`${roomNum}`);
      rooms.push({
        roomId: roomNum,
        roomName: data[0],
        gameType: data[1],
        currentUser: [],
        roomQuiz: [],
        nowQuizNumber: 0,
        quizing: false,
      });

      players[socket.id].room = roomNum;

      let roomInfo = rooms.find((room) => room.roomId == roomNum);

      if (roomInfo) {
        roomInfo.currentUser.push({
          socketId: socket.id,
          nickname: players[socket.id].object.nickname,
          score: 0,
        });
        if (roomInfo.gameType === "saja") {
          roomInfo.roomQuiz = _.sampleSize(wordQuizs, 10);
        } else if (roomInfo.gameType === "chosung") {
          roomInfo.roomQuiz = _.sampleSize(consonantQuizs, 10);
        } else {
          roomInfo.roomQuiz = _.sampleSize(characterQuizs, 10);
        }

        let payload = [roomInfo, players[socket.id].object];
        io.to(`${roomNum}`).emit("createRoomOK", payload);
        roomNum += 1;
      }
    });

    socket.on("enterRoom", (data) => {
      let roomInfo = rooms.find((room) => room.roomId == data);

      players[socket.id].room = data;

      if (roomInfo) {
        if (roomInfo.currentUser.length > 3) {
          io.to(socket.id).emit("goaway");
        } else {
          socket.join(`${data}`);
          roomInfo.currentUser.push({
            socketId: socket.id,
            nickname: players[socket.id].object.nickname,
            score: 0,
          });
          let payload = [roomInfo, players[socket.id].object];
          io.to(`${data}`).emit("enterRoomOK", payload);
          for (user of roomInfo.currentUser) {
            io.to(`${user.socketId}`).emit("enterRoomOK", payload);
          }
        }
      }
    });

    socket.on("leaveRoom", (nowRoom) => {
      socket.leave(`${nowRoom}`);

      players[socket.id].room = -1;

      let roomInfo = rooms.find((room) => room.roomId == nowRoom);
      if (roomInfo) {
        roomInfo.currentUser = roomInfo.currentUser.filter(
          (user) => user.socketId !== socket.id
        );

        io.to(`${socket.id}`).emit("deleteMsg");
        let payload = [roomInfo, players[socket.id].object];

        if (roomInfo.currentUser.length > 0) {
          for (user of roomInfo.currentUser) {
            io.to(`${user.socketId}`).emit("leaveRoomOK", payload);
          }
        } else {
          rooms = rooms.filter((room) => room.roomId !== roomInfo.roomId);
        }
      }
    });

    socket.on("msg", (data) => {
      // 받은 메세지를 다시 해당 룸의 모든 사용자에게 보내줌
      let roomInfo = rooms.find((room) => room.roomId == data[0]);
      if (roomInfo) {
        for (user of roomInfo.currentUser) {
          io.to(`${user.socketId}`).emit("msg", data);
        }
        if (roomInfo.nowQuizNumber >= 10) {
          io.to(`${roomInfo.roomId}`).emit("endQuiz");
          roomInfo.quizing = false;
        } else {
          if (
            data[3] !== "독초는 퀴즈를 뿌려라" &&
            roomInfo.currentUser.length < 2
          ) {
            io.to(`${roomInfo.roomId}`).emit("whyAlone");
          } else if (
            data[3] === "독초는 퀴즈를 뿌려라" &&
            roomInfo.currentUser.length < 2
          ) {
            io.to(`${roomInfo.roomId}`).emit("dontStartQuiz");
          } else {
            // 퀴즈시작
            if (
              data[3] === "독초는 퀴즈를 뿌려라" &&
              roomInfo.quizing == false
            ) {
              roomInfo.quizing = true;
              let payload = [
                roomInfo.gameType,
                roomInfo.roomQuiz[roomInfo.nowQuizNumber],
              ];
              io.to(`${roomInfo.roomId}`).emit("startQuiz", payload);
            }
          }

          // 받은 메세지 정답 검사
          if (roomInfo) {
            if (
              data[3] === roomInfo.roomQuiz[roomInfo.nowQuizNumber].right_answer
            ) {
              roomInfo.currentUser.find(
                (user) => user.socketId == socket.id
              ).score += 1;

              let payload = [socket.id, roomInfo.currentUser];

              io.to(`${roomInfo.roomId}`).emit("correct", payload);
              // 다음 퀴즈 전달

              rooms.find((room) => room.roomId == data[0]).nowQuizNumber += 1;
              if (roomInfo.nowQuizNumber >= 10) {
                let userScores = [];

                for (let user of roomInfo.currentUser) {
                  userScores.push(user.score);
                }

                const maxScore = Math.max(...userScores);

                let winners = [];

                for (let user of roomInfo.currentUser) {
                  if (user.score === maxScore) {
                    winners.push(user);
                  }
                }

                io.to(`${roomInfo.roomId}`).emit("endQuiz", winners);
                roomInfo.quizing = false;
                for (let winner of winners) {
                  io.to(`${winner.socketId}`).emit("winnerwinnerchickendinner");
                }
                io.to(`${roomInfo.roomId}`).emit("fuckoff");
              } else {
                let payload = [
                  roomInfo.gameType,
                  roomInfo.roomQuiz[roomInfo.nowQuizNumber],
                ];
                io.to(`${roomInfo.roomId}`).emit("nextQuiz", payload);
              }
            }
          }
        }
      }
    });
  });
} catch (err) {
  console.log(err);
}
