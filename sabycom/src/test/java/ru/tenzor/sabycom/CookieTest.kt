package ru.tenzor.sabycom

import org.junit.Test

import org.junit.Assert.*
import ru.tenzor.sabycom.utils.mergeCookies

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class CookieTest {

    @Test
    fun `When newCookie update key b in oldCookie, then merged cookie updated`() {
        val oldCookie = "a=1;b=2;c=3"
        val newCookie = "b=4"

        val mergeCookies = mergeCookies(oldCookie, newCookie)
        val find = mergeCookies.split(";").find { it.contains("b") }

        assertEquals("4", find?.split("=")?.get(1))
    }

    @Test
    fun `When newCookie not update key b in oldCookie, then merged cookie not updated`() {
        val oldCookie = "a=1;b=2;c=3"
        val newCookie = "f=4"

        val mergeCookies = mergeCookies(oldCookie, newCookie)
        val find = mergeCookies.split(";").find { it.contains("b") }

        assertNotEquals("4", find?.split("=")?.get(1))
    }

}