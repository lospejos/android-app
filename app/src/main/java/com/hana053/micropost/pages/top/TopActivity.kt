package com.hana053.micropost.pages.top

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.github.salomonbrys.kodein.KodeinInjector
import com.github.salomonbrys.kodein.android.AppCompatActivityInjector
import com.github.salomonbrys.kodein.instance
import com.hana053.micropost.R
import com.hana053.micropost.getOverridingModule
import rx.subscriptions.CompositeSubscription


class TopActivity : AppCompatActivity(), AppCompatActivityInjector {

    override val injector: KodeinInjector = KodeinInjector()

    private val presenter: TopPresenter by instance()
    private val view: TopView by instance()

    private var subscription: CompositeSubscription? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_top)
        initializeInjector()

        subscription = presenter.bind(view)
    }

    override fun onDestroy() {
        super.onDestroy()
        subscription?.unsubscribe()
        destroyInjector()
    }

    override fun provideOverridingModule()
        = getOverridingModule(TopActivity::class.java)

}

