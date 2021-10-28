package ru.tensor.sabycom.widget

import ru.tensor.sabycom.data.UserData


internal sealed class WebWidgetState

internal class Opened(val url: String, val userData: UserData) : WebWidgetState()

internal object Closed : WebWidgetState()
