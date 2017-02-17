package com.hana053.micropost.testing

import android.preference.PreferenceManager
import com.github.salomonbrys.kodein.Kodein
import com.hana053.micropost.appModule
import org.junit.Before


interface InjectableTest {

    @Before
    fun resetSharedPreferences() {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(app)
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
    }

    fun overrideAppBindings(init: Kodein.Builder.() -> Unit) {
        app.setKodein(Kodein {
            import(appModule(), allowOverride = true)
            init()
        })
    }

}