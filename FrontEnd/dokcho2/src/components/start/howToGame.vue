<template>
  <div class="howTo__container">
    <div class="howTo">
      <div class="subTitle">이동 및 상호작용 🏃‍♂️</div>
      <div class="content">
        - <b>W,A,S,D 또는 방향키</b>를 이용하여 캐릭터를 움직일 수 있어요.
        <br />
        - 인물 또는 빌런들에게 <b>마우스 좌클릭</b>을 통해 말을 걸 수 있어요.
        <br />
        - O 또는 P 키를 누르면 춤을 출 수 있어요.
      </div>
      <div class="subTitle">스토리 진행 📖</div>
      <div class="content">
        - 호랑이를 찾으러 가는 여정 사이에 인물들에게 도움을 주어 <br />
        &nbsp; &nbsp; 호랑이를 퇴치하는데 도움이 되는 물건들을 모아야 해요.
        <br />
        - 호랑이 부하들로부터 <b>퀴즈 문제와 대결</b>을 진행해야 해요. <br />
        - 중간 중간 유적지에서
        <b>미니게임을 통해 배틀 아이템을 획득</b>해보세요.
      </div>
      <div class="subTitle">대결 ⚔️</div>
      <div class="content">
        - 빌런들과 내 풀깨비가 배틀을 벌일 수 있어요. <br />
        - 보유한 배틀 아이템을 적절히 사용하여 빌런들을 무찔러봐요. <br />
        - 아이템의 효과는 아이템에 마우스를 가져다 대면 확인할 수 있어요.
      </div>
      <div class="subTitle">멀티 게임 🎮</div>
      <div class="content">
        - 메뉴에 있는 멀티 게임을 통해 다른 사람들과 역사 퀴즈를 함께 풀 수
        있어요.
      </div>

      <img
        class="exit__btn"
        @click="$emit('howtoGame')"
        src="@/assets/navbar/ExitButton.png"
        alt=""
      />
    </div>
  </div>
</template>

<script>
export default {}
</script>

<style scoped>
button {
  cursor: url('@/assets/selector.cur'), pointer;
}
.howTo__container {
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

.howTo {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  position: fixed;
  z-index: 40;
  top: 4%;
  left: 25%;
  width: 40%;
  height: 90%;
  background: rgba(225, 225, 225, 0.8);
  background-size: cover;
  background-repeat: no-repeat;
  background-position: center;
  backdrop-filter: blur(4px);
  border-radius: 20px;
  overflow: visible;
  padding: 0 5vw;
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

.title {
  font-weight: bolder;
  font-size: 2rem;
  margin-bottom: 4vh;
  margin-top: 0;
}

.subTitle {
  width: 100%;
  font-weight: bold;
  font-size: 1.5rem;
  margin-bottom: 3vh;
}
.content {
  width: 100%;
  margin-bottom: 3vh;
  font-size: 1.25rem;
  line-height: 1.6rem;
}
</style>
