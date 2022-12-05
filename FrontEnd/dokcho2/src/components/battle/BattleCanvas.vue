<template>
  <div class="battlecanvas">
    <loading-page v-show="isloading === true"></loading-page>

    <battle-status
      id="status"
      :myHpBar="myHpBar"
      :enemyHpBar="enemyHpBar"
      :begin="begin"
      :poison="poison"
      :myMaxHp="myMaxHp"
      :myHp="myHp"
      :enemyMaxHp="enemyMaxHp"
      :enemyHp="enemyHp"
      :myName="myName"
      :enemyName="enemyName"
      :myLevel="myLevel"
    ></battle-status>

    <div id="battleDiv"><div id="battle"></div></div>

    <div id="consoleDiv">
      <div class="consoleBox">
        <div id="console" @click="changePhase()" v-show="phase === 'start'">
          <p>{{ msg }}</p>
          <font-awesome-icon
            class="triangle triangle--top"
            icon="fa-solid fa-caret-down"
            style="font-size: 50px"
          />
        </div>

        <div id="console" @click="changePhase()" v-show="phase === 'ready'">
          <p>{{ msg }}</p>
          <font-awesome-icon
            class="triangle triangle--top"
            icon="fa-solid fa-caret-down"
            style="font-size: 50px"
          />
        </div>

        <div id="console" v-show="phase === 'selectAct'">
          <div class="btn__console">
            <div class="select__btn" v-for="(act, idx) in actList" :key="idx">
              <button @click="doSelectAct(act)">
                {{ act }}
              </button>
            </div>
          </div>
        </div>

        <div id="console" v-show="phase === 'showAct'">
          <p>{{ msg }}</p>
        </div>

        <div id="console" v-show="phase === 'actResult'">
          <p>{{ msg }}</p>
        </div>

        <div id="console" v-show="phase === 'showEnemyAct'">
          <p>{{ msg }}</p>
        </div>

        <div id="console" v-show="phase === 'enemyActResult'">
          <p>{{ msg }}</p>
        </div>

        <div id="console" v-show="phase === 'noHappen'">
          <p>{{ msg }}</p>
        </div>

        <div id="console" v-show="phase === 'end'">
          <p>{{ msg }}</p>
        </div>

        <div class="item__console" id="console" v-show="phase === 'selectItem'">
          <div class="item__body">
            <div class="item__item" v-for="(item, idx) in itemList" :key="idx">
              <div @click="doSelectItem(item)" class="tooltip">
                <img
                  :src="require('@/assets/item/' + item.itemId + '.png')"
                  alt=""
                  class="item__image"
                />
                <p>{{ item.itemName }}X{{ item.count }}</p>

                <span class="tooltiptext">{{ item.description }}</span>
              </div>
            </div>
          </div>

          <div>
            <button class="item__btn" @click="itemToSelect()">뒤로</button>
          </div>
        </div>

        <div
          id="console"
          v-show="phase === 'itemResult'"
          @click="changePhase()"
        >
          <p>{{ msg }}</p>
          <font-awesome-icon
            class="triangle triangle--top"
            icon="fa-solid fa-caret-down"
            style="font-size: 50px"
          />
        </div>

        <div id="console" v-show="phase === 'sacrifice'">
          <p>{{ msg }}</p>
        </div>

        <div id="console" v-show="phase === 'poison'" @click="changePhase()">
          <p>{{ msg }}</p>
          <font-awesome-icon
            class="triangle triangle--top"
            icon="fa-solid fa-caret-down"
            style="font-size: 50px"
          />
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import * as THREE from 'three'
import { GLTFLoader } from 'three/examples/jsm/loaders/GLTFLoader'

import { Monster } from '../modules/Monster'
import { Boss } from '../modules/Boss'
import { AnimationBoss } from '../modules/AnimationBoss'

import * as CANNON from 'cannon-es'

import BattleStatus from './BattleStatus.vue'
import { ref, watch } from 'vue'
import LoadingPage from './LoadingPage.vue'

import { CreateText } from '../modules/CreateText'

import { BASE_URL } from '@/constant/BASE_URL'
import axios from 'axios'
import Swal from 'sweetalert2'

import JSConfetti from 'js-confetti'

const jsConfetti = new JSConfetti()

export default {
  name: 'BattleCanvas',

  props: {
    nowPage: Number,
    startSignal: Number
  },

  components: { BattleStatus, LoadingPage },

  setup(props, { emit }) {
    const Toast = Swal.mixin({
      toast: true,
      position: 'top',
      showConfirmButton: false,
      timer: 3000,
      timerProgressBar: true
    })

    const isloading = ref(true)

    const myHpBar = ref('100')
    const enemyHpBar = ref('100')
    const begin = ref(0)

    const phase = ref('start')

    const myName = ref('')
    const myMaxHp = ref(100)
    const myLevel = ref(1)
    const myHp = ref(100)
    const myAttack = ref(20)
    const myDefense = ref(10)

    const enemyName = ref('')
    const enemyMaxHp = ref(100)
    const enemyHp = ref(100)
    const enemyAttack = ref(20)
    const enemyDefense = ref(10)

    const msg = ref(`적과 만났다!`)

    const actList = ref(['공격', '아이템'])
    const myAct = ref('')
    const status = ref('대기')

    const enemyAct = ref('')
    const enemyStatus = ref('대기')

    const myDamage = ref(0)
    const enemyDamage = ref(0)

    const itemList = ref([])

    const useItem = ref('')

    const absoluteDefense = ref(false)
    const doubleAttack = ref(false)
    const sacrifice = ref(false)
    const survive = ref(false)
    const stun = ref(false)
    const blood = ref(false)
    const poison = ref(false)
    const poisonCnt = ref(0)
    const incapacitate = ref(false)

    const defensePlayerText = new CreateText({
      content: '방어',
      x: -0.5,
      y: -10,
      z: 3,
      color: '#adb5bd'
    })

    const defenseEnemyText = new CreateText({
      content: '방어',
      x: -0.125,
      y: -10,
      z: -0.75,
      color: '#adb5bd'
    })

    const buffEnemyText = new CreateText({
      content: '버프',
      x: -0.125,
      y: -20,
      z: -0.75,
      color: '#ffcc00'
    })

    const battleAudio = new Audio('audio/Kung-Fu Temple.mp3')
    battleAudio.loop = true
    battleAudio.volume = 0.7

    const attackAudio = new Audio('audio/punch.mp3')
    battleAudio.loop = false
    attackAudio.volume = 1

    const defenseAudio = new Audio('audio/moove.mp3')
    battleAudio.loop = false
    defenseAudio.volume = 1

    const attackBossAudio = new Audio('audio/punch.mp3')
    battleAudio.loop = false
    attackAudio.volume = 1

    const defenseBossAudio = new Audio('audio/moove.mp3')
    battleAudio.loop = false
    defenseAudio.volume = 1

    const winAudio = new Audio('audio/win.mp3')
    winAudio.loop = false
    winAudio.volume = 1

    const loseAudio = new Audio('audio/lose.mp3')
    loseAudio.loop = false
    loseAudio.volume = 1

    function startConfetti() {
      jsConfetti.addConfetti()
    }

    watch(
      () => props.startSignal,
      () => {
        setTimeout(() => {
          isloading.value = true

          setTimeout(() => {
            isloading.value = false
          }, 1000)

          battleAudio.load()
          battleAudio.play()

          const userInfo = ref(JSON.parse(localStorage.getItem('userInfo')))

          begin.value += 1
          getItemList()
          getMonsterInfo(userInfo.value.representMonster)
          getBossInfo(userInfo.value.nowMissionId)
          initValue()

          // Texture
          const textureLoader = new THREE.TextureLoader()
          const floorTexture = textureLoader.load('/images/map18.png')
          floorTexture.wrapS = THREE.RepeatWrapping
          floorTexture.wrapT = THREE.RepeatWrapping
          floorTexture.repeat.x = 1
          floorTexture.repeat.y = 1

          const battleDiv = document.querySelector('#battleDiv')
          const battle = document.querySelector('#battle')
          battle.remove()

          const newBattle = document.createElement('div')
          newBattle.id = 'battle'
          battleDiv.appendChild(newBattle)

          // Renderer
          let canvas = document.querySelector('#battle')

          const renderer = new THREE.WebGLRenderer({ antialias: true })
          renderer.setSize(window.innerWidth, window.innerHeight)
          renderer.setPixelRatio(window.devicePixelRatio > 1 ? 2 : 1)
          canvas.appendChild(renderer.domElement)
          renderer.shadowMap.enabled = true
          renderer.shadowMap.type = THREE.PCFSoftShadowMap

          // Scene
          const scene = new THREE.Scene()

          // Camera
          const camera = new THREE.PerspectiveCamera(
            60,
            window.innerWidth / window.innerHeight,
            1,
            1000
          )

          camera.position.set(2, 3, 4.5)
          camera.lookAt(-0.5, 0, 1.5)
          scene.add(camera)

          // Light
          const ambientLight = new THREE.AmbientLight('white', 0.7)
          scene.add(ambientLight)

          const directionalLight = new THREE.DirectionalLight('white', 0.5)
          const directionalLightOriginPosition = new THREE.Vector3(1, 1, 1)
          directionalLight.position.x = directionalLightOriginPosition.x
          directionalLight.position.y = directionalLightOriginPosition.y
          directionalLight.position.z = directionalLightOriginPosition.z
          directionalLight.castShadow = true

          // mapSize 세팅으로 그림자 퀄리티 설정
          directionalLight.shadow.mapSize.width = 2048
          directionalLight.shadow.mapSize.height = 2048
          // 그림자 범위
          directionalLight.shadow.camera.left = -100
          directionalLight.shadow.camera.right = 100
          directionalLight.shadow.camera.top = 100
          directionalLight.shadow.camera.bottom = -100
          directionalLight.shadow.camera.near = -100
          directionalLight.shadow.camera.far = 100
          scene.add(directionalLight)

          // Cannon(물리 엔진)
          const cannonWorld = new CANNON.World()
          cannonWorld.gravity.set(0, -10, 0)

          const floorShape = new CANNON.Plane()
          const floorBody = new CANNON.Body({
            mass: 0,
            position: new CANNON.Vec3(0, 0, 0),
            shape: floorShape
          })
          floorBody.quaternion.setFromAxisAngle(
            new CANNON.Vec3(-1, 0, 0),
            Math.PI / 2
          )
          cannonWorld.addBody(floorBody)

          // Mesh
          const meshes = []
          const floorMesh = new THREE.Mesh(
            new THREE.PlaneGeometry(50, 50),
            new THREE.MeshStandardMaterial({
              map: floorTexture
            })
          )
          floorMesh.name = 'floor'
          floorMesh.rotation.x = -Math.PI / 2
          floorMesh.receiveShadow = true
          scene.add(floorMesh)
          meshes.push(floorMesh)

          const gltfLoader = new GLTFLoader()

          const player = new Monster({
            scene,
            meshes,
            cannonWorld,
            gltfLoader,
            modelSrc: `/models/${userInfo.value.representMonster}.glb`,
            x: -0.5,
            y: 2,
            z: 3
          })

          const villain = [
            '지현몬',
            '효근몬',
            '재준몬',
            '근희몬',
            '상균몬',
            '지원몬',
            '하민몬',
            '성빈몬'
          ]

          const noMotion = [0, 1, 4]

          let enemy

          if (noMotion.includes(userInfo.value.nowMissionId - 1)) {
            enemy = new Boss({
              scene,
              meshes,
              cannonWorld,
              gltfLoader,
              modelSrc: `/models/Villain/${
                villain[userInfo.value.nowMissionId - 1]
              }.glb`,
              x: -0.125,
              y: 2,
              z: -0.75
            })
          } else {
            enemy = new AnimationBoss({
              scene,
              meshes,
              cannonWorld,
              gltfLoader,
              modelSrc: `/battle/${
                villain[userInfo.value.nowMissionId - 1]
              }.glb`,
              x: -0.125,
              y: 2,
              z: -0.75
            })
          }

          scene.add(defensePlayerText.modelMesh)
          scene.add(defenseEnemyText.modelMesh)
          scene.add(buffEnemyText.modelMesh)

          // 그리기
          const clock = new THREE.Clock()

          function draw() {
            const delta = clock.getDelta()

            let cannonStepTime = 1 / 60
            if (delta < 0.01) cannonStepTime = 1 / 120
            cannonWorld.step(cannonStepTime, delta, 3)

            if (player.modelMesh) {
              player.modelMesh.position.copy(player.cannonBody.position)
              player.modelMesh.quaternion.copy(player.cannonBody.quaternion)
              player.modelMesh.lookAt(-0.5, 0, -3)

              if (status.value == '공격') {
                player.cannonBody.position.y += 0.06

                if (player.cannonBody.position.y >= 0.6) {
                  status.value = '대기'
                  attackAudio.play()
                }
              }

              if (status.value == '방어') {
                defensePlayerText.modelMesh.position.y =
                  player.modelMesh.position.y + 1
                status.value = '대기'
                defenseAudio.play()
              }

              if (defensePlayerText.modelMesh) {
                if (defensePlayerText.modelMesh.position.y >= 1) {
                  defensePlayerText.modelMesh.position.y += 0.0005
                  if (
                    defensePlayerText.modelMesh.position.y >=
                    player.modelMesh.position.y + 1.05
                  ) {
                    defensePlayerText.modelMesh.position.y = -10
                  }
                }
              }

              if (status.value == 'lose') {
                scene.remove(defensePlayerText.modelMesh)
                scene.remove(defenseEnemyText.modelMesh)
                scene.remove(buffEnemyText.modelMesh)

                player.cannonBody.position.x -= 0.1
                player.cannonBody.position.y += 0.1
                player.cannonBody.position.z += 0.1

                if (player.cannonBody.position.y >= 40) {
                  status.value = '대기'
                }
              }
            }

            if (enemy.modelMesh) {
              enemy.modelMesh.position.copy(enemy.cannonBody.position)
              enemy.modelMesh.quaternion.copy(enemy.cannonBody.quaternion)
              enemy.modelMesh.lookAt(-0.5, 0, 3)

              if (noMotion.includes(userInfo.value.nowMissionId - 1)) {
                if (enemyStatus.value == '공격') {
                  if (noMotion.includes(userInfo.value.nowMissionId - 1)) {
                    enemy.cannonBody.position.y += 0.06

                    if (enemy.cannonBody.position.y >= 0.6) {
                      attackAudio.play()

                      enemyStatus.value = '대기'
                    }
                  }
                }

                if (enemyStatus.value == '방어') {
                  defenseEnemyText.modelMesh.position.y =
                    enemy.modelMesh.position.y + 1.48
                  enemyStatus.value = '대기'
                  defenseAudio.play()
                }

                if (defenseEnemyText.modelMesh) {
                  if (defenseEnemyText.modelMesh.position.y >= 1) {
                    defenseEnemyText.modelMesh.position.y += 0.0005
                    if (
                      defenseEnemyText.modelMesh.position.y >=
                      enemy.modelMesh.position.y + 1.53
                    ) {
                      defenseEnemyText.modelMesh.position.y = -10
                    }
                  }
                }

                if (enemyStatus.value == '버프') {
                  buffEnemyText.modelMesh.position.y =
                    enemy.modelMesh.position.y + 1.48
                  enemyStatus.value = '대기'
                  defenseAudio.play()
                }

                if (buffEnemyText.modelMesh) {
                  if (buffEnemyText.modelMesh.position.y >= 1) {
                    buffEnemyText.modelMesh.position.y += 0.0005
                    if (
                      buffEnemyText.modelMesh.position.y >=
                      enemy.modelMesh.position.y + 1.53
                    ) {
                      buffEnemyText.modelMesh.position.y = -20
                    }
                  }
                }
              } else {
                if (enemyStatus.value == '대기') {
                  enemy.actions[0].stop()
                  enemy.actions[1].stop()
                  enemy.actions[2].play()
                } else if (enemyStatus.value == '공격') {
                  enemy.actions[2].stop()
                  enemy.actions[0].play()
                  attackBossAudio.play()

                  setTimeout(() => {
                    attackBossAudio.pause()
                  }, 1000)

                  setTimeout(() => {
                    enemyStatus.value = '대기'
                  }, 1800)
                } else {
                  enemy.actions[2].stop()
                  enemy.actions[1].play()
                  defenseBossAudio.play()

                  setTimeout(() => {
                    defenseBossAudio.pause()
                  }, 1000)

                  setTimeout(() => {
                    enemyStatus.value = '대기'
                  }, 1800)
                }
              }

              if (status.value == 'win') {
                scene.remove(defensePlayerText.modelMesh)
                scene.remove(defenseEnemyText.modelMesh)
                scene.remove(buffEnemyText.modelMesh)

                enemy.cannonBody.position.x += 0.1
                enemy.cannonBody.position.y += 0.1
                enemy.cannonBody.position.z -= 0.1

                if (enemy.cannonBody.position.y >= 40) {
                  enemyStatus.value = '대기'
                }
              }
            }

            if (player.mixer) player.mixer.update(delta)
            if (enemy.mixer) enemy.mixer.update(delta)

            renderer.render(scene, camera)
            renderer.setAnimationLoop(draw)
          }

          function setSize() {
            camera.left = -(window.innerWidth / window.innerHeight)
            camera.right = window.innerWidth / window.innerHeight
            camera.top = 1
            camera.bottom = -1

            camera.updateProjectionMatrix()
            renderer.setSize(window.innerWidth, window.innerHeight)
            renderer.render(scene, camera)
          }

          // 이벤트
          window.addEventListener('resize', setSize)
          draw()
        }, 100)
      }
    )

    function changePhase() {
      if (phase.value == 'start') {
        setTimeout(() => {
          msg.value = '행동을 선택해주세요...'
          phase.value = 'ready'
        }, 100)
      }
      if (phase.value == 'ready') {
        phase.value = 'selectAct'
      }
      if (phase.value == 'itemResult') {
        phase.value = 'selectAct'
      }
      if (phase.value == 'poison') {
        phase.value = 'selectAct'
      }
    }

    function doSelectAct(item) {
      status.value = item
      myAct.value = item

      enemySelectAct()

      if (myAct.value == '공격') {
        enemyDamage.value = enemyAttack.value - myDefense.value

        if (enemyDamage.value < 0) {
          enemyDamage.value = 0
        }

        msg.value = item + '을 선택했습니다.'
        phase.value = 'showAct'

        if (enemyAct.value == '방어') {
          myDamage.value = myAttack.value - enemyDefense.value

          if (myDamage.value < 0) {
            myDamage.value = 0
          }

          setTimeout(() => {
            if (stun.value == true) {
              stun.value = false

              setTimeout(() => {
                if (poison.value == true) {
                  poisonCnt.value += 1

                  if (poisonCnt.value > 10) {
                    poisonCnt.value = 0
                    poison.value = false
                    msg.value = `${enemyName.value}이(가) 독으로부터 회복되었다!`

                    phase.value = 'ready'
                  } else {
                    phase.value = 'poison'
                    enemyHp.value -= 10
                    enemyHpBar.value = Math.round(
                      (enemyHp.value / enemyMaxHp.value) * 100
                    )

                    msg.value = '독으로 인하여 10의 피해를 주었다!'

                    if (enemyHp.value <= 0) {
                      battleAudio.pause()
                      winAudio.play()
                      startConfetti()

                      phase.value = 'end'
                      status.value = 'win'

                      winBattle()

                      Toast.fire({
                        icon: 'success',
                        title: '배틀에서 이겨 유물을 획득했어요 🏆',
                        text: '유물을 돌려드리러 가요!'
                      })

                      setTimeout(() => {
                        winAudio.pause()
                        emit('changeBattle')
                        jsConfetti.clearCanvas()
                        isloading.value = true
                      }, 3000)
                    }
                  }
                } else {
                  phase.value = 'ready'
                }
              }, 800)
            } else {
              enemyShowAct()
            }
          }, 1000)
        } else {
          myDamage.value = myAttack.value - enemyDefense.value

          if (myDamage.value < 0) {
            myDamage.value = 0
          }

          setTimeout(() => {
            showActResult()
          }, 1000)
        }
      } else if (myAct.value == '방어') {
        enemyDamage.value = enemyAttack.value - myDefense.value
        if (enemyDamage.value < 0) {
          enemyDamage.value = 0
        }

        msg.value = item + '를 선택했습니다.'
        phase.value = 'showAct'

        setTimeout(() => {
          if (stun.value == true) {
            stun.value = false
            setTimeout(() => {
              if (poison.value == true) {
                poisonCnt.value += 1

                if (poisonCnt.value > 10) {
                  poisonCnt.value = 0
                  poison.value = false
                  msg.value = `${enemyName.value}이(가) 독으로부터 회복되었다!`

                  phase.value = 'ready'
                } else {
                  phase.value = 'poison'
                  enemyHp.value -= 10
                  enemyHpBar.value = Math.round(
                    (enemyHp.value / enemyMaxHp.value) * 100
                  )

                  msg.value = '독으로 인하여 10의 피해를 주었다!'

                  if (enemyHp.value <= 0) {
                    phase.value = 'end'
                    status.value = 'win'

                    winBattle()
                  }
                }
              } else {
                phase.value = 'ready'
              }
            }, 800)
          } else {
            enemyShowAct()
          }
        }, 1000)
      } else if (myAct.value == '아이템') {
        phase.value = 'selectItem'
      }
    }

    function enemySelectAct() {
      const num = Math.floor(Math.random() * 10)

      if (num <= 8) {
        enemyAct.value = '공격'
      } else {
        enemyAct.value = '버프'
      }
    }

    function showActResult() {
      if (myAct.value == '공격') {
        if (enemyAct.value == '방어') {
          msg.value =
            `${enemyName.value}의 방어태세로 ` +
            myDamage.value.toString() +
            '의 피해를 주었다!'
          phase.value = 'actResult'

          if (enemyHp.value - myDamage.value < 0) {
            enemyHp.value = 0
          } else {
            enemyHp.value -= myDamage.value
          }

          enemyHpBar.value = Math.round(
            (enemyHp.value / enemyMaxHp.value) * 100
          )

          if (blood.value == true) {
            blood.value = false

            const bloodSuck = Math.round(myDamage.value * 0.3)
            if (myHp.value + bloodSuck > myMaxHp.value) {
              myHp.value = myMaxHp.value
            } else {
              myHp.value += bloodSuck
            }

            myHpBar.value = Math.round((myHp.value / myMaxHp.value) * 100)
          }

          if (doubleAttack.value == true) {
            setTimeout(() => {
              if (enemyHp.value <= 0) {
                battleAudio.pause()
                winAudio.play()
                startConfetti()

                phase.value = 'end'
                status.value = 'win'
                winBattle()

                Toast.fire({
                  icon: 'success',
                  title: '배틀에서 이겨 유물을 획득했어요 🏆',
                  text: '유물을 돌려드리러 가요!'
                })

                setTimeout(() => {
                  winAudio.pause()
                  emit('changeBattle')
                  jsConfetti.clearCanvas()
                  isloading.value = true
                }, 3000)
              } else {
                status.value = '공격'
                doubleAttack.value = false
                setTimeout(() => {
                  showActResult()
                }, 1000)
              }
            }, 1200)
          } else {
            setTimeout(() => {
              if (enemyHp.value <= 0) {
                battleAudio.pause()
                winAudio.play()
                startConfetti()
                phase.value = 'end'
                status.value = 'win'
                winBattle()

                Toast.fire({
                  icon: 'success',
                  title: '배틀에서 이겨 유물을 획득했어요 🏆',
                  text: '유물을 돌려드리러 가요!'
                })

                setTimeout(() => {
                  winAudio.pause()
                  emit('changeBattle')
                  jsConfetti.clearCanvas()
                  isloading.value = true
                }, 3000)
              } else {
                phase.value = 'ready'
              }
            }, 1000)
          }
        } else {
          msg.value = myDamage.value.toString() + '의 피해를 주었다!'
          phase.value = 'actResult'

          if (enemyHp.value - myDamage.value < 0) {
            enemyHp.value = 0
          } else {
            enemyHp.value -= myDamage.value
          }

          enemyHpBar.value = Math.round(
            (enemyHp.value / enemyMaxHp.value) * 100
          )

          if (blood.value == true) {
            blood.value = false

            const bloodSuck = myDamage.value * 0.3
            if (myHp.value + bloodSuck > myMaxHp.value) {
              myHp.value = myMaxHp.value
            } else {
              myHp.value += bloodSuck
            }

            myHpBar.value = Math.round((myHp.value / myMaxHp.value) * 100)
          }

          if (doubleAttack.value == true) {
            setTimeout(() => {
              if (enemyHp.value <= 0) {
                battleAudio.pause()
                winAudio.play()
                startConfetti()

                phase.value = 'end'
                status.value = 'win'
                winBattle()

                Toast.fire({
                  icon: 'success',
                  title: '배틀에서 이겨 유물을 획득했어요 🏆',
                  text: '유물을 돌려드리러 가요!'
                })

                setTimeout(() => {
                  winAudio.pause()
                  emit('changeBattle')
                  jsConfetti.clearCanvas()
                  isloading.value = true
                }, 3000)
              } else {
                status.value = '공격'
                doubleAttack.value = false
                setTimeout(() => {
                  showActResult()
                }, 1000)
              }
            }, 1600)
          } else {
            setTimeout(() => {
              if (enemyHp.value <= 0) {
                battleAudio.pause()
                winAudio.play()
                startConfetti()

                phase.value = 'end'
                status.value = 'win'
                winBattle()

                Toast.fire({
                  icon: 'success',
                  title: '배틀에서 이겨 유물을 획득했어요 🏆',
                  text: '유물을 돌려드리러 가요!'
                })

                setTimeout(() => {
                  winAudio.pause()
                  emit('changeBattle')
                  jsConfetti.clearCanvas()
                  isloading.value = true
                }, 3000)
              } else {
                if (stun.value == true) {
                  stun.value = false
                  setTimeout(() => {
                    if (poison.value == true) {
                      poisonCnt.value += 1

                      if (poisonCnt.value > 10) {
                        poisonCnt.value = 0
                        poison.value = false
                        msg.value = `${enemyName.value}이(가) 독으로부터 회복되었다!`

                        phase.value = 'ready'
                      } else {
                        phase.value = 'poison'
                        enemyHp.value -= 10
                        enemyHpBar.value = Math.round(
                          (enemyHp.value / enemyMaxHp.value) * 100
                        )

                        msg.value = '독으로 인하여 10의 피해를 주었다!'

                        if (enemyHp.value <= 0) {
                          battleAudio.pause()
                          winAudio.play()
                          startConfetti()

                          phase.value = 'end'
                          status.value = 'win'
                          winBattle()

                          Toast.fire({
                            icon: 'success',
                            title: '배틀에서 이겨 유물을 획득했어요 🏆',
                            text: '유물을 돌려드리러 가요!'
                          })

                          setTimeout(() => {
                            winAudio.pause()
                            emit('changeBattle')
                            jsConfetti.clearCanvas()
                            isloading.value = true
                          }, 3000)
                        }
                      }
                    } else {
                      phase.value = 'ready'
                    }
                  }, 800)
                } else {
                  enemyShowAct()
                }
              }
            }, 1000)
          }
        }
      } else {
        if (enemyHp.value <= 0) {
          battleAudio.pause()
          winAudio.play()
          startConfetti()

          phase.value = 'end'
          status.value = 'win'
          winBattle()

          Toast.fire({
            icon: 'success',
            title: '배틀에서 이겨 유물을 획득했어요 🏆',
            text: '유물을 돌려드리러 가요!'
          })

          setTimeout(() => {
            winAudio.pause()
            emit('changeBattle')
            jsConfetti.clearCanvas()
            isloading.value = true
          }, 3000)
        }
      }
    }

    function enemyShowAct() {
      msg.value =
        `${enemyName.value}은(는) ` + enemyAct.value + '을(를) 선택했습니다.'
      enemyStatus.value = enemyAct.value
      phase.value = 'showEnemyAct'

      if (myAct.value == '방어' || sacrifice.value == true) {
        sacrifice.value = false
        if (enemyAct.value == '방어') {
          setTimeout(() => {
            phase.value = 'noHappen'
            msg.value = '아무일도 일어나지 않았다...'
            setTimeout(() => {
              if (poison.value == true) {
                poisonCnt.value += 1

                if (poisonCnt.value > 10) {
                  poisonCnt.value = 0
                  poison.value = false
                  msg.value = `${enemyName.value}이(가) 독으로부터 회복되었다!`

                  phase.value = 'ready'
                } else {
                  phase.value = 'poison'
                  enemyHp.value -= 10
                  enemyHpBar.value = Math.round(
                    (enemyHp.value / enemyMaxHp.value) * 100
                  )

                  msg.value = '독으로 인하여 10의 피해를 주었다!'

                  if (enemyHp.value <= 0) {
                    battleAudio.pause()
                    winAudio.play()
                    startConfetti()

                    phase.value = 'end'
                    status.value = 'win'
                    winBattle()

                    Toast.fire({
                      icon: 'success',
                      title: '배틀에서 이겨 유물을 획득했어요 🏆',
                      text: '유물을 돌려드리러 가요!'
                    })

                    setTimeout(() => {
                      winAudio.pause()
                      emit('changeBattle')
                      jsConfetti.clearCanvas()
                      isloading.value = true
                    }, 3000)
                  }
                }
              } else {
                phase.value = 'ready'
              }
            }, 800)
          }, 1000)
        } else {
          setTimeout(() => {
            enemyActResult()
          }, 800)
        }
      } else if (enemyAct.value == '방어') {
        setTimeout(() => {
          showActResult()
        }, 800)
      } else {
        setTimeout(() => {
          enemyActResult()
        }, 800)
      }
    }

    function enemyActResult() {
      if (survive.value == true) {
        survive.value = false

        if (enemyDamage.value >= myHp.value) {
          enemyDamage.value = myHp.value - 1
        }
      }

      if (enemyAct.value == '공격') {
        if (absoluteDefense.value == true) {
          msg.value = `${enemyName.value}의 공격이 무효화되었다!`
          phase.value = 'enemyActResult'

          absoluteDefense.value = false

          setTimeout(() => {
            if (poison.value == true) {
              poisonCnt.value += 1

              if (poisonCnt.value > 10) {
                poisonCnt.value = 0
                poison.value = false
                msg.value = `${enemyName.value}이(가) 독으로부터 회복되었다!`

                phase.value = 'ready'
              } else {
                phase.value = 'poison'
                enemyHp.value -= 10
                enemyHpBar.value = Math.round(
                  (enemyHp.value / enemyMaxHp.value) * 100
                )

                msg.value = '독으로 인하여 10의 피해를 주었다!'

                if (enemyHp.value <= 0) {
                  battleAudio.pause()
                  winAudio.play()
                  startConfetti()

                  phase.value = 'end'
                  status.value = 'win'
                  winBattle()

                  Toast.fire({
                    icon: 'success',
                    title: '배틀에서 이겨 유물을 획득했어요 🏆',
                    text: '유물을 돌려드리러 가요!'
                  })

                  setTimeout(() => {
                    winAudio.pause()
                    emit('changeBattle')
                    jsConfetti.clearCanvas()
                    isloading.value = true
                  }, 3000)
                }
              }
            } else {
              phase.value = 'ready'
            }
          }, 800)
        } else if (myAct.value == '방어') {
          if (incapacitate.value == true) {
            incapacitate.value = false
            if (enemyDamage.value < 30) {
              enemyDamage.value = 0
            } else {
              enemyDamage.value -= 30
            }
          }

          msg.value =
            `${myName.value}의 방어태세로 ` +
            enemyDamage.value.toString() +
            '의 피해를 받았다!'
          phase.value = 'enemyActResult'

          if (myHp.value - enemyDamage.value < 0) {
            myHp.value = 0
          } else {
            myHp.value -= enemyDamage.value
          }

          myHpBar.value = Math.round((myHp.value / myMaxHp.value) * 100)

          myAct.value = ''

          setTimeout(() => {
            if (myHp.value <= 0) {
              battleAudio.pause()
              loseAudio.play()

              phase.value = 'end'
              status.value = 'lose'
              msg.value = '배틀에서 졌다...'

              Toast.fire({
                icon: 'error',
                title: '유물을 획득하지 못했어요💦 다시 도전!'
              })

              setTimeout(() => {
                loseAudio.pause()
                emit('changeBattle')
                isloading.value = true
              }, 2000)
            } else if (poison.value == true) {
              poisonCnt.value += 1

              if (poisonCnt.value > 10) {
                poisonCnt.value = 0
                poison.value = false
                msg.value = `${enemyName.value}이(가) 독으로부터 회복되었다!`

                phase.value = 'ready'
              } else {
                phase.value = 'poison'
                enemyHp.value -= 10
                enemyHpBar.value = Math.round(
                  (enemyHp.value / enemyMaxHp.value) * 100
                )

                msg.value = '독으로 인하여 10의 피해를 주었다!'

                if (enemyHp.value <= 0) {
                  battleAudio.pause()
                  winAudio.play()
                  startConfetti()

                  phase.value = 'end'
                  status.value = 'win'
                  winBattle()

                  Toast.fire({
                    icon: 'success',
                    title: '배틀에서 이겨 유물을 획득했어요 🏆',
                    text: '유물을 돌려드리러 가요!'
                  })

                  setTimeout(() => {
                    winAudio.pause()
                    emit('changeBattle')
                    jsConfetti.clearCanvas()
                    isloading.value = true
                  }, 3000)
                }
              }
            } else {
              phase.value = 'ready'
            }
          }, 1000)
        } else {
          if (incapacitate.value == true) {
            incapacitate.value = false
            if (enemyDamage.value < 30) {
              enemyDamage.value = 0
            } else {
              enemyDamage.value -= 30
            }
          }

          msg.value = enemyDamage.value.toString() + '의 피해를 받았다!'
          phase.value = 'enemyActResult'

          if (myHp.value - enemyDamage.value < 0) {
            myHp.value = 0
          } else {
            myHp.value -= enemyDamage.value
          }

          myHpBar.value = Math.round((myHp.value / myMaxHp.value) * 100)

          setTimeout(() => {
            if (myHp.value <= 0) {
              battleAudio.pause()
              loseAudio.play()

              phase.value = 'end'
              status.value = 'lose'
              msg.value = '배틀에서 졌다...'

              Toast.fire({
                icon: 'error',
                title: '유물을 획득하지 못했어요💦 다시 도전!'
              })

              setTimeout(() => {
                loseAudio.pause()
                emit('changeBattle')
                isloading.value = true
              }, 2000)
            } else if (poison.value == true) {
              poisonCnt.value += 1

              if (poisonCnt.value > 10) {
                poisonCnt.value = 0
                poison.value = false
                msg.value = `${enemyName.value}이(가) 독으로부터 회복되었다!`

                phase.value = 'ready'
              } else {
                phase.value = 'poison'
                enemyHp.value -= 10
                enemyHpBar.value = Math.round(
                  (enemyHp.value / enemyMaxHp.value) * 100
                )

                msg.value = '독으로 인하여 10의 피해를 주었다!'

                if (enemyHp.value <= 0) {
                  battleAudio.pause()
                  winAudio.play()
                  startConfetti()

                  phase.value = 'end'
                  status.value = 'win'
                  winBattle()

                  Toast.fire({
                    icon: 'success',
                    title: '배틀에서 이겨 유물을 획득했어요 🏆',
                    text: '유물을 돌려드리러 가요!'
                  })

                  setTimeout(() => {
                    winAudio.pause()
                    emit('changeBattle')
                    jsConfetti.clearCanvas()
                    isloading.value = true
                  }, 3000)
                }
              }
            } else {
              phase.value = 'ready'
            }
          }, 1000)
        }
      } else if (enemyAct.value == '버프') {
        const buff = Math.round(enemyAttack.value * 0.5)
        enemyAttack.value += buff

        msg.value = buff.toString() + '만큼 공격력 상승!'
        phase.value = 'enemyActResult'

        setTimeout(() => {
          if (poison.value == true) {
            poisonCnt.value += 1

            if (poisonCnt.value > 10) {
              poisonCnt.value = 0
              poison.value = false
              msg.value = `${enemyName.value}이(가) 독으로부터 회복되었다!`

              phase.value = 'ready'
            } else {
              phase.value = 'poison'
              enemyHp.value -= 10
              enemyHpBar.value = Math.round(
                (enemyHp.value / enemyMaxHp.value) * 100
              )

              msg.value = '독으로 인하여 10의 피해를 주었다!'

              if (enemyHp.value <= 0) {
                battleAudio.pause()
                winAudio.play()
                startConfetti()

                phase.value = 'end'
                status.value = 'win'
                winBattle()

                Toast.fire({
                  icon: 'success',
                  title: '배틀에서 이겨 유물을 획득했어요 🏆',
                  text: '유물을 돌려드리러 가요!'
                })

                setTimeout(() => {
                  winAudio.pause()
                  emit('changeBattle')
                  jsConfetti.clearCanvas()
                  isloading.value = true
                }, 3000)
              }
            }
          } else {
            phase.value = 'ready'
          }
        }, 1000)
      }
    }

    function doSelectItem(item) {
      useItem.value = item.itemName
      deleteItem(item.itemId)

      if (useItem.value == '회복 물약(소)') {
        phase.value = 'itemResult'

        let heal = 50
        if (myHp.value + heal > myMaxHp.value) {
          heal = Math.round(myMaxHp.value - myHp.value)
        }

        msg.value = heal.toString() + ' 체력을 회복했다!'

        myHp.value += heal
        myHpBar.value = Math.round((myHp.value / myMaxHp.value) * 100)

        useItem.value = ''
      } else if (useItem.value == '회복 물약(중)') {
        phase.value = 'itemResult'

        let heal = 200
        if (myHp.value + heal > myMaxHp.value) {
          heal = Math.round(myMaxHp.value - myHp.value)
        }

        msg.value = heal.toString() + ' 체력을 회복했다!'

        myHp.value += heal
        myHpBar.value = Math.round((myHp.value / myMaxHp.value) * 100)

        useItem.value = ''
      } else if (useItem.value == '회복 물약(대)') {
        phase.value = 'itemResult'

        let heal = 500
        if (myHp.value + heal > myMaxHp.value) {
          heal = Math.round(myMaxHp.value - myHp.value)
        }

        msg.value = heal.toString() + ' 체력을 회복했다!'

        myHp.value += heal
        myHpBar.value = Math.round((myHp.value / myMaxHp.value) * 100)

        useItem.value = ''
      } else if (useItem.value == '공격 무효화') {
        phase.value = 'itemResult'
        absoluteDefense.value = true

        msg.value = '다음 적의 공격은 무효화된다!'
        useItem.value = ''
      } else if (useItem.value == '더블 어택') {
        phase.value = 'itemResult'
        doubleAttack.value = true

        msg.value = '다음 나의 공격은 2번 연속!'
        useItem.value = ''
      } else if (useItem.value == '공격력 증가') {
        phase.value = 'itemResult'
        const plusAttack = Math.round(myAttack.value * 0.2)
        myAttack.value += plusAttack

        msg.value = plusAttack.toString() + '만큼 공격력 증가!'
        useItem.value = ''
      } else if (useItem.value == '방어력 증가') {
        phase.value = 'itemResult'
        const plusDefense = Math.round(myDefense.value * 0.3)
        myDefense.value += plusDefense

        msg.value = plusDefense.toString() + '만큼 방어력 증가!'
        useItem.value = ''
      } else if (useItem.value == '희생') {
        phase.value = 'sacrifice'
        sacrifice.value = true
        status.value = '공격'

        setTimeout(() => {
          status.value = '공격'
        }, 1000)

        msg.value = '희생 주문을 발동했다!'
        useItem.value = ''

        const cost = Math.round(myHp.value * 0.8)
        myHp.value -= cost
        if (myHp.value <= 1) {
          myHp.value = 1
        }
        myHpBar.value = Math.round((myHp.value / myMaxHp.value) * 100)

        const instantDeath = Math.random(0, 1)

        setTimeout(() => {
          if (instantDeath <= 0.1) {
            msg.value = '성공!'
            sacrifice.value = false
            setTimeout(() => {
              enemyHp.value -= 999999
              enemyHpBar.value = Math.round(
                (enemyHp.value / enemyMaxHp.value) * 100
              )
              showActResult()
            }, 1600)
          } else {
            msg.value = '실패!'
            setTimeout(() => {
              enemyDamage.value = enemyAttack.value

              if (survive.value == true) {
                survive.value = false

                if (enemyDamage.value >= myHp.value) {
                  enemyDamage.value = myHp.value - 1
                }
              }

              if (stun.value == true) {
                stun.value = false
                setTimeout(() => {
                  if (poison.value == true) {
                    poisonCnt.value += 1

                    if (poisonCnt.value > 10) {
                      poisonCnt.value = 0
                      poison.value = false
                      msg.value = `${enemyName.value}이(가) 독으로부터 회복되었다!`

                      phase.value = 'ready'
                    } else {
                      phase.value = 'poison'
                      enemyHp.value -= 10
                      enemyHpBar.value = Math.round(
                        (enemyHp.value / enemyMaxHp.value) * 100
                      )

                      msg.value = '독으로 인하여 10의 피해를 주었다!'

                      if (enemyHp.value <= 0) {
                        battleAudio.pause()
                        winAudio.play()
                        startConfetti()

                        phase.value = 'end'
                        status.value = 'win'
                        winBattle()

                        Toast.fire({
                          icon: 'success',
                          title: '배틀에서 이겨 유물을 획득했어요 🏆',
                          text: '유물을 돌려드리러 가요!'
                        })

                        setTimeout(() => {
                          winAudio.pause()
                          emit('changeBattle')
                          jsConfetti.clearCanvas()
                          isloading.value = true
                        }, 3000)
                      }
                    }
                  } else {
                    phase.value = 'ready'
                  }
                }, 800)
              } else {
                enemyShowAct()
              }
            }, 1600)
          }
        }, 1400)
      } else if (useItem.value == '생존') {
        phase.value = 'itemResult'
        survive.value = true

        msg.value = '생존 아이템으로 인하여 죽지 않는다!'
        useItem.value = ''
      } else if (useItem.value == '기절') {
        phase.value = 'itemResult'
        stun.value = true

        msg.value = '상대방이 이번 턴에 행동하지 못한다!'
        useItem.value = ''
      } else if (useItem.value == '흡혈') {
        phase.value = 'itemResult'
        blood.value = true

        msg.value = '다음 공격 데미지의 30%만큼 체력 회복 한다!'
        useItem.value = ''
      } else if (useItem.value == '독극물') {
        enemyHp.value -= 30
        enemyHpBar.value = Math.round((enemyHp.value / enemyMaxHp.value) * 100)

        if (enemyHp.value <= 0) {
          battleAudio.pause()
          winAudio.play()
          startConfetti()

          phase.value = 'end'
          status.value = 'win'
          winBattle()

          Toast.fire({
            icon: 'success',
            title: '배틀에서 이겨 유물을 획득했어요 🏆',
            text: '유물을 돌려드리러 가요!'
          })

          setTimeout(() => {
            winAudio.pause()
            emit('changeBattle')
            jsConfetti.clearCanvas()
            isloading.value = true
          }, 3000)
        } else {
          phase.value = 'itemResult'
          poison.value = true
          poisonCnt.value = 0

          msg.value = '독!!!!!'
          useItem.value = ''
        }
      } else if (useItem.value == '무력화') {
        phase.value = 'itemResult'
        incapacitate.value = true

        msg.value = '다음 적의 데미지가 30만큼 감소된다!'
        useItem.value = ''
      }
    }

    function initValue() {
      myHpBar.value = '100'
      enemyHpBar.value = '100'
      begin.value = 0

      phase.value = 'start'
      msg.value = '적과 만났다!'

      actList.value = ['공격', '아이템']
      myAct.value = ''
      status.value = '대기'
      enemyAct.value = ''
      enemyStatus.value = '대기'

      myDamage.value = 0
      enemyDamage.value = 0

      useItem.value = ''

      absoluteDefense.value = false
      doubleAttack.value = false
      sacrifice.value = false
      survive.value = false
      stun.value = false
      blood.value = false
      poison.value = false
      poisonCnt.value = 0
      incapacitate.value = false
    }

    function itemToSelect() {
      phase.value = 'selectAct'
    }

    function getMonsterInfo(monsterId) {
      axios
        .get(BASE_URL + '/api/v1/monster/' + monsterId, {
          headers: {
            AUTHORIZATION: 'Bearer ' + localStorage.getItem('accessToken')
          }
        })
        .then((res) => {
          myName.value = res.data.name
          myMaxHp.value = res.data.hp
          myHp.value = res.data.hp
          myAttack.value = res.data.attack
          myDefense.value = res.data.defence
          myLevel.value = res.data.level
        })
        .catch((err) => console.log(err))
    }

    function getBossInfo(missionId) {
      axios
        .get(BASE_URL + '/api/v1/mission/boss/' + missionId, {
          headers: {
            AUTHORIZATION: 'Bearer ' + localStorage.getItem('accessToken')
          }
        })
        .then((res) => {
          enemyName.value = res.data.name
          enemyMaxHp.value = res.data.hp
          enemyHp.value = res.data.hp
          enemyAttack.value = res.data.attack
          enemyDefense.value = res.data.defence
        })
        .catch((err) => console.log(err))
    }

    function getItemList() {
      axios
        .get(BASE_URL + '/api/v1/item', {
          headers: {
            AUTHORIZATION: 'Bearer ' + localStorage.getItem('accessToken')
          }
        })
        .then((res) => {
          const useItemList = []
          res.data.forEach((d) => {
            if (d.type == 'USE_ITEM') {
              useItemList.push(d)
            }
          })
          itemList.value = useItemList
        })
        .catch((err) => console.log(err))
    }

    function deleteItem(itemId) {
      axios
        .delete(BASE_URL + '/api/v1/item/' + itemId, {
          headers: {
            AUTHORIZATION: 'Bearer ' + localStorage.getItem('accessToken')
          }
        })
        .then(() => {
          getItemList()
        })
        .catch((err) => console.log(err))
    }

    function winBattle() {
      msg.value = '배틀에서 이겼다!!!!'

      axios({
        url: BASE_URL + '/api/v1/mission/',
        method: 'PUT',
        headers: {
          AUTHORIZATION: 'Bearer ' + localStorage.getItem('accessToken')
        }
      }).then(() => {})
    }

    return {
      myHpBar,
      myMaxHp,
      myHp,
      enemyHpBar,
      enemyMaxHp,
      enemyHp,
      begin,
      phase,
      msg,
      changePhase,
      actList,
      status,
      doSelectAct,
      itemList,
      doSelectItem,
      poison,
      itemToSelect,
      myName,
      enemyName,
      isloading,
      myLevel
    }
  }
}
</script>

<style scoped>
.battlecanvas {
  width: 100vw;
  height: 100vh;
}

canvas {
  width: 100vw;
  height: 100vh;
}

#consoleDiv {
  position: fixed;
  top: 68%;
  left: 15%;
  width: 70%;
  height: 28%;
  background-color: rgb(229, 224, 196);
  z-index: 1;
  border-top-right-radius: 10vw;
  border-top-left-radius: 10vw;
  border-bottom-right-radius: 6vw;
  border-bottom-left-radius: 6vw;
  box-shadow: 10px 10px 10px #00000092;
  cursor: url('@/assets/selector.cur'), pointer;
}

#console {
  width: 80%;
  height: 70%;
  z-index: 2;
  margin-top: 6vh;
  margin-left: 7vw;
  margin-right: 7vw;
  font-size: 1.4rem;
  letter-spacing: 0.3px;
  word-spacing: 5px;
}

p {
  cursor: url('@/assets/selector.cur'), pointer;
}

.btn__console {
  display: flex;
  flex-direction: row;
  justify-content: center;
  align-items: center;
  margin-left: 3vw;
}
.select__btn {
  display: inline;
  margin-left: 1vw;
  margin-right: 1vw;
  cursor: url('@/assets/selector.cur'), pointer;
  margin-top: 4vh;
}

.select__btn button {
  height: 6vh;
  border-radius: 50px;
  border: none;
  width: 10vw;
  cursor: url('@/assets/selector.cur'), pointer;
  font-size: 1.2rem;
  background-color: #d5d5d5;
}

.select__btn button:hover {
  background-color: #6bfa8d;
  font-weight: bold;
}
.triangle {
  position: absolute;
  top: 50%;
  right: 10%;
  transition: 0.5s;
  display: inline-block;
}

.triangle:hover {
  scale: 1.1;
  color: #467302;
}

.triangle--top {
  animation: blinker 1s cubic-bezier(1, 1, 1, 1) infinite alternate;
}

.item__console {
  display: flex;
  flex-direction: row;
  justify-content: space-between;
}

.item__body {
  display: grid;
  grid-template-columns: repeat(8, 1fr);
}

.item__item {
  align-items: center;
  width: 70px;
  height: 70px;
  border-radius: 10px;
  margin-bottom: 1vh;
  margin-right: 1vh;
  cursor: url('@/assets/selector.cur'), pointer;
  text-align: center;
  transition: 0.5s;
}

.item__item p {
  font-size: 0.6rem;
}
.item__image {
  width: 90%;
}

.item__item:hover {
  scale: 1.05;
}

.item__btn {
  margin-top: 4vh;
  height: 6vh;
  border-radius: 30px;
  border: none;
  width: 6vw;
  cursor: url('@/assets/selector.cur'), pointer;
  font-size: 1rem;
  background-color: #d5d5d5;
}

.item__btn:hover {
  background-color: #6bfa8d;
  font-weight: bold;
}

@keyframes blinker {
  from {
    opacity: 1;
  }
  to {
    opacity: 0;
  }
}

.tooltip {
  position: relative;
  display: inline-block;
}

.tooltip .tooltiptext {
  visibility: hidden;
  width: 120px;
  background-color: black;
  color: #fff;
  text-align: center;
  border-radius: 6px;
  padding: 5px 0;
  font-size: 0.8rem;
  position: absolute;
  z-index: 1;
  bottom: 125%;
  left: 50%;
  margin-left: -60px;
  opacity: 0;
  transition: opacity 0.3s;
}

.tooltip .tooltiptext::after {
  content: '';
  position: absolute;
  top: 100%;
  left: 50%;
  margin-left: -5px;
  border-width: 5px;
  border-style: solid;
  border-color: rgb(0, 0, 0) transparent transparent transparent;
}

.tooltip:hover .tooltiptext {
  visibility: visible;
  opacity: 1;
}
</style>
