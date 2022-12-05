import { Body, Box, Vec3 } from 'cannon-es'

export class Wall {
  constructor(info) {
    this.cannonWorld = info.cannonWorld
    this.x = info.x
    this.z = info.z

    // left
    let shape = new Box(new Vec3(1, 5, Math.abs(this.z)))
    let cannonBody = new Body({
      mass: 0,
      position: new Vec3(-this.x, 0, 0),
      shape
    })
    this.cannonWorld.addBody(cannonBody)

    // right
    shape = new Box(new Vec3(1, 5, Math.abs(this.z)))
    cannonBody = new Body({
      mass: 0,
      position: new Vec3(this.x, 0, 0),
      shape
    })
    this.cannonWorld.addBody(cannonBody)

    // up
    shape = new Box(new Vec3(Math.abs(this.x), 5, 1))
    cannonBody = new Body({
      mass: 0,
      position: new Vec3(0, 0, -this.z),
      shape
    })
    this.cannonWorld.addBody(cannonBody)

    // down
    shape = new Box(new Vec3(Math.abs(this.x), 5, 1))
    cannonBody = new Body({
      mass: 0,
      position: new Vec3(0, 0, this.z),
      shape
    })
    this.cannonWorld.addBody(cannonBody)
  }
}
