package com.example.b_daygenie.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SliderColors
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableFloatState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.b_daygenie.R
import com.example.b_daygenie.model.Person
import com.example.b_daygenie.ui.theme.NavRoutes
import com.example.b_daygenie.utils.DateUtils
import com.example.b_daygenie.utils.SortState

private val somePeople = mutableStateListOf(
        Person(0, "John", "Doe", 2000, 5, 15, remarks = null, pictureUrl = null, age = 0),
        Person(0, "Jane", "Smith", 1995, 3,25, remarks = null, pictureUrl = null, age = 0),
        Person(0, "sample.longemail@securemail.com", "First Middle Last", 1995, 10, 25, remarks = null, pictureUrl = null, age = 0)
    )

    private var mainModifier = Modifier
        .fillMaxSize()
        .padding(top = 10.dp, start = 10.dp, end = 10.dp)

    @Composable
    fun HomeScreen( //TODO Birthdays can be null. What to do then?
        navController: NavController,
        birthdays: List<Person>,
        onPersonSelected: (Person) -> Unit = {},
        onUpdateList: () -> Unit,
        onSort: (MutableState<SortState>, MutableState<SortState>, MutableState<SortState>) -> Unit,
        onFilter: (MutableState<String>, MutableFloatState, MutableFloatState) -> Unit,
        errorMessage: String,
        onClearErrorMessage: () -> Unit,
        onLogout: () -> Unit,
        isLoadingBirthdays: Boolean
    ) {
        MainContent(navController, birthdays, onPersonSelected, onUpdateList, onSort, onFilter, errorMessage, onClearErrorMessage, onLogout, isLoadingBirthdays)
    }

    @Composable
    fun MainContent(navController: NavController,
                    birthdays: List<Person>,
                    onPersonSelected: (Person) -> Unit,
                    onUpdateList: () -> Unit,
                    onSort: (MutableState<SortState>, MutableState<SortState>, MutableState<SortState>) -> Unit,
                    onFilter: (MutableState<String>, MutableFloatState, MutableFloatState) -> Unit,
                    errorMessage: String,
                    onClearErrorMessage: () -> Unit,
                    onLogout: () -> Unit,
                    isLoadingBirthdays: Boolean
    ) {
        val showDialog = remember { mutableStateOf(false) }
        val ageSortState = remember { mutableStateOf(SortState.NONE) }
        val nameSortState = remember { mutableStateOf(SortState.NONE) }
        val birthdaySortState = remember { mutableStateOf(SortState.NONE) }
        val filterByNameTextField = remember { mutableStateOf("") }
        val filterByAgeStartPosition = remember { mutableFloatStateOf(0f) }
        val filterByAgeEndPosition = remember { mutableFloatStateOf(100f) }
        val snackBarHostState = remember { SnackbarHostState() }
        Scaffold (
            topBar = { ScreenToolbar(onUpdateList, showDialog, onLogout) },
            snackbarHost = { SnackbarHost(snackBarHostState) },
            floatingActionButton = { FloatingAddButton(navController) }
        ) { paddingValues ->
            Box(modifier = Modifier.padding(paddingValues).fillMaxSize()) {
                if (errorMessage.isNotEmpty()) {
                    ConnectionSnackbar("Reload", errorMessage, snackBarHostState, onUpdateList, onClearErrorMessage = onClearErrorMessage)
                }
                if (isLoadingBirthdays) {
                    LoadingIndicator()
                }
                if (showDialog.value) {
                    SortDialog(
                        showDialog,
                        ageSortState,
                        nameSortState,
                        birthdaySortState,
                        filterByNameTextField,
                        filterByAgeStartPosition,
                        filterByAgeEndPosition,
                        onSort,
                        onFilter,
                        onUpdateList
                    )
                }
                Column(mainModifier, horizontalAlignment = Alignment.CenterHorizontally) {
                    EntriesManager(birthdays, onPersonSelected)
                }
            }
        }
    }

@Composable
fun ConnectionSnackbar( // TODO, maybe implement all purpose snackbar
    buttonText: String,
    errorMessage: String,
    snackBarHostState: SnackbarHostState,
    onUpdateList: () -> Unit = {},
    onClearErrorMessage: () -> Unit
) {
    LaunchedEffect(errorMessage) {
        val result = snackBarHostState
            .showSnackbar(
                message = errorMessage,
                actionLabel = buttonText,
                duration = SnackbarDuration.Indefinite
            )
        when (result) {
            SnackbarResult.ActionPerformed -> {
                onClearErrorMessage()
                onUpdateList()
            }

            SnackbarResult.Dismissed -> {
                //Nothing needs to be done
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun ScreenToolbar(onUpdateList: () -> Unit, showDialog: MutableState<Boolean>, onLogout: () -> Unit) {
        TopAppBar(
            title = {},
            navigationIcon = {
                IconButton(onClick = { onLogout() }) {
                    Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = "Log out")
                }
            },
            actions = {
                IconButton(onClick = { onUpdateList() }) {
                    Icon(Icons.Filled.Refresh, contentDescription = "Refresh birthdays list")
                }
                IconButton(onClick = { showDialog.value = true }) {
                    Icon(Icons.Filled.MoreVert, contentDescription = "Sort/filter menu")
                }
            },
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor = Color(0xFF2A9D8F)
            )
        )
    }

    @Composable
    fun FloatingAddButton(navController: NavController) {
        FloatingActionButton(
            onClick = { navController.navigate(route = NavRoutes.EditProfile.route)},
            containerColor = Color(0xFF264653),
            contentColor = Color.White,
            elevation = FloatingActionButtonDefaults.elevation(8.dp)
        ) {
            Icon(Icons.Filled.Add, contentDescription = "Add new contact")
        }
    }

    @Composable
    fun EntriesManager(entries: List<Person>, onPersonSelected: (Person) -> Unit) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(15.dp)
        ) {
            itemsIndexed(entries) {index, item ->
                BirthdayEntry(item, onPersonSelected)
            }
        }
    }

    @Composable
    fun BirthdayEntry(entry: Person, onPersonSelected: (Person) -> Unit) {
        val daysToBirthday = DateUtils.calculateNextBirthday(entry)
        Box(
            Modifier
                .height(100.dp)
                .fillMaxWidth()
                .border(width = 2.dp, color = Color(0xFF264653), shape = RoundedCornerShape(16.dp))
                .background(Color.LightGray, RoundedCornerShape(16.dp))
                .padding(10.dp)
        ) {
            Card(onClick = { onPersonSelected(entry) },
                Modifier.fillMaxSize()
            ) {
                Row(
                    Modifier
                        .fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    BirthDayEntryPicture()
                    BirthdayEntryName(entry.userId, entry.name)
                    Column(
                        Modifier
                            .fillMaxSize(),
                        horizontalAlignment = Alignment.End,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text("B-Day In:")
                        Text(
                            text = daysToBirthday.toString(),
                            color = Color(0xFF264653),
                            fontWeight = FontWeight(500),
                            style = MaterialTheme.typography.headlineLarge,
                            fontSize = 45.sp
                        )
                    }
                }
            }
        }
    }

    @Composable
    fun BirthDayEntryPicture() {
        Image(
            painter = painterResource(id = R.drawable.ic_launcher_background),
            contentDescription = "Profile picture",
            modifier = Modifier
                .clip(CircleShape)
                .border(width = 1.dp, color = Color.DarkGray, CircleShape)
        )
    }

    @Composable
    fun BirthdayEntryName(name: String?, surname: String?) {
        Column(Modifier.widthIn(max = 170.dp)) {
            Text(
                text = name.toString(),
                color = Color(0xFF5D4037),
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                style = MaterialTheme.typography.titleSmall
            )
            Text(
                text = surname.toString(),
                color = Color(0xFF5D4037),
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }
    }

@Composable
fun SortDialog(
    showDialog: MutableState<Boolean>,
    ageSortState: MutableState<SortState>,
    nameSortState: MutableState<SortState>,
    birthdaySortState: MutableState<SortState>,
    filterByNameTextField: MutableState<String>,
    filterByAgeStartPosition: MutableFloatState,
    filterByAgeEndPosition: MutableFloatState,
    onSort: (MutableState<SortState>, MutableState<SortState>, MutableState<SortState>) -> Unit,
    onFilter: (MutableState<String>, MutableFloatState, MutableFloatState) -> Unit,
    onUpdateList: () -> Unit
) {

    Dialog(onDismissRequest = { showDialog.value = false }) {
        Card(shape = RoundedCornerShape(16.dp)) {
            Column(
                Modifier
                    .fillMaxWidth()
            ) {
                Row(
                    Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    IconButton(onClick = { showDialog.value = false}) {
                        Icon(Icons.Filled.Close, contentDescription = "Close dialog")
                    }
                    TextButton(onClick = {
                        ageSortState.value = SortState.NONE
                        nameSortState.value = SortState.NONE
                        birthdaySortState.value = SortState.NONE
                        filterByNameTextField.value = ""
                        filterByAgeStartPosition.floatValue = 0f
                        filterByAgeEndPosition.floatValue = 100f
                        onUpdateList()
                    }) {
                        Text(text = "Reset")
                    }
                }
                SortDialogContent(ageSortState, nameSortState, birthdaySortState, filterByNameTextField, filterByAgeStartPosition, filterByAgeEndPosition)
                Row(
                    Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = {
                        onSort(ageSortState, nameSortState, birthdaySortState)
                        onFilter(filterByNameTextField, filterByAgeStartPosition, filterByAgeEndPosition)
                        showDialog.value = false
                    }) {
                        Text("Show Results")
                    }
                }
            }
        }
    }
}

@Composable
private fun SortDialogContent(
    ageSortState: MutableState<SortState>,
    nameSortState: MutableState<SortState>,
    birthdaySortState: MutableState<SortState>,
    filterByNameTextField: MutableState<String>,
    filterByAgeStartPosition: MutableFloatState,
    filterByAgeEndPosition: MutableFloatState
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(400.dp)
            .padding(16.dp),
        shape = RoundedCornerShape(16.dp),
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .padding(20.dp)
        ) {
            Text(
                text = "Sort By",
                style = MaterialTheme.typography.bodyLarge
            )
            Row(
                Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                SortByButton(ageSortState, "Age")
                SortByButton(nameSortState, "Name")
            }
            Row(
                Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                SortByButton(birthdaySortState, "Birthday")
            }
            Spacer(Modifier.size(10.dp))
            HorizontalDivider(thickness = 2.dp)
            Spacer(Modifier.size(10.dp))
            Text(
                text = "Filter By",
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(Modifier.size(10.dp))
            FilterByName(filterByNameTextField)
            Spacer(Modifier.size(10.dp))
            FilterByAge(filterByAgeStartPosition, filterByAgeEndPosition)
        }
    }
}

@Composable
fun SortByButton(sortState: MutableState<SortState>, typeString: String) {
    val text = when (sortState.value) {
        SortState.NONE -> typeString
        SortState.ASCENDING -> "↑ $typeString"
        SortState.DESCENDING -> "↓ $typeString"
    }
    val buttonColor = when (sortState.value) {
        SortState.NONE -> ButtonDefaults.buttonColors(containerColor = Color.Transparent)
        SortState.ASCENDING -> ButtonDefaults.buttonColors(containerColor = Color.DarkGray)
        SortState.DESCENDING -> ButtonDefaults.buttonColors(containerColor = Color.DarkGray)
    }

    Button(
        onClick = {sortState.value = when (sortState.value) {
            SortState.NONE -> SortState.ASCENDING
            SortState.ASCENDING -> SortState.DESCENDING
            SortState.DESCENDING -> SortState.NONE
        }},
        shape = RoundedCornerShape(8.dp),
        colors = buttonColor,
        border = BorderStroke(1.dp, Color.Black)
    ) {
        Text(
            text = text,
            color = if(sortState.value == SortState.NONE) Color.Black else Color.White
        )

    }
}

@Composable
fun FilterByName(filterByNameTextField: MutableState<String>) {
    Text(
        "Name",
        fontWeight = FontWeight.Bold
    )
    OutlinedTextField(
        value = filterByNameTextField.value,
        onValueChange = { filterByNameTextField.value = it },
        placeholder = {Text("Search")},
        leadingIcon = {
            Icon(Icons.Filled.Search, contentDescription = "Filter by name")
        },
        shape = RoundedCornerShape(16.dp)
    )
}

@Composable
fun FilterByAge(filterByAgeStartPosition: MutableFloatState, filterByAgeEndPosition: MutableFloatState) {
    Text(
        "Age",
        fontWeight = FontWeight.Bold
    )

    Text(
        text = "From ${filterByAgeStartPosition.floatValue.toInt()} To ${filterByAgeEndPosition.floatValue.toInt()}",
    )
    Spacer(Modifier.size(10.dp))
    RangeSlider(
        value = filterByAgeStartPosition.floatValue..filterByAgeEndPosition.floatValue,
        steps = 99,
        onValueChange = { range ->
            filterByAgeStartPosition.floatValue = range.start
            filterByAgeEndPosition.floatValue = range.endInclusive
        },
        valueRange = 0f..100f,
        colors = SliderColors(
            thumbColor = Color(0xFF264653),
            activeTrackColor = Color(0xFF264653),
            activeTickColor = Color(0xFF264653),
            inactiveTrackColor = Color(0xFF2A9D8F),
            inactiveTickColor = Color(0xFF2A9D8F),
            disabledThumbColor = Color.Gray,
            disabledActiveTrackColor = Color.Gray,
            disabledActiveTickColor = Color.Gray,
            disabledInactiveTrackColor = Color.Gray,
            disabledInactiveTickColor = Color.Gray
        )
    )
}

@Composable
fun LoadingIndicator() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier.width(64.dp),
            color = MaterialTheme.colorScheme.secondary,
            trackColor = MaterialTheme.colorScheme.surfaceVariant,
        )
    }
}

    @Preview
    @Composable
    fun PreviewMain() {
        val navController = rememberNavController()
        val show = remember { mutableStateOf(false) }
        //MainContent(navController, somePeople, {}, {})
        //BirthdayEntry()
        //SortDialog(show)
    }