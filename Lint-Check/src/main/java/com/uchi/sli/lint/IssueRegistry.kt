/*
 *  Copyright (c) Ashish Yadav <mailtoashish693@gmail.com>
 *
 *  This program is free software; you can redistribute it and/or modify it
 */

@file:Suppress("UnstableApiUsage")

package com.uchi.sli.lint
import com.android.tools.lint.client.api.IssueRegistry
import com.android.tools.lint.detector.api.*
import com.uchi.sli.lint.rules.CopyrightHeaderExistence
import java.util.*

open class IssueRegistry : IssueRegistry() {
    // Keep this list lexicographically ordered.
    override val issues: List<Issue>
        get() {
            // Keep this list lexicographically ordered.
            return listOf(
                CopyrightHeaderExistence.ISSUE
            )
        }

    override val api: Int
        get() = CURRENT_API
}