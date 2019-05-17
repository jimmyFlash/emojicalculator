package com.raywenderlich.android.emojicalculator

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.hamcrest.Matchers.allOf
import org.junit.Test
import org.junit.runner.RunWith

/*
    This tells JUnit to run the tests inside of AndroidJUnit4 instead
    of the runner built into JUnit. AndroidJUnit4 is a test runner that runs
    JUnit style tests on Android devices. When used in Espresso tests,
    it controls launching the app and running UI tests.
  */
@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    /*
    the ActivityScenario to launch the MainActivity.
     The ActivityScenario class is a part of AndroidX
     and provides APIs to start and drive an Activity’s lifecycle state for testing
     */
    @Test
    fun appLaunchesSuccessfully() {
        ActivityScenario.launch(MainActivity::class.java)
    }

    /*
    There are three classes you need to know about to follow along:

    ViewMatchers contains methods that Espresso uses to find the view on your screen with
    which it needs to interact.

    ViewActions contains methods that tell Espresso how to automate your UI. For example,
    it contains methods like click() that you can use to tell Espresso to click on a button.

    ViewAssertions contains methods used to check if a view matches a certain set of conditions.

     */

    @Test
    fun onLaunchCheckAmountInputIsDisplayed() {
        // 1 Launch the activity
        ActivityScenario.launch(MainActivity::class.java)

        // 2 Add a matcher for a view with the ID inputAmount
        onView(withId(R.id.inputAmount))
                // 3 Verify that the matched view is displayed on screen
                .check(matches(isDisplayed()))
    }

    @Test
    fun onLaunchOkayButtonIsDisplayed() {
        ActivityScenario.launch(MainActivity::class.java)

        onView(withText(R.string.okay))
                .check(matches(isDisplayed()))
    }

    @Test
    fun whenOkayButtonIsPressedAndAmountIsEmptyTipIsEmpty() {
        ActivityScenario.launch(MainActivity::class.java)

        // 1 call .perform(click()) on it. You can combine matchers with .perform() to do a number of different actions on a view.
        onView(withId(R.id.buttonOkay))
                .perform(click())

        // 2 tell Espresso that it needs to match both the conditions on the view using allOf()
        onView(allOf(withId(R.id.textTip), withText("")))
                .check(matches(isDisplayed()))
    }

    /**
     * Add a test for when you type in an amount, then press the 👍
     * (Okay) button, the screen displays the correct tip
     */
    @Test
    fun whenOkayButtonIsPressedAndAmountIsFilledTipIsSet() {
        ActivityScenario.launch(MainActivity::class.java)

        // 1 The statement to type in “11” into the input. You use a matcher like you’ve learned, then call .perform(typeText("11")) to type that text into the field.
        onView(withId(R.id.inputAmount))
                .perform(typeText("11"))

        onView(withId(R.id.buttonOkay))
                .perform(click())

        // 2 You check that the view with the ID textTip has the correct text using withText() instead of combining allOf() and isDisplayed(). This is an alternate way to perform this check.
        onView(withId(R.id.textTip))
                .check(matches(withText("1.98")))
    }


    /**
     *
     */
    @Test
    fun whenOkayButtonIsPressedAndRoundSwitchIsSelectedAmountIsCorrect() {
        ActivityScenario.launch(MainActivity::class.java)

        onView(withId(R.id.switchRound))
                .perform(click())
        onView(withId(R.id.inputAmount))
                .perform(typeText("11"))
        onView(withId(R.id.buttonOkay))
                .perform(click())

        onView(withId(R.id.textTip))
                .check(matches(withText("2.00")))
    }


    /**
     * Screen Robots pull out some of these tasks into helper functions. This makes your tests
     * more expressive, readable and easier to update your if something in the UI changes
     *
     * You can think of a robot as a middle man between your view and a test: It serves the
     * same purpose as a presenter in the MVP architectural pattern.
     * The view itself knows nothing about the test and the test knows nothing about the
     * implementation of the view, it only knows what actions it needs to perform on a view.
     *
     * As this robot is only used in this test, this project adds it as an inner class
     */
    class CalculatorScreenRobot : ScreenRobot<CalculatorScreenRobot>() {
        /*
        The interface for a robot returns a robot so you can keep chaining actions and
        assertions together
         */

//        verify the tip amount
        fun verifyTipIsCorrect(tip: String): CalculatorScreenRobot {
            return checkViewHasText(R.id.textTip, tip)
        }

        fun inputCheckAmountAndSelectOkayButton(input: String): CalculatorScreenRobot {
            return enterTextIntoView(R.id.inputAmount, input)
                    .clickOkOnView(R.id.buttonOkay)
        }



    }



}
