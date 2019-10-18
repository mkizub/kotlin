/*
 * Copyright 2010-2019 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.nj2k;

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
@TestMetadata("nj2k/testData/copyPastePlainText")
@TestDataPath("$PROJECT_ROOT")
@RunWith(JUnit3RunnerWithInners.class)
public class TextNewJavaToKotlinCopyPasteConversionTestGenerated extends AbstractTextNewJavaToKotlinCopyPasteConversionTest {
    private void runTest(String testDataFilePath) throws Exception {
        KotlinTestUtils.runTest(this::doTest, this, testDataFilePath);
    }

    public void testAllFilesPresentInCopyPastePlainText() throws Exception {
        KotlinTestUtils.assertAllTestsPresentByMetadata(this.getClass(), new File("nj2k/testData/copyPastePlainText"), Pattern.compile("^([^\\.]+)\\.txt$"), true);
    }

    @TestMetadata("AsExpression.txt")
    public void testAsExpression() throws Exception {
        runTest("nj2k/testData/copyPastePlainText/AsExpression.txt");
    }

    @TestMetadata("AsExpressionBody.txt")
    public void testAsExpressionBody() throws Exception {
        runTest("nj2k/testData/copyPastePlainText/AsExpressionBody.txt");
    }

    @TestMetadata("ImportFromTarget.txt")
    public void testImportFromTarget() throws Exception {
        runTest("nj2k/testData/copyPastePlainText/ImportFromTarget.txt");
    }

    @TestMetadata("ImportResolve.txt")
    public void testImportResolve() throws Exception {
        runTest("nj2k/testData/copyPastePlainText/ImportResolve.txt");
    }

    @TestMetadata("InClassContextProperty.txt")
    public void testInClassContextProperty() throws Exception {
        runTest("nj2k/testData/copyPastePlainText/InClassContextProperty.txt");
    }

    @TestMetadata("InsideIdentifier.txt")
    public void testInsideIdentifier() throws Exception {
        runTest("nj2k/testData/copyPastePlainText/InsideIdentifier.txt");
    }

    @TestMetadata("IntoComment.txt")
    public void testIntoComment() throws Exception {
        runTest("nj2k/testData/copyPastePlainText/IntoComment.txt");
    }

    @TestMetadata("IntoRawStringLiteral.txt")
    public void testIntoRawStringLiteral() throws Exception {
        runTest("nj2k/testData/copyPastePlainText/IntoRawStringLiteral.txt");
    }

    @TestMetadata("IntoStringLiteral.txt")
    public void testIntoStringLiteral() throws Exception {
        runTest("nj2k/testData/copyPastePlainText/IntoStringLiteral.txt");
    }

    @TestMetadata("KT13529.txt")
    public void testKT13529() throws Exception {
        runTest("nj2k/testData/copyPastePlainText/KT13529.txt");
    }

    @TestMetadata("KT13529_1.txt")
    public void testKT13529_1() throws Exception {
        runTest("nj2k/testData/copyPastePlainText/KT13529_1.txt");
    }

    @TestMetadata("KT32603.txt")
    public void testKT32603() throws Exception {
        runTest("nj2k/testData/copyPastePlainText/KT32603.txt");
    }

    @TestMetadata("KT32604.txt")
    public void testKT32604() throws Exception {
        runTest("nj2k/testData/copyPastePlainText/KT32604.txt");
    }

    @TestMetadata("LocalAndMemberConflict.txt")
    public void testLocalAndMemberConflict() throws Exception {
        runTest("nj2k/testData/copyPastePlainText/LocalAndMemberConflict.txt");
    }

    @TestMetadata("LocalContextProperty.txt")
    public void testLocalContextProperty() throws Exception {
        runTest("nj2k/testData/copyPastePlainText/LocalContextProperty.txt");
    }

    @TestMetadata("MembersIntoClass.txt")
    public void testMembersIntoClass() throws Exception {
        runTest("nj2k/testData/copyPastePlainText/MembersIntoClass.txt");
    }

    @TestMetadata("MembersToTopLevel.txt")
    public void testMembersToTopLevel() throws Exception {
        runTest("nj2k/testData/copyPastePlainText/MembersToTopLevel.txt");
    }

    @TestMetadata("Override.txt")
    public void testOverride() throws Exception {
        runTest("nj2k/testData/copyPastePlainText/Override.txt");
    }

    @TestMetadata("OverrideInterface.txt")
    public void testOverrideInterface() throws Exception {
        runTest("nj2k/testData/copyPastePlainText/OverrideInterface.txt");
    }

    @TestMetadata("PostProcessing.txt")
    public void testPostProcessing() throws Exception {
        runTest("nj2k/testData/copyPastePlainText/PostProcessing.txt");
    }

    @TestMetadata("StatementsIntoFunction.txt")
    public void testStatementsIntoFunction() throws Exception {
        runTest("nj2k/testData/copyPastePlainText/StatementsIntoFunction.txt");
    }

    @TestMetadata("TopLevelContextProperty.txt")
    public void testTopLevelContextProperty() throws Exception {
        runTest("nj2k/testData/copyPastePlainText/TopLevelContextProperty.txt");
    }

    @TestMetadata("WholeFile.txt")
    public void testWholeFile() throws Exception {
        runTest("nj2k/testData/copyPastePlainText/WholeFile.txt");
    }
}
