package com.wasilyk.app.jpegtopngconverter.view

import io.reactivex.rxjava3.core.Completable

interface IConverter {
    fun convert(stringUri: String): Completable
}