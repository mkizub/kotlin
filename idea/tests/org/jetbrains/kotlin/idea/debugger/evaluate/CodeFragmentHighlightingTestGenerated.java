/*
 * Copyright 2010-2019 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.idea.debugger.evaluate;

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
@RunWith(JUnit3RunnerWithInners.class)
public class CodeFragmentHighlightingTestGenerated extends AbstractCodeFragmentHighlightingTest {
    @TestMetadata("idea/testData/checker/codeFragments")
    @TestDataPath("$PROJECT_ROOT")
    @RunWith(JUnit3RunnerWithInners.class)
    public static class CodeFragments extends AbstractCodeFragmentHighlightingTest {
        private void runTest(String testDataFilePath) throws Exception {
            KotlinTestUtils.runTest(this::doTest, this, testDataFilePath);
        }

        public void testAllFilesPresentInCodeFragments() throws Exception {
            KotlinTestUtils.assertAllTestsPresentByMetadata(this.getClass(), new File("idea/testData/checker/codeFragments"), Pattern.compile("^(.+)\\.kt$"), false);
        }

        @TestMetadata("anonymousObject.kt")
        public void testAnonymousObject() throws Exception {
            runTest("idea/testData/checker/codeFragments/anonymousObject.kt");
        }

        @TestMetadata("binaryExpression.kt")
        public void testBinaryExpression() throws Exception {
            runTest("idea/testData/checker/codeFragments/binaryExpression.kt");
        }

        @TestMetadata("blockCodeFragment.kt")
        public void testBlockCodeFragment() throws Exception {
            runTest("idea/testData/checker/codeFragments/blockCodeFragment.kt");
        }

        @TestMetadata("callExpression.kt")
        public void testCallExpression() throws Exception {
            runTest("idea/testData/checker/codeFragments/callExpression.kt");
        }

        @TestMetadata("classHeader.kt")
        public void testClassHeader() throws Exception {
            runTest("idea/testData/checker/codeFragments/classHeader.kt");
        }

        @TestMetadata("classHeaderWithTypeArguments.kt")
        public void testClassHeaderWithTypeArguments() throws Exception {
            runTest("idea/testData/checker/codeFragments/classHeaderWithTypeArguments.kt");
        }

        @TestMetadata("contextElementAsStatement.kt")
        public void testContextElementAsStatement() throws Exception {
            runTest("idea/testData/checker/codeFragments/contextElementAsStatement.kt");
        }

        @TestMetadata("elementAtIfWithoutBraces.kt")
        public void testElementAtIfWithoutBraces() throws Exception {
            runTest("idea/testData/checker/codeFragments/elementAtIfWithoutBraces.kt");
        }

        @TestMetadata("elementAtWhenBranch.kt")
        public void testElementAtWhenBranch() throws Exception {
            runTest("idea/testData/checker/codeFragments/elementAtWhenBranch.kt");
        }

        @TestMetadata("localVariables.kt")
        public void testLocalVariables() throws Exception {
            runTest("idea/testData/checker/codeFragments/localVariables.kt");
        }

        @TestMetadata("localVariablesOnReturn.kt")
        public void testLocalVariablesOnReturn() throws Exception {
            runTest("idea/testData/checker/codeFragments/localVariablesOnReturn.kt");
        }

        @TestMetadata("primaryConstructor.kt")
        public void testPrimaryConstructor() throws Exception {
            runTest("idea/testData/checker/codeFragments/primaryConstructor.kt");
        }

        @TestMetadata("primaryConstructorLocal.kt")
        public void testPrimaryConstructorLocal() throws Exception {
            runTest("idea/testData/checker/codeFragments/primaryConstructorLocal.kt");
        }

        @TestMetadata("privateFunArgumentsResolve.kt")
        public void testPrivateFunArgumentsResolve() throws Exception {
            runTest("idea/testData/checker/codeFragments/privateFunArgumentsResolve.kt");
        }

        @TestMetadata("privateFunTypeArguments.kt")
        public void testPrivateFunTypeArguments() throws Exception {
            runTest("idea/testData/checker/codeFragments/privateFunTypeArguments.kt");
        }

        @TestMetadata("privateMember.kt")
        public void testPrivateMember() throws Exception {
            runTest("idea/testData/checker/codeFragments/privateMember.kt");
        }

        @TestMetadata("privateMembers.kt")
        public void testPrivateMembers() throws Exception {
            runTest("idea/testData/checker/codeFragments/privateMembers.kt");
        }

        @TestMetadata("protectedMember.kt")
        public void testProtectedMember() throws Exception {
            runTest("idea/testData/checker/codeFragments/protectedMember.kt");
        }

        @TestMetadata("secondaryConstructor.kt")
        public void testSecondaryConstructor() throws Exception {
            runTest("idea/testData/checker/codeFragments/secondaryConstructor.kt");
        }

        @TestMetadata("secondaryConstructorWithoutBraces.kt")
        public void testSecondaryConstructorWithoutBraces() throws Exception {
            runTest("idea/testData/checker/codeFragments/secondaryConstructorWithoutBraces.kt");
        }

        @TestMetadata("simpleNameExpression.kt")
        public void testSimpleNameExpression() throws Exception {
            runTest("idea/testData/checker/codeFragments/simpleNameExpression.kt");
        }

        @TestMetadata("smartCasts.kt")
        public void testSmartCasts() throws Exception {
            runTest("idea/testData/checker/codeFragments/smartCasts.kt");
        }

        @TestMetadata("startingFromReturn.kt")
        public void testStartingFromReturn() throws Exception {
            runTest("idea/testData/checker/codeFragments/startingFromReturn.kt");
        }

        @TestMetadata("unusedEquals.kt")
        public void testUnusedEquals() throws Exception {
            runTest("idea/testData/checker/codeFragments/unusedEquals.kt");
        }

        @TestMetadata("withoutBodyFunction.kt")
        public void testWithoutBodyFunction() throws Exception {
            runTest("idea/testData/checker/codeFragments/withoutBodyFunction.kt");
        }

        @TestMetadata("withoutBodyProperty.kt")
        public void testWithoutBodyProperty() throws Exception {
            runTest("idea/testData/checker/codeFragments/withoutBodyProperty.kt");
        }
    }

    @TestMetadata("idea/testData/checker/codeFragments/imports")
    @TestDataPath("$PROJECT_ROOT")
    @RunWith(JUnit3RunnerWithInners.class)
    public static class Imports extends AbstractCodeFragmentHighlightingTest {
        private void runTest(String testDataFilePath) throws Exception {
            KotlinTestUtils.runTest(this::doTestWithImport, this, testDataFilePath);
        }

        public void testAllFilesPresentInImports() throws Exception {
            KotlinTestUtils.assertAllTestsPresentByMetadata(this.getClass(), new File("idea/testData/checker/codeFragments/imports"), Pattern.compile("^(.+)\\.kt$"), true);
        }

        @TestMetadata("hashMap.kt")
        public void testHashMap() throws Exception {
            runTest("idea/testData/checker/codeFragments/imports/hashMap.kt");
        }
    }
}
