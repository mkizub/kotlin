/*
 * Copyright 2010-2019 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.fir.java;

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
@TestMetadata("compiler/fir/resolve/testData/enhancement")
@TestDataPath("$PROJECT_ROOT")
@RunWith(JUnit3RunnerWithInners.class)
public class OwnFirTypeEnhancementTestGenerated extends AbstractOwnFirTypeEnhancementTest {
    private void runTest(String testDataFilePath) throws Exception {
        KotlinTestUtils.runTest(this::doTest, this, testDataFilePath);
    }

    public void testAllFilesPresentInEnhancement() throws Exception {
        KotlinTestUtils.assertAllTestsPresentByMetadata(this.getClass(), new File("compiler/fir/resolve/testData/enhancement"), Pattern.compile("^(.+)\\.java$"), true);
    }

    @TestMetadata("compiler/fir/resolve/testData/enhancement/jsr305")
    @TestDataPath("$PROJECT_ROOT")
    @RunWith(JUnit3RunnerWithInners.class)
    public static class Jsr305 extends AbstractOwnFirTypeEnhancementTest {
        private void runTest(String testDataFilePath) throws Exception {
            KotlinTestUtils.runTest(this::doTest, this, testDataFilePath);
        }

        public void testAllFilesPresentInJsr305() throws Exception {
            KotlinTestUtils.assertAllTestsPresentByMetadata(this.getClass(), new File("compiler/fir/resolve/testData/enhancement/jsr305"), Pattern.compile("^(.+)\\.java$"), true);
        }

        @TestMetadata("NonNullNever.java")
        public void testNonNullNever() throws Exception {
            runTest("compiler/fir/resolve/testData/enhancement/jsr305/NonNullNever.java");
        }

        @TestMetadata("Simple.java")
        public void testSimple() throws Exception {
            runTest("compiler/fir/resolve/testData/enhancement/jsr305/Simple.java");
        }

        @TestMetadata("Strange.java")
        public void testStrange() throws Exception {
            runTest("compiler/fir/resolve/testData/enhancement/jsr305/Strange.java");
        }

        @TestMetadata("compiler/fir/resolve/testData/enhancement/jsr305/typeQualifierDefault")
        @TestDataPath("$PROJECT_ROOT")
        @RunWith(JUnit3RunnerWithInners.class)
        public static class TypeQualifierDefault extends AbstractOwnFirTypeEnhancementTest {
            private void runTest(String testDataFilePath) throws Exception {
                KotlinTestUtils.runTest(this::doTest, this, testDataFilePath);
            }

            public void testAllFilesPresentInTypeQualifierDefault() throws Exception {
                KotlinTestUtils.assertAllTestsPresentByMetadata(this.getClass(), new File("compiler/fir/resolve/testData/enhancement/jsr305/typeQualifierDefault"), Pattern.compile("^(.+)\\.java$"), true);
            }

            @TestMetadata("FieldsAreNullable.java")
            public void testFieldsAreNullable() throws Exception {
                runTest("compiler/fir/resolve/testData/enhancement/jsr305/typeQualifierDefault/FieldsAreNullable.java");
            }

            @TestMetadata("ForceFlexibility.java")
            public void testForceFlexibility() throws Exception {
                runTest("compiler/fir/resolve/testData/enhancement/jsr305/typeQualifierDefault/ForceFlexibility.java");
            }

            @TestMetadata("ForceFlexibleOverOverrides.java")
            public void testForceFlexibleOverOverrides() throws Exception {
                runTest("compiler/fir/resolve/testData/enhancement/jsr305/typeQualifierDefault/ForceFlexibleOverOverrides.java");
            }

            @TestMetadata("NullabilityFromOverridden.java")
            public void testNullabilityFromOverridden() throws Exception {
                runTest("compiler/fir/resolve/testData/enhancement/jsr305/typeQualifierDefault/NullabilityFromOverridden.java");
            }

            @TestMetadata("OverridingDefaultQualifier.java")
            public void testOverridingDefaultQualifier() throws Exception {
                runTest("compiler/fir/resolve/testData/enhancement/jsr305/typeQualifierDefault/OverridingDefaultQualifier.java");
            }

            @TestMetadata("ParametersAreNonnullByDefault.java")
            public void testParametersAreNonnullByDefault() throws Exception {
                runTest("compiler/fir/resolve/testData/enhancement/jsr305/typeQualifierDefault/ParametersAreNonnullByDefault.java");
            }

            @TestMetadata("ParametersAreNonnullByDefaultPackage.java")
            public void testParametersAreNonnullByDefaultPackage() throws Exception {
                runTest("compiler/fir/resolve/testData/enhancement/jsr305/typeQualifierDefault/ParametersAreNonnullByDefaultPackage.java");
            }

            @TestMetadata("SpringNullable.java")
            public void testSpringNullable() throws Exception {
                runTest("compiler/fir/resolve/testData/enhancement/jsr305/typeQualifierDefault/SpringNullable.java");
            }

            @TestMetadata("SpringNullablePackage.java")
            public void testSpringNullablePackage() throws Exception {
                runTest("compiler/fir/resolve/testData/enhancement/jsr305/typeQualifierDefault/SpringNullablePackage.java");
            }
        }
    }

    @TestMetadata("compiler/fir/resolve/testData/enhancement/mapping")
    @TestDataPath("$PROJECT_ROOT")
    @RunWith(JUnit3RunnerWithInners.class)
    public static class Mapping extends AbstractOwnFirTypeEnhancementTest {
        private void runTest(String testDataFilePath) throws Exception {
            KotlinTestUtils.runTest(this::doTest, this, testDataFilePath);
        }

        @TestMetadata("AbstractMap.java")
        public void testAbstractMap() throws Exception {
            runTest("compiler/fir/resolve/testData/enhancement/mapping/AbstractMap.java");
        }

        public void testAllFilesPresentInMapping() throws Exception {
            KotlinTestUtils.assertAllTestsPresentByMetadata(this.getClass(), new File("compiler/fir/resolve/testData/enhancement/mapping"), Pattern.compile("^(.+)\\.java$"), true);
        }
    }

    @TestMetadata("compiler/fir/resolve/testData/enhancement/signatureAnnotations")
    @TestDataPath("$PROJECT_ROOT")
    @RunWith(JUnit3RunnerWithInners.class)
    public static class SignatureAnnotations extends AbstractOwnFirTypeEnhancementTest {
        private void runTest(String testDataFilePath) throws Exception {
            KotlinTestUtils.runTest(this::doTest, this, testDataFilePath);
        }

        public void testAllFilesPresentInSignatureAnnotations() throws Exception {
            KotlinTestUtils.assertAllTestsPresentByMetadata(this.getClass(), new File("compiler/fir/resolve/testData/enhancement/signatureAnnotations"), Pattern.compile("^(.+)\\.java$"), true);
        }

        @TestMetadata("DefaultEnum.java")
        public void testDefaultEnum() throws Exception {
            runTest("compiler/fir/resolve/testData/enhancement/signatureAnnotations/DefaultEnum.java");
        }

        @TestMetadata("DefaultLongLiteral.java")
        public void testDefaultLongLiteral() throws Exception {
            runTest("compiler/fir/resolve/testData/enhancement/signatureAnnotations/DefaultLongLiteral.java");
        }

        @TestMetadata("DefaultNull.java")
        public void testDefaultNull() throws Exception {
            runTest("compiler/fir/resolve/testData/enhancement/signatureAnnotations/DefaultNull.java");
        }

        @TestMetadata("DefaultNullAndParameter.java")
        public void testDefaultNullAndParameter() throws Exception {
            runTest("compiler/fir/resolve/testData/enhancement/signatureAnnotations/DefaultNullAndParameter.java");
        }

        @TestMetadata("DefaultParameter.java")
        public void testDefaultParameter() throws Exception {
            runTest("compiler/fir/resolve/testData/enhancement/signatureAnnotations/DefaultParameter.java");
        }

        @TestMetadata("EmptyParameterName.java")
        public void testEmptyParameterName() throws Exception {
            runTest("compiler/fir/resolve/testData/enhancement/signatureAnnotations/EmptyParameterName.java");
        }

        @TestMetadata("ReorderedParameterNames.java")
        public void testReorderedParameterNames() throws Exception {
            runTest("compiler/fir/resolve/testData/enhancement/signatureAnnotations/ReorderedParameterNames.java");
        }

        @TestMetadata("SameParameterName.java")
        public void testSameParameterName() throws Exception {
            runTest("compiler/fir/resolve/testData/enhancement/signatureAnnotations/SameParameterName.java");
        }

        @TestMetadata("SpecialCharsParameterName.java")
        public void testSpecialCharsParameterName() throws Exception {
            runTest("compiler/fir/resolve/testData/enhancement/signatureAnnotations/SpecialCharsParameterName.java");
        }

        @TestMetadata("StaticMethodWithDefaultValue.java")
        public void testStaticMethodWithDefaultValue() throws Exception {
            runTest("compiler/fir/resolve/testData/enhancement/signatureAnnotations/StaticMethodWithDefaultValue.java");
        }
    }
}
