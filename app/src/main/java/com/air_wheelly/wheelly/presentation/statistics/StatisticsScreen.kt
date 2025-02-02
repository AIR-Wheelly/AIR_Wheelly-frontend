package com.air_wheelly.wheelly.presentation.statistics

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.air_wheelly.wheelly.domain.statistics.StatisticsViewModel
import com.air_wheelly.wheelly.domain.statistics.StatisticsViewModelFactory
import com.google.accompanist.pager.ExperimentalPagerApi

@OptIn(ExperimentalPagerApi::class)
@Composable
fun StatisticsScreen(
    navController: NavController
) {
    val context = LocalContext.current
    val viewModel: StatisticsViewModel = viewModel(factory = StatisticsViewModelFactory(context))
    val state by viewModel.state.collectAsState()
    val pagerState = rememberPagerState(initialPage = state.selectedTabIndex) {
        viewModel.tabOptions.size
    }

    LaunchedEffect(key1 = state.selectedTabIndex) {
        viewModel.fetchTabData(state.selectedTabIndex)
    }

    LaunchedEffect(pagerState.currentPage) {
        if (state.selectedTabIndex != pagerState.currentPage) {
            viewModel.selectTab(pagerState.currentPage)
        }
    }

    LaunchedEffect(state.selectedTabIndex) {
        if (state.selectedTabIndex != pagerState.currentPage) {
            pagerState.animateScrollToPage(state.selectedTabIndex)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(0.dp)
    ) {
        TabRow(
            selectedTabIndex = state.selectedTabIndex,
            indicator = { tabPositions ->
                TabRowDefaults.Indicator(
                    Modifier.tabIndicatorOffset(tabPositions[state.selectedTabIndex]),
                    color = MaterialTheme.colorScheme.primary
                )
            }
        ) {
            viewModel.tabOptions.forEachIndexed { index, title ->
                Tab(
                    selected = state.selectedTabIndex == index,
                    onClick = { viewModel.selectTab(index) },
                    text = {
                        Text(
                            title,
                            style = MaterialTheme.typography.labelLarge,
                            color = if (state.selectedTabIndex == index)
                                MaterialTheme.colorScheme.primary
                            else
                                MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                )
            }
        }

        HorizontalPager(
            state = pagerState,
            modifier = Modifier.weight(1f)
        ) { page ->
            when (page) {
                0 -> PerCarStatisticsScreen(state.numberOfRentsPerCar)
                1 -> LastMonthStatisticsScreen(state.lastMonth)
            }
        }


        com.air_wheelly.wheelly.presentation.components.BottomNavigation(
            navController = navController,
            modifier = Modifier
                .padding(start = 0.dp, end = 0.dp)
                .fillMaxWidth()
        )
    }
}