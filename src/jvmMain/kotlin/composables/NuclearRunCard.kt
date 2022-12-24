package composables

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
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
import java.io.File
import java.util.*

val cardWidth = 300.dp
val characterImageSize = 150.dp
val weaponImageSize = 100.dp
val crownImageSize = 50.dp
const val BASE_RESOURCE_PATH = "src/jvmMain/resources"

enum class ResourcesPath(val path: String) {
    Characters("$BASE_RESOURCE_PATH/characters"),
    Crowns("$BASE_RESOURCE_PATH/crowns"),
    Weapons("$BASE_RESOURCE_PATH/weapons"),
    Enemies("$BASE_RESOURCE_PATH/enemies"),
    Areas("$BASE_RESOURCE_PATH/areas"),
    Mutations("$BASE_RESOURCE_PATH/mutations")
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
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                Crown(Crown.HASTE)
                Character()
            }
            Spacer(modifier = Modifier.height(14.dp))
            WeaponsRow()
        }
    }
}

@Composable
fun Crown(crown: Crown) {
    Column(
        modifier = Modifier.wrapContentSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ShowImage(Modifier.size(crownImageSize), crown.crownName, ResourcesPath.Crowns.path)
        Spacer(modifier = Modifier.height(4.dp))
        RoundLabel("Crown")
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
        Weapon("Assault Rifle", "Weapon A")
        Weapon("Golden Wrench", "Weapon B")
    }
}

@Composable
fun Weapon(name: String, label: String) {
    Column(
        modifier = Modifier.wrapContentSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ShowImage(Modifier.wrapContentHeight().width(weaponImageSize), name, ResourcesPath.Weapons.path)
        Spacer(modifier = Modifier.height(4.dp))
        RoundLabel(label)
    }
}

@Composable
fun Character(characterName: String = "Chicken") {
    Column(
        modifier = Modifier.padding(end = 15.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ShowImage(Modifier.size(characterImageSize), characterName, ResourcesPath.Characters.path)
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = characterName, fontSize = 20.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun ShowImage(modifier: Modifier = Modifier, imgName: String, imgPath: String) {
    val img = File("$imgPath/$imgName.png")

    val painterResource = lazyPainterResource(img) {
        coroutineContext = Job()
    }

    KamelImage(
        resource = painterResource,
        contentDescription = imgName,
        modifier = modifier
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