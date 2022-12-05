export class Village {
  constructor(info) {
    this.moving = false
    this.scene = info.scene
    this.meshes = info.meshes
    this.rotationY = info.rotationY || 0
    this.x = info.position.x
    this.y = info.position.y
    this.z = info.position.z
    info.gltfLoader.load(info.modelSrc, (glb) => {
      glb.scene.traverse((child) => {
        if (child.isMesh) {
          child.castShadow = true
        }
      })

      this.modelMesh = glb.scene
      this.modelMesh.scale.x = 0.023
      this.modelMesh.scale.y = 0.023
      this.modelMesh.scale.z = 0.023
      this.modelMesh.position.set(this.x, this.y, this.z)
      this.modelMesh.name = info.name
      this.scene.add(this.modelMesh)
    })
  }
}
