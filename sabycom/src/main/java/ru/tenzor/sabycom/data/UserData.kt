package ru.tenzor.sabycom.data

import java.util.UUID

/**
 * @author ma.kolpakov
 */
data class UserData(
    val id: UUID,
    val name: String? = null,
    val surname: String? = null,
    val email: String? = null,
    val phone: String? = null,
)
