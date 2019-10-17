package pl.wp.mqtttesting.base

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

abstract class BasePresenter<T> {

    private val compositeDisposable = CompositeDisposable()

    abstract fun attach(view: T)

    abstract fun detach()

    protected fun Disposable.remember() {
        compositeDisposable.add(this)
    }

    fun clear() {
        compositeDisposable.clear()
    }
}