import { Body, Box, Vec3 } from 'cannon-es'

export class Monster {
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
      this.modelMesh.scale.x = 0.4
      this.modelMesh.scale.y = 0.4
      this.modelMesh.scale.z = 0.4

      this.modelMesh.position.set(this.x, this.y, this.z)
      this.modelMesh.name = 'player'
      this.scene.add(this.modelMesh)

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
