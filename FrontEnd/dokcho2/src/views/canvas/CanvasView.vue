<template>
  <div class="wholeCanvas">
    <NavBar
      @changeNavbar="changeNavbar"
      @quizStart="quizStart"
      v-show="this.ending !== 1 && this.isBattle === 0"
    />
    <LoadingPage
      v-if="this.isGameStart === 0"
      @gameStart="gameStart"
      :isloading="this.isloading"
    />
    <WorldCanvas
      v-show="this.nowPage === 0 && this.isBattle === 0"
      @changeCanvas="changeCanvas"
      @changeBattle="changeBattle"
      @talkStart="talkStart"
      @loadingEnd="loadingEnd"
      :isGameStart="this.isGameStart"
      :nowPage="this.nowPage"
      :nowNavbar="this.nowNavbar"
      :ending="ending"
      @startEndingCredits="startEndingCredits"
    />
    <HouseCanvas
      v-show="this.nowPage === 1"
      @changeCanvas="changeCanvas"
      :nowPage="this.nowPage"
      :nowNavbar="this.nowNavbar"
      :ending="ending"
      @startEndingCredits="startEndingCredits"
    />
    <BattleCanvas
      v-show="this.isBattle === 1"
      @changeBattle="changeBattle"
      @startBattle="startBattle"
      :nowPage="this.nowPage"
      :nowNavbar="this.nowNavbar"
      :startSignal="this.startSignal"
    />
    <EndingCredits
      v-show="this.ending === 1"
      @startEndingCredits="startEndingCredits"
      :ending="ending"
    />
  </div>
</template>

<script>
import WorldCanvas from '../../components/canvas/WorldCanvas.vue'
import HouseCanvas from '../../components/canvas/HouseCanvas.vue'
import NavBar from '../../components/main/NavBar.vue'
import LoadingPage from '../../components/main/LoadingPage.vue'
import BattleCanvas from '../../components/battle/BattleCanvas.vue'

import EndingCredits from '../../components/ending/EndingCredits.vue'

export default {
  data() {
    return {
      nowPage: 0,
      isBattle: 0,
      nowNavbar: false,
      startSignal: 0,
      isloading: 0,
      isGameStart: 0,
      audio: new Audio('audio/oursound.mp3'),
      quizAudio: new Audio('audio/future.mp3'),
      ending: 0
    }
  },
  components: {
    WorldCanvas: WorldCanvas,
    HouseCanvas: HouseCanvas,
    NavBar: NavBar,
    BattleCanvas: BattleCanvas,
    LoadingPage: LoadingPage,

    EndingCredits: EndingCredits
  },
  mounted() {
    this.audio.loop = true
    this.audio.volume = 0.9
    this.audio.play()
  },
  beforeUnmount() {
    this.audio.pause()
  },
  methods: {
    changeCanvas() {
      if (this.nowPage === 0) {
        this.nowPage = 1
      } else {
        this.nowPage = 0
      }
    },

    changeBattle() {
      if (this.nowPage === 0 && this.isBattle === 0) {
        this.isBattle = 1
        this.startBattle()
      } else {
        this.isBattle = 0
        this.audio.play()
      }
    },

    startBattle() {
      this.startSignal += 1
      this.audio.pause()
    },

    changeNavbar() {
      if (this.nowNavbar === true) {
        this.nowNavbar = false
      } else {
        this.nowNavbar = true
      }
    },

    loadingEnd() {
      setTimeout(() => {
        this.isloading = 1
      }, 5000)
    },

    gameStart() {
      this.isGameStart = 1
    },

    quizStart(isOpen) {
      if (isOpen) {
        this.audio.pause()
        this.quizAudio.loop = true
        this.quizAudio.play()
      } else {
        this.quizAudio.pause()
        this.audio.play()
      }
    },

    startEndingCredits() {
      this.ending += 1
    }
  }
}
</script>

<style>
* {
  outline: none;
  margin: 0;
}
.wholeCanvas {
  width: 100vw;
  height: 100vh;
  overflow: hidden;
}

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
</style>
