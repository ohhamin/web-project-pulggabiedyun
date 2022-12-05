<template>
  <div class="talk">
    <div class="talk__box">
      <div class="box" v-if="this.text.text">
        <div
          class="content__box"
          @click="nextTalk()"
          v-if="this.isTalk.content.line.length !== nowPage.nowPage + 1"
        >
          <div class="name">{{ this.isTalk.name }}</div>
          <div class="content">
            {{ this.text.text }}
          </div>
        </div>
        <div
          class="content__box"
          v-else-if="this.isTalk.content.line.length === nowPage.nowPage + 1"
          @click="nextTalk()"
        >
          <div class="name">{{ this.isTalk.name }}</div>
          <div class="content">
            {{ this.text.text }}
          </div>
        </div>
        <div class="btn__box">
          <font-awesome-icon
            class="icon"
            @click="nextTalk()"
            v-if="
              this.isTalk.content.line.length !== nowPage.nowPage + 1 &&
              this.text.textArray.length <= this.text.textIdx
            "
            icon="fa-solid fa-caret-down"
            style="font-size: 50px"
          />
          <font-awesome-icon
            v-else-if="
              this.isTalk.content.line.length === nowPage.nowPage + 1 &&
              this.text.textArray.length <= this.text.textIdx
            "
            class="icon"
            icon="fa-solid fa-xmark"
            @click="endTalk()"
            style="font-size: 45px"
          />
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios'
import { BASE_URL } from '@/constant/BASE_URL'
import { ref } from 'vue'
import { useStore } from 'vuex'
import Swal from 'sweetalert2'
import JSConfetti from 'js-confetti'

const jsConfetti = new JSConfetti()

export default {
  props: {
    isTalk: Object
  },
  setup(props, { emit }) {
    let nowPage = ref({ nowPage: 0 })
    let text = ref({ text: '', textIdx: 0, textArray: [] })
    const store = useStore()
    const audio = new Audio('audio/button.mp3')
    text.value.textArray = props.isTalk.content.line[0].split('')

    setInterval(() => {
      if (text.value.textArray.length > text.value.textIdx) {
        text.value.text += text.value.textArray[text.value.textIdx]
        text.value.textIdx += 1
      }
    }, 50)

    // toast 설정
    const Toast = Swal.mixin({
      toast: true,
      position: 'top',
      customClass: {
        popup: 'mission-toast'
      },
      showConfirmButton: false,
      timer: 3000,
      timerProgressBar: true
    })

    // nexttalk 다음 대화로 넘기기
    function nextTalk() {
      if (text.value.textArray.length > text.value.textIdx) {
        text.value.textIdx = text.value.textArray.length
        text.value.text = props.isTalk.content.line[nowPage.value.nowPage]
      } else if (
        props.isTalk.content.line.length !==
        nowPage.value.nowPage + 1
      ) {
        nowPage.value.nowPage += 1
        text.value.textArray =
          props.isTalk.content.line[nowPage.value.nowPage].split('')
        text.value.textIdx = 0
        text.value.text = ''
        audio.play()
      }
    }

    // endtalk
    function endTalk() {
      audio.play()
      const content = props.isTalk.content
      const userInfo = JSON.parse(localStorage.getItem('userInfo'))

      const monsters = [
        { id: 2, name: '도꼬마리몬' },
        { id: 3, name: '마늘몬' }
      ]

      const clearImg = require('@/assets/mission/clear.png')

      function startConfetti() {
        jsConfetti.addConfetti()
      }

      function fetchMonster() {
        const monsterId = userInfo.representMonster
        axios({
          url: BASE_URL + '/api/v1/monster/' + monsterId,
          method: 'GET',
          headers: {
            AUTHORIZATION: 'Bearer ' + localStorage.getItem('accessToken')
          }
        })
          .then((res) => {
            let monster = res.data
            startConfetti()
            Swal.fire({
              title: 'Level Up!!🎉',
              html: `<div style="text-align:center;">
                  <img  style="height:200px;width:150px;text-align:center;" src="${require('@/assets/starting/' +
                    monsterId +
                    '.png')}"/>
                  <p style="font-size: 1.2rem;"><b>${
                    monster.name
                  }</b>이</p><br /> <p style="font-size: 1.2rem;"> <b>Lv.${
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

      function defaultAlert() {
        Swal.fire({
          title: `${content.characters}의 고민 해결 완료 ✨`,
          html:
            `<div style="display:flex; flex-direction: row; justify-content:center">

                  <div style="margin: 1vw">
                    <img  style="height:60px;width:60px;text-align:center;margin-bottom:1vh;" src="${require('@/assets/mission/exp.png')}"/>
                    <p style="font-size:0.9rem;">경험치 <b>${
                      content.exp
                    }</b></p>
                  </div>

                  <div style="margin: 1vw">
                  <img  style="height:60px;width:60px;text-align:center;" src="${require('@/assets/item/' +
                    content.item +
                    '.png')}"/>
                    <p style="font-size:0.9rem;">${content.itemName}</p>
                  </div>

                  </div>` + `<p>🔍 ${content.next}</p>`,
          imageUrl: clearImg,
          imageWidth: 300,
          imageHeight: 100,
          imageAlt: 'Custom image',
          background: 'rgba(255, 255, 255)'
        })
      }

      function levelUpAlert() {
        Swal.fire({
          title: `${content.characters}의 고민 해결 완료 ✨`,
          html:
            `<div style="display:flex; flex-direction: row; justify-content:center">

                  <div style="margin: 1vw">
                    <img  style="height:60px;width:60px;text-align:center;margin-bottom:1vh;" src="${require('@/assets/mission/exp.png')}"/>
                    <p style="font-size:0.9rem;">경험치 <b>${
                      content.exp
                    }</b></p>
                  </div>

                  <div style="margin: 1vw">
                  <img  style="height:60px;width:60px;text-align:center;" src="${require('@/assets/item/' +
                    content.item +
                    '.png')}"/>
                    <p style="font-size:0.9rem;">${content.itemName}</p>
                  </div>

                  </div>` + `<p>🔍 ${content.next}</p>`,
          imageUrl: clearImg,
          imageWidth: 300,
          imageHeight: 100,
          imageAlt: 'Custom image',
          background: 'rgba(255, 255, 255)'
        })
          .then((res) => {
            if (res.value) {
              fetchMonster()
            }
          })
          .catch((err) => console.log(err))
      }

      function defaultMonsterAlert() {
        if (content.missionId === 2) {
          Swal.fire({
            title: `${content.characters}의 고민 해결 완료 ✨`,
            html:
              `<div style="display:flex; flex-direction: row; justify-content:center">

                  <div style="margin: 1vw">
                    <img  style="height:60px;width:60px;text-align:center;margin-bottom:1vh;" src="${require('@/assets/mission/exp.png')}"/>
                    <p style="font-size:0.9rem;">경험치 <b>${
                      content.exp
                    }</b></p>
                  </div>

                  <div style="margin: 1vw">
                  <img  style="height:60px;width:60px;text-align:center;" src="${require('@/assets/item/' +
                    content.item +
                    '.png')}"/>
                    <p style="font-size:0.9rem;">${content.itemName}</p>
                  </div>

                  <div style="margin: 1vw">
                    <img  style="height:60px;width:60px;text-align:center;" src="${require('@/assets/mission/' +
                      monsters[0].id +
                      '.png')}"/>
                    <p style="font-size:0.9rem;">${monsters[0].name}</p>
                  </div>

                  </div>` + `<p>🔍 ${content.next}</p>`,
            imageUrl: clearImg,
            imageWidth: 300,
            imageHeight: 100,
            imageAlt: 'Custom image',
            background: 'rgba(255, 255, 255)'
          })
        }

        if (content.missionId === 5) {
          Swal.fire({
            title: `${content.characters}의 고민 해결 완료 ✨`,
            html:
              `<div style="display:flex; flex-direction: row; justify-content:center">

                  <div style="margin: 1vw">
                    <img  style="height:60px;width:60px;text-align:center;margin-bottom:1vh;" src="${require('@/assets/mission/exp.png')}"/>
                    <p style="font-size:0.9rem;">경험치 <b>${
                      content.exp
                    }</b></p>
                  </div>

                  <div style="margin: 1vw">
                  <img  style="height:60px;width:60px;text-align:center;" src="${require('@/assets/item/' +
                    content.item +
                    '.png')}"/>
                    <p style="font-size:0.9rem;">${content.itemName}</p>
                  </div>

                  <div style="margin: 1vw">
                    <img  style="height:60px;width:60px;text-align:center;" src="${require('@/assets/mission/' +
                      monsters[1].id +
                      '.png')}"/>
                    <p style="font-size:0.9rem;">${monsters[1].name}</p>
                  </div>

                  </div>` + `<p>🔍 ${content.next}</p>`,
            imageUrl: clearImg,
            imageWidth: 300,
            imageHeight: 100,
            imageAlt: 'Custom image',
            background: 'rgba(255, 255, 255)'
          })
        }

        if (content.missionId === 8) {
          Swal.fire({
            title: `${content.characters}를 물리쳤어요! 🏆`,
            html:
              `<div style="display:flex; flex-direction: row; justify-content:center">

                  <div style="margin: 1vw">
                    <img  style="height:60px;width:60px;text-align:center;margin-bottom:1vh;" src="${require('@/assets/mission/exp.png')}"/>
                    <p style="font-size:0.9rem;">경험치 <b>${
                      content.exp
                    }</b></p>
                  </div>

                  <div style="margin: 1vw">
                  <img  style="height:60px;width:60px;text-align:center;" src="${require('@/assets/item/18.png')}"/>
                    <p style="font-size:0.9rem;">동의보감</p>
                  </div>

                  </div>` + `<p>🔍 ${content.next}</p>`,
            imageUrl: clearImg,
            imageWidth: 300,
            imageHeight: 100,
            imageAlt: 'Custom image',
            background: 'rgba(255, 255, 255)'
          })
        }
      }

      function levelUpMonsterAlert() {
        if (content.missionId === 2) {
          Swal.fire({
            title: `${content.characters}의 고민 해결 완료 ✨`,
            html:
              `<div style="display:flex; flex-direction: row; justify-content:center">

                  <div style="margin: 1vw">
                    <img  style="height:60px;width:60px;text-align:center;margin-bottom:1vh;" src="${require('@/assets/mission/exp.png')}"/>
                    <p style="font-size:0.9rem;">경험치 <b>${
                      content.exp
                    }</b></p>
                  </div>

                  <div style="margin: 1vw">
                  <img  style="height:60px;width:60px;text-align:center;" src="${require('@/assets/item/' +
                    content.item +
                    '.png')}"/>
                    <p style="font-size:0.9rem;">${content.itemName}</p>
                  </div>

                  <div style="margin: 1vw">
                    <img  style="height:60px;width:60px;text-align:center;" src="${require('@/assets/mission/' +
                      monsters[0].id +
                      '.png')}"/>
                    <p style="font-size:0.9rem;">${monsters[0].name}</p>
                  </div>

                  </div>` + `<p>🔍 ${content.next}</p>`,
            imageUrl: clearImg,
            imageWidth: 300,
            imageHeight: 100,
            imageAlt: 'Custom image',
            background: 'rgba(255, 255, 255)'
          })
            .then((res) => {
              if (res.value) {
                fetchMonster()
              }
            })
            .catch((err) => console.log(err))
        }

        if (content.missionId === 5) {
          Swal.fire({
            title: `${content.characters}의 고민 해결 완료 ✨`,
            html:
              `<div style="display:flex; flex-direction: row; justify-content:center">

                  <div style="margin: 1vw">
                    <img  style="height:60px;width:60px;text-align:center;margin-bottom:1vh;" src="${require('@/assets/mission/exp.png')}"/>
                    <p style="font-size:0.9rem;">경험치 <b>${
                      content.exp
                    }</b></p>
                  </div>

                  <div style="margin: 1vw">
                  <img  style="height:60px;width:60px;text-align:center;" src="${require('@/assets/item/' +
                    content.item +
                    '.png')}"/>
                    <p style="font-size:0.9rem;">${content.itemName}</p>
                  </div>

                  <div style="margin: 1vw">
                    <img  style="height:60px;width:60px;text-align:center;" src="${require('@/assets/mission/' +
                      monsters[1].id +
                      '.png')}"/>
                    <p style="font-size:0.9rem;">${monsters[1].name}</p>
                  </div>

                  </div>` + `<p>🔍 ${content.next}</p>`,
            imageUrl: clearImg,
            imageWidth: 300,
            imageHeight: 100,
            imageAlt: 'Custom image',
            background: 'rgba(255, 255, 255)'
          })
            .then((res) => {
              if (res.value) {
                fetchMonster()
              }
            })
            .catch((err) => console.log(err))
        }

        if (content.missionId === 8) {
          Swal.fire({
            title: `${content.characters}를 물리쳤어요! 🏆`,
            html:
              `<div style="display:flex; flex-direction: row; justify-content:center">

                  <div style="margin: 1vw">
                    <img  style="height:60px;width:60px;text-align:center;margin-bottom:1vh;" src="${require('@/assets/mission/exp.png')}"/>
                    <p style="font-size:0.9rem;">경험치 <b>${
                      content.exp
                    }</b></p>
                  </div>

                  <div style="margin: 1vw">
                  <img  style="height:60px;width:60px;text-align:center;" src="${require('@/assets/item/18.png')}"/>
                    <p style="font-size:0.9rem;">동의보감</p>
                  </div>

                  </div>` + `<p>🔍 ${content.next}</p>`,
            imageUrl: clearImg,
            imageWidth: 300,
            imageHeight: 100,
            imageAlt: 'Custom image',
            background: 'rgba(255, 255, 255)'
          })
            .then((res) => {
              if (res.value) {
                fetchMonster()
              }
            })
            .catch((err) => console.log(err))
        }
      }

      // started : 미션 시작한 상태 -> 보스 찾아가 문제 풀기 / 대화 끝나고 바로 퀴즈 시작
      if (content.status === 'STARTED') {
        Toast.fire({
          icon: 'success',
          title: `${props.isTalk.name}이 내는 퀴즈를 통과해라!`
        })
        emit('quizStart')
      } else {
        if (userInfo.nowMissionId === content.missionId) {
          // ready : 미션 수행 가능한 상태 -> 위인이 도움 요청
          if (content.status === 'READY') {
            axios({
              url: BASE_URL + '/api/v1/mission/',
              method: 'PUT',
              headers: {
                AUTHORIZATION: 'Bearer ' + localStorage.getItem('accessToken')
              }
            })
              .then(() => {
                emit('talkClose')
                Toast.fire({
                  icon: 'success',
                  title: '유물 도둑을 찾아라!',
                  html: `<p>${content.next}</p>`
                })
              })
              .catch((err) => console.log(err))
          } else if (content.status === 'QUIZ_PASSED') {
            // quiz_passed : 퀴즈 다 맞힌 상태 -> 시비거는 보스와 배틀 시작
            emit('talkClose')
            Toast.fire({
              icon: 'success',
              title: `${props.isTalk.name}과의 배틀에서 승리하라!`
            })
            emit('enterBattle')
          } else if (content.status === 'BATTLE_WIN') {
            if (
              content.missionId === 2 ||
              content.missionId === 5 ||
              content.missionId === 8
            ) {
              // battle win : 배틀 이긴 상태 -> 위인한테 유물 가져다준다 / 미션 마지막!
              axios({
                url: BASE_URL + '/api/v1/mission/',
                method: 'PUT',
                headers: {
                  AUTHORIZATION: 'Bearer ' + localStorage.getItem('accessToken')
                }
              }).then((res) => {
                store.dispatch('fetchnowUserInfo')
                if (content.missionId === 2) {
                  const monster1 = { id: 2, name: '도꼬마리몬' }
                  axios({
                    url: BASE_URL + '/api/v1/monster/' + monster1.id,
                    method: 'POST',
                    headers: {
                      AUTHORIZATION:
                        'Bearer ' + localStorage.getItem('accessToken')
                    }
                  }).catch((err) => console.log(err))
                }
                if (content.missionId === 5) {
                  const monster2 = { id: 3, name: '마늘몬' }

                  axios({
                    url: BASE_URL + '/api/v1/monster/' + monster2.id,
                    method: 'POST',
                    headers: {
                      AUTHORIZATION:
                        'Bearer ' + localStorage.getItem('accessToken')
                    }
                  }).catch((err) => console.log(err))
                }
                if (res.data === true) {
                  emit('talkClose')
                  levelUpMonsterAlert()
                } else {
                  emit('talkClose')
                  defaultMonsterAlert()
                }
              })
            } else {
              // battle win : 배틀 이긴 상태 -> 위인한테 유물 가져다준다 / 미션 마지막!
              axios({
                url: BASE_URL + '/api/v1/mission/',
                method: 'PUT',
                headers: {
                  AUTHORIZATION: 'Bearer ' + localStorage.getItem('accessToken')
                }
              }).then((res) => {
                store.dispatch('fetchnowUserInfo')
                if (res.data === true) {
                  emit('talkClose')
                  levelUpAlert()
                } else {
                  emit('talkClose')
                  defaultAlert()
                }
              })
            }
          } else {
            emit('talkClose')
          }
        } else {
          emit('talkClose')
        }
      }
    }

    return {
      nowPage,
      text,
      endTalk,
      nextTalk
    }
  }
}
</script>

<style scoped>
.talk {
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
.talk__box {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  width: 100%;
  height: 50%;
}
.box {
  border-top-right-radius: 10vw;
  border-top-left-radius: 10vw;
  border-bottom-right-radius: 6vw;
  border-bottom-left-radius: 6vw;
  width: 60%;
  height: 50%;
  padding: 2vw 7vw;
  box-shadow: 10px 10px 10px #00000092;
  background-color: rgb(229, 224, 196);
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}
.content__box {
  position: relative;
  cursor: url('@/assets/selector.cur'), pointer;
}
.name {
  width: 10vw;
  height: 6vh;
  border-radius: 3vh;
  box-shadow: 2px 3px 5px 0px rgba(0, 0, 0, 0.5);
  background-color: rgb(242, 241, 235);
  top: -7vh;
  position: absolute;
  display: flex;
  justify-content: center;
  align-items: center;
  font-size: 3.5vh;
  font-weight: bold;
}

/* 대사  */
.content {
  margin-top: 5vh;
  margin-left: 2vh;
  font-size: 1.4rem;
  letter-spacing: 0.3px;
  word-spacing: 5px;
}
.btn__box {
  display: flex;
  justify-content: flex-end;
  margin-bottom: 3vh;
}

.icon {
  cursor: url('@/assets/selector.cur'), pointer;
  transition: 0.5s;
  animation: blinker 1s cubic-bezier(1, 1, 1, 1) infinite alternate;
}

.icon:hover {
  scale: 1.1;
  color: #467302;
}

.colored-toast {
  width: 900px;
}

@keyframes blinker {
  from {
    opacity: 1;
  }
  to {
    opacity: 0;
  }
}
</style>
