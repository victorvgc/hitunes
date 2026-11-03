package com.skilltest.hitunes.ui.layout

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class AppLayoutTest {
    @Test
    fun `phone portrait uses single pane`() {
        assertEquals(AppLayout.SinglePane, layout(widthDp = 411, heightDp = 891))
    }

    @Test
    fun `phone stays single pane even in a landscape window`() {
        assertEquals(AppLayout.SinglePane, layout(widthDp = 891, heightDp = 411))
    }

    @Test
    fun `tablet portrait uses single pane`() {
        assertEquals(AppLayout.SinglePane, layout(widthDp = 800, heightDp = 1280))
    }

    @Test
    fun `tablet landscape uses two panes`() {
        assertEquals(AppLayout.TwoPane, layout(widthDp = 1280, heightDp = 800))
    }

    @Test
    fun `car screens use two panes regardless of orientation`() {
        assertEquals(AppLayout.TwoPane, layout(widthDp = 768, heightDp = 1024, isCar = true))
        assertEquals(AppLayout.TwoPane, layout(widthDp = 1408, heightDp = 792, isCar = true))
    }

    @Test
    fun `square tablet windows use single pane`() {
        assertEquals(AppLayout.SinglePane, layout(widthDp = 800, heightDp = 800))
    }

    @Test
    fun `screens narrower than 600dp on their short side are phones`() {
        assertTrue(isPhone(smallestWidthDp = 411))
        assertTrue(isPhone(smallestWidthDp = 599))
        assertFalse(isPhone(smallestWidthDp = 600))
        assertFalse(isPhone(smallestWidthDp = 800))
    }

    private fun layout(widthDp: Int, heightDp: Int, isCar: Boolean = false) = appLayoutFor(
        widthDp = widthDp,
        heightDp = heightDp,
        smallestWidthDp = minOf(widthDp, heightDp),
        isCar = isCar,
    )
}
