/*
 * Copyright (c) 2023 Ashish Yadav <mailtoashish693@gmail.com>
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 3 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program.  If not, see <http://www.gnu.org/licenses/>.
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
 *  This program is free software; you can redistribute it and/or modify it under
 *  the terms of the GNU General Public License as published by the Free Software
 *  Foundation; either version 3 of the License, or (at your option) any later
 *  version.
 *
 *  This program is distributed in the hope that it will be useful, but WITHOUT ANY
 *  WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 *  PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License along with
 *  this program.  If not, see <http://www.gnu.org/licenses/>.
 */"""

    @Language("JAVA")
    private val mNoCopyrightHeader = """
        
        package com.uchi.upgrade;
        
        import org.json.JSONException;
        import org.json.JSONObject;
        
        import timber.log.Timber;
        
        public class Upgrade {
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
