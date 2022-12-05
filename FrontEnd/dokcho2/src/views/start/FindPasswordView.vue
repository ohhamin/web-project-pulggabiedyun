<template>
  <div class="background">
    <div class="findpasswordpage">
      <div class="findpasswordpage__text">
        회원가입할 때 입력하신 이메일로 비밀번호 변경 메일을 보내드릴게요 🐯
      </div>

      <input
        @keyup.enter="findpassword()"
        v-model="username"
        placeholder="아이디를 입력하세요"
      />
      <input
        @keyup.enter="findpassword()"
        v-model="email"
        placeholder="이메일을 입력하세요"
      />

      <div class="findpasswordpage__button">
        <button
          class="findpassword__button"
          type="submit"
          @click="findpassword()"
        >
          비밀번호찾기
        </button>
        <button class="login__button" type="submit" @click="to_login()">
          취소
        </button>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios'
import { BASE_URL } from '@/constant/BASE_URL'
import swal from 'sweetalert'

var emailCheck =
  /[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?/

export default {
  data() {
    return {
      username: this.username,
      email: this.email
    }
  },
  methods: {
    async findpassword() {
      if (!emailCheck.test(this.email)) {
        swal({
          title: '정확한 이메일 주소인지 확인해주세요!',
          text: '🐯',
          icon: 'warning',
          buttons: false,
          timer: 1500
        })
      } else {
        await axios
          .put(BASE_URL + '/api/v1/user/auth/find', {
            email: this.email,
            username: this.username
          })
          .then(() => {
            swal({
              title: '비밀번호 변경 메일을 보내드렸어요 ☺',
              text: '🐯',
              icon: 'success',
              buttons: false,
              timer: 1500
            })
          })
          .catch(() => {
            swal({
              title: '아이디나 이메일이 정확한지 확인해주세요!',
              text: '🐯',
              icon: 'warning',
              buttons: false,
              timer: 1500
            })
          })
      }
    },
    to_login() {
      this.$router.push('/')
    }
  }
}
</script>

<style scoped>
button {
  height: 7vh;
  border-radius: 50px;
  border: none;
  width: 15vw;
  margin: 10px;
  background-color: #bfbfbf;
  cursor: url('@/assets/selector.cur'), pointer;
}
.findpassword__button:hover {
  background-color: #467302;
  color: white;
}

.login__button:hover {
  background-color: #467302;
  color: white;
}

.findpasswordpage__img {
  width: 30vw;
}

.background {
  height: 100vh;
  overflow: hidden;
  margin: 0;
  background-image: linear-gradient(rgba(0, 0, 0, 0.5), rgba(0, 0, 0, 0.5)),
    url('../../assets/startimg.png');
  background-size: cover;
  background-repeat: no-repeat;
  background-position: center;
  display: flex;
  flex-direction: row;
  justify-content: center;
  align-items: center;
}

.findpasswordpage {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  height: 60%;
  width: 60vw;
  background-color: rgba(255, 255, 255, 0.8);
  background-size: 100% 100%;
  background-repeat: no-repeat;
  background-position: center;
  display: flex;
  padding: 5%;
  border-radius: 40px;
}

input {
  display: block;
  width: 30vw;
  height: 5vh;
  margin: 8px 0 8px 0;
  padding: 10px 15px 10px 25px;
  font-size: 16px;
  border: #ececec solid 2px;
  border-radius: 20px;
}

input::placeholder {
  color: #467302;
}

input:focus {
  outline: none;
  border: #467302 solid 2px;
}

.findpasswordpage__text {
  font-size: 3vmin;
  margin-bottom: 3vh;
}

.findpasswordpage__button {
  display: flex;
  flex-direction: row;
  justify-content: center;
  align-items: center;
  margin-top: 3vh;
}
</style>
