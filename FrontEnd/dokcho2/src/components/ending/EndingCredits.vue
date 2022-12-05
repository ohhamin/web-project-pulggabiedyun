<template>
  <div id="endingcredits">
    <canvas id="EndingCanvas"> </canvas>

    <div class="fade"></div>

    <section class="star-wars">
      <div class="crawl">
        <br />
        <br />
        <br />
        <br />
        <br />
        <br />
        <br />
        <br />
        <br />
        <br />
        <div class="title">
          <p>독초도감 최종판</p>
          <h1>풀깨비뎐</h1>
        </div>

        <div class="teammate">
          <div class="title">
            <h3>팀장</h3>
            <p>서상균</p>
            <br />
            <h3>팀원</h3>
            <p>김성빈</p>
            <p>김효근</p>
            <p>박지현</p>
            <p>오하민</p>
            <p>이근희</p>
            <br />
            <h3>출연</h3>
            <p>단군 장수왕 선덕여왕 공민왕</p>
            <p>세종대왕 이순신 유관순 허준</p>
            <p>지현몬 묘근몬 재준몬 근희몬</p>
            <p>상균몬 지원몬 하민몬 성빈몬</p>
            <br />
            <p>Special Thanks to...</p>
            <p>이재준 최지원</p>
          </div>
        </div>

        <div class="title">
          <h1>플레이 해주셔서 감사합니다!</h1>
        </div>
      </div>
    </section>
  </div>
</template>

<script>
import * as THREE from 'three'
import { GLTFLoader } from 'three/examples/jsm/loaders/GLTFLoader'

import { Ending } from '../modules/Ending'
import { EndingAnimation } from '../modules/EndingAnimation'

import { ref, watch } from 'vue'

export default {
  name: 'EndingCredits',

  props: { ending: Number },

  setup(props, { emit }) {
    const time = ref(0)
    watch(
      () => props.ending,
      () => {
        setInterval(() => {
          time.value += 1
        }, 1000)

        setTimeout(() => {
          // Texture
          const textureLoader = new THREE.TextureLoader()
          const floorTexture = textureLoader.load('/images/map18.png')
          floorTexture.wrapS = THREE.RepeatWrapping
          floorTexture.wrapT = THREE.RepeatWrapping
          floorTexture.repeat.x = 1
          floorTexture.repeat.y = 1

          // Renderer
          let canvas = document.querySelector('#EndingCanvas')

          const renderer = new THREE.WebGLRenderer({
            canvas,
            antialias: true
          })
          renderer.setSize(window.innerWidth, window.innerHeight)
          renderer.setPixelRatio(window.devicePixelRatio > 1 ? 2 : 1)
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

          camera.position.set(3, 1, 3)
          camera.lookAt(-1.5, 1, 1.5)
          scene.add(camera)

          // Light
          const ambientLight = new THREE.AmbientLight('white', 0.7)
          scene.add(ambientLight)

          const directionalLight = new THREE.DirectionalLight('white', 0.5)
          const directionalLightOriginPosition = new THREE.Vector3(1, 1, 1)
          directionalLight.position.x = directionalLightOriginPosition.x
          directionalLight.position.y = directionalLightOriginPosition.y
          directionalLight.position.z = directionalLightOriginPosition.z

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

          // 바닥 만들기
          const floorMesh = new THREE.Mesh(
            new THREE.PlaneGeometry(150, 150),
            new THREE.MeshStandardMaterial({
              map: floorTexture
            })
          )
          floorMesh.name = 'floor'
          floorMesh.rotation.x = -Math.PI / 2
          floorMesh.receiveShadow = true

          const gltfLoader = new GLTFLoader()

          const squirrel = new Ending({
            scene,
            gltfLoader,
            modelSrc: '/ending/지현몬.glb',
            x: 0.1,
            y: 0,
            z: -3
          })

          const deer = new Ending({
            scene,
            gltfLoader,
            modelSrc: '/ending/효근몬.glb',
            x: -3,
            y: 0,
            z: 6
          })

          const dog = new Ending({
            scene,
            gltfLoader,
            modelSrc: '/ending/상균몬.glb',
            x: -0.5,
            y: 0,
            z: -0.9
          })

          const fox = new EndingAnimation({
            scene,
            gltfLoader,
            modelSrc: '/ending/근희몬.glb',
            x: 0.9,
            y: 0,
            z: -0.4
          })

          const cat = new EndingAnimation({
            scene,
            gltfLoader,
            modelSrc: '/ending/지원몬.glb',
            x: -2.5,
            y: 0,
            z: 4
          })

          const tiger = new EndingAnimation({
            scene,
            gltfLoader,
            modelSrc: '/ending/성빈몬.glb',
            x: -0.7,
            y: 0,
            z: 4.3
          })

          const bird = new EndingAnimation({
            scene,
            gltfLoader,
            modelSrc: '/ending/재준몬.glb',
            x: -0.6,
            y: 0,
            z: 2.8
          })

          const cow = new EndingAnimation({
            scene,
            gltfLoader,
            modelSrc: '/ending/하민몬.glb',
            x: 0.2,
            y: 0,
            z: 1
          })

          const player = new EndingAnimation({
            scene,
            gltfLoader,
            modelSrc: '/ending/player.glb',
            x: -2,
            y: 0,
            z: -1.5
          })

          const monster = new EndingAnimation({
            scene,
            gltfLoader,
            modelSrc: '/ending/1.glb',
            x: -4,
            y: 0,
            z: 5
          })

          const clock = new THREE.Clock()

          function draw() {
            const delta = clock.getDelta()

            if (time.value >= 60) {
              emit('startEndingCredits')
              const endingcredits = document.querySelector('#endingcredits')
              endingcredits.remove()
            }

            if (squirrel.modelMesh) {
              squirrel.modelMesh.lookAt(3, 0, 3)
            }
            if (squirrel.mixer) squirrel.mixer.update(delta)

            if (deer.modelMesh) {
              deer.modelMesh.lookAt(3, 0, 3)
            }
            if (deer.mixer) deer.mixer.update(delta)

            if (dog.modelMesh) {
              dog.modelMesh.lookAt(3, 0, 3)
            }
            if (dog.mixer) dog.mixer.update(delta)

            if (fox.modelMesh) {
              fox.modelMesh.lookAt(3, 0, 3)
            }
            if (fox.mixer) fox.mixer.update(delta)

            if (bird.modelMesh) {
              bird.modelMesh.lookAt(3, 0, 3)
            }
            if (bird.mixer) bird.mixer.update(delta)

            if (tiger.modelMesh) {
              tiger.modelMesh.lookAt(3, 0, 3)
            }
            if (tiger.mixer) tiger.mixer.update(delta)

            if (cat.modelMesh) {
              cat.modelMesh.lookAt(3, 0, 3)
            }
            if (cat.mixer) cat.mixer.update(delta)

            if (cow.modelMesh) {
              cow.modelMesh.lookAt(3, 0, 3)
            }
            if (cow.mixer) cow.mixer.update(delta)

            if (player.modelMesh) {
              player.modelMesh.lookAt(3, 0, 3)
            }
            if (player.mixer) player.mixer.update(delta)

            if (monster.modelMesh) {
              monster.modelMesh.lookAt(3, 0, 3)
            }
            if (monster.mixer) monster.mixer.update(delta)

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
    return time
  }
}
</script>

<style>
body {
  position: absolute;
  width: 100%;
  height: 100%;
  background: #000;
  overflow: hidden;
}

canvas {
  position: absolute;
  width: 100vw;
  height: 80vh;
}

.fade {
  position: absolute;
  width: 100%;
  min-height: 40vh;
  top: -25px;
  background-image: linear-gradient(0deg, transparent, black 75%);
}

.star-wars {
  display: flex;
  justify-content: center;
  position: relative;
  height: 80vh;
  color: #feda4a;
  font-family: 'Pathway Gothic One', sans-serif;
  font-size: 400%;
  font-weight: 600;
  letter-spacing: 6px;
  line-height: 120%;
  perspective: 400px;
  text-align: justify;
  background-color: black;
  background-color: rgba(0, 0, 0, 0);
}

.crawl {
  position: relative;
  top: 9999px;
  transform-origin: 50% 100%;
  animation: crawl 140s linear;
}

.crawl > .title {
  font-size: 90%;
  text-align: center;
}

.crawl > .title h1 {
  margin: 0 0 100px;
  text-transform: uppercase;
}

.teammate {
  text-align: center;
}

.teammate > .title h3 {
  margin: 0 0 50px;
  text-transform: uppercase;
}

.teammate > .title p {
  margin: 0 0 10px;
  text-transform: uppercase;
}

@keyframes crawl {
  0% {
    top: 0;
    transform: rotateX(20deg) translateZ(0);
  }
  100% {
    top: -6000px;
    transform: rotateX(25deg) translateZ(-2000px);
  }
}
</style>
