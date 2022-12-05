<template>
  <div class="monsterDetail__container">
    <div class="monsterDetail">
      <div class="monster__img">
        <img class="banner__img" src="@/assets/monsters/Banner.png" alt="" />
        <img
          class="my__monster"
          :src="
            require(`@/assets/monsters/${this.monsterDetail.monsterId}.png`)
          "
          alt=""
        />
      </div>

      <div class="monster__data">
        <div class="monster__name">
          <p>{{ this.monsterDetail.name }}</p>
        </div>

        <div class="monster__detail">
          <div v-if="this.monsterDetail.name === '개나리몬'" class="detail">
            <p>속성</p>
            <p>균형</p>
          </div>
          <div
            v-else-if="this.monsterDetail.name === '도꼬마리몬'"
            class="detail"
          >
            <p>속성</p>
            <p>방어</p>
          </div>
          <div v-else class="detail">
            <p>속성</p>
            <p>공격</p>
          </div>
          <div class="detail">
            <p>Lv</p>
            <p>{{ this.monsterDetail.level }}</p>
          </div>
          <div class="detail">
            <p>Exp</p>
            <p>{{ this.monsterDetail.exp }}</p>
          </div>
          <div class="detail">
            <p>Hp</p>
            <p>{{ this.monsterDetail.hp }}</p>
          </div>
          <div class="detail">
            <p>공격력</p>
            <p>{{ this.monsterDetail.attack }}</p>
          </div>
          <div class="detail">
            <p>방어력</p>
            <p>{{ this.monsterDetail.defence }}</p>
          </div>
        </div>
      </div>
      <div>
        <button class="monster__btn" @click="setrepresentMonster()">
          대표 풀깨비로 설정
        </button>
      </div>

      <p class="exit__btn" @click="$emit('monsterClose')"><b>X</b></p>
    </div>
  </div>
</template>

<script>
import axios from 'axios'
import { BASE_URL } from '@/constant/BASE_URL'
import Swal from 'sweetalert2'
import { mapActions } from 'vuex'

export default {
  props: {
    monsterDetail: Object
  },
  data() {
    return {}
  },
  methods: {
    ...mapActions(['fetchnowUserInfo']),
    setrepresentMonster() {
      const id = Number(this.monsterDetail.monsterId)

      axios({
        url: BASE_URL + '/api/v1/user/represent/' + id,
        method: 'PUT',
        headers: {
          AUTHORIZATION: 'Bearer ' + localStorage.getItem('accessToken')
        }
      })
        .then(() => {
          this.fetchnowUserInfo()
          Swal.fire('대표 풀깨비로 설정되었습니다!', '    ', 'success')
        })
        .catch((err) => {
          console.log(err)
        })
    }
  }
}
</script>

<style scoped>
.monsterDetail__container {
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
.monsterDetail {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  position: fixed;
  z-index: 30;
  top: 20%;
  left: 35%;
  width: 25%;
  height: 70%;
  background: rgba(255, 255, 255, 0.9);
  backdrop-filter: blur(4px);
  border-radius: 20px;
}

.monster__img {
  margin-bottom: 2vh;
  text-align: center;
}

.banner__img {
  position: absolute;
  width: 18vw;
  z-index: 40;
  left: 13.5%;
  top: 3%;
}

.my__monster {
  margin-top: 5vh;
  width: 15vw;
  border-radius: 20px;
}

.monster__data {
  background-color: #fffdbf;
  width: 50%;
  height: 30%;
  border-radius: 10px;
  padding: 5%;
  text-align: center;
  margin-bottom: 2vh;
}

.monster__name {
  font-weight: bold;
  font-size: 1.2rem;
  margin-bottom: 2vh;
}

.detail {
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  margin-bottom: 0.3vh;
}

.monster__btn {
  height: 4vh;
  border-radius: 50px;
  border: none;
  width: 12vw;
  cursor: url('@/assets/selector.cur'), pointer;
}

.monster__btn:hover {
  background-color: #6bfa8d;
}
p {
  margin: 0;
}

.exit__btn {
  width: 2.5vw;
  display: flex;
  justify-content: center;
  align-items: center;
  position: absolute;
  top: 3vh;
  right: 1vw;
  cursor: url('@/assets/selector.cur'), pointer;
  font-size: 1.2rem;
  transition: 0.5s;
}

.exit__btn:hover {
  scale: 1.1;
}
</style>
