package com.example.expansetracker

import androidx.compose.ui.graphics.Color
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.annotation.Size
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.expansetracker.feature.add_expanse.AddExpanse
import com.example.expansetracker.feature.entity_list.EntityListPage
import com.example.expansetracker.feature.entity_list.ListTitlePage
import com.example.expansetracker.feature.entity_list.TitleList
import com.example.expansetracker.feature.home.HomeScreen
import com.example.expansetracker.feature.home.OnboardingScreen
import com.example.expansetracker.feature.stats.GraphScreen
import com.example.expansetracker.feature.stats.StatsScreen
import com.example.expansetracker.ui.theme.Zinc

/*@OptIn(ExperimentalAnimationApi::class)
@Composable
fun FlipScreen(navController: NavController,screenContent: @Composable () -> Unit){
    AnimatedContent (
        targetState = screenContent,
        transitionSpec = {
            slideInHorizontally{it} + fadeIn() with slideOutHorizontally{ -it } + fadeOut()
        }
    ){

    }
}*/
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NavHostScreen(){
    val navController = rememberNavController()
    var bottomBarVisibility by remember {
        mutableStateOf(true)
    }
    Scaffold (containerColor = MaterialTheme.colorScheme.background, bottomBar = {
        AnimatedVisibility(visible = bottomBarVisibility) {
            NavigationBottomBar(
                navController = navController,
                items = listOf(
                    NavItem(route = "/home", icon = R.drawable.home),
                    NavItem(route = "/stats", icon = R.drawable.ic_stats),
                    NavItem(route = "/TitleList", icon = R.drawable.list)
                )
            )
        }
    }){
        NavHost(navController = navController, startDestination = "/onBoarding", modifier = Modifier.padding(it)){
            composable (route = "/onBoarding",
                enterTransition = { slideInVertically(
                    initialOffsetY = {it},
                    animationSpec = tween(durationMillis = 700)
                )},
                exitTransition = {fadeOut(animationSpec = tween(700))}
            ){
                bottomBarVisibility = false
                OnboardingScreen(navController)
             }
            composable(route = "/home",
                enterTransition = {slideInHorizontally(
                    initialOffsetX = {it},
                    animationSpec = tween(durationMillis = 30)
                )},
                exitTransition = {
                    slideOutHorizontally(
                        targetOffsetX = {-it},
                        animationSpec = tween(durationMillis = 30)
                    )
                }
                ) {
                bottomBarVisibility = true
                HomeScreen(navController)
            }
            composable(route = "/add",
                enterTransition = {
                    slideInHorizontally(
                     initialOffsetX = {fullWidth -> fullWidth},
                        animationSpec = tween(durationMillis = 30)
                    )
                },
                exitTransition = {slideOutHorizontally(
                    targetOffsetX = {fullWidth-> fullWidth},
                    animationSpec = tween(durationMillis = 30)
                )}
                ) {
                bottomBarVisibility = false
                AddExpanse(navController)
            }
            composable(route = "/stats",
                enterTransition = {slideInHorizontally(
                    initialOffsetX = {-it},
                    animationSpec = tween(durationMillis = 30)
                )},
                exitTransition = {slideOutHorizontally(
                    targetOffsetX = {it},
                    animationSpec = tween(durationMillis = 30)
                )}
                ){
                bottomBarVisibility = true
                StatsScreen(navController)
              //  GraphScreen(navController)
            }
           composable (route = "/entity_list?title={title}",
               arguments = listOf(navArgument ("title"){ defaultValue = "Default"}),
               ){ backStackEntry->
               bottomBarVisibility = true
               val title = backStackEntry.arguments?.getString("title") ?: "Default"
                EntityListPage(navController,selectedTitle = title)
           }
            composable (route = "/TitleList"){
                TitleList(navController)
            }
            composable (route = "/TitleListPage"){
                ListTitlePage(navController)
            }
        }
    }
}

data class NavItem(
    val route: String,
    val icon: Int
)

@Composable
fun NavigationBottomBar(
    navController : NavController,
    items : List<NavItem>
){
    //Bottom Navigation Bar
    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry.value?.destination?.route

    BottomAppBar(containerColor = Color.White) {
        items.forEach {item->
            NavigationBarItem(selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                        Icon(painter = painterResource(id = item.icon), contentDescription = null,)
                       },
                alwaysShowLabel = false,
                colors = NavigationBarItemDefaults.colors(
                    selectedTextColor = Zinc,
                    selectedIconColor = Zinc,
                    unselectedIconColor = Color.Gray,
                    unselectedTextColor = Color.Gray,
                    indicatorColor = Color.Transparent
                ),
                )
        }
    }
}