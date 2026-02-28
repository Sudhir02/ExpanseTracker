package com.example.expansetracker.feature.entity_list

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import com.example.expansetracker.viewModel.EntityListViewModel
import com.example.expansetracker.viewModel.EntityListViewModelFactory
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.TextField
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.expansetracker.ui.theme.Zinca
import com.example.expansetracker.ui.theme.variableFont
import com.example.expansetracker.R
import com.example.expansetracker.ui.theme.XtraBold

@Composable
fun TitleList(navController: NavController) {
    val viewModel = EntityListViewModelFactory(LocalContext.current).create(EntityListViewModel::class.java)
    Log.d("titleList","ViewModel initialized")

    val listTitle by viewModel.listTitle.collectAsState(initial = emptyList())
    Log.d("titleList","List Title collected: $listTitle")



    var newListName by remember { mutableStateOf("") }

    Surface (modifier = Modifier.fillMaxSize(), color = Color.White){
           ConstraintLayout (modifier = Modifier.fillMaxWidth()){
               val(nameRow,list,title) = createRefs()
               Image(painter = painterResource(id = R.drawable.topbar), contentDescription = null,
                 modifier = Modifier
                     .constrainAs(nameRow) {
                         top.linkTo(parent.top)
                         start.linkTo(parent.start)
                         end.linkTo(parent.end)
                     }
               )
               Image(painter = painterResource(id = R.drawable.ellipse_small), contentDescription = null,
                   modifier = Modifier.padding(horizontal = 140.dp))
               Image(painter = painterResource(id = R.drawable.ellipse_medium), contentDescription = null,
                     modifier = Modifier.padding(horizontal = 70.dp)
                   )
               Image(painter = painterResource(id = R.drawable.ellipse_large), contentDescription = null)
           }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ){
            Text(text = "Create List",
                fontSize = 30.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White,
                fontFamily = variableFont,
                modifier = Modifier.padding(16.dp)
            )
            OutlinedTextField(
                value = newListName,
                onValueChange = {newListName = it},
                label = { Text(text = "Create List Name", fontFamily = variableFont) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    focusedLabelColor = Color.White,
                    unfocusedLabelColor = Color.White,
                    focusedBorderColor = Color.White,
                    unfocusedBorderColor = Color.Gray
                )
            )
            Button (onClick = {
                if (newListName.isNotBlank()) {
                    viewModel.insertListTitle(newListName)
                    newListName = ""
                  //  navController.navigate("/TitleListPage")
                }
            },
                modifier = Modifier
                     .fillMaxWidth()
                    .padding(13.dp),
                   // .shadow(8.dp, shape = RoundedCornerShape(20.dp)),
                colors = ButtonDefaults.buttonColors(
                    contentColor = Color.White,
                    containerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,

                ),
                enabled = newListName.isNotBlank()
            ){
                Text(text = "Create List",
                    color = Color.White,
                    fontWeight = FontWeight.ExtraBold,
                    fontFamily = XtraBold
                )
            }
            Spacer(modifier = Modifier.height(15.dp))
                    ListTitlePage(navController)
        }
    }
}
@Composable
@Preview(showBackground = true)
fun TitleListPreview() {
    TitleList(rememberNavController())
}