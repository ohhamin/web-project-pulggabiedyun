import { AnimationMixer } from 'three'

export class EndingAnimation {
  constructor(info) {
    this.moving = false
    this.scene = info.scene
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
      this.modelMesh.scale.x = 0.4
      this.modelMesh.scale.y = 0.4
      this.modelMesh.scale.z = 0.4

      this.modelMesh.position.set(this.x, this.y, this.z)
      this.modelMesh.name = 'boss'
      this.scene.add(this.modelMesh)

      this.actions = []

      this.mixer = new AnimationMixer(this.modelMesh)
      this.actions[0] = this.mixer.clipAction(glb.animations[0])
      this.actions[0].play()
    })
  }
}
