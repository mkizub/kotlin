/*
 * Copyright 2010-2019 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.idea.perf;

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
@TestMetadata("idea/idea-completion/testData/incrementalResolve")
@TestDataPath("$PROJECT_ROOT")
@RunWith(JUnit3RunnerWithInners.class)
public class PerformanceCompletionIncrementalResolveTestGenerated extends AbstractPerformanceCompletionIncrementalResolveTest {
    private void runTest(String testDataFilePath) throws Exception {
        KotlinTestUtils.runTest(this::doPerfTest, this, testDataFilePath);
    }

    public void testAllFilesPresentInIncrementalResolve() throws Exception {
        KotlinTestUtils.assertAllTestsPresentByMetadata(this.getClass(), new File("idea/idea-completion/testData/incrementalResolve"), Pattern.compile("^(.+)\\.kt$"), true);
    }

    @TestMetadata("codeAboveChanged.kt")
    public void testCodeAboveChanged() throws Exception {
        runTest("idea/idea-completion/testData/incrementalResolve/codeAboveChanged.kt");
    }

    @TestMetadata("codeAboveChanged2.kt")
    public void testCodeAboveChanged2() throws Exception {
        runTest("idea/idea-completion/testData/incrementalResolve/codeAboveChanged2.kt");
    }

    @TestMetadata("dataFlowInfoFromPrevStatement.kt")
    public void testDataFlowInfoFromPrevStatement() throws Exception {
        runTest("idea/idea-completion/testData/incrementalResolve/dataFlowInfoFromPrevStatement.kt");
    }

    @TestMetadata("dataFlowInfoFromSameStatement.kt")
    public void testDataFlowInfoFromSameStatement() throws Exception {
        runTest("idea/idea-completion/testData/incrementalResolve/dataFlowInfoFromSameStatement.kt");
    }

    @TestMetadata("doNotAnalyzeComplexStatement.kt")
    public void testDoNotAnalyzeComplexStatement() throws Exception {
        runTest("idea/idea-completion/testData/incrementalResolve/doNotAnalyzeComplexStatement.kt");
    }

    @TestMetadata("noDataFlowFromOldStatement.kt")
    public void testNoDataFlowFromOldStatement() throws Exception {
        runTest("idea/idea-completion/testData/incrementalResolve/noDataFlowFromOldStatement.kt");
    }

    @TestMetadata("noPrevStatement.kt")
    public void testNoPrevStatement() throws Exception {
        runTest("idea/idea-completion/testData/incrementalResolve/noPrevStatement.kt");
    }

    @TestMetadata("outOfBlockModification.kt")
    public void testOutOfBlockModification() throws Exception {
        runTest("idea/idea-completion/testData/incrementalResolve/outOfBlockModification.kt");
    }

    @TestMetadata("prevStatementNotResolved.kt")
    public void testPrevStatementNotResolved() throws Exception {
        runTest("idea/idea-completion/testData/incrementalResolve/prevStatementNotResolved.kt");
    }

    @TestMetadata("sameStatement.kt")
    public void testSameStatement() throws Exception {
        runTest("idea/idea-completion/testData/incrementalResolve/sameStatement.kt");
    }
}
