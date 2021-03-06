/*
 * Copyright 2010-2019 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.jvm.abi;

import com.intellij.testFramework.TestDataPath;
import org.jetbrains.kotlin.test.JUnit3RunnerWithInners;
import org.jetbrains.kotlin.test.KotlinTestUtils;
import org.jetbrains.kotlin.test.TestMetadata;
import org.junit.runner.RunWith;

import java.io.File;
import java.util.regex.Pattern;

/** This class is generated by {@link org.jetbrains.kotlin.generators.tests.TestsPackage}. DO NOT MODIFY MANUALLY */
@SuppressWarnings("all")
@TestMetadata("plugins/jvm-abi-gen/testData/compile")
@TestDataPath("$PROJECT_ROOT")
@RunWith(JUnit3RunnerWithInners.class)
public class CompileAgainstJvmAbiTestGenerated extends AbstractCompileAgainstJvmAbiTest {
    private void runTest(String testDataFilePath) throws Exception {
        KotlinTestUtils.runTest(this::doTest, this, testDataFilePath);
    }

    public void testAllFilesPresentInCompile() throws Exception {
        KotlinTestUtils.assertAllTestsPresentByMetadata(this.getClass(), new File("plugins/jvm-abi-gen/testData/compile"), Pattern.compile("^([^\\.]+)$"), false);
    }

    @TestMetadata("anonymousObject")
    public void testAnonymousObject() throws Exception {
        runTest("plugins/jvm-abi-gen/testData/compile/anonymousObject/");
    }

    @TestMetadata("classes")
    public void testClasses() throws Exception {
        runTest("plugins/jvm-abi-gen/testData/compile/classes/");
    }

    @TestMetadata("clinit")
    public void testClinit() throws Exception {
        runTest("plugins/jvm-abi-gen/testData/compile/clinit/");
    }

    @TestMetadata("inlineAnonymousObject")
    public void testInlineAnonymousObject() throws Exception {
        runTest("plugins/jvm-abi-gen/testData/compile/inlineAnonymousObject/");
    }

    @TestMetadata("inlineReifiedFunction")
    public void testInlineReifiedFunction() throws Exception {
        runTest("plugins/jvm-abi-gen/testData/compile/inlineReifiedFunction/");
    }

    @TestMetadata("privateOnlyConstructors")
    public void testPrivateOnlyConstructors() throws Exception {
        runTest("plugins/jvm-abi-gen/testData/compile/privateOnlyConstructors/");
    }

    @TestMetadata("topLevel")
    public void testTopLevel() throws Exception {
        runTest("plugins/jvm-abi-gen/testData/compile/topLevel/");
    }
}
