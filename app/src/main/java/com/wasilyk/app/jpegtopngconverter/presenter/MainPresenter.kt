package com.wasilyk.app.jpegtopngconverter.presenter

import com.wasilyk.app.jpegtopngconverter.view.IConverter
import com.wasilyk.app.jpegtopngconverter.view.MainView
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import moxy.MvpPresenter

class MainPresenter(private val converter: IConverter): MvpPresenter<MainView>() {

    private val disposables = CompositeDisposable()

    fun convert(stringUri: String) {
        viewState.loading()
        converter.convert(stringUri)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { viewState.complete() },
                { viewState.cancel() }
            )
            .addTo(disposables)
    }

    fun cancel() {
        disposables.dispose()
        viewState.cancel()
    }

    override fun onDestroy() {
        disposables.dispose()
        super.onDestroy()
    }

    private fun Disposable.addTo(compositeDisposable: CompositeDisposable) {
        compositeDisposable.add(this)
    }
}