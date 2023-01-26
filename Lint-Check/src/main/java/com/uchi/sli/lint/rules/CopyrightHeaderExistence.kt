/*
 *  Copyright (c) Ashish Yadav <mailtoashish693@gmail.com>
 *
 *  This program is free software; you can redistribute it and/or modify it
 */
@file:Suppress("UnstableApiUsage")

package com.uchi.sli.lint.rules

import com.android.tools.lint.detector.api.*
import com.google.common.annotations.Beta
import com.google.common.annotations.VisibleForTesting
import com.uchi.sli.lint.utils.Constants
import org.jetbrains.uast.UClass
import org.jetbrains.uast.UElement
import java.util.regex.Pattern

@Beta
class CopyrightHeaderExistence : Detector(), SourceCodeScanner {
    companion object{
        /**
         * &#64;SuppressWarnings doesn't work as it's the first statement, so allow suppression via:
         * <pre>//noinspection MissingCopyrightHeader &lt;reason&gt;</pre>
         */
        private val IGNORE_CHECK_PATTERN = Pattern.compile("MissingCopyrightHeader")

        private val COPYRIGHT_PATTERN = Pattern.compile("This program is free software")

        const val DESCRIPTION = "All files in Sling must contain a copyright header"
        @VisibleForTesting
        const val ID = "MissingCopyrightHeader"
        private const val EXPLANATION = "All files in Sling must contain a " +
                "copyright header. The copyright header can be set in " +
                "Settings - Editor - Copyright - Copyright Profiles - Add Profile - Sling. " +
                "Or search in Settings for 'Copyright' and it may be added as the first line of the file."

        val ISSUE: Issue = Issue.create(
            ID,
            DESCRIPTION,
            EXPLANATION,
            Constants.SLING_CODE_STYLE_CATEGORY,
            Constants.SLING_CODE_STYLE_PRIORITY,
            Constants.SLING_CODE_STYLE_SEVERITY,
            Implementation(CopyrightHeaderExistence::class.java, Scope.JAVA_FILE_SCOPE)
        )

    }

    override fun getApplicableUastTypes(): List<Class<out UElement?>> {
        return listOf(UClass::class.java)
    }

    override fun afterCheckFile(context: Context) {
        val contents = context.getContents()
        if (contents == null || COPYRIGHT_PATTERN.matcher(contents).find()
            || IGNORE_CHECK_PATTERN.matcher(contents).find()
        ) {
            return
        }

        // select from the start to the first line with content
        var end = 0
        var foundChar = false
        for (i in contents.indices) {
            foundChar = foundChar or !Character.isWhitespace(contents[i])
            if (foundChar && contents[i] == '\n') {
                end = i
                break
            }
        }

        // If there is no line break, highlight the contents
        val endOffset = if (end == 0) contents.length else end
        val location: Location = Location.create(context.file, contents.subSequence(0, endOffset), 0, endOffset)
        context.report(ISSUE, location, DESCRIPTION)
    }


}