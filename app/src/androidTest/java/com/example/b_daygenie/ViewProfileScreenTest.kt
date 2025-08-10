package com.example.b_daygenie

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.filterToOne
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.rememberNavController
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.b_daygenie.model.Person
import com.example.b_daygenie.screens.ViewProfileScreen
import junit.framework.TestCase.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ViewProfileScreenTest {
    private val testPerson = Person(
        id = 0,
        userId = "ABC123",
        name ="John Doe",
        birthYear = 2000,
        birthMonth = 1,
        birthDayOfMonth = 1,
        remarks = "remarks",
        pictureUrl = null,
        age = 25
    )
    private val wasDeleted = mutableStateOf(false)

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun viewProfile_displaysProfileNameAndBirthday() {
        composeTestRule.setContent {
            ViewProfileScreen(
                navController = rememberNavController(),
                person = testPerson,
                onPersonSelected = {wasDeleted.value = true},
                onPersonSelectedEdit = {}
            )
        }

        composeTestRule
            .onAllNodesWithText("John Doe")
            .onFirst()
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText("Next Birthday In:", useUnmergedTree = true)
            .assertIsDisplayed()

        composeTestRule
            .onAllNodes(hasText("Days", substring = true), useUnmergedTree = true)
            .filterToOne(hasText("Days", substring = true))
            .assertIsDisplayed()
    }

    @Test
    fun viewProfile_deleteDialogAppearsAndCancels() {
        composeTestRule.setContent {
            ViewProfileScreen(
                navController = rememberNavController(),
                person = testPerson,
                onPersonSelected = {},
                onPersonSelectedEdit = {}
            )
        }

        composeTestRule
            .onNodeWithContentDescription("Delete profile")
            .performClick()

        composeTestRule
            .onNodeWithText("Confirm Action")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText("Cancel")
            .performClick()

        composeTestRule
            .onNodeWithText("Confirm Action")
            .assertDoesNotExist()
    }

    @Test
    fun viewProfile_deleteDialogAppearsAndConfirms() {
        composeTestRule.setContent {
            ViewProfileScreen(
                navController = rememberNavController(),
                person = testPerson,
                onPersonSelected = {
                    wasDeleted.value = true
                },
                onPersonSelectedEdit = {}
            )
        }

        composeTestRule
            .onNodeWithContentDescription("Delete profile")
            .performClick()

        composeTestRule
            .onNodeWithText("Confirm Action")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText("Delete")
            .performClick()

        composeTestRule.runOnIdle {
            assertTrue(wasDeleted.value)
        }
    }
}