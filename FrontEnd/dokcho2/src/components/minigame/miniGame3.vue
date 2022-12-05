<template>
  <div class="miniGame__container">
    <div class="miniGame">
      <!-- 해당 게임에 대한 설명 -->
      <div v-if="!game.game" class="game__description">
        <img class="game__img" src="@/assets/minigame/3.png" alt="" />
        <div class="game__text">
          <p>
            주어지는 초성 문제를 보고 알맞은 정답을 입력해주세요! <br />
            문제를 푸는데 주어지는 시간은 <b>단 20초!</b> <br />
            다양한 역사적 물건과 장소가 문제로 나와요. 10초가 지나면 조금 더
            맞추기 쉽게 힌트를 드려요.
            <br />‘띄어쓰기’까지 알맞게 써주셔야 정답이 인정돼요. 초성 퀴즈를
            풀러 가볼까요?
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

      <!-- 보상 받는 창 -->

      <div v-else-if="chosung.nowPage === 3" class="game__result">
        <img class="success__img" src="@/assets/minigame/success.png" alt="" />
        <div class="game__get" v-if="reward.item">
          <div class="result__point">
            <p>
              총 경험치 <b>{{ reward.exp }}</b> 포인트 획득🌿
            </p>
          </div>
          <h3>🎁 성공 보수 아이템 🎁</h3>
          <div class="tooltip">
            <span class="tooltiptext"> {{ reward.item.description }}</span>
            <img
              :src="require('@/assets/item/' + reward.item.itemId + '.png')"
              alt=""
              class="item__image"
            />

            <div class="battle__item">
              <p>{{ reward.item.itemName }} <br /></p>
            </div>
          </div>
        </div>
      </div>

      <!-- 게임 내용 >.<  -->

      <div v-else class="game__play">
        <div class="play__header" :class="{ yes__time: chosung.time <= 10 }">
          <p>⏱ {{ chosung.time }}</p>
        </div>

        <div class="play__body">
          <div class="question" v-if="chosung.time <= 10">
            <h2>{{ chosung.quiz[chosung.nowPage].hint }}</h2>
          </div>
          <div class="question" v-else>
            <h2>{{ chosung.quiz[chosung.nowPage].question }}</h2>
          </div>

          <div class="answer">
            <input
              type="text"
              v-model="chosung.input"
              @keyup.enter="submitInput()"
            />
            <button class="ans" @click="submitInput()">제출</button>
          </div>
        </div>
      </div>

      <img
        v-if="!game.game || chosung.nowPage === 3"
        class="exit__btn"
        @click="$emit('miniGame3Close')"
        src="@/assets/navbar/ExitButton.png"
        alt=""
      />
    </div>
  </div>
</template>

<script>
import { ref, onMounted } from 'vue'
import axios from 'axios'
import swal from 'sweetalert'
import Swal from 'sweetalert2'

import { BASE_URL } from '@/constant/BASE_URL'
import { useStore } from 'vuex'
import JSConfetti from 'js-confetti'

const jsConfetti = new JSConfetti()

export default {
  components: {},
  setup(props, { emit }) {
    const store = useStore()
    const game = ref({ game: false })
    let chosung = ref({ quiz: [], nowPage: 0, time: 20, input: '' })
    let reward = ref({ exp: 15, item: {} })

    // 퀴즈 데이터 받아오기

    async function fetchQuiz() {
      if (store.getters.isAccessTokenExpired) {
        await store.dispatch('doRefreshToken')
      }
      axios({
        url: BASE_URL + '/api/v1/game/consonant/auth/3',
        method: 'GET'
      })
        .then((res) => {
          chosung.value.quiz = res.data
        })
        .catch((err) => console.log(err))
    }

    function changeGame() {
      startTimer()
      game.value.game = true
    }
    // 타이머
    function startTimer() {
      let gameTimer = setInterval(() => {
        chosung.value.time -= 1
        if (chosung.value.time === -1) {
          Swal.fire({
            title: '다시 도전해주세요!',
            icon: 'error',
            html:
              '<p>정답은 바로 ...</p>' +
              '<br />' +
              `<h3><b>${
                chosung.value.quiz[chosung.value.nowPage].right_answer
              }!</b></h3>` +
              '<br />' +
              `<p>${chosung.value.quiz[chosung.value.nowPage].description}</p>`
          })
          clearInterval(gameTimer)
          setTimeout(() => {
            emit('miniGame3Close')
          }, 100)
        }
        if (chosung.value.nowPage === 3) {
          clearInterval(gameTimer)
        }
      }, 1000)
    }

    function startConfetti() {
      jsConfetti.addConfetti()
    }

    function submitInput() {
      if (
        chosung.value.input ===
        chosung.value.quiz[chosung.value.nowPage].right_answer
      ) {
        chosung.value.time = 20
        chosung.value.input = ''
        chosung.value.nowPage += 1
      } else {
        chosung.value.input = ''
      }
      if (chosung.value.nowPage === 3) {
        swal({
          title: '축하합니다🎊',
          icon: 'success',
          text: '모든 문제를 맞췄어요!',
          buttons: false,
          timer: 1000
        })

        setTimeout(() => {
          axios({
            url:
              BASE_URL + '/api/v1/mission/mini?rewardExp=' + reward.value.exp,
            method: 'PUT',
            headers: {
              AUTHORIZATION: 'Bearer ' + localStorage.getItem('accessToken')
            }
          })
            .then((res) => {
              reward.value.item = res.data.itemDto

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
      }
    }
    onMounted(() => fetchQuiz())

    return {
      store,
      game,
      chosung,
      reward,
      fetchQuiz,
      changeGame,
      submitInput
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

.game__play {
  margin: 10vh;
  display: flex;
  flex-direction: column;
  justify-content: center;
}

.play__header {
  font-size: 2rem;
  margin-bottom: 2vh;
  width: 70%;
  margin: auto;
}

.play__body {
  border-radius: 10px;
  padding: 3vw;
}

.question {
  background-color: white;
  border-radius: 10px;
  text-align: center;
  width: 80%;
  margin: auto;
  letter-spacing: 0.5rem;
  padding: 10% 0;
}

.answer {
  display: flex;
  flex-direction: row;
  margin: 7vh auto;
  text-align: center;
  justify-content: center;
}

input[type='text'] {
  width: 25vw;
  line-height: 4.5vh;
  margin: 1vw;
  font-size: 1.5rem;
  padding-left: 2vw;
  letter-spacing: 0.3rem;
}

.ans {
  margin: 1vw;
  height: 6vh;
  border-radius: 10px;
  border: none;
  width: 7vw;
  cursor: url('@/assets/selector.cur'), pointer;
  font-size: 1rem;
  background-color: #d5d5d5;
}

.ans:hover {
  background-color: #6bfa8d;
  font-weight: bold;
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

.yes__time {
  color: red;
}
</style>
