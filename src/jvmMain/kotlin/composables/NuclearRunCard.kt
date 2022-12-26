package composables

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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.kamel.image.KamelImage
import io.kamel.image.lazyPainterResource
import kotlinx.coroutines.Job
import models.Crown
import models.Enemy
import models.Weapon
import java.io.File
import java.util.*

val cardWidth = 300.dp
val characterImageSize = 150.dp
val weaponImageSize = 100.dp
val crownImageSize = 40.dp
val enemyImageSize = 100.dp
const val BASE_RESOURCE_PATH = "src/jvmMain/resources"

enum class ResourcesPath(val path: String, val imgType: String, val label: String, val imgSize: Dp) {
    Characters("$BASE_RESOURCE_PATH/characters", "png", "Character", characterImageSize),
    Crowns("$BASE_RESOURCE_PATH/crowns", "png", "Crown", crownImageSize),
    Weapons("$BASE_RESOURCE_PATH/weapons", "png", "Weapon", weaponImageSize),
    Enemies("$BASE_RESOURCE_PATH/enemies", "gif", "Enemy", enemyImageSize),
    Areas("$BASE_RESOURCE_PATH/areas", "webp", "Area", 100.dp),
    Mutations("$BASE_RESOURCE_PATH/mutations", "webp", "Mutations", 40.dp)
}

@Composable
fun NuclearRunCard(
    shape: Shape = MaterialTheme.shapes.medium,
    elevation: Dp = 1.dp
) {
    Card(
        shape = shape,
        elevation = elevation,
        backgroundColor = Color(0xFFF0F1EA),
    ) {
        Column(
            modifier = Modifier.padding(8.dp).width(cardWidth)
        ) {
            Title("Normal", 1671496539273)
            Spacer(modifier = Modifier.height(8.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Start) {
                Character()
                Spacer(Modifier.width(55.dp))
                CrownEnemyWorldColumn()
            }
            Spacer(modifier = Modifier.height(14.dp))
            WeaponsRow()
        }
    }
}

@Composable
fun CrownEnemyWorldColumn() {
    Column {
        ItemColumn(Crown.HASTE.crownName, ResourcesPath.Crowns)
        ItemColumn(Enemy.BANDIT.enemyName, ResourcesPath.Enemies)
    }
}

@Composable
fun ItemColumn(imgName: String, resourcesPath: ResourcesPath) {
    Column(
        modifier = Modifier.wrapContentSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ShowImage(imgName = imgName, resourcesPath = resourcesPath)
        Spacer(modifier = Modifier.height(4.dp))
        RoundLabel(resourcesPath.label)
    }
}

@Composable
fun Title(type: String, timeStamp: Long) {
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
            text = date,
            style = TextStyle(
                fontSize = 18.sp,
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
        ItemColumn(Weapon.ASSAULT_RIFLE.weapName, ResourcesPath.Weapons)
        ItemColumn(Weapon.GOLDEN_WRENCH.weapName, ResourcesPath.Weapons)
    }
}

@Composable
fun Character(characterName: String = "Chicken") {
    Column(
        modifier = Modifier.border(1.dp, Color.Red),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ShowImage(Modifier.size(characterImageSize), characterName, ResourcesPath.Characters)
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = characterName, fontSize = 20.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun ShowImage(modifier: Modifier = Modifier, imgName: String, resourcesPath: ResourcesPath) {
    val img = File("${resourcesPath.path}/$imgName.${resourcesPath.imgType}")

    val painterResource = lazyPainterResource(img) {
        coroutineContext = Job()
    }

    KamelImage(
        resource = painterResource,
        contentDescription = imgName,
        modifier = modifier.size(resourcesPath.imgSize)
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