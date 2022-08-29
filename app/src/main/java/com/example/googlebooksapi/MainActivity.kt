package com.example.googlebooksapi

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.*

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.dp
import androidx.navigation.NavArgumentBuilder
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.googlebooksapi.model.*
import com.example.googlebooksapi.model.remote.BookApi
import com.example.googlebooksapi.model.remote.BookItem
import com.example.googlebooksapi.model.remote.BookResponse
import com.example.googlebooksapi.ui.screens.BookDetail
import com.example.googlebooksapi.ui.theme.GoogleBooksApiTheme
import com.example.googlebooksapi.util.ARG_BOOK
import com.example.googlebooksapi.util.DETAIL_SCREEN
import com.example.googlebooksapi.util.SEARCH_SCREEN
import com.example.googlebooksapi.util.navigateDetailScreen
import com.example.googlebooksapi.viewmodel.BookViewModel
import com.firebase.ui.auth.AuthUI
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "MainActivity"

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalUnitApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainApp {

                val navController = rememberNavController()
                val viewModel: BookViewModel by viewModels()

                NavHost(navController = navController,
                    startDestination = SEARCH_SCREEN ){
                    composable(
                        route = SEARCH_SCREEN
                    ){
                        SearchScreenStateFull(viewModel = viewModel,
                        navigation = navController){
                            logOutCurrentUser ()
                        }

                    }
                    composable(
                        route = navigateDetailScreen,
                        arguments= listOf(
                            navArgument(ARG_BOOK){
                                type = NavType.ParcelableType<BookItem>(BookItem::class.java)
                            }
                        )
                    ){ backStackEntry ->
                        BookDetail(bookItem = backStackEntry
                            .arguments?.getParcelable<BookItem>(ARG_BOOK) ?: throw Exception("Data is not ready")
                        )
                    }
                }




            }
        }


        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result

            // Log and toast
            val msg =  "FCM token: $token"
            Log.d(TAG, msg)
            Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
        })
    }

     fun logOutCurrentUser() {
        AuthUI.getInstance()
            .signOut(this)
            .addOnCompleteListener{
                Log.d(TAG, "onStop: ${it.result}")
            }
    }

}

@Composable
fun SearchScreenStateFull(viewModel: BookViewModel,
                          navigation: NavController,
                            logOutCurrentUser: () -> Unit) {

    // It will be a "main" container in your activity
    // used to render ToolBar/BottomAppBar
    //along with the content
    Scaffold(
        topBar = {
            MainTopBar{
                logOutCurrentUser()
            }
        }
    ) {
        it.toString()


        val uiState = viewModel.uiState.collectAsState().value
        SearchScreen(viewModel)

        when (uiState) {
            is Response -> {
                BookResponseScreen(uiState.data){bookItem->
                    navigation.navigate("$DETAIL_SCREEN$bookItem")

                }
            }
            is Failure -> {
                ErrorScreen(uiState.reasoon)
            }
            is Loading -> {
                LoadingScreen(uiState.isLoading)
            }
            is Empty -> {}
        }
    }
}

@Composable
fun MainTopBar(logOutCurrentUser: () -> Unit) {
    var menuLogOutIsExpanded: Boolean by remember {
        mutableStateOf(false)
    }
    Log.d(TAG, "MainTopBar: Photo Url= ${FirebaseAuth
        .getInstance()
        .currentUser?.photoUrl}" +
            "${FirebaseAuth.getInstance().currentUser?.displayName}")

    TopAppBar(
        title = {
            LocalContext.current.getString(R.string.app_name)
        },
        actions = {
            Icon(
                imageVector = Icons.Filled.Menu,
                contentDescription = LocalContext.current.getString(R.string.Menu),
                modifier = Modifier.clickable { menuLogOutIsExpanded = true }
            )
            DropdownMenu(
                expanded = menuLogOutIsExpanded,
                onDismissRequest = { menuLogOutIsExpanded = false }
            ) {
                DropdownMenuItem(
                    onClick = {
                        menuLogOutIsExpanded = false
                        logOutCurrentUser()
                    }
                ) {
                    Text(text = LocalContext
                        .current
                        .getString(R.string.log_out))
                }
            }
        }
    )
}


@Composable
fun LoadingScreen(isLoading: Boolean){
    Log.d(TAG, "LoadingScreen: $isLoading")
}

@Composable
fun ErrorScreen(reason: String){
    Log.d(TAG, "ErrorScreen: $reason")
}
@Composable
 fun BookResponseScreen(data: BookResponse, openDetails: (BookItem) -> Unit){
    LazyColumn( ) {
        items(data.items.size){position ->
            BookItemStateLess(data.items[position], openDetails )
        }
    }
}
@Composable
fun BookItemStateLess(book: BookItem, openDetails: (BookItem)-> Unit) {
    Box(modifier = Modifier
        .padding(
            start = 10.dp,
            top = 2.dp,
            end = 5.dp,
            bottom = 2.dp
        )
        .fillMaxWidth()) {
        Row {
            AsyncImage(model = ImageRequest.Builder(LocalContext.current)
                .data(book.volumeInfo.imageLinks?.smallThumbnail?: "")
                .crossfade(true)
                .build(),
                contentScale = ContentScale.Inside,
                modifier =  Modifier.clip(CircleShape),
                contentDescription = stringResource(id = R.string.book_cover_content_description),
                placeholder = painterResource(id = R.drawable.ic_baseline_error_24),
                error = painterResource(id = R.drawable.ic_baseline_error_24)
            )
            Column {
                Text(
                    text = book.volumeInfo.title,
                    modifier = Modifier.clickable { openDetails(book) }
                )
                Row {

                    Text(text = book.volumeInfo.title)
                    Text(text = book.volumeInfo.authors.toString())
                    book.volumeInfo.publishedDate?.let { Text(text = it) }
                    book.volumeInfo.description?.let { Text(text = it) }
                }
            }
        }
    }
}

@Composable
fun MainApp(content : @Composable () -> Unit) {
    GoogleBooksApiTheme {
        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colors.background
        ) {

            content()
        }
    }
}

@Composable
fun SearchScreen(viewModel: BookViewModel) {
    var bookQuery by remember {
        mutableStateOf("")
    }

    var bookQuerySize by remember {
        mutableStateOf("")
    }
    var selectedPrintType by remember {
        mutableStateOf("")
    }
    val context = LocalContext.current

    viewModel.uiState.collectAsState().value

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(50.dp),
        shape = RoundedCornerShape(20.dp),
        elevation = 14.dp)
    {
        Column {
            TextField(
                onValueChange = { bookQuery = it },
                value = bookQuery,
                // function {} that is null that is default argument value

                label = { Text(text = "Search by book name")}

            )
            TextField(
                value = bookQuerySize ,
                onValueChange = {bookQuerySize = it},
                label = { Text(text = "Search result ")}
            )
            BookPrintType {printType-> selectedPrintType = printType
            }
            Button(onClick = {
                Toast.makeText(context,
                    selectedPrintType,
                    Toast.LENGTH_SHORT).show()
                viewModel.searchBook(bookQuery, bookQuerySize, selectedPrintType)

            }) {
                Text(text = "Search")
            }
        }
    }
}

@Composable
fun BookPrintType(selected: (String)-> Unit) {
    var isExpanded by remember {
        mutableStateOf(false)
    }

    var selection by remember{
        mutableStateOf("")
    }
    val dropDownIcon = if (isExpanded) Icons.Filled.ArrowDropDown
    else Icons.Filled.KeyboardArrowUp
    Column(modifier = Modifier
        .padding(1.dp)
        .fillMaxWidth()
    ) {
        Row (modifier = Modifier.fillMaxWidth()){

            Text(
                text = selection
            )

            Icon(imageVector =
                dropDownIcon,
                contentDescription = "A dropdown icon",
                modifier = Modifier.clickable { isExpanded = !isExpanded})

        }
    }
    DropdownMenu(expanded = isExpanded,
        onDismissRequest = { isExpanded = false })
    {
      val context = LocalContext.current
      context.resources.getStringArray(R.array.book_print_type).forEach { printType ->
          DropdownMenuItem(onClick = {
              isExpanded = false
              selected(printType)
              selection = printType
          }) {
              Text(text = printType)
          }
      }
    }
}

@Preview(showBackground = true)
@Composable
fun SearchScreenPreview(){
//    val viewModel: BookViewModel = BookViewModel()
//    MainApp {
//        SearchScreen(viewModel)
//    }

}
