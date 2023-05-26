@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.task_periodicscreen

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.task_periodicscreen.ui.theme.Task_PeriodicScreenTheme
import org.w3c.dom.Text
import java.time.LocalDate
import java.time.Month
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.time.temporal.TemporalAdjusters
import java.time.temporal.WeekFields
import java.util.Locale

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Task_PeriodicScreenTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreenView()
                }
            }
        }
    }
}

private val AppBarHeight = 56.dp


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreenView() {
    val navController = rememberNavController()
    Scaffold(
        topBar = { MyTopAppBar(navController = navController) },
        content = {
            Box(modifier = Modifier.fillMaxSize()) {
                // Content of your screen goes here
                MyApp(navController = navController)
            }
        }
    )
}


@Composable
fun MyApp(navController: NavController){

    MyTopAppBar(navController = navController)
    NavHost(navController = navController as NavHostController, startDestination = "metricScreen") {
        composable("metricScreen") {
            MetricScreen()
        }
        composable("periodicScreen") {
            MonthScreen()
        }
        composable("analyticsScreen"){
            AnalyticalScreen()
        }
    }

}

@SuppressLint("SuspiciousIndentation")
@Composable
fun MyTopAppBar(navController: NavController)
{
    val list= listOf<String>("Metric","Periodic","Analytics")

//    Scaffold(topBar = { TopAppBar(title = { Text("My App") }) })
//    { contentPadding ->
//        Box(modifier = Modifier.padding(contentPadding)) {
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)) {
                list.forEach {item->
                    Card(shape = RoundedCornerShape(4.dp), modifier = Modifier
                        .weight(1f)
                        .clickable {
                            when (item) {
                                "Metric" -> navController.navigate("metricScreen")
                                "Periodic" -> navController.navigate("periodicScreen")
                                "Analytics" -> navController.navigate("analyticsScreen")
                            }
                        }) {
                        Text(text=item,modifier = Modifier.padding(8.dp))
                    }
                }
            }
//        }
//    }
}


//periodic screen
@Composable
fun MonthScreen() {

    val friendList = listOf(
        "1-7 May",
        "8-14 May", "15-21 May","22-28 May","29 May - 4 June"
    )

    var isExpanded by remember { mutableStateOf(false)}

    val currentMonth = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        LocalDate.now().month
    } else {
        TODO("VERSION.SDK_INT < O")
    }
    val months = Month.values().toList().map { it.getDisplayName(TextStyle.FULL, Locale.getDefault()) }
//    val months = Month.values()

    var selectedMonth by remember { mutableStateOf<Month?>(null) }
    var visibleMonths by remember { mutableStateOf<List<Month>>(emptyList()) }

    LaunchedEffect(Unit) {
        val newVisibleMonths = mutableListOf<Month>()
        for (month in Month.values()) {
            if (month < currentMonth || month == currentMonth) {
                newVisibleMonths.add(month)
            }
        }
        visibleMonths = newVisibleMonths.reversed()
    }


    Column(modifier=Modifier.padding(top= AppBarHeight)) {
        Text(text = "2023", textAlign = TextAlign.Center, fontSize = 20.sp,modifier= Modifier
            .fillMaxWidth()
            .padding(8.dp))
        LazyColumn() {
            items(visibleMonths){ month->
                val monthName = months[month.value - 1]
                Card(
                    shape= RoundedCornerShape(8.dp), modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .wrapContentHeight()
                        .clickable {
                            isExpanded = !isExpanded
                            selectedMonth = month
//                            navController.navigate("weekListScreen/${month.name}")
//                        navController.navigate("weekListScreen/${month.name}/${month.value}")

                        }
                ) {
                    Column( ){
                        Row(modifier = Modifier.padding(8.dp)) {
                            Text(text =monthName,fontWeight = if (selectedMonth == month) FontWeight.Bold else FontWeight.Normal,modifier = Modifier
                                .padding(8.dp)
                                .weight(1f), textAlign = TextAlign.Start,style=MaterialTheme.typography.bodyMedium)
                            Icon(painter = painterResource(id = if(isExpanded) R.drawable.baseline_arrow_drop_down_24  else R.drawable.baseline_arrow_drop_up_24), contentDescription = "",modifier = Modifier.padding(8.dp))
                        }

                    }

                }
                if (isExpanded && selectedMonth == month) {
                    AnimatedVisibility(
                        visible = isExpanded,
                        enter = slideInVertically() + fadeIn(),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(horizontal = 16.dp)) {

                            friendList.asSequence().forEach { friend ->
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 8.dp)
                                ) {
                                    Row(modifier=Modifier.padding(8.dp) ) {
                                        Text(text = "Weekly",modifier=Modifier.weight(1f))
                                        Icon(painter = painterResource(id = R.drawable.baseline_arrow_forward_ios_24), contentDescription ="")

                                    }
                                    Text(
                                        text = friend,
                                        modifier = Modifier.padding(16.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
@Composable
fun MetricScreen(list1: List<DataList1> = datalist1 , list2: List<DataList2> = datalist2)
{
    Column(modifier=Modifier.padding(top= AppBarHeight)) {
        LazyColumn{
            item{
                Text(text = "Activity", modifier = Modifier.padding(start=18.dp,end=18.dp,top=8.dp, bottom = 8.dp), fontSize = 20.sp)

            }
            items(list1) { item ->
                Card(
                    shape = RoundedCornerShape(8.dp), modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth()
                        .wrapContentHeight()
                ) {
                    Row() {
                        ListedDataScreen1(list = item, modifier = Modifier.weight(1f))
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_arrow_forward_ios_24),
                            contentDescription = "",
                            modifier = Modifier
                                .padding(18.dp)
                                .size(16.dp)
                        )
                    }
                }
            }

            item{
                Text(text = "Shoe", modifier = Modifier.padding(start=18.dp,end=18.dp,top=8.dp, bottom = 8.dp), fontSize = 20.sp)
            }
            items(list2) { item ->
                Card(
                    shape = RoundedCornerShape(8.dp), modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth()
                        .wrapContentHeight()
                ) {
                    Row() {
                        Row(modifier=Modifier.weight(1f)) {
                            ListedDataScreen2(list = item, modifier = Modifier)

                        }
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_arrow_forward_ios_24),
                            contentDescription = "",
                            modifier = Modifier
                                .padding(18.dp)
                                .size(16.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ListedDataScreen1( list: DataList1,modifier: Modifier){
    Row(modifier.padding(18.dp))
    {
        Icon(painter = painterResource(id = list.drawableRes), contentDescription ="" )
        Text(text =list.text, modifier.padding(start=8.dp) )
    }
}
@Composable
fun ListedDataScreen2( list: DataList2,modifier: Modifier){
    Column(modifier.padding(18.dp))
    {
        Text(text = list.text1, modifier.padding(start=8.dp,bottom=6.dp))
        Text(text =list.text2, modifier.padding(start=8.dp),fontSize=14.sp )
    }
}

data class DataList1 constructor(val drawableRes: Int,val text:String)

val datalist1= arrayListOf<DataList1>(
    DataList1(R.drawable.walk,"Steps"),
    DataList1(R.drawable.baseline_stairs_24,"Stairs Climbed"),
    DataList1(R.drawable.distance,"Distance"),
    DataList1(R.drawable.intense,"Intense Activity"),
    DataList1(R.drawable.calories,"Calories"),
    DataList1(R.drawable.baseline_star_border_24,"Walking Quality Score")
)

data class DataList2 constructor(val text1:String,val text2:String)

val datalist2= arrayListOf<DataList2>(
    DataList2("Shoe usage","Track hours of use"),
    DataList2("Cushioning","Layer of protection and support between the foot and the ground")
)

@Composable
fun AnalyticalScreen(list3: List<Datalist3> = datalist3)
{
    val selectedItemIndex = remember{ mutableStateOf(-1) }
    val expanded = selectedItemIndex.value == 1

    Column(modifier=Modifier.padding(top= AppBarHeight)) {
        Text(text = "2023", textAlign = TextAlign.Center, fontSize = 20.sp,modifier= Modifier
            .fillMaxWidth()
            .padding(18.dp))
        LazyColumn {
            itemsIndexed(list3){index,item->
                AnalyticalHelper1(list3 =item,
                    onItemClick = {
                        if (selectedItemIndex.value == index) {
                            selectedItemIndex.value = -1 // Reset to initial state
                        } else {
                            selectedItemIndex.value = index
                        }
                    }
                )
                if( expanded && index==selectedItemIndex.value){
                    AnimatedVisibility(visible = expanded,
                        enter = slideInVertically() + fadeIn(),
                        modifier = Modifier.fillMaxSize()) {

//                        Card(shape = RoundedCornerShape(8.dp), modifier = Modifier.padding(18.dp)) {
//                            DropDownAnalyticsHelper()
//                        }
                        DropDownAnalyticsHelper()
                    }
                }}}
    }

}
@Composable
fun AnalyticalHelper1(list3: Datalist3, onItemClick:()->Unit)
{
    var expanded by remember{ mutableStateOf(false) }

    Card(modifier= Modifier
        .fillMaxWidth()
        .padding(8.dp)
        .clickable {
            expanded = !expanded
            onItemClick()
        })
    {
        Row(modifier= Modifier
            .padding(8.dp)
        ) {
            Row(modifier = Modifier.weight(1f)) {
                Column() {
                    Text(text = list3.title,modifier=Modifier.padding(bottom=8.dp))
                    Text(text = list3.description)
                }
            }
            Icon(
                painter = painterResource(id = if (expanded) R.drawable.baseline_arrow_drop_up_24 else R.drawable.baseline_arrow_drop_down_24),
                contentDescription = ""
            )
        }
    }
}

@Composable
fun DropDownAnalyticsHelper()
{
    val DateList= listOf("1-7 May","8-14 May","15-21 May","22-28 May","29 May - 4 June")

    Column(modifier=Modifier.wrapContentHeight()) {
        OutlinedButton(onClick = { /*TODO*/ },modifier= Modifier
            .fillMaxWidth()
            .padding(8.dp))
        {
            Row() {
                Icon(painter = painterResource(id = R.drawable.baseline_add_24) , contentDescription = "")
                Text(text = "Generate Report", fontSize = 16.sp)
            }
        }

            DateList.forEach{ item->
                Card(shape = RoundedCornerShape(8.dp),modifier=Modifier.padding(14.dp)) {
                    Row(modifier=Modifier.padding(8.dp)) {
                        Text(text = item,Modifier.weight(1f))
                        Icon(painter = painterResource(id = R.drawable.baseline_arrow_forward_ios_24), contentDescription = "")
                    }}
                 }
        Button(onClick = { /*TODO*/ }, modifier= Modifier
            .fillMaxWidth()
            .padding(8.dp)) {
            Text(text = "See all", textAlign = TextAlign.Center)
        }
    }
}


data class Datalist3 constructor(val title:String, val description:String)

val datalist3= arrayListOf<Datalist3>(
    Datalist3("Walking Musculature Score","Assessment of your walking from a muscle engagement perspective."),
    Datalist3("Walking Joint Mobility Score","Assessment of your walking from a joint mobility and health perspective."),
    Datalist3("Personal Gait Scan","Assessment of your unique walking gait profile.")
)

//@Composable
//fun AnimationVi() {
//    var isVisible by remember { mutableStateOf(false) }
//
//    val friendList = listOf(
//        "1-7 May",
//        "8-14 May", "15-21 May","22-28 May","29 May - 4 June"
//    )
//
//    Column(modifier = Modifier.fillMaxSize()) {
//        Card(
//            modifier = Modifier
//                .padding(8.dp).wrapContentSize()
//                .clickable { isVisible = !isVisible }
//        ) {
//            Text(text = "Weekly")
//        }
//        AnimatedVisibility(
//            visible = isVisible,
//            enter = slideInVertically() + fadeIn(),
//            modifier = Modifier.fillMaxWidth()
//        ) {
//            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
//                friendList.forEach { friend ->
//                    Card(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(vertical = 8.dp)
//                    ) {
//                        Text(
//                            text = friend,
//                            modifier = Modifier.padding(16.dp)
//                        )
//                    }
//                }
//            }
//        }
//    }
//}

//@Composable
//fun WeekListScreen( selectedMonth: Month?) {
//
//    val startOfMonth = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//        LocalDate.now().with(TemporalAdjusters.firstDayOfMonth())
//    } else {
//        TODO("VERSION.SDK_INT < O")
//    }
//    val endOfMonth = LocalDate.now().with(TemporalAdjusters.lastDayOfMonth())
//    val weeks = generateWeeks(startOfMonth, endOfMonth)
//
//    val filteredWeeks = weeks.filter { week ->
//        selectedMonth != null && week.startDate.month == selectedMonth
//    }
//    LazyColumn(modifier = Modifier.padding(16.dp)) {
//        items(filteredWeeks) { week ->
//            Card(modifier = Modifier
//                .fillMaxWidth()
//                .padding(8.dp)) {
//
//                    Column() {
//                    Row() {
//                        Text(text = "Weekly", textAlign = TextAlign.Start,modifier= Modifier
//                            .padding(8.dp)
//                            .weight(1f))
//                        Icon(painter = painterResource(id = R.drawable.baseline_arrow_forward_ios_24), contentDescription = "",modifier = Modifier.padding(8.dp))
//                    }
//                    Text(
//                        text = "${week.startDate.dayOfMonth} ${week.startDate.month.getDisplayName(TextStyle.SHORT, Locale.getDefault())}" +
//                                " - ${week.endDate.dayOfMonth} ${week.endDate.month.getDisplayName(TextStyle.SHORT, Locale.getDefault())}",
//                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
//                    )
//
//
//            }
//        }
//    }}
//}
//
//
//@Composable
//fun generateWeeks(startOfMonth: LocalDate, endOfMonth: LocalDate): List<Week> {
//    val weeks = mutableListOf<Week>()
//    var currentWeekStart = startOfMonth
//    while (if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            currentWeekStart.isBefore(endOfMonth)
//        } else {
//            TODO("VERSION.SDK_INT < O")
//        }
//    ) {
//        val currentWeekEnd = currentWeekStart.plusDays(6)
//        weeks.add(Week(currentWeekStart, currentWeekEnd))
//        currentWeekStart = currentWeekStart.plusWeeks(1)
//    }
//    return weeks
//}
//
//
//data class Week(val startDate: LocalDate, val endDate: LocalDate) {
//    val weekNumber: Int = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//        startDate.get(WeekFields.ISO.weekOfWeekBasedYear())
//    } else {
//        TODO("VERSION.SDK_INT < O")
//    }
//
//    // Custom formatted date range
//    val formattedDateRange: String = buildString {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            append("${startDate.dayOfMonth} ${startDate.month.getDisplayName(TextStyle.SHORT, Locale.getDefault())}")
//        }
//        if (if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                startDate.month != endDate.month
//            } else {
//                TODO("VERSION.SDK_INT < O")
//            }
//        ) {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                append(" - ${endDate.dayOfMonth} ${endDate.month.getDisplayName(TextStyle.SHORT, Locale.getDefault())}")
//            }
//        }
//    }
//}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Task_PeriodicScreenTheme(darkTheme = true) {
//        CardUIDesign("")
        MetricScreen()
    }
}