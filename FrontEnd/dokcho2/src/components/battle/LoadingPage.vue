<template>
  <div class="loadingPage">
    <div class="loading__false">
      <p class="entering">전장으로 이동중...</p>
      <div class="loading">
        <img class="img1" src="@/assets/loading/1.png" alt="1" />
        <img class="img2" src="@/assets/loading/2.png" alt="2" />
        <img class="img3" src="@/assets/loading/3.png" alt="3" />
        <img class="img4" src="@/assets/loading/4.png" alt="4" />
      </div>
    </div>
  </div>
</template>

<script>
export default {}
</script>

<style scoped>
.loadingPage {
  position: fixed;
  width: 100vw;
  height: 100vh;
  z-index: 9999;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
}

.loading__img {
  margin-top: 5vh;
  width: 40%;
  border-radius: 30px;
}

.loading__false {
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  background-image: linear-gradient(rgba(0, 0, 0, 0.5), rgba(0, 0, 0, 0.5)),
    url('../../assets/battleLoading.png');
  background-size: cover;
}

.loading__true {
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.785);
  text-align: center;
}

.story {
  text-align: center;
  margin: 5vw 5vw 5vw 5vw;
  font-size: 18px;
  color: white;
  line-height: 2rem;
}

.start__btn {
  min-width: 15vw;
  min-height: 10vh;
  font-size: 20px;
  transition: 0.5s;
  background-color: #6bfa8d;
  margin-top: 20%;
}

.start__btn:hover {
  scale: 1.05;
  background-color: #6bfa8d;
}

.loading__box {
  width: 50vw;
  height: 25vh;
  display: flex;
  bottom: 5vh;
  flex-direction: column;
  justify-content: center;
  align-items: center;
}

.loading {
  width: 50vw;
  display: flex;
  justify-content: center;
  align-items: center;
}

.entering {
  margin: 3vh;
  margin-top: 30vh;
  color: white;
  font-size: 2rem;
  font-weight: bold;
}

@keyframes jump {
  0% {
    transform: translate3d(0, 0, 0);
  }
  40% {
    transform: translate3d(0, 30%, 0);
  }
  100% {
    transform: translate3d(0, 100%, 0);
  }
}

.loading > img {
  width: 5vw;
  height: 5vw;
  margin: 1vw;
}

.img1 {
  transform-origin: 0% 0%;
  animation: jump 0.5s linear alternate infinite;
}
.img2 {
  transform-origin: 50% 50%;
  animation: jump 0.5s linear alternate infinite;
  animation-delay: 0.1s;
}
.img3 {
  transform-origin: 100% 100%;
  animation: jump 0.5s linear alternate infinite;
  animation-delay: 0.2s;
}
.img4 {
  transform-origin: 50% 50%;
  animation: jump 0.5s linear alternate infinite;
  animation-delay: 0.3s;
}
</style>
