import { Body, Box, Vec3 } from 'cannon-es'

export class Building {
  constructor(info) {
    this.moving = false
    this.scene = info.scene
    this.meshes = info.meshes
    this.cannonWorld = info.cannonWorld
    this.rotationY = info.rotationY || 0
    this.width = info.width || 6
    this.height = info.height || 6
    this.depth = info.depth || 6
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
      this.modelMesh.scale.x = 2
      this.modelMesh.scale.y = 2
      this.modelMesh.scale.z = 2
      this.modelMesh.position.set(this.x, this.y, this.z)
      this.modelMesh.name = info.name
      this.scene.add(this.modelMesh)
      this.meshes.push(this.modelMesh)
      this.setCannonBody()
    })
  }
  setCannonBody() {
    const shape = new Box(
      new Vec3(this.width / 2, this.height / 2, this.depth / 2)
    )
    this.cannonBody = new Body({
      mass: 0,
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
