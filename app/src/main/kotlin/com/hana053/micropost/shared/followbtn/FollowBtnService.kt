package com.hana053.micropost.shared.followbtn

import com.hana053.micropost.domain.User
import com.hana053.micropost.interactor.RelationshipInteractor
import com.hana053.micropost.service.HttpErrorHandler
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.functions.Action1
import rx.schedulers.Schedulers


class FollowBtnService(
    private val relationshipInteractor: RelationshipInteractor,
    private val httpErrorHandler: HttpErrorHandler
) {
    fun handleFollowBtnClicks(view: FollowBtnView): Observable<User> {
        val obs = if (view.isFollowState()) follow(view) else unfollow(view)
        return obs.withBtnDisabled(view.enabled)
            .map { view.user }
    }

    private fun follow(view: FollowBtnView): Observable<Void> {
        return relationshipInteractor.follow(view.user.id)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext { view.toUnfollow() }
            .doOnError { httpErrorHandler.handleError(it) }
            .onErrorResumeNext { Observable.empty() }
    }

    private fun unfollow(view: FollowBtnView): Observable<Void> {
        return relationshipInteractor.unfollow(view.user.id)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext { view.toFollow() }
            .doOnError { httpErrorHandler.handleError(it) }
            .onErrorResumeNext { Observable.empty() }
    }

    private fun <T> Observable<T>.withBtnDisabled(enabled: Action1<in Boolean>): Observable<T> {
        return Observable.using({
            enabled.call(false)
        }, { this }, {
            enabled.call(true)
        })
    }
}