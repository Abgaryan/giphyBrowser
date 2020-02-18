package com.matso.giphybrowser.ui.base

sealed class ViewState<out T : Any>
data class Success<out T : Any>(val data: T) : ViewState<T>()
data class Error<out T : Any>(val error: Exception) : ViewState<T>()
class Loading<out T : Any> : ViewState<T>()

