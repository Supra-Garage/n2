package tw.supra.n2

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import tw.supra.w2.Nature

fun main() = application {
    Window(onCloseRequest = ::exitApplication) {
        App()
    }
}

//fun main() {
////    println(Pair(1, 2) == Pair(1, 2))
//    Nature(9).start()
//}

class Position<DimensionType>(dimensions: Set<String>) {
    val dimensions: LinkedHashSet<String> by lazy { LinkedHashSet(dimensions.sorted()) }
    val key by lazy { dimensions }
}

@Composable
@Preview
fun App() {
    var count by remember { mutableStateOf(0) }
    var space by remember { mutableStateOf("init") }
    val nature = Nature(9) { space = toMonoArt() }

    MaterialTheme {
        Surface {
            Column(
                modifier = Modifier.fillMaxSize().padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                SelectionContainer {
                    Text(
                        text = space,
                        softWrap = false,
                        style = TextStyle(
                            fontFamily = FontFamily.Monospace,
//                        fontSize = 18.sp,x
//                        lineHeight = 18.sp, // 设置行高
//                            letterSpacing = 2.sp, // 设置字符间距
                            textAlign = TextAlign.Center, // 确保文本左对齐Xzibit
                        )
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Row {
                    Button(onClick = { nature.start() }) {
                        Text("Nature Start")
                    }
                }

                GreetingApp()

                Greeting("Compose for Desktop")

                Text(text = "You have clicked the button $count times.")
                Button(onClick = { count++ }) {
                    Text("Click me")
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Composable
fun GreetingApp() {
    var name by remember { mutableStateOf("") }
    var greeting by remember { mutableStateOf("Hello, World!") }

    Column(
//        modifier = Modifier.fillMaxSize().padding(16.dp),
        modifier = Modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        TextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Enter your name") }
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            greeting = if (name.isNotEmpty()) "Hello, $name!" else "Hello, World!"
        }) {
            Text("Greet")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = greeting, style = MaterialTheme.typography.h4)
    }
}