<template>
  <div>
    <div id="enemyStatus">
      <div class="status__header">
        <p>{{ enemyName }}</p>
      </div>

      <div class="Hp__container" id="enemyMaxHp">
        <div class="Hp__title">
          <img src="@/assets/battle/hp.png" />
        </div>

        <div class="Hp__blood__enemy" id="enemyHp"></div>
      </div>

      <div class="status__footer">
        <p>{{ enemyHp }} / {{ enemyMaxHp }}</p>
      </div>
    </div>
    <div id="myStatus">
      <div class="mystatus__header">
        <p>{{ myName }}</p>
        <p>Lv.{{ myLevel }}</p>
      </div>

      <div class="Hp__container" id="myMaxHp">
        <div class="Hp__title">
          <img src="@/assets/battle/hp.png" />
        </div>

        <div class="Hp__blood__mine" id="myHp"></div>
      </div>

      <div class="status__footer">
        <p>{{ myHp }} / {{ myMaxHp }}</p>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      time: 0,

      getEnemyHpBar: 100,
      getmyHpBar: 100
    }
  },

  props: {
    myHpBar: String,
    enemyHpBar: String,
    begin: Number,
    poison: Boolean,
    myMaxHp: Number,
    myHp: Number,
    enemyMaxHp: Number,
    enemyHp: Number,
    myName: String,
    enemyName: String,
    myLevel: Number
  },

  created() {
    setInterval(() => {
      this.time += 1
    }, 30)
  },

  watch: {
    time() {
      if (this.enemyHpBar < this.getEnemyHpBar) {
        this.getEnemyHpBar -= 1
      } else if (this.enemyHpBar > this.getEnemyHpBar) {
        this.getEnemyHpBar += 1
      }
      var enemyHpBarPer = this.getEnemyHpBar.toString() + '%'
      document.querySelector('#enemyHp').style.width = enemyHpBarPer

      if (this.myHpBar < this.getmyHpBar) {
        this.getmyHpBar -= 1
      } else if (this.myHpBar > this.getmyHpBar) {
        this.getmyHpBar += 1
      }
      var myHpBarPer = this.getmyHpBar.toString() + '%'
      document.querySelector('#myHp').style.width = myHpBarPer
    },

    begin() {
      this.getEnemyHpBar = 100
      this.getmyHpBar = 100

      document.querySelector('#enemyHp').style.width = this.getEnemyHpBar
      document.querySelector('#myHp').style.width = this.getmyHpBar
    },

    poison() {
      if (this.poison == true) {
        document.querySelector('#enemyHp').style.backgroundColor = 'purple'
      } else {
        document.querySelector('#enemyHp').style.backgroundColor = 'green'
      }
    }
  },

  methods: {}
}
</script>

<style scoped>
#enemyStatus {
  position: absolute;
  top: 10%;
  left: 17%;

  padding: 1vw;
  width: 22%;
  height: 11%;
  background-color: rgb(248, 242, 234);
  z-index: 1;
  box-shadow: inset 0 3px 6px rgba(0, 0, 0, 0.16), 0 4px 6px rgba(0, 0, 0, 0.45);
  border-radius: 15px;
}

#myStatus {
  position: absolute;
  top: 45%;
  left: 60%;

  padding: 1vw;
  width: 22%;
  height: 11%;
  background-color: rgb(248, 242, 234);
  z-index: 1;
  box-shadow: inset 0 3px 6px rgba(0, 0, 0, 0.16), 0 4px 6px rgba(0, 0, 0, 0.45);
  border-radius: 15px;
}

.status__header {
  margin-left: 2vh;
  margin-bottom: 1vh;
  font-size: 1.1rem;
  font-weight: bold;
}

.mystatus__header {
  margin-left: 2vh;
  margin-right: 3vh;
  margin-bottom: 1vh;
  font-size: 1.1rem;
  font-weight: bold;
  display: flex;
  justify-content: space-between;
}

.Hp__container {
  height: 30%;
  width: 95%;
  border-style: ridge;
  background-color: gray;
  margin: auto;
  display: flex;
  flex-direction: row;
  border-radius: 10px;
  margin-bottom: 1vh;
  box-shadow: 0 3px 6px rgba(0, 0, 0, 0.16), 0 3px 6px rgba(0, 0, 0, 0.23),
    0 0 5px #aaa inset;
}

.Hp__title {
  display: inline-block;
  width: 10%;
  background-color: rgba(0, 0, 0, 0.656);
  height: 100%;
  margin: 0%;
  padding-left: 1vh;
  border-top-left-radius: 7px;
  border-bottom-left-radius: 7px;
  align-items: center;
}

.Hp__title img {
  width: 70%;
  margin-top: 0.5vh;
  margin-left: 0.5vh;
}

.Hp__blood__enemy {
  height: 100%;
  width: 80%;
  border-top-right-radius: 7px;
  border-bottom-right-radius: 7px;
  background: linear-gradient(19deg, #ffdf5f, #e95fff);
  background-size: 200% 200%;
  animation: gradientblood 4s ease infinite;
}

.Hp__blood__mine {
  height: 100%;
  width: 80%;
  border-top-right-radius: 7px;
  border-bottom-right-radius: 7px;
  background: linear-gradient(271deg, #fedf54, #6bfa8d);
  background-size: 200% 200%;
  animation: gradientblood 4s ease infinite;
}

.status__footer {
  display: flex;
  justify-content: flex-end;
  margin-right: 1vw;
  font-size: 0.8rem;
}

@keyframes gradientblood {
  0% {
    background-position: 0% 50%;
  }
  50% {
    background-position: 100% 50%;
  }
  100% {
    background-position: 0% 50%;
  }
}
</style>
