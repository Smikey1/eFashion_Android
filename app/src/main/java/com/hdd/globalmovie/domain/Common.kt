package com.hdd.globalmovie.domain

import android.content.Context
import com.hdd.globalmovie.R
import java.io.File

object Common {
    fun getAppPath(context: Context):String {
        val directory = File(context.getExternalFilesDir(null).toString()
                    + File.separator
                    + context.resources.getString(R.string.app_name)
                    + File.separator
        )
        if (!directory.exists()) {
            directory.mkdir()
        }
            return directory.path + File.separator
    }
}

