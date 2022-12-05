import { Body, Box, Vec3 } from 'cannon-es'
import { AnimationMixer } from 'three'

export class Player {
  constructor(info) {
    this.moving = false
    this.scene = info.scene
    this.cannonWorld = info.cannonWorld
    this.rotationY = info.rotationY || 0

    this.width = info.width || 0.8
    this.height = info.height || 0.5
    this.depth = info.depth || 1

    this.x = info.x || 0
    this.y = info.y || 0
    this.z = info.z || 0

    info.gltfLoader.load(info.modelSrc, (glb) => {
      glb.scene.traverse((child) => {
        if (child.isMesh) {
          child.castShadow = true
        }
      })

      this.modelMesh = glb.scene
      this.modelMesh.scale.x = 40
      this.modelMesh.scale.y = 40
      this.modelMesh.scale.z = 40

      this.modelMesh.position.set(this.x, this.y, this.z)
      this.modelMesh.name = 'player'
      this.scene.add(this.modelMesh)

      this.actions = []

      this.mixer = new AnimationMixer(this.modelMesh)
      this.actions[0] = this.mixer.clipAction(glb.animations[1])
      this.actions[1] = this.mixer.clipAction(glb.animations[3])
      this.actions[2] = this.mixer.clipAction(glb.animations[2])
      this.actions[3] = this.mixer.clipAction(glb.animations[0])
      this.actions[0].play()

      this.setCannonBody()
    })
  }

  setCannonBody() {
    const shape = new Box(
      new Vec3(this.width / 2, this.height / 2, this.depth / 2)
    )
    this.cannonBody = new Body({
      mass: 1,
      position: new Vec3(this.x, this.y, this.z),
      shape
    })

    this.cannonBody.quaternion.setFromAxisAngle(
      new Vec3(0, 1, 0), // y축
      this.rotationY
    )

    this.modelMesh.cannonBody = this.cannonBody

    this.cannonWorld.addBody(this.cannonBody)
  }
}
