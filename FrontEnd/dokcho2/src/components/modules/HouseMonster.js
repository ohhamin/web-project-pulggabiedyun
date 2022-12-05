import { Body, Box, Vec3 } from 'cannon-es'

export class HouseMonster {
  constructor(info) {
    this.moving = false
    this.scene = info.scene
    this.meshes = info.meshes
    this.cannonWorld = info.cannonWorld
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
      this.modelMesh.scale.x = 0.5
      this.modelMesh.scale.y = 0.5
      this.modelMesh.scale.z = 0.5
      this.modelMesh.position.set(this.x, this.y, this.z)
      this.modelMesh.name = info.name
      this.scene.add(this.modelMesh)
      this.meshes.push(this.modelMesh)
      this.setCannonBody()
    })
  }

  setCannonBody() {
    const shape = new Box(new Vec3(0.08, 0.08, 0.08))
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
