package com.example.expansetracker.feature.home


import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.expansetracker.R
import com.example.expansetracker.data.module.ExpanseEntity
import com.example.expansetracker.feature.entity_list.DeleteConfirmationDialog
import com.example.expansetracker.ui.theme.XtraBold
import com.example.expansetracker.ui.theme.Zinc
import com.example.expansetracker.ui.theme.Zinca
import com.example.expansetracker.ui.theme.variableFont
import com.example.expansetracker.viewModel.HomeViewModeFactory
import com.example.expansetracker.viewModel.HomeViewModel
import com.example.expansetracker.viewModel.calculateOverallTotals
import java.text.SimpleDateFormat
import java.time.YearMonth
import java.util.Calendar
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(navController:NavController){
    val viewModel: HomeViewModel =  HomeViewModeFactory(LocalContext.current).create(HomeViewModel::class.java)
    val expanses = viewModel.expanses.collectAsState()


    // Dynamic greeting based on time of day
    val greeting = remember {
        val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        when(hour){
            in 4..12 -> "Good Morning"
            in 12..17 -> "Good Afternoon"
            in 17..18 -> "Good Evening"
            else -> "Good Night"
        }
    }
    Surface(modifier = Modifier.fillMaxSize(), color = Color.White){
        ConstraintLayout(modifier = Modifier.fillMaxSize()){
            val (nameRow,list,card,topBar,add) = createRefs()
            Image(painter = painterResource(id = R.drawable.topbar), contentDescription = null,
                modifier = Modifier.constrainAs(topBar){
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                )
            Image(painter = painterResource(id = R.drawable.ellipse_small), contentDescription = null,
                    modifier = Modifier.padding(horizontal = 140.dp)
                )
            Image(painter = painterResource(id = R.drawable.ellipse_medium), contentDescription = null,
                  modifier = Modifier.padding(horizontal = 70.dp)
                )
            Image(painter = painterResource(id = R.drawable.ellipse_large), contentDescription = null,
                //Modifier.padding
                )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 64.dp, start = 16.dp)
                    .constrainAs(nameRow){
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
            ){
                Column{
                     Text(text = greeting,
                         fontSize = 16.sp,
                         color = Color.White,
                         fontFamily = XtraBold,
                         style = androidx.compose.material3.MaterialTheme.typography.headlineLarge,
                     )
                  //  Text(text = "CodeWithSudhir", fontSize =20.sp, fontWeight = FontWeight.Bold, color = Color.White )
                }
                /*
                Image(painter = painterResource(id = R.drawable.notification),
                    contentDescription = null,
                    modifier = Modifier.align(Alignment.CenterEnd)
                )*/
            }
            CardItem(
                modifier = Modifier
                    .constrainAs(card) {
                        top.linkTo(nameRow.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    },
                balance = viewModel.getBalance(expanses.value),
                income = viewModel.getTotalIncome(expanses.value),
                expanses = viewModel.getTotalExpanse(expanses.value),
            )
            TransactionList(modifier = Modifier.fillMaxWidth().constrainAs(list){
                top.linkTo(card.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                bottom.linkTo(parent.bottom)
                height = Dimension.fillToConstraints
            }, list = expanses.value, viewModel = viewModel)


                Image(
                    painter = painterResource(id = R.drawable.add),
                    contentDescription = null,
                    modifier = Modifier.constrainAs(add) {
                        bottom.linkTo(parent.bottom)
                        end.linkTo(parent.end)
                    }.size(100.dp).clip(CircleShape).clickable {
                        navController.navigate("/add")
                    })
        }
    }
}
@Composable
fun CardItem(modifier: Modifier, balance: String, income: String, expanses: String){
Column(modifier= modifier
    .padding(16.dp)
    .fillMaxWidth()
    .height(200.dp)
    .clip(RoundedCornerShape(16.dp))
    .background(Zinc)
    .padding(16.dp)
) {
    Box(modifier = Modifier.fillMaxWidth().weight(1f)) {
        Column(modifier = Modifier.align(Alignment.Center)) {
            Text(text = "Total Balance", fontSize = 16.sp, color = Color.White)
            Text(text = balance,
                   fontSize = 20.sp,
                   fontWeight = FontWeight.Bold,
                   color =  Color.White
            )
        }
       /* Image(painter = painterResource(id = R.drawable.dots_menu),
            contentDescription = null,
            modifier = Modifier.align(Alignment.CenterEnd)
        )*/
    }
    Box(modifier= Modifier.fillMaxWidth().weight(1f)){
        CardRowItem(
            modifier = Modifier.align(Alignment.CenterStart),
            title = "Income",
            amount = income,
            image = R.drawable.income
        )
       CardRowItem(
           modifier= Modifier.align(Alignment.CenterEnd),
           title = "Expense",
           amount = expanses,
           image = R.drawable.expense
       )
    }
}
}
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TransactionList(modifier: Modifier, list: List<ExpanseEntity>, viewModel: HomeViewModel) {
    //Group transaction by month
    val transactionByMonth = list.groupBy { entity ->
        try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
            val date = inputFormat.parse(entity.date)
            SimpleDateFormat("MMMM-yyyy", Locale.getDefault()).format(date)

        } catch (e: Exception) {
            // Fallback in case of parsing error
            entity.date
            // "Empty"
        }
    }

    var showDeleteDialog by remember { mutableStateOf(false) }
    var titleToDelete by remember{ mutableStateOf<String?>(null) }

    val expandStates = remember {
        mutableStateMapOf<String, Boolean>().apply {
            transactionByMonth.keys.forEach { this[it] = true }
        }
    }
    LazyColumn(
        modifier = modifier
            .padding(horizontal = 16.dp)
    ) {
        item {
            Box(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Recent Transaction",
                    fontSize = 15.sp,
                    color = Color.Black,
                    fontFamily = variableFont,
                    fontWeight = FontWeight.W900
                )
                Text(
                    text = "Clear All", fontFamily = XtraBold,
                    fontSize = 16.sp,
                    color = Color.White,
                    modifier = Modifier.align(Alignment.CenterEnd)
                        .clickable {
                            titleToDelete = "All Transactions"
                            showDeleteDialog = true
                        }
                        .background(Zinc),
                )
            }
        }
        transactionByMonth.forEach { (month, transaction) ->
            item(key = month) {
                var isExpanded = expandStates[month] ?: true

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .clickable { expandStates[month] = !(expandStates[month] ?: true) },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = month,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = if (isExpanded) "Show" else "Hide",
                        fontSize = 18.sp,
                        color = Zinca,
                        fontWeight = FontWeight.Bold,
                        fontFamily = XtraBold
                    )
                }
                if(showDeleteDialog && titleToDelete != null){
                    DeleteConfirmationDialog(
                        listTitle = titleToDelete!!,
                        onConfirm = {
                            viewModel.deleteAllTransaction()
                            titleToDelete = null
                            showDeleteDialog = false
                        },
                        onDismiss = {
                            titleToDelete = null
                            showDeleteDialog = false
                        }
                    )
                }
                if (!isExpanded) {
                    transaction.reversed().forEach { item ->
                        TransactionItem(
                            title = item.title,
                            amount = item.amount.toString(),
                            icon = viewModel.getItemIcon(item),
                            date = item.date.toString(),
                            color = if (item.type == "Income") Color.Green else Color.Red,
                            onDelete = {
                                viewModel.deleteTransaction(item)
                            }
                        )
                        Spacer(modifier = Modifier.height(1.dp))
                    }
                }
            }
        }
    }
}


@SuppressLint("DefaultLocale")
@Composable
fun CardRowItem(modifier: Modifier,title: String,amount: String,image: Int){
Column(modifier = modifier){
    Row{
       Image(painter = painterResource(id = image), contentDescription = null)
        Spacer(modifier= Modifier.size(8.dp))
        Text(text =  title, fontSize = 16.sp, color = Color.White)
    }
    Text(text = amount, fontSize = 20.sp, color = Color.White)
}
}
@Composable
fun TransactionItem(title: String,amount: String,icon: Int,date: String,color: Color,onDelete: ()->Unit){
    Box(modifier = Modifier
        .fillMaxWidth()
        .padding(1.dp)
        .shadow(8.dp)
        .clip(RoundedCornerShape(2.dp))
        .background(Color.White)
        .pointerInput (Unit){
            detectTapGestures(
                onDoubleTap = {
                    onDelete()
                }
            )
        }
    ){
        Row(modifier = Modifier.padding(8.dp)){
            Image(painter = painterResource(id = icon),
                contentDescription = null,
                modifier = Modifier.size(50.dp)
            )
            Spacer(modifier = Modifier.size(8.dp))
            Column{
                Text(text = title, fontSize = 16.sp, color = Color.Black)
                Text(text = date, fontSize = 12.sp, color = Color.Black)
            }
        }
        Text(
            text = "₹$amount",
            fontSize = 20.sp,
            modifier = Modifier.align(Alignment.CenterEnd),
            color = color
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
@Preview(showBackground = true)
fun PreviewHomeScreen(){
    HomeScreen(rememberNavController())
}