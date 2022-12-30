package composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.kamel.image.KamelImage
import io.kamel.image.lazyPainterResource
import kotlinx.coroutines.Job
import models.*
import java.io.File
import java.util.*

val cardWidth = 300.dp
val characterImageSize = 150.dp
val weaponImageSize = 50.dp
val crownImageSize = 60.dp
val enemyImageSize = 120.dp
const val BASE_RESOURCE_PATH = "src/jvmMain/resources"

enum class ResourcesPath(val path: String, val imgType: String, val label: String, val imgSize: Dp) {
    Characters("$BASE_RESOURCE_PATH/characters", "png", "Character", characterImageSize),
    Crowns("$BASE_RESOURCE_PATH/crowns", "png", "Crown", crownImageSize),
    Weapons("$BASE_RESOURCE_PATH/weapons", "png", "Weapon", weaponImageSize),
    Enemies("$BASE_RESOURCE_PATH/enemies", "png", "Enemy", enemyImageSize),
    Areas("$BASE_RESOURCE_PATH/areas", "webp", "Area", 100.dp),
    Mutations("$BASE_RESOURCE_PATH/mutations", "webp", "Mutations", 32.dp),
    UltraMutations("$BASE_RESOURCE_PATH/mutations/ultra", "webp", "Ultra", 50.dp),
}

@Composable
fun NuclearRunCard(
    shape: Shape = MaterialTheme.shapes.medium,
    elevation: Dp = 1.dp,
    run: NuclearRun = nuclearRunExample
) {
    Card(
        shape = shape,
        elevation = elevation,
        backgroundColor = Color(0xFFF0F1EA),
    ) {
        Column(
            modifier = Modifier.padding(8.dp).width(cardWidth)
        ) {
            Title("Normal", 1671496539273, "Desert", "2")
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Character(run.character)
                Spacer(Modifier.width(55.dp))
                CrownEnemyWorldColumn()
            }
            Spacer(modifier = Modifier.height(14.dp))
            WeaponsRow()
            if (run.ultraMutation != "None") {
                Spacer(modifier = Modifier.height(14.dp))
                UltraMutationItem(run.ultraMutation, run.character)
            }
            Spacer(modifier = Modifier.height(14.dp))
            MutationsRow(run.mutations)
        }
    }
}

@Composable
fun MutationsRow(mutations: List<String>) {
    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            mutations.forEach { mutation ->
                ShowImage(
                    imgName = mutation,
                    resourcesPath = ResourcesPath.Mutations
                )
                Spacer(modifier = Modifier.width(1.dp))
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        RoundLabel(ResourcesPath.Mutations.label)
    }
}

@Composable
fun UltraMutationItem(ultra: String, characterName: String) {
    val ultraMutationFileName = "${characterName}_$ultra"
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ShowImage(
            imgName = ultraMutationFileName,
            resourcesPath = ResourcesPath.UltraMutations
        )
        Spacer(modifier = Modifier.height(4.dp))
        RoundLabel(ResourcesPath.UltraMutations.label)
    }
}

@Composable
fun CrownEnemyWorldColumn() {
    Column(modifier = Modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        ItemColumn(Crown.HASTE.crownName, ResourcesPath.Crowns)
        Spacer(modifier = Modifier.height(8.dp))
        ItemColumn(Enemy.BANDIT.enemyName, ResourcesPath.Enemies)
        //ItemColumn(World.DESERT.worldName, ResourcesPath.Areas)
    }
}

@Composable
fun ItemColumn(imgName: String, resourcesPath: ResourcesPath) {
    Column(
        modifier = Modifier.wrapContentSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ShowImage(
            imgName = imgName,
            resourcesPath = resourcesPath
        )
        Spacer(modifier = Modifier.height(4.dp))
        RoundLabel(resourcesPath.label)
    }
}

@Composable
fun Title(type: String, timeStamp: Long, area: String, level: String) {
    val date = timeStamp.millisToDateString()
    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = type,
            style = TextStyle(
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = "Area: $area - Level: $level",
            style = TextStyle(
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal
            )
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = date,
            style = TextStyle(
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal
            )
        )
    }
}

private fun Long.millisToDateString(): String {
    val date = Date(this)
    return date.toInstant().toString().split("T")[0]
}

@Composable
fun WeaponsRow() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        ItemColumn(
            Weapon.ASSAULT_RIFLE.weapName,
            ResourcesPath.Weapons,
        )
        ItemColumn(
            Weapon.GOLDEN_WRENCH.weapName,
            ResourcesPath.Weapons,
        )
    }
}

@Composable
fun Character(characterName: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ShowImage(characterName, ResourcesPath.Characters)
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = characterName, fontSize = 20.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun ShowImage(imgName: String, resourcesPath: ResourcesPath) {
    val img = File("${resourcesPath.path}/$imgName.${resourcesPath.imgType}")

    val painterResource = lazyPainterResource(img) {
        coroutineContext = Job()
    }

    /*KamelImage(
        resource = painterResource,
        contentDescription = imgName,
        contentScale = ContentScale.FillWidth,
        modifier = Modifier.size(resourcesPath.imgSize).border(1.dp, Color.Black)
    )

     */

    Image(
        painter = painterResource("${resourcesPath.path}/$imgName.${resourcesPath.imgType}"),
        contentDescription = imgName
    )
}

@Composable
fun RoundLabel(text: String) {
    Surface(
        color = Color(0x5000de00),
        shape = RoundedCornerShape(20.dp)
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 4.dp),
            style = TextStyle(color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
        )
    }
}