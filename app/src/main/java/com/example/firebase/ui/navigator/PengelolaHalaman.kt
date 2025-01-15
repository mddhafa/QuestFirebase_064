package com.example.firebase.ui.navigator

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.firebase.ui.view.DestinasiDetail
import com.example.firebase.ui.view.DetailScreen
import com.example.firebase.ui.view.HomeScreen
import com.example.firebase.ui.view.InsertMhsView


@Composable
fun PengelolaHalaman(
    modifier: Modifier,
    navController: NavHostController = rememberNavController()
){
    NavHost(
        navController = navController,
        startDestination = DestinasiHome.route,
        modifier = Modifier
    ) {
        composable(DestinasiHome.route){
            HomeScreen(
                navigateToItemEntry = {
                    navController.navigate(DestinasiInsert.route)
                },
                onDetailClick = {
                    navController.navigate("${DestinasiDetail.route}/$it")
                    println("PengelolaHalaman: $it")
                }
            )
        }

        composable(DestinasiInsert.route){
            InsertMhsView(
                onBack = { navController.popBackStack() },
                onNavigate = { navController.navigate(DestinasiHome.route) }
            )
        }
        composable(
            DestinasiDetail.routeWithArgs,
            arguments = listOf(
                navArgument(DestinasiDetail.NIM){
                    type = NavType.StringType
                }
            )
        ){backStackEntry ->
            val nim = backStackEntry.arguments?.getString(DestinasiDetail.NIM)
            nim?.let {
                DetailScreen(
                    onBackClick = {
                        navController.popBackStack() // Kembali ke halaman sebelumnya
                    },
                )
            }

        }
    }
}