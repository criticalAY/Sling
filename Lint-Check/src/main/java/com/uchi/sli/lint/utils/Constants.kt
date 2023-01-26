/*
 *  Copyright (c) Ashish Yadav <mailtoashish693@gmail.com>
 *
 *  This program is free software; you can redistribute it and/or modify it
 */
package com.uchi.sli.lint.utils

import com.android.tools.lint.detector.api.Category
import com.android.tools.lint.detector.api.Severity

/**
 * Hold some constants applicable to all lint issues.
 */
object Constants {
    /**
     * A special [Category] which groups the Lint issues related to the usage of the new SystemTime class as a
     * sub category for [Category.CORRECTNESS].
     */
    val SLING_TIME_CATEGORY: Category = Category.create(Category.CORRECTNESS, "Sling", 10)

    /**
     * The priority for the Lint issues used by all rules related to the restrictions introduced by SystemTime.
     */
    const val SLING_TIME_PRIORITY = 10

    /**
     * The severity for the Lint issues used by all rules related to the restrictions introduced by SystemTime.
     */
    val SLING_TIME_SEVERITY = Severity.FATAL

    /**
     * A special [Category] which groups the Lint issues related to Code Style as a
     * sub category for [Category.COMPLIANCE].
     */
    val SLING_CODE_STYLE_CATEGORY: Category = Category.create(Category.COMPLIANCE, "CodeStyle", 10)

    /**
     * The priority for the Lint issues used by rules related to Code Style.
     */
    const val SLING_CODE_STYLE_PRIORITY = 10

    /**
     * The severity for the Lint issues used by rules related to Code Style.
     */
    val SLING_CODE_STYLE_SEVERITY = Severity.FATAL

    /**
     * A special [Category] which groups the Lint issues related to XML as a
     * sub category for [Category.CORRECTNESS].
     */
    val SLING_XML_CATEGORY: Category = Category.create(Category.CORRECTNESS, "XML", 10)

    /**
     * The priority for the Lint issues used by rules related to XML.
     */
    const val SLING_XML_PRIORITY = 10

    /**
     * The severity for the Lint issues used by rules related to XML.
     */
    val SLING_XML_SEVERITY = Severity.FATAL
}
