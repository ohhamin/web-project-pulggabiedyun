<template>
  <router-view />
  <div class="fullPage">
    <img
      class="fullPage__btn"
      src="@/assets/open.png"
      @click="fullPageChange"
      v-if="fullPage === false"
    />
    <img
      class="fullPage__btn"
      src="@/assets/close.png"
      @click="fullPageChange"
      v-if="fullPage === true"
    />
  </div>
</template>

<script>
export default {
  data() {
    return {
      fullPage: false
    }
  },
  methods: {
    // 전체화면 변경
    fullPageChange() {
      const documentElement = document.documentElement
      if (document.fullscreenElement === null) {
        //전체화면 아닌 상태
        documentElement.requestFullscreen()
        this.fullPage = true
      } else {
        //전체화면 상태
        document.exitFullscreen()
        this.fullPage = false
      }
    }
  }
}
</script>

<style>
.fullPage {
  position: fixed;
  display: inline;
  z-index: 20;
  bottom: 5%;
  left: 93%;
}

.fullPage__btn {
  cursor: url('@/assets/selector.cur'), pointer;
  transition: 0.5s;
}

.fullPage__btn:hover {
  scale: 1.2;
}

input {
  border-radius: 20px;
  padding: 0 1vw;
}

button {
  height: 4vh;
  border-radius: 50px;
  border: none;
  width: 5vw;
  margin: 0.5vh;
  cursor: url('@/assets/selector.cur'), pointer;
}

body {
  cursor: url(pointer.cur), auto;
  -webkit-user-select: none;
  -moz-user-select: none;
  -ms-user-select: none;
}

@font-face {
  font-family: 'Mabinogi';
  src: url('fonts/Mabinogi_Classic_TTF.woff') format('woff');
  font-weight: normal;
  font-style: normal;
}

* {
  font-family: 'Mabinogi';
}

*::-webkit-scrollbar {
  width: 10px;
}
*::-webkit-scrollbar-thumb {
  background-color: #467302;
  border-radius: 10px;
  width: 8px;
  background-clip: padding-box;
  border: 2px solid transparent;
}
*::-webkit-scrollbar-track {
  background-color: #bcd497;
  opacity: 0.5;
  border-radius: 10px;
  box-shadow: inset 0px 0px 5px white;
}
</style>
