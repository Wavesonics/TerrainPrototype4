import com.soywiz.klock.*
import com.soywiz.korev.*
import com.soywiz.korge.input.*
import com.soywiz.korge.scene.*
import com.soywiz.korge.view.*
import com.soywiz.korim.bitmap.*
import com.soywiz.korim.color.*
import com.soywiz.korim.format.*
import com.soywiz.korio.file.std.*

class TerrainTest(
) : Scene()
{
	private lateinit var bmp: Bitmap

	override suspend fun Container.sceneInit()
	{
		val boxSize = 30
		val clear = Array(boxSize * boxSize) { Colors.TRANSPARENT_BLACK }.toRgbaArray()

		image(resourcesVfs["background.png"].readBitmap())

		val bitmap = resourcesVfs["terrain-00.png"].readBitmap()
		val terrain = image(bitmap)

		bmp = terrain.bitmap.bmp

		terrain.addUpdater {
			if((views.input.mouseButtons or MouseButton.LEFT.id) != MouseButton.LEFT.id)
			{
				val xi = mouse.currentPosLocal.x.toInt()
				val yi = mouse.currentPosLocal.y.toInt()

				if(xi >= 0.0 && (xi - boxSize) < terrain.width && xi >= 0.0 && (yi - boxSize) < terrain.height)
				{
					bmp.lock {
						try
						{
							bmp.writePixelsUnsafe(xi, yi, boxSize, boxSize, clear)
						}
						catch(e: Exception)
						{
							print("OOB: $x, $y | ${terrain.width}, ${terrain.height}")
						}
					}
				}
			}
		}

		terrain.addFixedUpdater(Frequency(30.0)) {
			val clear = Array(1) { Colors.TRANSPARENT_BLACK }.toRgbaArray()

			val w = terrain.width.toInt()
			val h = terrain.height.toInt()
			val pixels = RgbaArray(1)
			bmp.lock {
				for(x in 0 until w)
				{
					for(y in h-1 downTo 0)
					{
						bmp.readPixelsUnsafe(x, y, 1, 1, pixels)
						val pixel = pixels[0]
						if(pixel.a == 0)
						{
							val above = y - 1
							if(above in 0 until h)
							{
								bmp.readPixelsUnsafe(x, above, 1, 1, pixels)
								bmp.writePixelsUnsafe(x, y, 1, 1, pixels)
								bmp.writePixelsUnsafe(x, above, 1, 1, clear)
							}
						}
					}
				}
			}
		}
	}
}
