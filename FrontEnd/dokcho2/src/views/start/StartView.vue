<template>
  <div class="startpage">
    <img class="logo__img" src="../../assets/logo.png" alt="logo" />
    <div class="loginpage">
      <div class="login__form">
        <input v-model="username" placeholder="아이디를 입력하세요" />
        <input
          @keyup.enter="login()"
          v-model="password"
          type="password"
          placeholder="비밀번호를 입력하세요"
        />
        <a class="findpassword__a" href="#" @click="findpassword()"
          >비밀번호를 잊으셨나요?</a
        >
      </div>
      <div class="login__button">
        <img
          class="login__btn"
          @click="login()"
          src="../../assets/login.png"
          alt=""
        />
      </div>
    </div>
    <div class="other__btn">
      <img
        class="howto__btn"
        @click="howtoGame()"
        src="../../assets/game.png"
        alt=""
      />
      <img
        class="signup__btn"
        @click="signUp()"
        src="../../assets/join.png"
        alt=""
      />
    </div>
    <howToGame v-if="howToGame" @howtoGame="howtoGame" />
  </div>
</template>

<script>
import axios from 'axios'
import { BASE_URL } from '@/constant/BASE_URL'
import { mapActions, mapGetters } from 'vuex'
import swal from 'sweetalert'
import howToGame from '../../components/start/howToGame.vue'

export default {
  name: 'StartView',
  data() {
    return {
      username: '',
      password: '',
      kakaoLoginURL: BASE_URL + '/oauth2/authorization/kakao',
      howToGame: false,
      audio: new Audio('audio/dokchoTitle.mp3'),
      buttonAudio: new Audio('audio/button.mp3')
    }
  },
  components: {
    howToGame: howToGame
  },
  mounted() {
    this.audio.loop = true
    this.audio.volume = 0.5
    this.audio.play()
  },
  beforeUnmount() {
    this.audio.pause()
  },
  methods: {
    ...mapActions(['doRefreshToken', 'fetchUserInfo', 'fetchUserDeck']),
    ...mapGetters(['isAccessTokenExpired']),
    kakaoLogin() {
      this.buttonAudio.play()
      window.location.href = this.kakaoLoginURL
    },
    findpassword() {
      this.buttonAudio.play()
      this.$router.push({ name: 'findpassword' })
    },
    signUp() {
      this.buttonAudio.play()
      this.$router.push({ name: 'signup' })
    },
    howtoGame() {
      this.buttonAudio.play()
      if (this.howToGame === false) {
        this.howToGame = true
      } else {
        this.howToGame = false
      }
    },
    async login() {
      this.buttonAudio.play()

      try {
        const result = await axios.post(
          BASE_URL + '/api/v1/user/auth/login',
          {
            username: this.username,
            password: this.password
          },
          {
            headers: {
              'Content-Type': 'application/json'
            }
          }
        )
        if (result.status === 200) {
          localStorage.setItem('accessToken', result.data.accessToken)
          localStorage.setItem('refreshToken', result.data.refreshToken)
          const option = {
            headers: {
              AUTHORIZATION: 'Bearer ' + localStorage.getItem('accessToken')
            }
          }
          await axios
            .get(BASE_URL + '/api/v1/user/myinfo', option)
            .then((res) => {
              this.fetchUserInfo(res.data)
            })
          await this.$router.replace({ name: 'main' })
        }
      } catch (err) {
        swal({
          title: '로그인에 실패하였습니다',
          text: '아이디나 비밀번호를 확인해주세요!',
          icon: 'warning',
          buttons: false,
          timer: 1500
        })
      }
    }
  }
}
</script>

<style scoped>
.startpage {
  background-image: linear-gradient(rgba(0, 0, 0, 0.5), rgba(0, 0, 0, 0.5)),
    url('../../assets/startimg.png');
  background-size: cover;
  width: 100vw;
  height: 100vh;
}
input {
  display: block;
  width: 15vw;
  height: 3vh;
  margin: 8px 0 8px 0;
  padding: 10px 15px 10px 25px;
  font-size: 16px;
  border: #ececec solid 2px;
  border-radius: 10px;
}

input::placeholder {
  color: #467302;
}

input:focus {
  outline: none;
}
.loginpage {
  height: 20%;
  display: flex;
  flex-direction: row;
  margin: auto;
  justify-content: center;
}

.login__form {
  display: flex;
  flex-direction: column;
}
.logo__img {
  height: 30%;
  object-fit: contain;
  display: block;
  margin: auto;
  padding-top: 15vh;
  width: 31vw;
  margin-bottom: 2vh;
}

.login__button {
  margin-top: 0;
  margin-left: 1vw;
  text-align: center;
  width: 12vh;
}

.login__btn {
  width: 100%;
  margin-top: 2vh;
  margin-left: 1vh;
}

.login__btn:hover {
  cursor: url('@/assets/selector.cur'), pointer;
  scale: 1.05;
}
.kakao__button {
  display: flex;
  flex-direction: row;
  text-align: center;
  margin: 5vmin;
}
.kakaologin__btn {
  margin: auto;
  width: 10vw;
}

.kakaologin__btn:hover {
  cursor: url('@/assets/selector.cur'), pointer;
}
.other__btn {
  display: flex;
  flex-direction: row;
  justify-content: center;
  margin-top: 3vh;
  margin-right: 3vw;
}

.howto__btn,
.signup__btn {
  width: 10vw;
  margin-left: 2%;
}

.howto__btn:hover,
.signup__btn:hover {
  cursor: url('@/assets/selector.cur'), pointer;
  scale: 1.05;
}

.findpassword__a {
  color: gray;
  text-decoration: none;
  font-size: 2vh;
}

a:hover {
  color: #6bfa8d;
}
</style>
