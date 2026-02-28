package com.example.expansetracker.feature.entity_list




import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.example.expansetracker.data.module.ListEntity
import com.example.expansetracker.feature.add_expanse.ExpanseDropDown
import com.example.expansetracker.ui.theme.OutBorder
import com.example.expansetracker.ui.theme.XtraBold
import com.example.expansetracker.ui.theme.Zinca
import com.example.expansetracker.ui.theme.variableFont
import com.example.expansetracker.viewModel.EntityListViewModel
import com.example.expansetracker.viewModel.EntityListViewModelFactory
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
 fun EntityListPage(navController: NavController,selectedTitle: String = "") {
      val viewModel = EntityListViewModelFactory(LocalContext.current).create(EntityListViewModel::class.java)

     //Collect the list of entities from the ViewModel as a State
     val entities by viewModel.entites.collectAsState(initial = emptyList())

    //Filter entities by selected title (assuming entities have a listTitle field)
    val filteredEntities = entities.filter { it.listTitle == selectedTitle }

    // Local state for input fields
    var newEntityName by remember { mutableStateOf("") }
    var newEntityQuantity by remember { mutableStateOf("") }

    var newQuantityType by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
   // var expanded by remember { mutableStateOf(false) }  // For DropDown Visibility

   // val quantityType = listOf("gm","ग्राम","Kg","कि.ग्रा.","L","ली","Dozen","दर्जन","Pcs")  // Available Quantity type


    // Coroutine scope for launching suspend function
    val scope = rememberCoroutineScope()

  val isValidForm by remember {
      derivedStateOf {
            newQuantityType != "Select Quantity Type" && newQuantityType != ""
      }
  }


    Scaffold(
        containerColor = Color.White
    ) { padding ->
        Column(modifier = Modifier.padding()) {
                Card(
                    modifier = Modifier.shadow(8.dp).padding(8.dp).fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    )
                ) {
                    Column(modifier = Modifier.padding(8.dp)) {
                       //Display selected title
                        Text(
                            text = "List: $selectedTitle",
                            style = MaterialTheme.typography.headlineSmall,
                            modifier = Modifier.padding(8.dp),
                            color = Zinca,
                            fontFamily = XtraBold
                        )

                        //Input fields for new entity
                        TextField(
                            value = newEntityName,
                            onValueChange = { newEntityName = it },
                            placeholder = { Text(text = "Enter Entity Name", color = Color.Gray) },
                            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                            colors = TextFieldDefaults.colors(
                                focusedTextColor = Zinca,
                                unfocusedTextColor = Color.Gray,
                                focusedContainerColor = Color.White,
                                unfocusedContainerColor = Color.White,
                                focusedIndicatorColor = Zinca,
                                focusedLabelColor = Color.Black
                            )
                        )
                        TextField(
                            value = newEntityQuantity,
                            onValueChange = { newEntityQuantity = it },
                            placeholder = { Text(text = "Enter Quantity", color = Color.Gray) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 8.dp),
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Decimal
                            ),
                            colors = TextFieldDefaults.colors(
                                focusedTextColor = Zinca,
                                unfocusedTextColor = Color.Gray,
                                focusedContainerColor = Color.White,
                                unfocusedContainerColor = Color.White,
                                focusedIndicatorColor = Zinca,
                                focusedLabelColor = Color.Black
                            )
                        )
//                        TextField(
//                            value = price,
//                            onValueChange = { price = it },
//                            placeholder = { Text(text = "Enter Price", color = Color.Gray) },
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .padding(bottom = 8.dp),
//                            keyboardOptions = KeyboardOptions(
//                                keyboardType = KeyboardType.Decimal
//                            ),
//                            colors = TextFieldDefaults.colors(
//                                focusedTextColor = Zinca,
//                                unfocusedTextColor = Color.Gray,
//                                focusedContainerColor = Color.White,
//                                unfocusedContainerColor = Color.White,
//                                focusedIndicatorColor = Zinca,
//                                focusedLabelColor = Color.Black
//                            )
//                        )
                        ExpanseDropDown(
                            listOf("Select Quantity Type","gm","ग्राम","Kg","कि.ग्रा.","L","ली","Dozen","दर्जन","Pcs"),
                            onItemSelected = { selectedItem ->
                                newQuantityType = selectedItem
                            },
                        )
             //           ExposedDropdownMenuBox (expanded = expanded, onExpandedChange = {expanded = it}) {
//                            TextField(
//                                value = newQuantityType,
//                                onValueChange = { },
//                                readOnly = true,
//                                placeholder = {
//                                    Text(
//                                        text = "Select Quantity Type (e.g., KG, L)",
//                                        color = Color.Gray
//                                    )
//                                },
//                                modifier = Modifier
//                                    .fillMaxWidth()
//                                    .padding(bottom = 8.dp)
//                                    .clickable { expanded = true }
//                                    .menuAnchor(),
//                                colors = TextFieldDefaults.textFieldColors(
//                                    containerColor = Color.White,
//                                    focusedTextColor = Color.Black,
//                                    unfocusedTextColor = Color.Gray,
//                                    focusedIndicatorColor = Zinca,
//                                    unfocusedIndicatorColor = Color.Gray,
//                                    ),
//                                trailingIcon = {
//                                    androidx.compose.material3.Icon(
//                                        imageVector = androidx.compose.material.icons.Icons.Default.Menu,
//                                        contentDescription = null,
//                                        modifier = Modifier.clickable { expanded = true },
//                                        tint = Zinca
//                                    )
//                                },
//                                enabled = false
//                            )
//
//                            ExposedDropdownMenu(
//                                expanded = expanded,
//                                onDismissRequest = { expanded = false },
//                                modifier = Modifier.background(Color.White).shadow(5.dp)
//                            ) {
//
//                                quantityType.forEach { type ->
//                                    DropdownMenuItem(
//                                        text = { Text(text = type, color = Color.Black) },
//                                        onClick = {
//                                            newQuantityType = type
//                                            expanded = false
//                                        },
//                                        // modifier = Modifier.fillMaxWidth()
//                                    )
//                                }
//                            }
//                        }
                        Button(
                            onClick = {
                                val quantity = newEntityQuantity.toDoubleOrNull() ?: 0.0
                                if (newEntityName.isNotBlank() && quantity > 0) {
                                    val model = ListEntity(
                                        id = 0, // Auto-generated by Room, so set to 0
                                        entity = newEntityName,
                                        quantity = quantity,
                                        listTitle = selectedTitle,
                                        quantityType = newQuantityType,
                                        price = price.toDoubleOrNull() ?: 0.0
                                    )
                                    // Call ViewModel function to add entity
                                    scope.launch {
                                        viewModel.addEntity(model)
                                    }
                                    newEntityName = ""
                                    newEntityQuantity = ""
                                    newQuantityType = ""
                                }
                            }, modifier = Modifier.fillMaxWidth().padding(8.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF4CAF50),
                                contentColor = Color.White,
                                disabledContainerColor = Color.Gray,
                            ),
                            enabled = isValidForm
                        ) {
                            Text(text = "Add Item", color = Color.White, fontFamily = XtraBold)
                        }
                    }
                }
                 Card{
                     LazyColumn {
                         items(filteredEntities.reversed()) { entity ->
                             EntityItem(entity = entity, onDelete  = {
                                 scope.launch { viewModel.deleteEntity(entity) }
                             })
                         }
                    }
                }
            }
        }
    }




@Composable
fun EntityItem(entity: ListEntity,onDelete: () -> Unit) {
    val viewModel =
        EntityListViewModelFactory(LocalContext.current).create(EntityListViewModel::class.java)
    //State for delete dialog
//    var showDeleteDialog by remember { mutableStateOf(false) }
//    var entityToDelete by remember { mutableStateOf<String?>(null) }

    //State for checkbox
    var isChecked by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Zinca)
            .pointerInput(Unit) {
                detectTapGestures(
                    onDoubleTap = { onDelete() }
                )
            }
    ) {
        Row (modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = isChecked,
                onCheckedChange = { isChecked = it },
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .weight(1f),
                colors = CheckboxDefaults.colors(
                    checkedColor = Color.White,
                    uncheckedColor = Color.White,
                    checkmarkColor = Zinca
                )

            )
            Text(
                text = entity.entity,
                style = MaterialTheme.typography.bodyMedium.copy(
                    textDecoration = if (isChecked) TextDecoration.LineThrough else TextDecoration.None
                ),
                color = Color.White,
                modifier = Modifier.weight(1f),
                fontFamily = XtraBold
            )
            Text(
                text = "${entity.quantity} ${entity.quantityType}", //Adjust based on your quantity type logic
                style = MaterialTheme.typography.bodyMedium.copy(
                    textDecoration = if (isChecked) TextDecoration.LineThrough else TextDecoration.None

                ),
                color = Color.White,
                modifier = Modifier.weight(1f),
                fontFamily = XtraBold
            )
            IconButton(
                onClick = {
                    onDelete()
                },
                modifier = Modifier
                    .weight(1f)
            ) {
                androidx.compose.material3.Icon(
                    imageVector = androidx.compose.material.icons.Icons.Default.Clear,
                    contentDescription = "More Options",
                    tint = Color.White,
                )
            }
        }
    }
}

@Composable
@Preview
fun EntityListPreview(){
    val navController = rememberNavController()
    EntityListPage(navController, selectedTitle = "Groceries")
}


