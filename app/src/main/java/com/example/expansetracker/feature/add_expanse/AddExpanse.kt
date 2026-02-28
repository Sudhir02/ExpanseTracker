@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.expansetracker.feature.add_expanse


import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.expansetracker.R
import com.example.expansetracker.data.module.ExpanseEntity
import com.example.expansetracker.ui.theme.OutBorder
import com.example.expansetracker.ui.theme.XtraBold
import com.example.expansetracker.viewModel.AddExpanseViewModal
import com.example.expansetracker.viewModel.AddExpanseViewModal.AddExpanseViewModalFactory
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import java.time.LocalDate
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import kotlinx.coroutines.launch
import java.lang.Compiler.enable
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AddExpanse(navController: NavController){
    val viewModel = AddExpanseViewModalFactory(LocalContext.current).create(AddExpanseViewModal::class.java)
    val coroutineScope = rememberCoroutineScope()

    Surface(modifier = Modifier.fillMaxSize(), color = Color.White){
        ConstraintLayout(modifier = Modifier.fillMaxWidth()){
            val(nameRow,card,topBar) = createRefs()
            Image(painter = painterResource(id = R.drawable.topbar),
                contentDescription = null,
                modifier = Modifier.constrainAs(topBar){
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
            )
            Image(painter = painterResource(id = R.drawable.ellipse_large), contentDescription = null)
            Image(painter = painterResource(id = R.drawable.ellipse_medium),
                contentDescription = null,
                modifier = Modifier.padding(horizontal = 70.dp)
                )
            Image(painter = painterResource(id = R.drawable.ellipse_small),
                contentDescription = null,
                modifier = Modifier.padding(horizontal = 140.dp)
            )
            Box(modifier = Modifier.fillMaxWidth()
                .padding(top = 60.dp, start = 16.dp, end = 16.dp,)
                    .constrainAs(nameRow){
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
                ){
                Image(painter = painterResource(id = R.drawable.icon),
                    contentDescription = null,
                    modifier = Modifier.align(Alignment.CenterStart).clickable { navController.navigate("/home") }
                )
                Text(
                    text = "Add Expense",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.padding(16.dp).align(Alignment.Center),
                    fontFamily = XtraBold
                )
            }
            DataForm(modifier = Modifier.padding(top = 60.dp).constrainAs(card){
                top.linkTo(nameRow.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }, onExpanseClick = {
                coroutineScope.launch {
                 viewModel.addExpanse(it)
                    navController.popBackStack()
                }
            })
        }
        }
}
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DataForm(modifier: Modifier,onExpanseClick:(model: ExpanseEntity)->Unit){
    val name = remember { mutableStateOf("") }
    val amount = remember { mutableStateOf("") }
    var pickedDate: LocalDate by remember { mutableStateOf(LocalDate.now()) }
    var formattedDate = remember{derivedStateOf{
        DateTimeFormatter
            .ofPattern("MMM/dd/yyyy")
            .format(pickedDate)

    } }
    val dateDialogState = rememberMaterialDialogState()
    val category = remember { mutableStateOf("") }
    val type = remember { mutableStateOf("")}

    val isFormValid by remember {
        derivedStateOf {
            var i=0
            name.value.isNotEmpty() &&
                    amount.value.isNotEmpty() && amount.value.toDoubleOrNull() != null &&
                    amount.value != "0" && amount.value != "0.0" && amount.value != "0.00" &&
                    amount.value != "0." && amount.value != ".0" && amount.value != ".00" &&
                    category.value.isNotEmpty() && category.value != "Select Category" &&
                    type.value.isNotEmpty() && type.value != "Type"
        }
    }
    Column(modifier=modifier.padding(16.dp).fillMaxWidth()
        .shadow(16.dp)
        .clip(RoundedCornerShape(16.dp))
        .background(Color.White)
        .padding(16.dp)
        .verticalScroll(rememberScrollState())
    ){
       //Text(text = "Name", fontSize = 14.sp, color = Color.Black)
        Spacer(modifier = Modifier.size(8.dp))
        OutlinedTextField(value = name.value, onValueChange = {
            name.value= it
        },modifier = Modifier.fillMaxWidth()
            , placeholder = { Text(text = "Enter Name", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color.Gray) },
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedTextColor = Color.Gray,
                focusedTextColor = Color.Black,
                focusedBorderColor = OutBorder
            ))
        Spacer(modifier= Modifier.size(4.dp))
        //Text(text = "Amount", fontSize = 14.sp, color = Color.Black)
        Spacer(modifier = Modifier.size(8.dp))
        OutlinedTextField(value = amount.value, onValueChange = {
            amount.value= it
        }, modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(text = "Enter Amount", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color.Gray) },
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedTextColor = Color.Gray,
                focusedTextColor = Color.Black,
                focusedBorderColor = OutBorder
            ),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Decimal
            )

        )

        //date picker
       // Text(text = "Date", fontSize = 14.sp, color = Color.Black)
        Spacer(modifier = Modifier.size(8.dp))
        OutlinedTextField(value = formattedDate.value, onValueChange = { },
            modifier = Modifier.fillMaxWidth(),
                //.clickable { dateDialogState.show() },
            placeholder = { Text(text = "Select Date", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color.Black) },
            trailingIcon = {
                IconButton(onClick = { dateDialogState.show() }) {
                    Image(painter = painterResource(id = R.drawable.icons_calendar), contentDescription = null)
                }
            },
            enabled = true,
            readOnly = true,
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedTextColor = Color.Gray,
                focusedTextColor = Color.Black,
                focusedBorderColor = OutBorder
                //disabledTextColor = Color.Red


            )
            )
           MaterialDialog(
               dialogState = dateDialogState,
               buttons = {
                   positiveButton(text = "Ok")
                   negativeButton(text = "Cancel")
               }
           ){
             datepicker(
                 initialDate = LocalDate.now(),
                 title = "Pick a date",
                 allowedDateValidator = {
                     it.dayOfMonth % 1 == 0 
                 }
             ){
                    pickedDate = it
             }
           }
        // dropdown
        //Text(text = "Category", fontSize = 14.sp, color = Color.Black)
        Spacer(modifier = Modifier.size(8.dp))
        ExpanseDropDown(
                listOf("Select Category","Netflix","Paypal","Salary","Transfer","Youtube","Upwork","Grocery","Shopping","Tea","Fuel","Money","Coffee","Rent","Bills","Other"),
            onItemSelected = { category.value = it}
        )
        //type
        //Text(text = "Type", fontSize = 14.sp, color = Color.Black)
        Spacer(modifier = Modifier.size(8.dp))
        ExpanseDropDown(
            listOf("Type","Income","Expense"),
            onItemSelected = {
                type.value = it
            }
        )
        Spacer(modifier = Modifier.size(16.dp))
        Button(onClick = {
                val model = ExpanseEntity(
                null,
                name.value,
                amount.value.toDoubleOrNull() ?: 0.0,
                    formattedDate.value,
                category.value,
                type.value
            )
            onExpanseClick(model)
        },modifier= Modifier
            .clip(RoundedCornerShape(8.dp))
            .fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = OutBorder,
                disabledContainerColor = Color.Gray

            ),
            enabled = isFormValid){
          Text(text = "Add Expense", fontSize = 14.sp, color = Color.White, fontFamily = XtraBold)
        }
    }
}

    @Composable
    fun ExpanseDropDown(listOfItems: List<String>, onItemSelected:(item: String)->Unit){
           val expanded = remember {
               mutableStateOf(false)
           }
           val selectedItem = remember {
               mutableStateOf<String>(listOfItems[0])
           }
        ExposedDropdownMenuBox(expanded = expanded.value, onExpandedChange = {expanded.value = it}){
            TextField(value = selectedItem.value,onValueChange = {},
                modifier = Modifier
                    .fillMaxWidth()
                   .background(Color.LightGray)
                    .menuAnchor(),
                readOnly = true,
               // placeholder = { Text(text = "Select Category", color = Color.Black) },
                colors = TextFieldDefaults.textFieldColors(
                    unfocusedTextColor = Color.Gray,
                    focusedTextColor = Color.Black,
                    containerColor = Color.White,
                    focusedIndicatorColor = OutBorder,
                ),
                trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded   = expanded.value)
                }
                )
            ExposedDropdownMenu(
                expanded = expanded.value, onDismissRequest = {expanded.value = false}, modifier = Modifier.background(Color.White)
            ){
                listOfItems.forEach {
                    DropdownMenuItem(text = {Text(text = it, color = Color.Black)}, onClick = {
                        selectedItem.value = it
                        onItemSelected(selectedItem.value)
                        expanded.value = false})
                }
            }
        }
    }

@RequiresApi(Build.VERSION_CODES.O)
@Composable
@Preview(showBackground = true)
fun AddExpansePreview(){
    AddExpanse(rememberNavController())
}
