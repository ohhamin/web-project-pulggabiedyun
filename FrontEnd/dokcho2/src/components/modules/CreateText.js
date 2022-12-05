import * as THREE from 'three'
import { FontLoader } from 'three/examples/jsm/loaders/FontLoader'
import { TextGeometry } from 'three/examples/jsm/geometries/TextGeometry'

export class CreateText {
  constructor(info) {
    this.model = info.model
    this.content = info.content

    const loader = new FontLoader()

    loader.load('/font/dohun.json', (font) => {
      const geometry = new TextGeometry(this.content, {
        font: font,
        size: 0.3,
        height: 0.01
      })

      const material = new THREE.MeshStandardMaterial({
        color: info.color,
        roughness: 0.1,
        metalness: 0.1
      })

      const text = new THREE.Mesh(geometry, material)

      text.position.set(info.x, info.y - 10, info.z)

      this.modelMesh = text

      // info.scene.add(text)
    })
  }
}
