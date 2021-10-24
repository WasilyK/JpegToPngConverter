package com.wasilyk.app.jpegtopngconverter.view

import moxy.MvpView
import moxy.viewstate.strategy.SingleStateStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(SingleStateStrategy::class)
interface MainView: MvpView {
    fun loading()
    fun complete()
    fun error()
    fun cancel()
}