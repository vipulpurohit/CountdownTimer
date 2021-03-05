/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.androiddevchallenge

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.expandIn
import androidx.compose.animation.shrinkOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.androiddevchallenge.ui.theme.MyTheme
import com.example.androiddevchallenge.ui.theme.bittersweet
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyTheme {
                MyApp()
            }
        }
    }
}

// Start building your app here!
@OptIn(ExperimentalAnimationApi::class)
@Composable
fun MyApp() {
    Surface(
        color = MaterialTheme.colors.background,
    ) {
        Box(
            Modifier.fillMaxSize(),
            contentAlignment = Alignment.BottomCenter
        ) {
            val timerStared: MutableState<Boolean> = remember { mutableStateOf(false) }
            val timerProgress: MutableState<Float> = remember { mutableStateOf(0.0f) }
            val progressHeight = remember { Animatable(0.0f) }

            // Timer progress
            // TimerValueComposable(progressHeight.value)
            val listState = rememberLazyListState()
            val pagerState = remember { PagerState() }
            // Countdown timer
            Pager(
                modifier = Modifier
                    .padding(start = 24.dp, top = 16.dp, end = 24.dp)
                    .fillMaxSize()
                    .height(200.dp),
                state = pagerState,
            ) {
                pagerState.maxPage = 50
                Column(
                    Modifier
                        .padding(4.dp)
                        .fillMaxHeight()
                        .padding(horizontal = 12.dp, vertical = 8.dp)

                ) {
                    Box(
                        Modifier
                            .fillMaxWidth(0.45f)
                            .weight(1f)
                            .align(Alignment.CenterHorizontally),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            fontSize = 130.sp,
                            fontFamily = FontFamily.SansSerif,
                            fontWeight = FontWeight.Bold,
                            text = "$page"
                        )
                    }
                }
            }

            // Launch button
            AnimatedVisibility(
                visible = !timerStared.value,
                enter = expandIn(expandFrom = Alignment.Center),
                exit = shrinkOut(shrinkTowards = Alignment.Center)
            ) {
                LaunchButtonComposable(timerStared.value, timerProgress.value) { state, progress ->
                    timerStared.value = state
                    timerProgress.value = progress
                    GlobalScope.launch {
                        progressHeight.animateTo(progress)
                    }
                }
            }
        }
    }
}

@Composable
private fun TimerValueComposable(progressHeight: Float) {
    Spacer(
        Modifier
            .fillMaxHeight(progressHeight)
            .fillMaxWidth()
            .background(bittersweet)
    )
}

@Composable
private fun LaunchButtonComposable(
    currentState: Boolean,
    timerProgress: Float,
    launchStateChange: ((state: Boolean, progress: Float) -> Unit)
) {
    Button(
        elevation = ButtonDefaults.elevation(
            defaultElevation = 4.dp,
            pressedElevation = 2.dp
        ),
        modifier = Modifier
            .wrapContentSize(Alignment.Center)
            .padding(vertical = 20.dp)
            .size(70.dp),
        shape = CircleShape,
        colors = ButtonDefaults.textButtonColors(
            backgroundColor = bittersweet,
        ),
        onClick = {
            GlobalScope.launch {
                launchStateChange(!currentState, 0.0f)
                delay(1000)
                launchStateChange(!currentState, 0.2f)
                delay(1000)
                launchStateChange(!currentState, 0.4f)
                delay(1000)
                launchStateChange(!currentState, 0.6f)
                delay(1000)
                launchStateChange(!currentState, 0.8f)
                delay(1000)
                launchStateChange(currentState, 1.0f)
            }
        },
    ) {
        Image(
            modifier = Modifier
                .fillMaxSize(),
            painter = painterResource(id = R.drawable.ic_launch),
            contentDescription = "",
            contentScale = ContentScale.Fit
        )
    }
}

@Preview("Light Theme", widthDp = 360, heightDp = 640)
@Composable
fun LightPreview() {
    MyTheme {
        MyApp()
    }
}

/*
@Preview("Dark Theme", widthDp = 360, heightDp = 640)
@Composable
fun DarkPreview() {
    MyTheme(darkTheme = true) {
        MyApp()
    }
}
*/