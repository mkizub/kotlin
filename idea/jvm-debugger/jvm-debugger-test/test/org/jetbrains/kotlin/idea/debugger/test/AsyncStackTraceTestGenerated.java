/*
 * Copyright 2010-2019 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.idea.debugger.test;

import com.intellij.testFramework.TestDataPath;
import org.jetbrains.kotlin.test.JUnit3RunnerWithInners;
import org.jetbrains.kotlin.test.KotlinTestUtils;
import org.jetbrains.kotlin.test.TargetBackend;
import org.jetbrains.kotlin.test.TestMetadata;
import org.junit.runner.RunWith;

import java.io.File;
import java.util.regex.Pattern;

/** This class is generated by {@link org.jetbrains.kotlin.generators.tests.TestsPackage}. DO NOT MODIFY MANUALLY */
@SuppressWarnings("all")
@TestMetadata("idea/jvm-debugger/jvm-debugger-test/testData/asyncStackTrace")
@TestDataPath("$PROJECT_ROOT")
@RunWith(JUnit3RunnerWithInners.class)
public class AsyncStackTraceTestGenerated extends AbstractAsyncStackTraceTest {
    private void runTest(String testDataFilePath) throws Exception {
        KotlinTestUtils.runTest(this::doTest, this, testDataFilePath);
    }

    public void testAllFilesPresentInAsyncStackTrace() throws Exception {
        KotlinTestUtils.assertAllTestsPresentByMetadata(this.getClass(), new File("idea/jvm-debugger/jvm-debugger-test/testData/asyncStackTrace"), Pattern.compile("^(.+)\\.kt$"), true);
    }

    @TestMetadata("asyncFunctions.kt")
    public void testAsyncFunctions() throws Exception {
        runTest("idea/jvm-debugger/jvm-debugger-test/testData/asyncStackTrace/asyncFunctions.kt");
    }

    @TestMetadata("asyncLambdas.kt")
    public void testAsyncLambdas() throws Exception {
        runTest("idea/jvm-debugger/jvm-debugger-test/testData/asyncStackTrace/asyncLambdas.kt");
    }

    @TestMetadata("asyncSimple.kt")
    public void testAsyncSimple() throws Exception {
        runTest("idea/jvm-debugger/jvm-debugger-test/testData/asyncStackTrace/asyncSimple.kt");
    }
}
