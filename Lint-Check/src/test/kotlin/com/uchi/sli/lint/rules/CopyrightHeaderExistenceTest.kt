/*
 *  Copyright (c) Ashish Yadav <mailtoashish693@gmail.com>
 *
 *  This program is free software; you can redistribute it and/or modify it
 */
@file:Suppress("UnstableApiUsage")
package com.uchi.sli.lint.rules

import com.android.tools.lint.checks.infrastructure.TestFile
import com.android.tools.lint.checks.infrastructure.TestLintTask
import com.google.common.annotations.Beta
import org.intellij.lang.annotations.Language
import org.junit.Assert
import org.junit.Test

@Suppress("UnstableApiUsage")
@Beta
class CopyrightHeaderExistenceTest {
        @Language("JAVA")
        private val mCopyrightHeader = """/*
 *  Copyright (c) Ashish Yadav <mailtoashish693@gmail.com>
 *
 *  This program is free software; you can redistribute it and/or modify it
 */"""

    @Language("JAVA")
    private val mNoCopyrightHeader = """
        
        package com.ichi2.upgrade;
        
        import com.ichi2.libanki.Collection;
        
        import org.json.JSONException;
        import org.json.JSONObject;
        
        import timber.log.Timber;
        
        public class Upgrade {
        foo(){
        }
        }
    """.trimIndent()

        @Test
        fun fileWithCopyrightHeaderPasses() {
            TestLintTask.lint()
                .allowMissingSdk()
                .files(TestFile.JavaTestFile.create(mCopyrightHeader))
                .issues(CopyrightHeaderExistence.ISSUE)
                .run()
                .expectClean()
        }

        @Test
        fun fileWithNoCopyrightHeaderFails() {
            TestLintTask.lint()
                .allowMissingSdk()
                .allowCompilationErrors() // import failures
                .files(TestFile.JavaTestFile.create(mNoCopyrightHeader))
                .issues(CopyrightHeaderExistence.ISSUE)
                .run()
                .expectErrorCount(1)
                .check({ output: String ->
                    Assert.assertTrue(output.contains(CopyrightHeaderExistence.ID))
                    Assert.assertTrue(output.contains(CopyrightHeaderExistence.DESCRIPTION))
                })
        }
}