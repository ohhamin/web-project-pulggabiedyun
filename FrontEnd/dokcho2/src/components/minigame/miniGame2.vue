<template>
  <div class="miniGame__container">
    <div class="miniGame">
      <!-- 해당 게임에 대한 설명 -->
      <div v-if="!game.game" class="game__description">
        <img class="game__img" src="@/assets/minigame/2.png" alt="" />
        <div class="game__text">
          <p>
            9회 안에 세자리의 숫자를 맞춰야 합니다.<br />
            각 숫자는 1~9까지로 이루어져 있으며, 중복되지 않습니다. 숫자를
            입력하면 다음과 같은 결과가 나타납니다. <br />
            <b
              >스트라이크 : 숫자와 위치가 전부 맞았을 때. <br />
              볼 : 숫자는 맞지만 위치가 틀렸을 때. <br />
              아웃 : 숫자와 위치가 전부 틀렸을 때.
            </b>
          </p>
        </div>

        <div class="game__item">
          <h3 class="reward__header">✨보상✨</h3>
          <div class="game__reward">
            <p class="reward__text"><b>🌞 경험치</b> : 15 포인트</p>
            <p class="reward__text">
              <b>🎁 아이템</b> : 배틀 아이템 중 1개 랜덤
            </p>
          </div>
        </div>
        <button class="start__btn" @click="changeGame()">게 임 시 작</button>
      </div>

      <div class="game__result" v-else-if="nowStrike.nowStrike">
        <img class="success__img" src="@/assets/minigame/success.png" alt="" />
        <div class="game__get">
          <div class="result__point">
            <p>총 경험치 <b>15</b> 포인트 획득🌿</p>
          </div>
          <h3>🎁 성공 보수 아이템 🎁</h3>
          <div class="tooltip">
            <span class="tooltiptext"> {{ item.item.description }}</span>
            <img
              :src="require('@/assets/item/' + item.item.itemId + '.png')"
              alt=""
              class="item__image"
            />

            <div class="battle__item">
              <p>{{ item.item.itemName }} <br /></p>
            </div>
          </div>
        </div>
      </div>
      <div class="game__result" v-else-if="nowFinished.nowFinished">
        <img class="success__img" src="@/assets/minigame/fail.png" alt="" />
        <div class="game__dontget">
          <h3>숫자를 맞추지 못해서 아무것도 획득하지 못했어요 .. 😥</h3>
          <p>다시 한 번 도전해보세요!</p>
        </div>
      </div>

      <!-- 게임 내용 >.<  -->
      <div v-else class="game__play">
        <div class="input__answer">
          <input
            type="number"
            v-model="inputAnswer[0]"
            min="1"
            max="9"
            maxlength="1"
            class="number__box"
          />
          <input
            type="number"
            v-model="inputAnswer[1]"
            min="1"
            max="9"
            maxlength="1"
            class="number__box"
          />
          <input
            type="number"
            v-model="inputAnswer[2]"
            min="1"
            max="9"
            maxlength="1"
            class="number__box"
            @keyup.enter="showResult()"
          />
          <button @click="showResult()" class="do__input">입력</button>
        </div>
        <br />
        <hr />

        <div class="show__result">
          <table class="show__table">
            <tr>
              <th class="table__title" colspan="3"><h3>Score Board</h3></th>
            </tr>
            <tr>
              <td class="inning">1회</td>
              <td class="nums">{{ scoreBoard[0].input }}</td>
              <td class="score">{{ scoreBoard[0].result }}</td>
            </tr>
            <tr>
              <td class="inning">2회</td>
              <td>{{ scoreBoard[1].input }}</td>
              <td>{{ scoreBoard[1].result }}</td>
            </tr>
            <tr>
              <td class="inning">3회</td>
              <td>{{ scoreBoard[2].input }}</td>
              <td>{{ scoreBoard[2].result }}</td>
            </tr>
            <tr>
              <td class="inning">4회</td>
              <td>{{ scoreBoard[3].input }}</td>
              <td>{{ scoreBoard[3].result }}</td>
            </tr>
            <tr>
              <td class="inning">5회</td>
              <td>{{ scoreBoard[4].input }}</td>
              <td>{{ scoreBoard[4].result }}</td>
            </tr>
            <tr>
              <td class="inning">6회</td>
              <td>{{ scoreBoard[5].input }}</td>
              <td>{{ scoreBoard[5].result }}</td>
            </tr>
            <tr>
              <td class="inning">7회</td>
              <td>{{ scoreBoard[6].input }}</td>
              <td>{{ scoreBoard[6].result }}</td>
            </tr>
            <tr>
              <td class="inning">8회</td>
              <td>{{ scoreBoard[7].input }}</td>
              <td>{{ scoreBoard[7].result }}</td>
            </tr>
            <tr>
              <td class="inning">9회</td>
              <td>{{ scoreBoard[8].input }}</td>
              <td>{{ scoreBoard[8].result }}</td>
            </tr>
          </table>
        </div>
      </div>

      <img
        class="exit__btn"
        @click="$emit('miniGame2Close')"
        src="@/assets/navbar/ExitButton.png"
        alt=""
      />
    </div>
  </div>
</template>

<script>
import { ref } from 'vue'
import axios from 'axios'
import { BASE_URL } from '@/constant/BASE_URL'
import swal from 'sweetalert'
import Swal from 'sweetalert2'
import JSConfetti from 'js-confetti'

const jsConfetti = new JSConfetti()

export default {
  components: {},
  setup() {
    const nowStrike = ref({ nowStrike: false })
    const nowFinished = ref({ nowFinished: false })
    const item = ref({ item: {} })
    const game = ref({ game: false })

    function changeGame() {
      game.value.game = true
    }

    const numbers = ref([1, 2, 3, 4, 5, 6, 7, 8, 9])
    const answer = ref([])

    function getAnswer() {
      while (answer.value.length < 3) {
        let num = Math.floor(Math.random() * numbers.value.length)

        if (answer.value.includes(numbers.value[num])) {
          continue
        } else {
          answer.value.push(numbers.value[num])
        }
      }
    }

    getAnswer()

    const inputAnswer = ref(['', '', ''])
    const turn = ref(-1)

    const scoreBoard = ref([
      { input: '', result: '' },
      { input: '', result: '' },
      { input: '', result: '' },
      { input: '', result: '' },
      { input: '', result: '' },
      { input: '', result: '' },
      { input: '', result: '' },
      { input: '', result: '' },
      { input: '', result: '' }
    ])

    const strike = ref(0)
    const ball = ref(0)

    function showResult() {
      let isValid = true
      inputAnswer.value.forEach((el) => {
        if (el < 1 || el > 9 || isNaN(el)) {
          isValid = false
        }
      })

      function startConfetti() {
        jsConfetti.addConfetti()
      }

      if (isValid) {
        turn.value += 1

        strike.value = 0
        ball.value = 0

        for (let i = 0; i < 3; i++) {
          if (answer.value[i] == inputAnswer.value[i]) {
            strike.value += 1
          } else {
            if (answer.value.includes(inputAnswer.value[i])) {
              ball.value += 1
            }
          }
        }

        scoreBoard.value[turn.value].input =
          inputAnswer.value[0] +
          ', ' +
          inputAnswer.value[1] +
          ', ' +
          inputAnswer.value[2]

        // 정답!!!!!
        if (strike.value == 3) {
          scoreBoard.value[turn.value].result = '정답'
          // 정답보상
          setTimeout(() => {
            axios({
              url: BASE_URL + '/api/v1/mission/mini?rewardExp=15',
              method: 'PUT',
              headers: {
                AUTHORIZATION: 'Bearer ' + localStorage.getItem('accessToken')
              }
            })
              .then((res) => {
                item.value.item = res.data.itemDto
                // levelup이 true로 들어오면 현재 representMonster -> detail 받아서 레벨업 alert 띄우기
                if (res.data.levelup === true) {
                  const user = JSON.parse(localStorage.getItem('userInfo'))
                  const monsterId = user.representMonster
                  let monster = []

                  axios({
                    url: BASE_URL + '/api/v1/monster/' + monsterId,
                    method: 'GET',
                    headers: {
                      AUTHORIZATION:
                        'Bearer ' + localStorage.getItem('accessToken')
                    }
                  })
                    .then((res) => {
                      startConfetti()
                      monster = res.data
                      Swal.fire({
                        title: 'Level Up!!🎉',
                        html: `<div style="text-align:center;">
                    <img  style="height:100px;width:100px;text-align:center;" src="${require('@/assets/starting/' +
                      monsterId +
                      '.png')}"/>
                    <p><b>${monster.name}</b>이</p><br /> <p> <b>Lv.${
                          monster.level - 1
                        } 👉 Lv.${monster.level}</b>로 성장했어요!</p>
                    </div>`,
                        timer: 5000,
                        showConfirmButton: false
                      })
                      setTimeout(() => {
                        jsConfetti.clearCanvas()
                      }, 5000)
                    })
                    .catch((err) => console.log(err))
                }
              })
              .catch((err) => console.log(err))
          }, 1000)

          nowStrike.value.nowStrike = true
        }
        // 스트라이크, 볼
        else if (strike.value > 0 && ball.value > 0) {
          scoreBoard.value[turn.value].result =
            strike.value.toString() +
            ' 스트라이크, ' +
            ball.value.toString() +
            ' 볼'
        }
        // 스트라이크
        else if (strike.value > 0) {
          scoreBoard.value[turn.value].result =
            strike.value.toString() + ' 스트라이크'
        }
        // 볼
        else if (ball.value > 0) {
          scoreBoard.value[turn.value].result = ball.value.toString() + ' 볼'
        }
        // 아웃
        else {
          scoreBoard.value[turn.value].result = '아웃'
        }

        inputAnswer.value = ['', '', '']

        // 실패!!!!
        if (turn.value >= 8 && strike.value != 3) {
          // 실패 메시지
          nowFinished.value.nowFinished = true
        }
      } else {
        swal({
          title: '유효한 숫자가 아닙니다!',
          icon: 'error',
          text: '1 ~ 9 사이의 숫자를 입력하세요',
          buttons: false,
          timer: 2000
        })
      }
    }

    return {
      nowStrike,
      nowFinished,
      item,
      game,
      changeGame,
      answer,
      inputAnswer,
      showResult,
      scoreBoard
    }
  }
}
</script>

<style scoped>
.miniGame__container {
  display: flex;
  align-items: center;
  justify-content: center;
  position: fixed;
  z-index: 30;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
}

.miniGame {
  top: 25%;
  width: 60%;
  height: 80%;
  background-color: rgb(255, 244, 231);
  z-index: 40;
  border-radius: 30px;
}

.game__description {
  margin: 5vh;
  text-align: center;
}
.game__img {
  margin-top: 5vh;
  margin-bottom: 5vh;
  width: 50%;
}

.game__text {
  line-height: 1.5;
}

.game__item {
  margin: 3vh;
  background-color: white;
  border-radius: 20px;
  display: flex;
  flex-direction: column;
  width: 80%;
  margin-left: 5.5vw;
}

.reward__header {
  margin-top: 3vh;
  margin-bottom: 3vh;
}

.game__reward {
  display: flex;
  flex-direction: row;
  justify-content: center;
  margin-bottom: 3vh;
}

.reward__text {
  margin-left: 2vh;
  margin-right: 2vh;
}

.game__play {
  margin: 5vh;
}

.start__btn {
  height: 6vh;
  border-radius: 50px;
  border: none;
  width: 10vw;
  cursor: url('@/assets/selector.cur'), pointer;
  font-size: 1.2rem;
  font-weight: bold;
}

.start__btn:hover {
  background-color: #6bfa8d;
}

.exit__btn {
  width: 4vw;
  display: flex;
  justify-content: center;
  align-items: center;
  position: absolute;
  top: 15vh;
  right: 25vw;
  cursor: url('@/assets/selector.cur'), pointer;
}
.input__answer {
  margin-top: 12vh;
  margin-left: 4vw;
  display: flex;
  flex-direction: row;
  justify-content: center;
}

.number__box {
  margin-left: 0.5vw;
  margin-right: 0.5vw;
  width: 4vw;
  height: 4vw;
  font-size: 2rem;
  text-align: center;
}

.do__input {
  margin-left: 1vw;
  margin-right: 1vw;
  height: 3vw;
  border-radius: 50px;
  border: none;
  width: 5vw;
  cursor: url('@/assets/selector.cur'), pointer;
  font-size: 1.2rem;
  background-color: #d5d5d5;
}

.do__input:hover {
  background-color: #6bfa8d;
}

.show__result {
  margin-top: 3vh;
  border-radius: 30px;
}

th,
td {
  border: 1px solid #d1d1d1;
  border-collapse: separate !important;
}

table {
  margin-left: auto;
  margin-right: auto;
  width: 50%;
  text-align: center;
  font-size: 18px;
  border-collapse: separate !important;
  border-radius: 30px;
  border-style: hidden;
}

.inning {
  width: 20%;
}

.nums {
  width: 30%;
}

.score {
  width: 50%;
}

tr {
  height: 32px;
  border-radius: 30px;
}

.game__result {
  width: 100%;
  height: 50vh;
  border-radius: 20px;
  text-align: center;
  margin-top: 15vh;
}

.success__img {
  width: 40%;
  margin: auto;
}

.game__result {
  width: 100%;
  height: 50vh;
  border-radius: 20px;
  text-align: center;
  margin-top: 15vh;
}

.success__img {
  width: 40%;
  margin: auto;
}

.game__get {
  width: 60%;
  margin: auto;
  display: flex;
  flex-direction: column;
  margin-top: 3vh;
  background-color: white;
  border-radius: 20px;
}

.result__point {
  margin-top: 2vh;
  margin-bottom: 2vh;
  text-align: center;
  font-size: 2rem;
}

.result__point p {
  margin-bottom: 2vh;
}

.item__image {
  width: 4vw;
  cursor: url('@/assets/selector.cur'), pointer;
}

.item__item p {
  font-size: 0.5rem;
}

.game__get h3 {
  margin: 2vh;
}
.tooltip {
  position: relative;
  display: inline-block;
  text-align: center;
  margin-bottom: 2vh;
}

.tooltip .tooltiptext {
  visibility: hidden;
  width: 200px;
  background-color: black;
  color: #fff;
  text-align: center;
  border-radius: 6px;
  padding: 5px 0;
  font-size: 0.8rem;
  position: absolute;
  z-index: 1;
  top: 100%;
  left: 50%;
  margin-left: -60px;
}

.tooltip:hover .tooltiptext {
  visibility: visible;
}

.game__dontget {
  height: 50%;
  width: 80%;
  margin: auto;
  display: flex;
  flex-direction: column;
  margin-top: 5vh;
  background-color: white;
  border-radius: 20px;
  padding: 3vh;
  text-align: center;
}

.game__dontget h3 {
  margin-top: 7vh;
  margin-bottom: 2vh;
}

.table__title {
  background-color: white;
  height: 6vh;
  text-align: center;
}

.inning {
  background-color: white;
}

/* Chrome, Safari, Edge, Opera */
input::-webkit-outer-spin-button,
input::-webkit-inner-spin-button {
  -webkit-appearance: none;
  margin: 0;
}

/* Firefox */
input[type='number'] {
  -moz-appearance: textfield;
}
</style>
