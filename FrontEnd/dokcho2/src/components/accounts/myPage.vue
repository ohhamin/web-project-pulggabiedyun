<template>
  <div class="myPage__container">
    <div class="myPage">
      <div class="myPage__box">
        <div class="myPage__header">
          <img
            class="my__monster"
            :src="
              require(`@/assets/monsters/${this.userInfo.representMonster}.png`)
            "
            alt=""
          />

          <div class="myPage__info">
            <div class="my__info">
              <h1>
                {{ this.userInfo.nickname }} ({{ this.userInfo.username }}) 님
              </h1>
              <p>{{ this.userInfo.email }}</p>
            </div>
            <div class="my__edit">
              <button class="password__btn" @click="openPassword()">
                비밀번호변경
              </button>
              <changePassword
                v-if="this.password"
                @closePassword="closePassword"
              />
            </div>
          </div>
        </div>
        <div class="myPage__body">
          <div class="myPage__mission">
            <p>{{ this.userInfo.nowMissionId }}번</p>
          </div>
          <div class="myPage__monster">
            <p>{{ this.monsterNumber }}마리</p>
          </div>
          <div class="myPage__item">
            <p>{{ this.itemNumber.length }}개</p>
          </div>
        </div>
        <div class="myPage__footer">
          <button class="footer__btn" @click="goReset()">초기화</button>
          <button class="footer__btn" @click="deleteUser()">탈퇴</button>
        </div>
        <img
          class="exit__btn"
          @click="$emit('mypageClose')"
          src="@/assets/navbar/ExitButton.png"
          alt=""
        />
      </div>
    </div>
  </div>
</template>

<script>
import swal from 'sweetalert'
import Swal from 'sweetalert2'

import axios from 'axios'
import { BASE_URL } from '@/constant/BASE_URL'
import changePassword from '../accounts/changePassword.vue'
import { mapActions } from 'vuex'

const swalWithBootstrapButtons = Swal.mixin({
  customClass: {
    title: 'custom-title-class',
    confirmButton: 'btn btn-success',
    cancelButton: 'btn btn-danger'
  }
})

var passwordCheck = /^(?=.*[a-zA-Z])(?=.*[!@#$%^*+=-])(?=.*[0-9]).{8,25}$/

export default {
  components: { changePassword },
  data() {
    return {
      showMenu: false,
      isNone: false,
      userInfo: JSON.parse(localStorage.getItem('userInfo')),
      oldPassword: this.oldPassword,
      newPassword: this.newPassword,
      newPassword2: this.newPassword2,
      isPasswordChecked: false,
      today: '',
      cdate: '',
      password: false,
      monsterDetail: {},
      monsterNumber: '',
      itemNumber: '',
      audio: new Audio('audio/button.mp3')
    }
  },
  methods: {
    ...mapActions(['fetchnowUserInfo']),
    openPassword() {
      this.audio.play()
      this.password = true
    },
    closePassword() {
      this.password = false
    },
    overflow(value) {
      this.showMenu = value
    },
    displayNone() {
      this.isNone = !this.isNone
    },
    checkPassword() {
      if (passwordCheck.test(this.newPassword)) {
        this.isPasswordChecked = true
      } else {
        this.isPasswordChecked = false
      }
    },
    changePassword() {
      this.audio.play()
      if (!passwordCheck.test(this.newPassword)) {
        swal({
          title:
            '비밀번호는 영문자+숫자+특수문자 조합으로 8~25자리를 사용해야해요🙏',
          icon: 'error',
          text: '🤔',
          buttons: false,
          timer: 2000
        })
      } else if (this.newPassword === this.newPassword2) {
        axios
          .put(
            BASE_URL + '/api/v1/user/password/',
            {
              newPW: this.newPassword,
              nowPW: this.oldPassword
            },
            {
              headers: {
                'Content-type': 'application/json',
                AUTHORIZATION: 'Bearer ' + localStorage.getItem('accessToken')
              }
            }
          )
          .then(() => {
            swal({
              title: '비밀번호가 변경되었습니다!😘',
              icon: 'success',
              text: '바뀐 비밀번호로 로그인 해주세요😉',
              buttons: false,
              timer: 1500
            })
          })
          .catch((err) => {
            console.log(err)
            swal({
              title: '현재 비밀번호를 확인해주세요😥',
              icon: 'warning',
              text: '입력해주신 비밀번호와 현재 비밀번호가 다른 것 같아요😅',
              buttons: false,
              timer: 1500
            })
          })
      } else {
        swal({
          title: '새 비밀번호를 한번 더 확인해 주세요😢',
          icon: 'error',
          text: '두 비밀번호가 다른 것 같아요..😅',
          buttons: false,
          timer: 1500
        })
      }
    },
    deleteUser() {
      this.audio.play()
      swalWithBootstrapButtons
        .fire({
          title: '풀깨비섬에서 떠나시겠어요?',
          text: `${this.userInfo.nickname}님이 없으면 우리의 역사가 위험해요 😥`,
          icon: 'question',
          showCancelButton: true,
          confirmButtonText: '예',
          cancelButtonText: '아니오',
          reverseButtons: true
        })
        .then((res) => {
          if (res.value) {
            this.fetchDeleteUser()
          }
        })
    },
    fetchDeleteUser() {
      axios
        .delete(BASE_URL + '/api/v1/user/', {
          headers: {
            'Content-type': 'application/json',
            AUTHORIZATION: 'Bearer ' + localStorage.getItem('accessToken')
          }
        })
        .then(() => {
          swal({
            title: '탈퇴가 완료되었어요😭',
            icon: 'success',
            text: '악당 호랑이를 물리치러 언제든지 돌아오세요!',
            buttons: false,
            timer: 1500
          })
          localStorage.clear()
        })
        .catch((err) => {
          console.log(err)
        })
    },
    fetchrepresentMonster() {
      this.audio.play()
      const id = this.userInfo.representMonster
      axios({
        url: BASE_URL + '/api/v1/monster/' + id,
        method: 'GET',
        headers: {
          AUTHORIZATION: 'Bearer ' + localStorage.getItem('accessToken')
        }
      })
        .then((res) => {
          this.monsterDetail = res.data
        })
        .catch((err) => {
          console.log(err)
        })
    },
    goReset() {
      this.audio.play()
      // swal 띄워서 진짜 지울건지 확인
      swalWithBootstrapButtons
        .fire({
          title: '진행도를 초기화하시겠어요?',
          text: `${this.userInfo.nickname}님의 미션과 아이템이 모두 사라져요😥`,
          icon: 'danger',
          showCancelButton: true,
          confirmButtonText: '예',
          cancelButtonText: '아니오',
          reverseButtons: true
        })
        .then((res) => {
          if (res.value) {
            this.fetchReset()
          }
        })
    },

    fetchReset() {
      // 확인되면 axios 요청
      axios({
        url: BASE_URL + '/api/v1/user/reset',
        method: 'DELETE',
        headers: {
          AUTHORIZATION: 'Bearer ' + localStorage.getItem('accessToken')
        }
      })
        .then(() => {
          this.fetchnowUserInfo()

          swal({
            title: '진행도 초기화가 완료되었어요!',
            icon: 'success',
            text: '악당 호랑이를 물리치는 여정을 새롭게 시작하세요!',
            buttons: false,
            timer: 1500
          }).then(() => {
            this.$router.go()
          })
        })
        .catch((err) => {
          console.log(err)
        })
    },

    fetchUserMonster() {
      // 보유한 풀깨비 개수 세기 ~
      axios({
        url: BASE_URL + '/api/v1/monster',
        method: 'GET',
        headers: {
          AUTHORIZATION: 'Bearer ' + localStorage.getItem('accessToken')
        }
      })
        .then((res) => {
          this.monsterNumber = res.data.length
        })
        .catch((err) => {
          console.log(err)
        })
    },

    fetchUserItem() {
      // 보유한 아이템 개수 세기 ~
      axios({
        url: BASE_URL + '/api/v1/item',
        method: 'GET',
        headers: {
          AUTHORIZATION: 'Bearer ' + localStorage.getItem('accessToken')
        }
      })
        .then((res) => {
          this.itemNumber = res.data.filter(
            (item) => item.itemId < 8 || item.itemId === 18 || item.itemId > 20
          )
        })
        .catch((err) => {
          console.log(err)
        })
    }
  },
  created() {
    this.fetchrepresentMonster()
    this.fetchUserMonster()
    this.fetchUserItem()
  }
}
</script>

<style scoped>
button {
  height: 4vh;
  border-radius: 50px;
  border: none;
  width: 12vw;
}
.myPage__container {
  width: 100vw;
  height: 100vh;
  z-index: 30;
  top: 0;
  left: 0;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  position: absolute;
}

.myPage {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  position: fixed;
  z-index: 40;
  top: 4%;
  left: 25%;
  width: 50%;
  height: 90%;
  background: rgba(225, 225, 225, 0.714);
  background-size: cover;
  background-repeat: no-repeat;
  background-position: center;
  backdrop-filter: blur(4px);
  border-radius: 20px;
  overflow: visible;
}

.myPage__box {
  margin-top: 10vh;
  background: white;
  padding-top: 5vh;
  padding-bottom: 3vh;
  width: 80%;
  border-radius: 10px;
}

.myPage__header {
  width: 80%;
  display: flex;
  flex-direction: row;
  margin: auto;
  margin-bottom: 2vh;
}

.myPage__info {
  width: 100%;
}

.my__monster {
  width: 50%;
  border-radius: 20px;
}

.my__info {
  background-color: white;
  width: 100%;
  height: 40%;
  padding: 1vh;
  text-align: end;
  margin-bottom: 2vh;
  margin-top: 1vh;
}

.my__edit {
  width: 100%;
  padding: 1vh;
  text-align: end;
}

.password__btn {
  width: 8vw;
  cursor: url('@/assets/selector.cur'), pointer;
}

.password__btn:hover {
  background-color: #6bfa8d;
}

.myPage__body {
  display: flex;
  flex-direction: row;
  justify-content: space-around;
  border-radius: 10px;
  padding: 2vw;
}

.myPage__body p {
  padding-top: 30%;
  font-size: 1.5vw;
  font-weight: bold;
}

.myPage__mission,
.myPage__item,
.myPage__monster {
  width: 8vw;
  height: 8vw;
  border-radius: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.myPage__mission {
  background-size: cover;
  background-repeat: no-repeat;
  background-position: center;
  background-image: url('@/assets/mypage/mission.png');
  text-align: center;
}

.myPage__item {
  background: url('@/assets/mypage/item.png');
  background-size: cover;
  background-repeat: no-repeat;
  background-position: center;
}

.myPage__monster {
  background: url('@/assets/mypage/monster.png');
  background-size: cover;
  background-repeat: no-repeat;
  background-position: center;
}

.myPage__footer {
  width: 80%;
  display: flex;
  flex-direction: row;
  margin: auto;
  margin-top: 2vh;
  margin-bottom: 2vh;
  justify-content: center;
}

.footer__btn {
  width: 8vw;
  cursor: url('@/assets/selector.cur'), pointer;
  margin-left: 1vw;
}

.footer__btn:hover {
  background-color: #ff6a60;
}
.exit__btn {
  width: 4vw;
  display: flex;
  justify-content: center;
  align-items: center;
  position: absolute;
  top: 4vh;
  right: 4vw;
  cursor: url('@/assets/selector.cur'), pointer;
}
</style>
