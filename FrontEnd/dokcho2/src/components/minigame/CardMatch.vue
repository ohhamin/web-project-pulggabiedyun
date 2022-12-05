<template>
  <div class="game__container">
    <div class="on__game">
      <div class="game__header" v-if="!lastStage.lastStage">
        <!-- 힌트 / 횟수 -->
        <div class="game__point">
          <p>
            현재까지 획득한 포인트 : <b>{{ point.point }}</b>
          </p>
        </div>
        <h3>⭐스테이지 {{ stage.stage + 1 }}</h3>
        <br />
        <div class="game__hint" v-if="quizs.quizs.length > 0">
          <p @click="useHintChance()">
            🔍 힌트 ({{ hint.hint }}회)
            <span v-if="useHint.useHint">
              : "{{ quizs.quizs[stage.stage].description }}"</span
            >
          </p>
        </div>
      </div>
      <div
        class="game__body"
        v-if="quizs.quizs.length > 0 && !lastStage.lastStage"
      >
        <!-- 퀴즈 -->
        <div class="question">
          <br />
          <h2>{{ quizs.quizs[stage.stage].question }}□□</h2>
        </div>
        <div class="answer">
          <button
            class="ans"
            @click="selectAnswer(quizs.quizs[stage.stage].answer1)"
          >
            1. {{ quizs.quizs[stage.stage].answer1 }}
          </button>
          <button
            class="ans"
            @click="selectAnswer(quizs.quizs[stage.stage].answer2)"
          >
            2. {{ quizs.quizs[stage.stage].answer2 }}
          </button>
          <button
            class="ans"
            @click="selectAnswer(quizs.quizs[stage.stage].answer3)"
          >
            3. {{ quizs.quizs[stage.stage].answer3 }}
          </button>
          <button
            class="ans"
            @click="selectAnswer(quizs.quizs[stage.stage].answer4)"
          >
            4. {{ quizs.quizs[stage.stage].answer4 }}
          </button>
        </div>
      </div>
      <div class="game__result" v-if="lastStage.lastStage && point.point > 0">
        <img class="success__img" src="@/assets/minigame/success.png" alt="" />
        <div class="game__get">
          <div class="result__point">
            <p>
              총 경험치 <b>{{ point.point }}</b> 포인트 획득🌿
            </p>
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
      <div class="game__result" v-if="lastStage.lastStage && point.point <= 0">
        <img class="success__img" src="@/assets/minigame/fail.png" alt="" />
        <div class="game__dontget">
          <h3>총 점수가 {{ point.point }}점이라 아무것도.. 얻지 못했어요</h3>
          <p>다시 한 번 도전해보세요!</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, onMounted } from 'vue'
import axios from 'axios'
import swal from 'sweetalert'
import { BASE_URL } from '@/constant/BASE_URL'

import { useStore } from 'vuex'
import Swal from 'sweetalert2'
import JSConfetti from 'js-confetti'

const jsConfetti = new JSConfetti()

// 스테이지수 (5개)
// 힌트수 (1회 /2회)
// 얻은 점수 (게임 끝났을 때 그만큼 넣어준다) : 문제 맞추면 + 3 힌트 사용했으면 + 2, 틀리면 - 1

export default {
  setup() {
    const stage = ref({ stage: 0 })
    const hint = ref({ hint: 2 })
    const quizs = ref({ quizs: [] })
    const useHint = ref({ useHint: false })
    const selectedAnswer = ref({ selectedAnswer: '' })
    const point = ref({ point: 0 })
    const lastStage = ref({ lastStage: false })
    const item = ref({ item: {} })

    const store = useStore()

    // 정답 선택
    function selectAnswer(ans) {
      selectedAnswer.value.selectedAnswer = ans
      checkStage()
    }

    // 퀴즈 가져오기
    async function fetchQuiz() {
      if (store.getters.isAccessTokenExpired) {
        await store.dispatch('doRefreshToken')
      }
      axios({
        url: BASE_URL + '/api/v1/game/words/auth/5',
        method: 'GET'
      })
        .then((res) => {
          quizs.value.quizs = res.data
        })
        .catch((err) => console.log(err))
    }

    // 힌트사용
    function useHintChance() {
      if (hint.value.hint > 0 && !useHint.value.useHint) {
        hint.value.hint -= 1
        useHint.value.useHint = true
      }
    }

    // 정답체크 : 체크해서 일치 -> 포인트 넣어주고 -> swal 띄운 후 -> 다음 단계
    // 마지막 스테이지면 총 획득 점수 알려주고 -> 경험치 넣어주고 -> 종료

    // 정답체크해서 포인트 넣어주기
    function checkAnswer() {
      if (
        selectedAnswer.value.selectedAnswer ===
          quizs.value.quizs[stage.value.stage].right_answer &&
        !useHint.value.useHint
      ) {
        point.value.point += 3
        swal({
          className: 'swal-wide',
          title: '정답입니다!',
          icon: 'success',
          text: `+3점 :D

          ${quizs.value.quizs[stage.value.stage].question}${
            quizs.value.quizs[stage.value.stage].right_answer
          } : ${quizs.value.quizs[stage.value.stage].description}`,
          buttons: false,
          timer: 1800
        })
      } else if (
        selectedAnswer.value.selectedAnswer ===
          quizs.value.quizs[stage.value.stage].right_answer &&
        useHint.value.useHint
      ) {
        point.value.point += 2
        swal({
          title: '정답입니다!',
          icon: 'success',
          text: `힌트 사용! +2점 :)

          ${quizs.value.quizs[stage.value.stage].question}${
            quizs.value.quizs[stage.value.stage].right_answer
          } : ${quizs.value.quizs[stage.value.stage].description}`,
          buttons: false,
          timer: 1800
        })
      } else {
        point.value.point -= 1
        swal({
          title: '아쉽게도 오답이에요..!',
          icon: 'error',
          text: `-1점 :(

          ${quizs.value.quizs[stage.value.stage].question}${
            quizs.value.quizs[stage.value.stage].right_answer
          } : ${quizs.value.quizs[stage.value.stage].description}`,
          buttons: { cancel: '->' }
          // timer: 2000
        })
      }
    }

    // 스테이지 확인해서 정답 체크 (포인트 넣기)

    function checkStage() {
      const now = stage.value.stage
      if (now !== 4) {
        checkAnswer()
        useHint.value.useHint = false
        stage.value.stage += 1
      } else if (now === 4) {
        checkAnswer()

        // 포인트 보내고 아이템 받기
        if (point.value.point > 0) {
          sendPoint()
        }
        setTimeout(() => {
          lastStage.value.lastStage = true
        }, 500)
      }
    }

    // 포인트 보내기 ..........
    function sendPoint() {
      function startConfetti() {
        jsConfetti.addConfetti()
      }
      axios({
        url: BASE_URL + '/api/v1/mission/mini?rewardExp=' + point.value.point,
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
                AUTHORIZATION: 'Bearer ' + localStorage.getItem('accessToken')
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
    }

    onMounted(() => fetchQuiz())
    return {
      stage,
      hint,
      quizs,
      useHint,
      selectedAnswer,
      point,
      lastStage,
      selectAnswer,
      useHintChance,
      item,
      sendPoint
    }
  }
}
</script>

<style scoped>
.game__container {
  z-index: 40;
  width: 100%;
  height: 100%;
}

.on__game {
  display: flex;
  flex-direction: column;
  justify-content: center;
  width: 80%;
  margin: 15% auto;
}

.game__header {
  display: flex;
  flex-direction: column;
  margin-bottom: 5%;
}

.game__hint {
  cursor: url('@/assets/selector.cur'), pointer;
}

.game__hint:hover {
  color: #59d075;
  font-weight: bold;
}
.game__point {
  align-self: end;
}

.question {
  background-color: white;
  border-radius: 10px;
  text-align: center;
  height: 10vh;
  letter-spacing: 0.5em;
}

.answer {
  margin: 5vh auto;
  text-align: center;
}
.ans {
  height: 6vh;
  border-radius: 50px;
  border: none;
  width: 10vw;
  cursor: url('@/assets/selector.cur'), pointer;
  font-size: 1.2rem;
  background-color: #d5d5d5;
}

.ans:hover {
  background-color: #6bfa8d;
  font-weight: bold;
}

.game__result {
  width: 100%;
  height: 50vh;
  border-radius: 20px;
  text-align: center;
}

.success__img {
  width: 60%;
  margin: auto;
}

.game__get {
  width: 80%;
  margin: auto;
  display: flex;
  flex-direction: column;
  margin-top: 3vh;
  background-color: white;
  border-radius: 20px;
}

.game__dontget {
  width: 80%;
  margin: auto;
  display: flex;
  flex-direction: column;
  margin-top: 10vh;
  background-color: white;
  border-radius: 20px;
  padding: 3vh;
}

.game__dontget h3 {
  margin: 2vh;
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
</style>
