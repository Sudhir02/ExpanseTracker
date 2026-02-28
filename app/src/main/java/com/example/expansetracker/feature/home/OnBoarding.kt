package com.example.expansetracker.feature.home

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.with
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Brush.Companion.horizontalGradient
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.MotionScene
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.expansetracker.ui.theme.Zinc
import com.example.expansetracker.R
import com.example.expansetracker.ui.theme.InterFont
import com.example.expansetracker.ui.theme.XtraBold
import com.example.expansetracker.ui.theme.Zinca
import com.example.expansetracker.ui.theme.variableFont

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun OnboardingScreen(navController: NavController) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color.White
    ) {
        Box {
            Column(
                modifier = Modifier.align(Alignment.Center)
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Spacer(modifier = Modifier.height(100.dp))
                Image(
                    painter = painterResource(id = R.drawable.man), contentDescription = null,
                    modifier = Modifier.size(250.dp)
                )
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = "Spend Smarter",
                    fontSize = 30.sp,
                    fontWeight = FontWeight.W900,
                    color = Zinca,
                    fontFamily = XtraBold
                )
                Text(
                    text = "Save More",
                    fontSize = 30.sp,
                    fontWeight = FontWeight.W900,
                    color = Zinca,
                    fontFamily = XtraBold
                )
                Spacer(modifier = Modifier.height(10.dp))
                   //State to trigger animation
                    val isClicked = remember{ mutableStateOf(false) }

                  AnimatedContent (
                      targetState = isClicked.value,
                      transitionSpec = {
                          // Animation duration and type
                          fadeIn(animationSpec = tween(500)) with  fadeOut(animationSpec = tween(500))
                      }
                  ) { clicked ->
                      Button(
                          onClick = {
                              isClicked.value = true
                              navController.navigate("/home") {
                                  popUpTo("/onBoarding") {
                                      inclusive = true
                                  }
                              }
                          },
                          modifier = Modifier
                              .padding(16.dp)
                              .fillMaxWidth()
                              .height(60.dp)
                              .shadow(8.dp, shape = RoundedCornerShape(24.dp)),
                          shape = RoundedCornerShape(24.dp),

                          colors = ButtonDefaults.buttonColors(
                              containerColor = Zinca
                          )
                      ) {
                          Text(
                              text = if (clicked) "Get Started" else "Get started",
                              fontSize = 18.sp,
                              color = Color.White,
                              fontFamily = variableFont,
                              fontWeight = FontWeight.W900,
                          )
                      }
                  }
            }

        }
    }
}


@Composable
@Preview(showBackground = true)
fun OnboardingScreenPreview(){
    OnboardingScreen(rememberNavController())
}
