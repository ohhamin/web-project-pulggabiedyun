import { AnimationMixer } from 'three'

export class myMon {
  constructor(info) {
    this.moving = false
    this.scene = info.scene
    this.meshes = info.meshes
    this.rotationY = info.rotationY || 0
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
      this.modelMesh.name = info.name
      this.scene.add(this.modelMesh)
      this.meshes.push(this.modelMesh)

      this.actions = []

      this.mixer = new AnimationMixer(this.modelMesh)
      this.actions[0] = this.mixer.clipAction(glb.animations[0])
    })
  }
}
