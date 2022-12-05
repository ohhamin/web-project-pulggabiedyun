<template>
  <div class="myMap__container">
    <div class="myMap">
      <div class="myMap__header">
        <p>미니맵</p>
      </div>
      <div class="myMapImage__box">
        <div class="myMapImage">
          <div
            class="myCharacter"
            :style="
              'left:' + nowPosition.x + '%;' + 'top:' + nowPosition.z + '%;'
            "
          ></div>
        </div>
      </div>
      <img
        class="exit__btn"
        @click="closeModal3()"
        src="@/assets/navbar/ExitButton.png"
        alt=""
      />
    </div>
  </div>
</template>

<script>
export default {
  setup(props, { emit }) {
    function closeModal3() {
      emit('closeModal3')
    }

    let nowPosition = { x: 0, z: 0 }
    if (localStorage.getItem('position')) {
      nowPosition = JSON.parse(localStorage.getItem('position'))
      nowPosition.x = (nowPosition.x + 75) / 1.5 - 1.5
      nowPosition.z = (nowPosition.z + 75) / 1.5 - 1.5
    }
    return {
      closeModal3,
      nowPosition
    }
  }
}
</script>

<style scoped>
.myMap__container {
  width: 100%;
  height: 100%;
  z-index: 30;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  position: fixed;
}

.myMap__header {
  font-weight: bolder;
  font-size: 2rem;
  margin-bottom: 3vh;
  margin-top: 0;
}

.myMap {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  position: fixed;
  z-index: 40;
  top: 5%;
  left: 25%;
  width: 50%;
  height: 90%;
  background: rgba(169, 170, 150, 0.714);
  background-size: cover;
  background-repeat: no-repeat;
  background-position: center;
  backdrop-filter: blur(4px);
  border-radius: 20px;
  overflow: visible;
}

.myMapImage__box {
  display: flex;
  justify-content: center;
  align-items: center;
  border: 1px solid white;
  background-color: #ffffff;
  width: 34vw;
  height: 34vw;
  top: 30%;
  padding: 1vh;
  border-radius: 10px;
  box-shadow: 1px 1px 5px 1px gray;
}

.myMapImage {
  width: 90%;
  height: 90%;
  background-image: url('@/assets/minimap.png');
  background-size: cover;
  background-repeat: no-repeat;
  background-position: center;
}

.myCharacter {
  width: 3%;
  height: 3%;
  position: relative;
  background-color: red;
}

.exit__btn {
  width: 4vw;
  display: flex;
  justify-content: center;
  align-items: center;
  position: absolute;
  top: 4vh;
  right: 4vw;
  cursor: url('@/assets/selector.cur'), pointer;
}
</style>
