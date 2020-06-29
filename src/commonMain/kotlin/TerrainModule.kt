import com.soywiz.korge.scene.*
import com.soywiz.korim.color.*
import com.soywiz.korinject.*
import com.soywiz.korma.geom.*

object TerrainModule : Module() {
	override val title: String = "Terrain Prototype 4"
	override val size: SizeInt = SizeInt(1024, 768)
	override val mainScene = TerrainTest::class
	override val bgcolor: RGBA = Colors.BLUE

	override suspend fun AsyncInjector.configure(): Unit {
		mapPrototype { TerrainTest() }
	}
}