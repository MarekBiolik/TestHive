package com.testhive.android

import android.content.Context
import com.testhive.android.data.FileDeckLibrary
import com.testhive.android.data.FileProgressStore

class AppContainer(context: Context) {
    val progress = FileProgressStore(context)
    val decks = FileDeckLibrary(context).also { it.ensureBundledSample() }
}
