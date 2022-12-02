package com.example.vinyls.view

import android.os.SystemClock
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.*
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.vinyls.R
import com.example.vinyls.utils.hasItemCount
import com.example.vinyls.utils.waitUntil
import com.example.vinyls.utils.setTextInTextView
import org.hamcrest.Matcher
import org.hamcrest.Matchers.*
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class MainActivityTest {
    @get:Rule
    val mActivityTestRule = ActivityScenarioRule(MainActivity::class.java)

    @Before
    fun setUp() {
    }

    @Test
    fun homeIconNavigateToMainPage() {
        onView(withId(R.id.tvAlbum)).perform(click())

        SystemClock.sleep(1000);
        onView(withId(R.id.imHouse)).perform(click())

        onView(withId(R.id.mainActivity)).check(matches(isDisplayed()))
    }

    /**
     * Album Tests
     */
    @Test
    fun navigateToAlbumList() {
        onView(withId(R.id.tvAlbum)).perform(click())
        onView(withId(R.id.fragmentAlbumList)).check(matches(isDisplayed()))
    }

    @Test
    fun navigateToAlbumDetails() {
        onView(withId(R.id.tvAlbum)).perform(click())

        onView(withId(R.id.albumsRv)).perform(
            waitUntil(hasItemCount(greaterThan(0)) as Matcher<View>),
            actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click())
        )
    }

    @Test
    fun createAlbum() {
        onView(withId(R.id.tvAlbum)).perform(click())
        onView(withId(R.id.btnCrearAlbum)).perform(click())

        onView(withId(R.id.editTextNombreAlbum)).perform(setTextInTextView("Album Test"))
        onView(withId(R.id.editTextFechaAlbum)).perform(setTextInTextView("2022-11-29"))

        onView(withId(R.id.spinnergen)).perform(click())
        onView(withText("Salsa")).perform(click())

        onView(withId(R.id.spinnersello)).perform(click())
        onView(withText("Elektra")).perform(click())

        onView(withId(R.id.editTextCoverUrl)).perform(setTextInTextView("https://definicion.de/wp-content/uploads/2008/04/musica-1.jpg"))
        onView(withId(R.id.editTextDescripcion)).perform(setTextInTextView("E2E Test Auto Generated Album"))

        onView(withId(R.id.btnCrearAlbumRed)).perform(click())

        SystemClock.sleep(2000)
        onView(withId(R.id.tvName)).check(matches(isDisplayed()))
    }

    @Test
    fun addTrackToAlbum() {
        onView(withId(R.id.tvAlbum)).perform(click())

        onView(withId(R.id.albumsRv)).perform(
            waitUntil(hasItemCount(greaterThan(0)) as Matcher<View>),
            actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click())
        )

        onView(withId(R.id.ivBotonAgregarTrack)).perform(scrollTo()).perform( click())

        SystemClock.sleep(2000)
        onView(withId(R.id.etName)).perform(setTextInTextView("Track Test"), closeSoftKeyboard())
        onView(withId(R.id.etDuration)).perform(setTextInTextView("2:50"),  closeSoftKeyboard())

        onView(withId(R.id.btnAddTrack)).perform(click())

        SystemClock.sleep(2000)
        onView(withId(R.id.fragmentAlbumList)).check(matches(isDisplayed()))
    }


    /**
     * Musicians Tests
     */
    @Test
    fun navigateToMusicianList() {
        onView(withId(R.id.tvArtista)).perform(click())
        onView(withId(R.id.fragmentMusicianList)).check(matches(isDisplayed()))
    }

    @Test
    fun navigateToMusicianDetails() {
        onView(withId(R.id.tvArtista)).perform(click())

        onView(withId(R.id.artistRv)).perform(
            waitUntil(hasItemCount(greaterThan(0)) as Matcher<View>),
            actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click())
        )
    }


    /**
     * Collector Tests
     */
    @Test
    fun navigateToCollectorsList() {
        onView(withId(R.id.tvColeccionista)).perform(click())
        onView(withId(R.id.fragmentColeccionistaList)).check(matches(isDisplayed()))
    }

    @Test
    fun navigateToCollectorDetails() {
        onView(withId(R.id.tvColeccionista)).perform(click())

        onView(withId(R.id.collectorRv)).perform(
            waitUntil(hasItemCount(greaterThan(0)) as Matcher<View>),
            actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click())
        )
    }

}