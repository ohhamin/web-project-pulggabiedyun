<template>
  <div class="notfound__container">
    <div class="notfound__page">
      <div class="notfound__body">
        <p>잠깐! 여기는 섬으로 가는 길이 아니에요!</p>
        <img class="img__404" src="@/assets/404.png" alt="" />
      </div>
      <div class="notfound__btn">
        <button @click="goToStart()">섬으로 들어가기</button>
        <button @click="signUp()">회원가입하러가기</button>
      </div>
    </div>
  </div>
</template>

<script>
import { useRouter } from 'vue-router'

export default {
  setup() {
    const router = useRouter()

    function goToStart() {
      router.push({ path: '/' })
    }

    function signUp() {
      router.push({ path: '/signup' })
    }

    return {
      router,
      goToStart,
      signUp
    }
  }
}
</script>

<style scoped>
button {
  height: 6vh;
  border-radius: 50px;
  border: none;
  width: 12vw;
  cursor: url('@/assets/selector.cur'), pointer;
  margin-left: 3vw;
  margin-right: 3vw;
  font-size: 1.2rem;
  transition: 0.5s;
}

button:hover {
  scale: 1.05;
  background-color: #6bfa8d;
  /* color: white; */
}

.notfound__container {
  background-image: linear-gradient(rgba(0, 0, 0, 0.5), rgba(0, 0, 0, 0.5)),
    url('@/assets/forest2.png');
  background-size: cover;
  width: 100vw;
  height: 100vh;
  text-align: center;
}

.notfound__page {
  display: flex;
  flex-direction: column;
  margin: auto;
  justify-content: center;
}
.notfound__body {
  margin-top: 15vh;
}
.img__404 {
  width: 25vw;
  margin-left: 5vw;
}

p {
  color: white;
  font-size: 2rem;
  font-weight: bold;
}

.notfound__btn {
  display: flex;
  flex-direction: row;
  justify-content: center;
}
</style>
