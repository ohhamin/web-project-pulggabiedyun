<template>
  <div class="quiz">
    <div class="quiz__box">
      <div class="box" v-if="quiz.content[quiz.nowPage]">
        <div class="question">
          {{ quiz.nowPage + 1 }}. {{ quiz.content[quiz.nowPage].question }}
        </div>
        <div class="content">
          <img :src="quiz.content[quiz.nowPage].img" alt="quiz-image" />
          <div class="answers">
            <div
              class="answer1"
              @click="nextQuiz(quiz.content[quiz.nowPage].answer1)"
            >
              (1) {{ quiz.content[quiz.nowPage].answer1 }}
            </div>
            <div
              class="answer2"
              @click="nextQuiz(quiz.content[quiz.nowPage].answer2)"
            >
              (2) {{ quiz.content[quiz.nowPage].answer2 }}
            </div>
            <div
              class="answer3"
              @click="nextQuiz(quiz.content[quiz.nowPage].answer3)"
            >
              (3) {{ quiz.content[quiz.nowPage].answer3 }}
            </div>
            <div
              class="answer4"
              @click="nextQuiz(quiz.content[quiz.nowPage].answer4)"
            >
              (4) {{ quiz.content[quiz.nowPage].answer4 }}
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios'
import swal from 'sweetalert'
import Swal from 'sweetalert2'
import { BASE_URL } from '@/constant/BASE_URL'
import { onMounted } from '@vue/runtime-core'
import { ref } from 'vue'

export default {
  setup(props, { emit }) {
    let quiz = ref({ content: [], nowPage: 0 })
    const userInfo = JSON.parse(localStorage.getItem('userInfo'))
    const audio = new Audio('audio/button.mp3')

    const Toast = Swal.mixin({
      toast: true,
      position: 'top',
      showConfirmButton: false,
      timer: 3000,
      timerProgressBar: true
    })

    function getQuiz() {
      axios({
        url: BASE_URL + '/api/v1/mission/quiz/' + userInfo.nowMissionId,
        method: 'GET',
        headers: {
          AUTHORIZATION: 'Bearer ' + localStorage.getItem('accessToken')
        }
      })
        .then((res) => {
          quiz.value.content = res.data
        })
        .catch((err) => console.log(err))
    }

    function endQuiz() {
      Toast.fire({
        icon: 'success',
        html:
          '<p style="text-align:center;"><b>퀴즈를 통과했어요! </b></p>' +
          '<br />' +
          `<p style="text-align:center;">말을 걸어 유물을 돌려받으러 가요 🚀</p>`
      })
      axios({
        url: BASE_URL + '/api/v1/mission/',
        method: 'PUT',
        headers: {
          AUTHORIZATION: 'Bearer ' + localStorage.getItem('accessToken')
        }
      })
      setTimeout(() => {
        emit('quizClose')
      }, 1000)
    }

    function nextQuiz(answer) {
      audio.play()
      if (answer === quiz.value.content[quiz.value.nowPage].right_answer) {
        quiz.value.nowPage += 1
        if (quiz.value.nowPage !== quiz.value.content.length) {
          swal({
            title: '정답!!!',
            icon: 'success',
            text: '다음 문제도 풀어볼까요?',
            buttons: false,
            timer: 800
          })
        } else if (quiz.value.nowPage === quiz.value.content.length) {
          endQuiz()
        }
      } else {
        swal({
          title: '틀렸습니다. 더 공부하고 도전하세요!',
          icon: 'error',
          text: '   ',
          buttons: false,
          timer: 2000
        })
        emit('quizClose')
      }
    }

    onMounted(() => getQuiz())

    return {
      quiz,
      nextQuiz,
      endQuiz
    }
  }
}
</script>

<style scoped>
.quiz {
  display: flex;
  align-items: flex-end;
  justify-content: center;
  position: fixed;
  z-index: 30;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  cursor: url('@/assets/pointer.cur'), auto;
}
.quiz__box {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  width: 100%;
  height: 100%;
}
.box {
  border-radius: 30px;
  padding: 3vw;
  width: 60%;
  height: 60%;
  background-color: rgb(229, 224, 196);
  display: flex;
  flex-direction: column;
}
.question {
  font-size: 1.5vw;
  font-weight: bold;
  width: 100%;
  height: 10%;
  margin-left: 5vw;
}
.content {
  width: 90%;
  height: 90%;
  margin-top: 1vh;
  margin: auto;
  display: flex;
  justify-content: space-evenly;
  background-color: white;
  border-radius: 10px;
}
.content > img {
  width: 25vw;
  height: 50vh;
  margin-top: 2vh;
}
.answers {
  display: flex;
  flex-direction: column;
  justify-content: center;
  margin-left: 2vw;
}
.answers > div {
  font-size: 1.2rem;
  display: flex;
  align-items: center;
  margin: 1vw;
  padding-left: 2vw;
  width: 20vw;
  height: 7vh;
  border-radius: 3vh;
  background-color: rgb(242, 241, 235);
  transition: 0.5s;
  cursor: url('@/assets/selector.cur'), pointer;
}

.answers > div:hover {
  scale: 1.05;
  background-color: #6bfa8d;
}
</style>
