package com.example.expansetracker.feature.stats

import androidx.annotation.Size
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.expansetracker.data.module.ExpanseEntity
import com.example.expansetracker.viewModel.HomeViewModeFactory
import com.example.expansetracker.viewModel.HomeViewModel
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.math.max

@Composable
fun  GraphScreen (navController: NavController){
    val viewModel: HomeViewModel = HomeViewModeFactory(LocalContext.current).create(HomeViewModel::class.java)
    val expanses = viewModel.expanses.collectAsState()

    Scaffold (topBar = {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            contentAlignment = androidx.compose.ui.Alignment.Center
        ) {
            Text(
                text = "Income and Expanse by Month",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }){ padding ->
        Column (
            modifier = Modifier
                .fillMaxWidth()
                .padding(padding)
                .padding(horizontal = 16.dp)
        ){
           MonthlyBarGarph(expanses = expanses.value)
        }
    }
}

@Composable
fun MonthlyBarGarph(expanses: List<ExpanseEntity>){
    val months = expanses.groupBy { entity->
        entity.date?.let { date ->
            try{
                val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val parsedDate  = inputFormat.parse(date)
                SimpleDateFormat("MMMM yyyy", Locale.getDefault()).format(parsedDate ?: return@let "Unknown")
            }catch (e: Exception){
                "Unknown"
            }
        } ?: "Unknown"
    }.filter { it.key != "Unknown" }

    val sortedMonths = months.keys.sortedByDescending {
        try {
            SimpleDateFormat("MMMM yyyy", Locale.getDefault()).parse(it)?.time ?: 0
        }catch (e: Exception){
            0
        }
    }
    if (sortedMonths.isEmpty()){
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ){
            Text(
                text = "No data to display",
                fontSize = 16.sp,
                color = Color.Gray
            )
        }
        return
    }

    val data = sortedMonths.take(6).map { month ->
        val transaction = months[month]?: emptyList()
        val income = transaction.filter { it.type == "Income"}.sumOf { it.amount }.toDouble()
        val expanse = transaction.filter { it.type == "Expanse"}.sumOf { it.amount }.toDouble()
        Triple(month, income, expanse)
    }

    val maxValue = data.maxOf { max(it.second,it.third) }.toFloat()
    val barWidth = 50.dp
    val spacing = 20.dp

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
            .padding(vertical = 16.dp)
    ){
        androidx.compose.foundation.Canvas(modifier = Modifier.fillMaxSize()){
            val canvasWidth = size.width
            val canvasHeight = size.height
            val totalBars = data.size*2
            val totalSpacing = spacing.toPx() * (totalBars - 1)
            val availableWidth = canvasWidth - totalSpacing
            val barWidthPx = barWidth.toPx().coerceAtMost(availableWidth / totalBars)

            data.forEachIndexed { index,(month, income, expanse) ->
                val xOffset = index * (2* barWidthPx +  2*spacing.toPx())
                val incomeHeight = if (maxValue>0)(income.toFloat()/maxValue) * canvasHeight * 0.8f else 0f
                val expanseHeight = if (maxValue>0)(expanse.toFloat()/maxValue) * canvasHeight * 0.8f else 0f

                // Draw income bar green
                drawRect(
                    color = Color.Green,
                    topLeft = Offset(xOffset,canvasHeight - incomeHeight),
                    size = androidx.compose.ui.geometry.Size(barWidthPx,incomeHeight)
                )

                  // Draw expanse bar red
                drawRect(
                    color = Color.Red,
                    topLeft =  Offset(xOffset + barWidthPx + spacing.toPx(),canvasHeight - expanseHeight),
                    size =androidx.compose.ui.geometry.Size(barWidthPx,expanseHeight)
                )
            }
        }
    }
    Column (modifier = Modifier.fillMaxWidth()){
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            horizontalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceBetween
        ){
            data.forEach { (month,_,_) ->
                Text(
                    text = month.split(" ")[0],
                    fontSize = 12.sp,
                    modifier = Modifier.weight(1f),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
            }
        }
        Row (modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp)

        ){
            Text(
                text = "Income",
                color = Color.Green,
                fontSize = 14.sp,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = "Expense",
                color = Color.Red,
                fontSize = 14.sp,
                modifier = Modifier.weight(1f)
            )
        }
    }
}


