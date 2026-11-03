package com.skilltest.ui

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import org.junit.Assert.assertEquals
import org.junit.Test

class CollapsingTitleStateTest {
    private val state = CollapsingTitleState(rangePx = 100f)
    private val connection = state.nestedScrollConnection

    @Test
    fun `scrolling the list up shrinks the title before the list moves`() {
        val consumed = connection.onPreScroll(Offset(0f, -40f), NestedScrollSource.UserInput)

        assertEquals(-40f, consumed.y)
        assertEquals(0.4f, state.fraction)
    }

    @Test
    fun `the title stops shrinking at body size and leaves the rest to the list`() {
        val consumed = connection.onPreScroll(Offset(0f, -150f), NestedScrollSource.UserInput)

        assertEquals(-100f, consumed.y)
        assertEquals(1f, state.fraction)
    }

    @Test
    fun `scrolling down only grows the title once the list is back at the top`() {
        connection.onPreScroll(Offset(0f, -100f), NestedScrollSource.UserInput)

        assertEquals(
            Offset.Zero,
            connection.onPreScroll(Offset(0f, 30f), NestedScrollSource.UserInput)
        )
        assertEquals(1f, state.fraction)

        val consumed = connection.onPostScroll(
            consumed = Offset.Zero,
            available = Offset(0f, 30f),
            source = NestedScrollSource.UserInput,
        )

        assertEquals(30f, consumed.y)
        assertEquals(0.7f, state.fraction, 0.0001f)
    }

    @Test
    fun `the title never grows past its full size`() {
        val consumed = connection.onPostScroll(
            consumed = Offset.Zero,
            available = Offset(0f, 50f),
            source = NestedScrollSource.UserInput,
        )

        assertEquals(Offset.Zero, consumed)
        assertEquals(0f, state.fraction)
    }
}
