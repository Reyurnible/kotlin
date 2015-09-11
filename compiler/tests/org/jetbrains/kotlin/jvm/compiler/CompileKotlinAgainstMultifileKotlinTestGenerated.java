/*
 * Copyright 2010-2015 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jetbrains.kotlin.jvm.compiler;

import com.intellij.testFramework.TestDataPath;
import org.jetbrains.kotlin.test.JUnit3RunnerWithInners;
import org.jetbrains.kotlin.test.JetTestUtils;
import org.jetbrains.kotlin.test.TestMetadata;
import org.junit.runner.RunWith;

import java.io.File;
import java.util.regex.Pattern;

/** This class is generated by {@link org.jetbrains.kotlin.generators.tests.TestsPackage}. DO NOT MODIFY MANUALLY */
@SuppressWarnings("all")
@TestMetadata("compiler/testData/codegen/boxMultifileClasses")
@TestDataPath("$PROJECT_ROOT")
@RunWith(JUnit3RunnerWithInners.class)
public class CompileKotlinAgainstMultifileKotlinTestGenerated extends AbstractCompileKotlinAgainstMultifileKotlinTest {
    public void testAllFilesPresentInBoxMultifileClasses() throws Exception {
        JetTestUtils.assertAllTestsPresentByMetadata(this.getClass(), new File("compiler/testData/codegen/boxMultifileClasses"), Pattern.compile("^(.+)\\.1.kt$"), true);
    }

    @TestMetadata("compiler/testData/codegen/boxMultifileClasses/calls")
    @TestDataPath("$PROJECT_ROOT")
    @RunWith(JUnit3RunnerWithInners.class)
    public static class Calls extends AbstractCompileKotlinAgainstMultifileKotlinTest {
        public void testAllFilesPresentInCalls() throws Exception {
            JetTestUtils.assertAllTestsPresentByMetadata(this.getClass(), new File("compiler/testData/codegen/boxMultifileClasses/calls"), Pattern.compile("^(.+)\\.1.kt$"), true);
        }

        @TestMetadata("callFromOtherPackage.1.kt")
        public void testCallFromOtherPackage() throws Exception {
            String fileName = JetTestUtils.navigationMetadata("compiler/testData/codegen/boxMultifileClasses/calls/callFromOtherPackage.1.kt");
            doBoxTest(fileName);
        }
    }
}