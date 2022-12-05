<template>
  <div class="background">
    <div class="setnicknamepage">
      <div class="setnicknamepage__text">
        풀깨비뎐에 새겨질 당신의 이름을 알려주세요 🐯
      </div>

      <div>
        한글이나 영문자, 숫자의 조합으로 1~4자리
        <br />
        <br />
      </div>
      <input v-model="nickname" placeholder="닉네임" />

      <div class="setnicknamepage__button">
        <button
          class="setnickname__button"
          type="submit"
          @click="isNicknameDuplicate()"
        >
          중복확인
        </button>

        <button class="login__button" type="submit" @click="setnickname()">
          완료
        </button>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios'
import { BASE_URL } from '@/constant/BASE_URL'
import { mapActions } from 'vuex'
import swal from 'sweetalert'

var nicknameCheck = /^[가-힣a-zA-Z0-9]{1,4}$/

export default {
  name: 'SetNicknameView',
  data() {
    return {
      nickname: this.nickname,
      nicknameDuplicate: true
    }
  },
  methods: {
    ...mapActions(['fetchnowUserInfo']),
    isNicknameDuplicate() {
      if (!nicknameCheck.test(this.nickname)) {
        swal({
          title:
            '닉네임은 한글이나 영문자, 숫자의 조합으로 1~4자리를 사용해야 해요🙏',
          text: '🐯',
          icon: 'warning',
          buttons: false,
          timer: 1500
        })
      } else {
        axios
          .get(BASE_URL + '/api/v1/user/auth/check/nickname/' + this.nickname, {
            nickname: this.nickname
          })
          .then((res) => {
            if (res.data === false) {
              this.nicknameDuplicate = false

              swal({
                title: `<${this.nickname}> 닉네임 사용 가능!`,
                text: '   ',
                icon: 'success',
                buttons: false,
                timer: 1500
              })
            } else {
              this.nicknameDuplicate = true
              swal({
                title: '이미 존재하는 닉네임입니다😥',
                text: '🐯',
                icon: 'warning',
                buttons: false,
                timer: 1500
              })
            }
          })
          .catch((err) => {
            console.log(err)
          })
      }
    },
    setnickname() {
      if (this.nicknameDuplicate === true) {
        swal({
          title: '잠깐! 🚨',
          text: '닉네임 중복 검사를 먼저 진행해주세요.',
          icon: 'warning',
          buttons: false,
          timer: 1500
        })
      } else if (!nicknameCheck.test(this.nickname)) {
        swal({
          title:
            '닉네임은 한글이나 영문자, 숫자의 조합으로 1~4자리를 사용해야 해요🙏',
          text: '🐯',
          icon: 'warning',
          buttons: false,
          timer: 1500
        })
      } else {
        const option = {
          headers: {
            AUTHORIZATION: 'Bearer ' + localStorage.getItem('accessToken')
          }
        }
        axios
          .put(
            BASE_URL + '/api/v1/user/set/nickname?nickname=' + this.nickname,
            null,
            option
          )
          .then(() => {
            this.fetchnowUserInfo()
            swal({
              title: this.nickname + '님, 환영합니다!🥰',
              text: '풀깨비와 함께 섬을 탐험해보세요!',
              icon: 'success',
              buttons: false,
              timer: 1500
            })
            this.$router.push({ name: 'startingcard' })
          })
      }
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
  cursor: url('@/assets/selector.cur'), pointer;
}
.setnickname__button:hover {
  background-color: #467302;
  color: white;
}

.login__button:hover {
  background-color: #467302;
  color: white;
}

.setnicknamepage__img {
  width: 30vw;
}

.background {
  height: 100vh;
  overflow: hidden;
  margin: 0;
  background-image: linear-gradient(rgba(0, 0, 0, 0.5), rgba(0, 0, 0, 0.5)),
    url('@/assets/startimg.png');
  background-size: cover;
  background-repeat: no-repeat;
  background-position: center;
  display: flex;
  flex-direction: row;
  justify-content: center;
  align-items: center;
}

.setnicknamepage {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  height: 60%;
  width: 70vw;
  background-color: white;
}

input {
  display: block;
  width: 30vw;
  height: 7vh;
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

.setnicknamepage__text {
  font-size: 2vw;
  margin-bottom: 3vh;
}

.setnicknamepage__button {
  display: flex;
  flex-direction: row;
  justify-content: center;
  align-items: center;
}

@media screen and (max-width: 850px) {
  button {
    height: 6vh;
    border-radius: 50px;
    border: none;
    width: 35vw;
    margin: 10px;
  }

  .setnicknamepage {
    height: 95%;
    border-radius: 40px;
  }

  .setnicknamepage__img {
    width: 80vw;
  }

  .setnicknamepage__text {
    font-size: 5vw;
    text-align: center;
    margin: 3vw;
  }
  input {
    width: 70vw;
    height: 8vh;
    font-size: 2vh;
    background-size: 3vw 3vw;
    padding-left: 4vw;
  }
}
</style>
