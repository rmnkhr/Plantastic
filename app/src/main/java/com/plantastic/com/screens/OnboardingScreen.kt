package com.plantastic.com.screens

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.plantastic.com.Destinations.MAIN
import com.plantastic.com.Destinations.ONBOARDING
import com.plantastic.com.R
import kotlinx.coroutines.launch

const val PREFS_NAME = "plantastic_prefs"
const val KEY_ONBOARDING_SHOWN = "onboarding_shown"

@Composable
fun OnboardingScreen(navController: NavController) {
    val pages = onboardingPages
    val pagerState = rememberPagerState(initialPage = 0, pageCount = { pages.size })
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 160.dp), // Щоб не перекривалось кнопками
                contentAlignment = Alignment.TopCenter
            ) {
                OnboardingPageContent(page = pages[page])
            }
        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            DotsIndicator(
                totalDots = pages.size,
                selectedIndex = pagerState.currentPage
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    if (pagerState.currentPage < pages.lastIndex) {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(pagerState.currentPage + 1)
                        }
                    } else {
                        markOnboardingShown(context)
                        navController.navigate(MAIN) {
                            popUpTo(ONBOARDING) { inclusive = true }
                        }
                    }
                },
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text(text = pages[pagerState.currentPage].buttonText)
            }

            pages[pagerState.currentPage].secondaryButtonText?.let { text ->
                TextButton(onClick = {
                    markOnboardingShown(context)
                    navController.navigate(MAIN) {
                        popUpTo(ONBOARDING) { inclusive = true }
                    }
                }) {
                    Text(text = text)
                }
            }
        }
    }
}

fun markOnboardingShown(context: Context) {
    val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    prefs.edit().putBoolean(KEY_ONBOARDING_SHOWN, true).apply()
}

fun wasOnboardingShown(context: Context): Boolean {
    val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    return prefs.getBoolean(KEY_ONBOARDING_SHOWN, false)
}

@Composable
fun OnboardingPageContent(page: OnboardingPage) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = page.imageRes),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
        )
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = page.title,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = page.subtitle,
            fontSize = 16.sp,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
        )
    }
}

@Composable
fun DotsIndicator(totalDots: Int, selectedIndex: Int) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        repeat(totalDots) { index ->
            val color = if (index == selectedIndex)
                MaterialTheme.colorScheme.primary
            else
                MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)

            Box(
                modifier = Modifier
                    .padding(4.dp)
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(color)
            )
        }
    }
}


data class OnboardingPage(
    val imageRes: Int,
    val title: String,
    val subtitle: String,
    val buttonText: String,
    val secondaryButtonText: String? = null
)

val onboardingPages = listOf(
    OnboardingPage(
        R.drawable.ic_launcher_foreground,
        "Welcome to Plantastic!",
        "Your personal green assistant",
        "Next"
    ),
    OnboardingPage(
        R.drawable.ic_launcher_foreground,
        "Identify any plant instantly",
        "Snap a photo and learn how to care for it",
        "Next"
    ),
    OnboardingPage(
        R.drawable.ic_launcher_foreground,
        "Never forget plant care again",
        "We’ll remind you when to water, repot, or fertilize",
        "Next"
    ),
    OnboardingPage(
        R.drawable.ic_launcher_foreground,
        "Track your green family",
        "Name your plants, add photos, and watch them grow",
        "Next"
    ),
    OnboardingPage(
        R.drawable.ic_launcher_foreground,
        "Stay in the loop",
        "Enable reminders for plant care activities",
        "Enable Notifications",
        "Skip"
    ),
    OnboardingPage(
        R.drawable.ic_launcher_foreground,
        "Pick your vibe",
        "Light, dark, or dynamic theme – it’s up to you",
        "Next"
    ),
    OnboardingPage(
        R.drawable.ic_launcher_foreground,
        "Let’s make your plants happy!",
        "Add your first plant and get growing",
        "Start"
    )
)

@Preview(showBackground = true)
@Composable
fun OnboardingScreenPreview() {
    // Preview the OnboardingScreen with a mock NavController
    val navController = NavController(LocalContext.current)
    OnboardingScreen(navController)
}