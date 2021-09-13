package ru.tenzor.sabycom.utils

/**
 * @author ma.kolpakov
 */

internal fun mergeCookies(oldCookie: String?, newCookie: String): String {
    if (oldCookie.isNullOrEmpty()) return newCookie
    val oldCookies = oldCookie.split(COOKIE_SEPARATOR).toMutableList()
    val newCookies = newCookie.split(COOKIE_SEPARATOR)
    newCookies.forEach { newCookieString ->
        val cookieKey = newCookieString.split(COOKIE_EQUALS)[0]
        val index = oldCookies.indexOfFirst { it.contains(cookieKey) }
        if (index >= 0)
            oldCookies[index] = newCookieString
        else
            oldCookies.add(newCookieString)
    }
    return oldCookies.joinToString(COOKIE_SEPARATOR)
}

private const val COOKIE_SEPARATOR = "; "
private const val COOKIE_EQUALS = "="