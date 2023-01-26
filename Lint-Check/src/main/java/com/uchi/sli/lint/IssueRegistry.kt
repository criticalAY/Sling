/*
 *  Copyright (c) Ashish Yadav <mailtoashish693@gmail.com>
 *
 *  This program is free software; you can redistribute it and/or modify it
 *
 *  This program is distributed in the hope that it will be useful, but WITHOUT ANY
 *  WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 *  PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 */

@file:Suppress("UnstableApiUsage")

package com.uchi.sli.lint
import com.android.tools.lint.client.api.IssueRegistry
import com.android.tools.lint.detector.api.*

import java.util.*

open class IssueRegistry : IssueRegistry() {
    // Keep this list lexicographically ordered.
    override val issues: List<Issue>
        get() {
            // Keep this list lexicographically ordered.
            return listOf(
            )
        }
}