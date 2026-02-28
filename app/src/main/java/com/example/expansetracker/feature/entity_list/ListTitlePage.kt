package com.example.expansetracker.feature.entity_list


import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row


import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.example.expansetracker.viewModel.EntityListViewModel
import com.example.expansetracker.viewModel.EntityListViewModelFactory
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import com.example.expansetracker.ui.theme.Zinca
import androidx.compose.material3.IconButton
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.room.util.TableInfo
import com.example.expansetracker.ui.theme.XtraBold
import com.example.expansetracker.ui.theme.variableFont
import com.example.expansetracker.R

@Composable
fun ListTitlePage (navController: NavController) {
    val viewModel =
        EntityListViewModelFactory(LocalContext.current).create(EntityListViewModel::class.java)
    Log.d("ListTitlePage", "ViewModel initialized")
    val listTitle by viewModel.listTitle.collectAsState(initial = emptyList())
    Log.d("ListTitlePage", "List Title collected: $listTitle")

    var showDeleteDialog by remember { mutableStateOf(false) }
    var titleToDelete by remember{ mutableStateOf<String?>(null) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        verticalArrangement = Arrangement.spacedBy(1.dp)
    ) {
        item {
            Text(
                text = "Existing Lists",
                style = androidx.compose.material3.MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(horizontal = 8.dp),
                color = Color.Black,
                fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                fontFamily = variableFont
            )
        }
        if (listTitle.isEmpty()) {
            item {
                Column {
                    Text(
                        text = "No lists available",
                        color = Color.Gray,
                        modifier = Modifier.padding(8.dp)
                    )
                    Icon(
                        imageVector = Icons.Default.List,
                        contentDescription = "No lists",
                        tint = Color.Gray,
                        modifier = Modifier
                            .padding(3.dp)
                            .size(100.dp)

                        )
                }
            }
        } else {
            items(listTitle.reversed()) { title ->

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 2.dp, horizontal = 5.dp)
                        .clickable {
                            Log.d(
                                "ListTitlePage",
                                "Navigating to entity_list with title: $title"
                            )
                            navController.navigate("/entity_list?title=$title")
                        }
                    //.padding(8.dp),
                    ,
                    colors = CardDefaults.cardColors(
                        contentColor = Color.White,
                        containerColor = Zinca,
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 10.dp)
                            .fillMaxWidth(),
                        //verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = title,
                            style = androidx.compose.material3.MaterialTheme.typography.titleMedium,
                            modifier = Modifier
                                //.padding(16.dp)
                                .align(Alignment.CenterStart),
                            fontFamily = XtraBold
                            )

                        IconButton(
                            onClick = {
                                titleToDelete = title
                                showDeleteDialog = true
                            },
                            modifier = Modifier
                                .align(Alignment.CenterEnd)
                                .padding(8.dp)
                            ) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = "Delete list",
                            )
                        }
                    }
                }
            }
            }
        }
    if(showDeleteDialog && titleToDelete != null){
         DeleteConfirmationDialog(
             listTitle = titleToDelete!!,
             onConfirm = {
                 viewModel.deleteListTitle(titleToDelete!!)
                 titleToDelete = null
                 showDeleteDialog = false
             },
             onDismiss = {
                 titleToDelete = null
                 showDeleteDialog = false
             }
         )
    }
    }
