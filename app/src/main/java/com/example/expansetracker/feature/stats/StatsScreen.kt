package com.example.expansetracker.feature.stats


import android.view.LayoutInflater
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import com.example.expansetracker.R
import com.example.expansetracker.Utils
import com.example.expansetracker.ui.theme.Zinc
import com.example.expansetracker.viewModel.StatsViewModel
import com.example.expansetracker.viewModel.StatsViewModelFactory
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineDataSet
//import androidx.hilt.navigation.compose.hiltViewModel


@Composable
fun StatsScreen(navController: NavController) {
    Scaffold ( containerColor = Color.White, topBar = {
        Box(modifier = Modifier.fillMaxWidth()
            .padding(top = 64.dp, start = 16.dp, end = 16.dp, bottom = 8.dp)
        ){
            Image(painter = painterResource(id = R.drawable.icon),contentDescription = null,
                modifier = Modifier.align(Alignment.CenterStart),
                colorFilter = androidx.compose.ui.graphics.ColorFilter.tint(Zinc)
                )

            Text(text = "Statistics",
                fontSize = 18.sp,

                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(16.dp)
                    .align (Alignment.Center),
                color = Zinc
            )
            Image(painter = painterResource(id = R.drawable.dots_menu),
            contentDescription = null,
                modifier = Modifier.padding(16.dp)
                    .align(Alignment.CenterEnd),
                colorFilter = androidx.compose.ui.graphics.ColorFilter.tint(Zinc)
            )
        }
    }){
        val viewModel  = StatsViewModelFactory(navController.context).create(StatsViewModel::class.java)
        val dataState = viewModel.entries.collectAsState(emptyList())
        Column (modifier = Modifier.padding(it)){
         val entries = viewModel.getEntriesForChart(dataState.value)
            LineChart(entries = entries)
        }
    }
}



@Composable
fun LineChart(entries: List<Entry>) {
    val context = LocalContext.current
    AndroidView(factory = {
        val view = LayoutInflater.from(context).inflate(R.layout.stats_line_chart,null)
        view
    }, modifier = Modifier.height(250.dp).fillMaxWidth()) {view->
       val lineChart = view.findViewById<LineChart>(R.id.lineChart)
        val dataSet = LineDataSet(entries,"Expenses").apply{
            color = android.graphics.Color.parseColor("#FF2F7E79")
            valueTextColor = android.graphics.Color.BLACK
            lineWidth = 3f
            axisDependency = YAxis.AxisDependency.RIGHT
            setDrawFilled(true)
            mode = LineDataSet.Mode.CUBIC_BEZIER
            valueTextSize = 12f
            valueTextColor = android.graphics.Color.parseColor("#FF2F7E79")
        }
        lineChart.xAxis.valueFormatter =
            object : com.github.mikephil.charting.formatter.ValueFormatter() {
               override fun getFormattedValue(value: Float): String {
                    return Utils.formatDateForChart(value.toLong())
                }
            }

        lineChart.data = com.github.mikephil.charting.data.LineData(dataSet)
       lineChart.axisLeft.isEnabled = false
        lineChart.axisRight.isEnabled = false
        lineChart.axisRight.setDrawGridLines(false)
       lineChart.axisLeft.setDrawGridLines(false)
        lineChart.xAxis.setDrawGridLines(false)
        lineChart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        lineChart.invalidate()

    }
}