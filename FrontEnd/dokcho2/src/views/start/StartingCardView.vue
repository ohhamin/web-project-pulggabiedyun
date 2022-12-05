<template>
  <div class="introCard">
    <div class="introCard__top">
      <p class="TITLE">섬을 함께 탐험할 풀깨비가 찾아왔어요!</p>
    </div>

    <div class="introCard__body">
      <div class="introCard__middle">
        <div class="dockchoCard">
          <div class="imageBG">
            <img src="@/assets/starting/1.png" alt="" class="card__image" />
          </div>
          <p class="card__name">개나리몬</p>
          <p>" 난 봄이 좋아! "</p>
          <p>
            병충해와 추위에 잘 견디는 개나리는 <br /><br />
            예전부터 관상용, 생울타리용으로 사용했대!
          </p>
        </div>
      </div>
      <div class="introCard__bottom">
        <button
          class="main__btn"
          :class="this.isChecked ? '' : 'none'"
          @click="goToMain()"
        >
          개나리몬과 함께 탐험 시작하기
        </button>
      </div>
    </div>
  </div>
</template>

<script>
import { mapActions } from 'vuex'

export default {
  data() {
    return {
      userInfo: JSON.parse(localStorage.getItem('userInfo'))
    }
  },
  methods: {
    ...mapActions(['fetchnowUserInfo']),
    goToMain() {
      this.$router.push({ path: '/main' })
    }
  }
}
</script>

<style scoped>
.introCard {
  background-image: linear-gradient(rgba(0, 0, 0, 0.5), rgba(0, 0, 0, 0.5)),
    url('@/assets/forest2.png');
  background-size: cover;
  width: 100vw;
  height: 100vh;
  text-align: center;
}

.introCard__body {
  display: inline-block;
  width: 30%;
  height: 70%;
  background-color: rgba(255, 255, 255, 0.7);
  border-radius: 40px;
}

.introCard__top {
  align-items: center;
  text-align: center;
  color: white;
}

.introCard__top p {
  padding: 7vh;
  font-size: 5vh;
  margin: 0;
}

.introCard__middle {
  display: flex;
  flex-direction: row;
  justify-content: center;
  margin-top: 5vh;
  margin-bottom: 5vh;
}

.dockchoCard {
  margin: auto;
}

.card__image {
  width: 50%;
}

.card__name {
  margin-bottom: 5vh;
  font-weight: bold;
  font-size: 3vh;
}

.introCard__bottom {
  margin-top: 5%;
}

.main__btn {
  height: 5vh;
  border-radius: 50px;
  border: none;
  width: 12vw;
}
.main__btn:hover {
  cursor: url('@/assets/selector.cur'), pointer;
  background-color: green;
  color: white;
}
</style>
