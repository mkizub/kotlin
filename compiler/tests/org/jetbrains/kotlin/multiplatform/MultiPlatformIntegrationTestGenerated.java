/*
 * Copyright 2010-2019 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.multiplatform;

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
@TestMetadata("compiler/testData/multiplatform")
@TestDataPath("$PROJECT_ROOT")
@RunWith(JUnit3RunnerWithInners.class)
public class MultiPlatformIntegrationTestGenerated extends AbstractMultiPlatformIntegrationTest {
    private void runTest(String testDataFilePath) throws Exception {
        KotlinTestUtils.runTest(this::doTest, this, testDataFilePath);
    }

    public void testAllFilesPresentInMultiplatform() throws Exception {
        KotlinTestUtils.assertAllTestsPresentByMetadata(this.getClass(), new File("compiler/testData/multiplatform"), Pattern.compile("^([^\\.]+)$"), true);
    }

    @TestMetadata("compatibleProperties")
    public void testCompatibleProperties() throws Exception {
        runTest("compiler/testData/multiplatform/compatibleProperties/");
    }

    @TestMetadata("compilerArguments")
    public void testCompilerArguments() throws Exception {
        runTest("compiler/testData/multiplatform/compilerArguments/");
    }

    @TestMetadata("contracts")
    public void testContracts() throws Exception {
        runTest("compiler/testData/multiplatform/contracts/");
    }

    @TestMetadata("createImplClassInPlatformModule")
    public void testCreateImplClassInPlatformModule() throws Exception {
        runTest("compiler/testData/multiplatform/createImplClassInPlatformModule/");
    }

    @TestMetadata("explicitActualOnOverrideOfAbstractMethod")
    public void testExplicitActualOnOverrideOfAbstractMethod() throws Exception {
        runTest("compiler/testData/multiplatform/explicitActualOnOverrideOfAbstractMethod/");
    }

    @TestMetadata("genericDeclarations")
    public void testGenericDeclarations() throws Exception {
        runTest("compiler/testData/multiplatform/genericDeclarations/");
    }

    @TestMetadata("incompatibleCallables")
    public void testIncompatibleCallables() throws Exception {
        runTest("compiler/testData/multiplatform/incompatibleCallables/");
    }

    @TestMetadata("incompatibleClasses")
    public void testIncompatibleClasses() throws Exception {
        runTest("compiler/testData/multiplatform/incompatibleClasses/");
    }

    @TestMetadata("incompatibleFunctions")
    public void testIncompatibleFunctions() throws Exception {
        runTest("compiler/testData/multiplatform/incompatibleFunctions/");
    }

    @TestMetadata("incompatibleNestedClasses")
    public void testIncompatibleNestedClasses() throws Exception {
        runTest("compiler/testData/multiplatform/incompatibleNestedClasses/");
    }

    @TestMetadata("incompatibleProperties")
    public void testIncompatibleProperties() throws Exception {
        runTest("compiler/testData/multiplatform/incompatibleProperties/");
    }

    @TestMetadata("incorrectImplInClass")
    public void testIncorrectImplInClass() throws Exception {
        runTest("compiler/testData/multiplatform/incorrectImplInClass/");
    }

    @TestMetadata("inlineClasses")
    public void testInlineClasses() throws Exception {
        runTest("compiler/testData/multiplatform/inlineClasses/");
    }

    @TestMetadata("innerGenericClass")
    public void testInnerGenericClass() throws Exception {
        runTest("compiler/testData/multiplatform/innerGenericClass/");
    }

    @TestMetadata("jsNameClash")
    public void testJsNameClash() throws Exception {
        runTest("compiler/testData/multiplatform/jsNameClash/");
    }

    @TestMetadata("jvmMultifileClass")
    public void testJvmMultifileClass() throws Exception {
        runTest("compiler/testData/multiplatform/jvmMultifileClass/");
    }

    @TestMetadata("missingOverload")
    public void testMissingOverload() throws Exception {
        runTest("compiler/testData/multiplatform/missingOverload/");
    }

    @TestMetadata("optionalExpectation")
    public void testOptionalExpectation() throws Exception {
        runTest("compiler/testData/multiplatform/optionalExpectation/");
    }

    @TestMetadata("optionalExpectationIncorrectUse")
    public void testOptionalExpectationIncorrectUse() throws Exception {
        runTest("compiler/testData/multiplatform/optionalExpectationIncorrectUse/");
    }

    @TestMetadata("simple")
    public void testSimple() throws Exception {
        runTest("compiler/testData/multiplatform/simple/");
    }

    @TestMetadata("simpleNoImplKeywordOnTopLevelFunction")
    public void testSimpleNoImplKeywordOnTopLevelFunction() throws Exception {
        runTest("compiler/testData/multiplatform/simpleNoImplKeywordOnTopLevelFunction/");
    }

    @TestMetadata("weakIncompatibilityWithoutActualModifier")
    public void testWeakIncompatibilityWithoutActualModifier() throws Exception {
        runTest("compiler/testData/multiplatform/weakIncompatibilityWithoutActualModifier/");
    }

    @TestMetadata("compiler/testData/multiplatform/classScopes")
    @TestDataPath("$PROJECT_ROOT")
    @RunWith(JUnit3RunnerWithInners.class)
    public static class ClassScopes extends AbstractMultiPlatformIntegrationTest {
        private void runTest(String testDataFilePath) throws Exception {
            KotlinTestUtils.runTest(this::doTest, this, testDataFilePath);
        }

        public void testAllFilesPresentInClassScopes() throws Exception {
            KotlinTestUtils.assertAllTestsPresentByMetadata(this.getClass(), new File("compiler/testData/multiplatform/classScopes"), Pattern.compile("^([^\\.]+)$"), true);
        }

        @TestMetadata("constructorIncorrectSignature")
        public void testConstructorIncorrectSignature() throws Exception {
            runTest("compiler/testData/multiplatform/classScopes/constructorIncorrectSignature/");
        }

        @TestMetadata("enumsWithDifferentEntries")
        public void testEnumsWithDifferentEntries() throws Exception {
            runTest("compiler/testData/multiplatform/classScopes/enumsWithDifferentEntries/");
        }

        @TestMetadata("fakeOverrides")
        public void testFakeOverrides() throws Exception {
            runTest("compiler/testData/multiplatform/classScopes/fakeOverrides/");
        }

        @TestMetadata("functionAndPropertyWithSameName")
        public void testFunctionAndPropertyWithSameName() throws Exception {
            runTest("compiler/testData/multiplatform/classScopes/functionAndPropertyWithSameName/");
        }

        @TestMetadata("functionIncorrectSignature")
        public void testFunctionIncorrectSignature() throws Exception {
            runTest("compiler/testData/multiplatform/classScopes/functionIncorrectSignature/");
        }

        @TestMetadata("functionIncorrectSignatureFromSuperclass")
        public void testFunctionIncorrectSignatureFromSuperclass() throws Exception {
            runTest("compiler/testData/multiplatform/classScopes/functionIncorrectSignatureFromSuperclass/");
        }

        @TestMetadata("missingConstructor")
        public void testMissingConstructor() throws Exception {
            runTest("compiler/testData/multiplatform/classScopes/missingConstructor/");
        }

        @TestMetadata("missingFunction")
        public void testMissingFunction() throws Exception {
            runTest("compiler/testData/multiplatform/classScopes/missingFunction/");
        }

        @TestMetadata("simple")
        public void testSimple() throws Exception {
            runTest("compiler/testData/multiplatform/classScopes/simple/");
        }

        @TestMetadata("compiler/testData/multiplatform/classScopes/constructorIncorrectSignature")
        @TestDataPath("$PROJECT_ROOT")
        @RunWith(JUnit3RunnerWithInners.class)
        public static class ConstructorIncorrectSignature extends AbstractMultiPlatformIntegrationTest {
            private void runTest(String testDataFilePath) throws Exception {
                KotlinTestUtils.runTest(this::doTest, this, testDataFilePath);
            }

            public void testAllFilesPresentInConstructorIncorrectSignature() throws Exception {
                KotlinTestUtils.assertAllTestsPresentByMetadata(this.getClass(), new File("compiler/testData/multiplatform/classScopes/constructorIncorrectSignature"), Pattern.compile("^([^\\.]+)$"), true);
            }
        }

        @TestMetadata("compiler/testData/multiplatform/classScopes/enumsWithDifferentEntries")
        @TestDataPath("$PROJECT_ROOT")
        @RunWith(JUnit3RunnerWithInners.class)
        public static class EnumsWithDifferentEntries extends AbstractMultiPlatformIntegrationTest {
            private void runTest(String testDataFilePath) throws Exception {
                KotlinTestUtils.runTest(this::doTest, this, testDataFilePath);
            }

            public void testAllFilesPresentInEnumsWithDifferentEntries() throws Exception {
                KotlinTestUtils.assertAllTestsPresentByMetadata(this.getClass(), new File("compiler/testData/multiplatform/classScopes/enumsWithDifferentEntries"), Pattern.compile("^([^\\.]+)$"), true);
            }
        }

        @TestMetadata("compiler/testData/multiplatform/classScopes/fakeOverrides")
        @TestDataPath("$PROJECT_ROOT")
        @RunWith(JUnit3RunnerWithInners.class)
        public static class FakeOverrides extends AbstractMultiPlatformIntegrationTest {
            private void runTest(String testDataFilePath) throws Exception {
                KotlinTestUtils.runTest(this::doTest, this, testDataFilePath);
            }

            public void testAllFilesPresentInFakeOverrides() throws Exception {
                KotlinTestUtils.assertAllTestsPresentByMetadata(this.getClass(), new File("compiler/testData/multiplatform/classScopes/fakeOverrides"), Pattern.compile("^([^\\.]+)$"), true);
            }
        }

        @TestMetadata("compiler/testData/multiplatform/classScopes/functionAndPropertyWithSameName")
        @TestDataPath("$PROJECT_ROOT")
        @RunWith(JUnit3RunnerWithInners.class)
        public static class FunctionAndPropertyWithSameName extends AbstractMultiPlatformIntegrationTest {
            private void runTest(String testDataFilePath) throws Exception {
                KotlinTestUtils.runTest(this::doTest, this, testDataFilePath);
            }

            public void testAllFilesPresentInFunctionAndPropertyWithSameName() throws Exception {
                KotlinTestUtils.assertAllTestsPresentByMetadata(this.getClass(), new File("compiler/testData/multiplatform/classScopes/functionAndPropertyWithSameName"), Pattern.compile("^([^\\.]+)$"), true);
            }
        }

        @TestMetadata("compiler/testData/multiplatform/classScopes/functionIncorrectSignature")
        @TestDataPath("$PROJECT_ROOT")
        @RunWith(JUnit3RunnerWithInners.class)
        public static class FunctionIncorrectSignature extends AbstractMultiPlatformIntegrationTest {
            private void runTest(String testDataFilePath) throws Exception {
                KotlinTestUtils.runTest(this::doTest, this, testDataFilePath);
            }

            public void testAllFilesPresentInFunctionIncorrectSignature() throws Exception {
                KotlinTestUtils.assertAllTestsPresentByMetadata(this.getClass(), new File("compiler/testData/multiplatform/classScopes/functionIncorrectSignature"), Pattern.compile("^([^\\.]+)$"), true);
            }
        }

        @TestMetadata("compiler/testData/multiplatform/classScopes/functionIncorrectSignatureFromSuperclass")
        @TestDataPath("$PROJECT_ROOT")
        @RunWith(JUnit3RunnerWithInners.class)
        public static class FunctionIncorrectSignatureFromSuperclass extends AbstractMultiPlatformIntegrationTest {
            private void runTest(String testDataFilePath) throws Exception {
                KotlinTestUtils.runTest(this::doTest, this, testDataFilePath);
            }

            public void testAllFilesPresentInFunctionIncorrectSignatureFromSuperclass() throws Exception {
                KotlinTestUtils.assertAllTestsPresentByMetadata(this.getClass(), new File("compiler/testData/multiplatform/classScopes/functionIncorrectSignatureFromSuperclass"), Pattern.compile("^([^\\.]+)$"), true);
            }
        }

        @TestMetadata("compiler/testData/multiplatform/classScopes/missingConstructor")
        @TestDataPath("$PROJECT_ROOT")
        @RunWith(JUnit3RunnerWithInners.class)
        public static class MissingConstructor extends AbstractMultiPlatformIntegrationTest {
            private void runTest(String testDataFilePath) throws Exception {
                KotlinTestUtils.runTest(this::doTest, this, testDataFilePath);
            }

            public void testAllFilesPresentInMissingConstructor() throws Exception {
                KotlinTestUtils.assertAllTestsPresentByMetadata(this.getClass(), new File("compiler/testData/multiplatform/classScopes/missingConstructor"), Pattern.compile("^([^\\.]+)$"), true);
            }
        }

        @TestMetadata("compiler/testData/multiplatform/classScopes/missingFunction")
        @TestDataPath("$PROJECT_ROOT")
        @RunWith(JUnit3RunnerWithInners.class)
        public static class MissingFunction extends AbstractMultiPlatformIntegrationTest {
            private void runTest(String testDataFilePath) throws Exception {
                KotlinTestUtils.runTest(this::doTest, this, testDataFilePath);
            }

            public void testAllFilesPresentInMissingFunction() throws Exception {
                KotlinTestUtils.assertAllTestsPresentByMetadata(this.getClass(), new File("compiler/testData/multiplatform/classScopes/missingFunction"), Pattern.compile("^([^\\.]+)$"), true);
            }
        }

        @TestMetadata("compiler/testData/multiplatform/classScopes/simple")
        @TestDataPath("$PROJECT_ROOT")
        @RunWith(JUnit3RunnerWithInners.class)
        public static class Simple extends AbstractMultiPlatformIntegrationTest {
            private void runTest(String testDataFilePath) throws Exception {
                KotlinTestUtils.runTest(this::doTest, this, testDataFilePath);
            }

            public void testAllFilesPresentInSimple() throws Exception {
                KotlinTestUtils.assertAllTestsPresentByMetadata(this.getClass(), new File("compiler/testData/multiplatform/classScopes/simple"), Pattern.compile("^([^\\.]+)$"), true);
            }
        }
    }

    @TestMetadata("compiler/testData/multiplatform/compatibleProperties")
    @TestDataPath("$PROJECT_ROOT")
    @RunWith(JUnit3RunnerWithInners.class)
    public static class CompatibleProperties extends AbstractMultiPlatformIntegrationTest {
        private void runTest(String testDataFilePath) throws Exception {
            KotlinTestUtils.runTest(this::doTest, this, testDataFilePath);
        }

        public void testAllFilesPresentInCompatibleProperties() throws Exception {
            KotlinTestUtils.assertAllTestsPresentByMetadata(this.getClass(), new File("compiler/testData/multiplatform/compatibleProperties"), Pattern.compile("^([^\\.]+)$"), true);
        }
    }

    @TestMetadata("compiler/testData/multiplatform/compilerArguments")
    @TestDataPath("$PROJECT_ROOT")
    @RunWith(JUnit3RunnerWithInners.class)
    public static class CompilerArguments extends AbstractMultiPlatformIntegrationTest {
        private void runTest(String testDataFilePath) throws Exception {
            KotlinTestUtils.runTest(this::doTest, this, testDataFilePath);
        }

        public void testAllFilesPresentInCompilerArguments() throws Exception {
            KotlinTestUtils.assertAllTestsPresentByMetadata(this.getClass(), new File("compiler/testData/multiplatform/compilerArguments"), Pattern.compile("^([^\\.]+)$"), true);
        }
    }

    @TestMetadata("compiler/testData/multiplatform/contracts")
    @TestDataPath("$PROJECT_ROOT")
    @RunWith(JUnit3RunnerWithInners.class)
    public static class Contracts extends AbstractMultiPlatformIntegrationTest {
        private void runTest(String testDataFilePath) throws Exception {
            KotlinTestUtils.runTest(this::doTest, this, testDataFilePath);
        }

        public void testAllFilesPresentInContracts() throws Exception {
            KotlinTestUtils.assertAllTestsPresentByMetadata(this.getClass(), new File("compiler/testData/multiplatform/contracts"), Pattern.compile("^([^\\.]+)$"), true);
        }
    }

    @TestMetadata("compiler/testData/multiplatform/createImplClassInPlatformModule")
    @TestDataPath("$PROJECT_ROOT")
    @RunWith(JUnit3RunnerWithInners.class)
    public static class CreateImplClassInPlatformModule extends AbstractMultiPlatformIntegrationTest {
        private void runTest(String testDataFilePath) throws Exception {
            KotlinTestUtils.runTest(this::doTest, this, testDataFilePath);
        }

        public void testAllFilesPresentInCreateImplClassInPlatformModule() throws Exception {
            KotlinTestUtils.assertAllTestsPresentByMetadata(this.getClass(), new File("compiler/testData/multiplatform/createImplClassInPlatformModule"), Pattern.compile("^([^\\.]+)$"), true);
        }
    }

    @TestMetadata("compiler/testData/multiplatform/defaultArguments")
    @TestDataPath("$PROJECT_ROOT")
    @RunWith(JUnit3RunnerWithInners.class)
    public static class DefaultArguments extends AbstractMultiPlatformIntegrationTest {
        private void runTest(String testDataFilePath) throws Exception {
            KotlinTestUtils.runTest(this::doTest, this, testDataFilePath);
        }

        public void testAllFilesPresentInDefaultArguments() throws Exception {
            KotlinTestUtils.assertAllTestsPresentByMetadata(this.getClass(), new File("compiler/testData/multiplatform/defaultArguments"), Pattern.compile("^([^\\.]+)$"), true);
        }

        @TestMetadata("useDefaultArgumentsInDependency")
        public void testUseDefaultArgumentsInDependency() throws Exception {
            runTest("compiler/testData/multiplatform/defaultArguments/useDefaultArgumentsInDependency/");
        }

        @TestMetadata("compiler/testData/multiplatform/defaultArguments/useDefaultArgumentsInDependency")
        @TestDataPath("$PROJECT_ROOT")
        @RunWith(JUnit3RunnerWithInners.class)
        public static class UseDefaultArgumentsInDependency extends AbstractMultiPlatformIntegrationTest {
            private void runTest(String testDataFilePath) throws Exception {
                KotlinTestUtils.runTest(this::doTest, this, testDataFilePath);
            }

            public void testAllFilesPresentInUseDefaultArgumentsInDependency() throws Exception {
                KotlinTestUtils.assertAllTestsPresentByMetadata(this.getClass(), new File("compiler/testData/multiplatform/defaultArguments/useDefaultArgumentsInDependency"), Pattern.compile("^([^\\.]+)$"), true);
            }
        }
    }

    @TestMetadata("compiler/testData/multiplatform/explicitActualOnOverrideOfAbstractMethod")
    @TestDataPath("$PROJECT_ROOT")
    @RunWith(JUnit3RunnerWithInners.class)
    public static class ExplicitActualOnOverrideOfAbstractMethod extends AbstractMultiPlatformIntegrationTest {
        private void runTest(String testDataFilePath) throws Exception {
            KotlinTestUtils.runTest(this::doTest, this, testDataFilePath);
        }

        public void testAllFilesPresentInExplicitActualOnOverrideOfAbstractMethod() throws Exception {
            KotlinTestUtils.assertAllTestsPresentByMetadata(this.getClass(), new File("compiler/testData/multiplatform/explicitActualOnOverrideOfAbstractMethod"), Pattern.compile("^([^\\.]+)$"), true);
        }
    }

    @TestMetadata("compiler/testData/multiplatform/genericDeclarations")
    @TestDataPath("$PROJECT_ROOT")
    @RunWith(JUnit3RunnerWithInners.class)
    public static class GenericDeclarations extends AbstractMultiPlatformIntegrationTest {
        private void runTest(String testDataFilePath) throws Exception {
            KotlinTestUtils.runTest(this::doTest, this, testDataFilePath);
        }

        public void testAllFilesPresentInGenericDeclarations() throws Exception {
            KotlinTestUtils.assertAllTestsPresentByMetadata(this.getClass(), new File("compiler/testData/multiplatform/genericDeclarations"), Pattern.compile("^([^\\.]+)$"), true);
        }
    }

    @TestMetadata("compiler/testData/multiplatform/implTypeAlias")
    @TestDataPath("$PROJECT_ROOT")
    @RunWith(JUnit3RunnerWithInners.class)
    public static class ImplTypeAlias extends AbstractMultiPlatformIntegrationTest {
        private void runTest(String testDataFilePath) throws Exception {
            KotlinTestUtils.runTest(this::doTest, this, testDataFilePath);
        }

        public void testAllFilesPresentInImplTypeAlias() throws Exception {
            KotlinTestUtils.assertAllTestsPresentByMetadata(this.getClass(), new File("compiler/testData/multiplatform/implTypeAlias"), Pattern.compile("^([^\\.]+)$"), true);
        }

        @TestMetadata("discriminateHeaderClassInFavorOfTypeAlias")
        public void testDiscriminateHeaderClassInFavorOfTypeAlias() throws Exception {
            runTest("compiler/testData/multiplatform/implTypeAlias/discriminateHeaderClassInFavorOfTypeAlias/");
        }

        @TestMetadata("generic")
        public void testGeneric() throws Exception {
            runTest("compiler/testData/multiplatform/implTypeAlias/generic/");
        }

        @TestMetadata("nestedClassesViaTypeAlias")
        public void testNestedClassesViaTypeAlias() throws Exception {
            runTest("compiler/testData/multiplatform/implTypeAlias/nestedClassesViaTypeAlias/");
        }

        @TestMetadata("compiler/testData/multiplatform/implTypeAlias/discriminateHeaderClassInFavorOfTypeAlias")
        @TestDataPath("$PROJECT_ROOT")
        @RunWith(JUnit3RunnerWithInners.class)
        public static class DiscriminateHeaderClassInFavorOfTypeAlias extends AbstractMultiPlatformIntegrationTest {
            private void runTest(String testDataFilePath) throws Exception {
                KotlinTestUtils.runTest(this::doTest, this, testDataFilePath);
            }

            public void testAllFilesPresentInDiscriminateHeaderClassInFavorOfTypeAlias() throws Exception {
                KotlinTestUtils.assertAllTestsPresentByMetadata(this.getClass(), new File("compiler/testData/multiplatform/implTypeAlias/discriminateHeaderClassInFavorOfTypeAlias"), Pattern.compile("^([^\\.]+)$"), true);
            }
        }

        @TestMetadata("compiler/testData/multiplatform/implTypeAlias/generic")
        @TestDataPath("$PROJECT_ROOT")
        @RunWith(JUnit3RunnerWithInners.class)
        public static class Generic extends AbstractMultiPlatformIntegrationTest {
            private void runTest(String testDataFilePath) throws Exception {
                KotlinTestUtils.runTest(this::doTest, this, testDataFilePath);
            }

            public void testAllFilesPresentInGeneric() throws Exception {
                KotlinTestUtils.assertAllTestsPresentByMetadata(this.getClass(), new File("compiler/testData/multiplatform/implTypeAlias/generic"), Pattern.compile("^([^\\.]+)$"), true);
            }
        }

        @TestMetadata("compiler/testData/multiplatform/implTypeAlias/nestedClassesViaTypeAlias")
        @TestDataPath("$PROJECT_ROOT")
        @RunWith(JUnit3RunnerWithInners.class)
        public static class NestedClassesViaTypeAlias extends AbstractMultiPlatformIntegrationTest {
            private void runTest(String testDataFilePath) throws Exception {
                KotlinTestUtils.runTest(this::doTest, this, testDataFilePath);
            }

            public void testAllFilesPresentInNestedClassesViaTypeAlias() throws Exception {
                KotlinTestUtils.assertAllTestsPresentByMetadata(this.getClass(), new File("compiler/testData/multiplatform/implTypeAlias/nestedClassesViaTypeAlias"), Pattern.compile("^([^\\.]+)$"), true);
            }
        }
    }

    @TestMetadata("compiler/testData/multiplatform/incompatibleCallables")
    @TestDataPath("$PROJECT_ROOT")
    @RunWith(JUnit3RunnerWithInners.class)
    public static class IncompatibleCallables extends AbstractMultiPlatformIntegrationTest {
        private void runTest(String testDataFilePath) throws Exception {
            KotlinTestUtils.runTest(this::doTest, this, testDataFilePath);
        }

        public void testAllFilesPresentInIncompatibleCallables() throws Exception {
            KotlinTestUtils.assertAllTestsPresentByMetadata(this.getClass(), new File("compiler/testData/multiplatform/incompatibleCallables"), Pattern.compile("^([^\\.]+)$"), true);
        }
    }

    @TestMetadata("compiler/testData/multiplatform/incompatibleClasses")
    @TestDataPath("$PROJECT_ROOT")
    @RunWith(JUnit3RunnerWithInners.class)
    public static class IncompatibleClasses extends AbstractMultiPlatformIntegrationTest {
        private void runTest(String testDataFilePath) throws Exception {
            KotlinTestUtils.runTest(this::doTest, this, testDataFilePath);
        }

        public void testAllFilesPresentInIncompatibleClasses() throws Exception {
            KotlinTestUtils.assertAllTestsPresentByMetadata(this.getClass(), new File("compiler/testData/multiplatform/incompatibleClasses"), Pattern.compile("^([^\\.]+)$"), true);
        }
    }

    @TestMetadata("compiler/testData/multiplatform/incompatibleFunctions")
    @TestDataPath("$PROJECT_ROOT")
    @RunWith(JUnit3RunnerWithInners.class)
    public static class IncompatibleFunctions extends AbstractMultiPlatformIntegrationTest {
        private void runTest(String testDataFilePath) throws Exception {
            KotlinTestUtils.runTest(this::doTest, this, testDataFilePath);
        }

        public void testAllFilesPresentInIncompatibleFunctions() throws Exception {
            KotlinTestUtils.assertAllTestsPresentByMetadata(this.getClass(), new File("compiler/testData/multiplatform/incompatibleFunctions"), Pattern.compile("^([^\\.]+)$"), true);
        }
    }

    @TestMetadata("compiler/testData/multiplatform/incompatibleNestedClasses")
    @TestDataPath("$PROJECT_ROOT")
    @RunWith(JUnit3RunnerWithInners.class)
    public static class IncompatibleNestedClasses extends AbstractMultiPlatformIntegrationTest {
        private void runTest(String testDataFilePath) throws Exception {
            KotlinTestUtils.runTest(this::doTest, this, testDataFilePath);
        }

        public void testAllFilesPresentInIncompatibleNestedClasses() throws Exception {
            KotlinTestUtils.assertAllTestsPresentByMetadata(this.getClass(), new File("compiler/testData/multiplatform/incompatibleNestedClasses"), Pattern.compile("^([^\\.]+)$"), true);
        }
    }

    @TestMetadata("compiler/testData/multiplatform/incompatibleProperties")
    @TestDataPath("$PROJECT_ROOT")
    @RunWith(JUnit3RunnerWithInners.class)
    public static class IncompatibleProperties extends AbstractMultiPlatformIntegrationTest {
        private void runTest(String testDataFilePath) throws Exception {
            KotlinTestUtils.runTest(this::doTest, this, testDataFilePath);
        }

        public void testAllFilesPresentInIncompatibleProperties() throws Exception {
            KotlinTestUtils.assertAllTestsPresentByMetadata(this.getClass(), new File("compiler/testData/multiplatform/incompatibleProperties"), Pattern.compile("^([^\\.]+)$"), true);
        }
    }

    @TestMetadata("compiler/testData/multiplatform/incorrectImplInClass")
    @TestDataPath("$PROJECT_ROOT")
    @RunWith(JUnit3RunnerWithInners.class)
    public static class IncorrectImplInClass extends AbstractMultiPlatformIntegrationTest {
        private void runTest(String testDataFilePath) throws Exception {
            KotlinTestUtils.runTest(this::doTest, this, testDataFilePath);
        }

        public void testAllFilesPresentInIncorrectImplInClass() throws Exception {
            KotlinTestUtils.assertAllTestsPresentByMetadata(this.getClass(), new File("compiler/testData/multiplatform/incorrectImplInClass"), Pattern.compile("^([^\\.]+)$"), true);
        }
    }

    @TestMetadata("compiler/testData/multiplatform/inlineClasses")
    @TestDataPath("$PROJECT_ROOT")
    @RunWith(JUnit3RunnerWithInners.class)
    public static class InlineClasses extends AbstractMultiPlatformIntegrationTest {
        private void runTest(String testDataFilePath) throws Exception {
            KotlinTestUtils.runTest(this::doTest, this, testDataFilePath);
        }

        public void testAllFilesPresentInInlineClasses() throws Exception {
            KotlinTestUtils.assertAllTestsPresentByMetadata(this.getClass(), new File("compiler/testData/multiplatform/inlineClasses"), Pattern.compile("^([^\\.]+)$"), true);
        }
    }

    @TestMetadata("compiler/testData/multiplatform/innerGenericClass")
    @TestDataPath("$PROJECT_ROOT")
    @RunWith(JUnit3RunnerWithInners.class)
    public static class InnerGenericClass extends AbstractMultiPlatformIntegrationTest {
        private void runTest(String testDataFilePath) throws Exception {
            KotlinTestUtils.runTest(this::doTest, this, testDataFilePath);
        }

        public void testAllFilesPresentInInnerGenericClass() throws Exception {
            KotlinTestUtils.assertAllTestsPresentByMetadata(this.getClass(), new File("compiler/testData/multiplatform/innerGenericClass"), Pattern.compile("^([^\\.]+)$"), true);
        }
    }

    @TestMetadata("compiler/testData/multiplatform/jsNameClash")
    @TestDataPath("$PROJECT_ROOT")
    @RunWith(JUnit3RunnerWithInners.class)
    public static class JsNameClash extends AbstractMultiPlatformIntegrationTest {
        private void runTest(String testDataFilePath) throws Exception {
            KotlinTestUtils.runTest(this::doTest, this, testDataFilePath);
        }

        public void testAllFilesPresentInJsNameClash() throws Exception {
            KotlinTestUtils.assertAllTestsPresentByMetadata(this.getClass(), new File("compiler/testData/multiplatform/jsNameClash"), Pattern.compile("^([^\\.]+)$"), true);
        }
    }

    @TestMetadata("compiler/testData/multiplatform/jvmMultifileClass")
    @TestDataPath("$PROJECT_ROOT")
    @RunWith(JUnit3RunnerWithInners.class)
    public static class JvmMultifileClass extends AbstractMultiPlatformIntegrationTest {
        private void runTest(String testDataFilePath) throws Exception {
            KotlinTestUtils.runTest(this::doTest, this, testDataFilePath);
        }

        public void testAllFilesPresentInJvmMultifileClass() throws Exception {
            KotlinTestUtils.assertAllTestsPresentByMetadata(this.getClass(), new File("compiler/testData/multiplatform/jvmMultifileClass"), Pattern.compile("^([^\\.]+)$"), true);
        }
    }

    @TestMetadata("compiler/testData/multiplatform/missingOverload")
    @TestDataPath("$PROJECT_ROOT")
    @RunWith(JUnit3RunnerWithInners.class)
    public static class MissingOverload extends AbstractMultiPlatformIntegrationTest {
        private void runTest(String testDataFilePath) throws Exception {
            KotlinTestUtils.runTest(this::doTest, this, testDataFilePath);
        }

        public void testAllFilesPresentInMissingOverload() throws Exception {
            KotlinTestUtils.assertAllTestsPresentByMetadata(this.getClass(), new File("compiler/testData/multiplatform/missingOverload"), Pattern.compile("^([^\\.]+)$"), true);
        }
    }

    @TestMetadata("compiler/testData/multiplatform/optionalExpectation")
    @TestDataPath("$PROJECT_ROOT")
    @RunWith(JUnit3RunnerWithInners.class)
    public static class OptionalExpectation extends AbstractMultiPlatformIntegrationTest {
        private void runTest(String testDataFilePath) throws Exception {
            KotlinTestUtils.runTest(this::doTest, this, testDataFilePath);
        }

        public void testAllFilesPresentInOptionalExpectation() throws Exception {
            KotlinTestUtils.assertAllTestsPresentByMetadata(this.getClass(), new File("compiler/testData/multiplatform/optionalExpectation"), Pattern.compile("^([^\\.]+)$"), true);
        }
    }

    @TestMetadata("compiler/testData/multiplatform/optionalExpectationIncorrectUse")
    @TestDataPath("$PROJECT_ROOT")
    @RunWith(JUnit3RunnerWithInners.class)
    public static class OptionalExpectationIncorrectUse extends AbstractMultiPlatformIntegrationTest {
        private void runTest(String testDataFilePath) throws Exception {
            KotlinTestUtils.runTest(this::doTest, this, testDataFilePath);
        }

        public void testAllFilesPresentInOptionalExpectationIncorrectUse() throws Exception {
            KotlinTestUtils.assertAllTestsPresentByMetadata(this.getClass(), new File("compiler/testData/multiplatform/optionalExpectationIncorrectUse"), Pattern.compile("^([^\\.]+)$"), true);
        }
    }

    @TestMetadata("compiler/testData/multiplatform/regressions")
    @TestDataPath("$PROJECT_ROOT")
    @RunWith(JUnit3RunnerWithInners.class)
    public static class Regressions extends AbstractMultiPlatformIntegrationTest {
        private void runTest(String testDataFilePath) throws Exception {
            KotlinTestUtils.runTest(this::doTest, this, testDataFilePath);
        }

        public void testAllFilesPresentInRegressions() throws Exception {
            KotlinTestUtils.assertAllTestsPresentByMetadata(this.getClass(), new File("compiler/testData/multiplatform/regressions"), Pattern.compile("^([^\\.]+)$"), true);
        }

        @TestMetadata("incompatibleClassScopesWithImplTypeAlias")
        public void testIncompatibleClassScopesWithImplTypeAlias() throws Exception {
            runTest("compiler/testData/multiplatform/regressions/incompatibleClassScopesWithImplTypeAlias/");
        }

        @TestMetadata("kt17001")
        public void testKt17001() throws Exception {
            runTest("compiler/testData/multiplatform/regressions/kt17001/");
        }

        @TestMetadata("kt28385")
        public void testKt28385() throws Exception {
            runTest("compiler/testData/multiplatform/regressions/kt28385/");
        }

        @TestMetadata("compiler/testData/multiplatform/regressions/incompatibleClassScopesWithImplTypeAlias")
        @TestDataPath("$PROJECT_ROOT")
        @RunWith(JUnit3RunnerWithInners.class)
        public static class IncompatibleClassScopesWithImplTypeAlias extends AbstractMultiPlatformIntegrationTest {
            private void runTest(String testDataFilePath) throws Exception {
                KotlinTestUtils.runTest(this::doTest, this, testDataFilePath);
            }

            public void testAllFilesPresentInIncompatibleClassScopesWithImplTypeAlias() throws Exception {
                KotlinTestUtils.assertAllTestsPresentByMetadata(this.getClass(), new File("compiler/testData/multiplatform/regressions/incompatibleClassScopesWithImplTypeAlias"), Pattern.compile("^([^\\.]+)$"), true);
            }
        }

        @TestMetadata("compiler/testData/multiplatform/regressions/kt17001")
        @TestDataPath("$PROJECT_ROOT")
        @RunWith(JUnit3RunnerWithInners.class)
        public static class Kt17001 extends AbstractMultiPlatformIntegrationTest {
            private void runTest(String testDataFilePath) throws Exception {
                KotlinTestUtils.runTest(this::doTest, this, testDataFilePath);
            }

            public void testAllFilesPresentInKt17001() throws Exception {
                KotlinTestUtils.assertAllTestsPresentByMetadata(this.getClass(), new File("compiler/testData/multiplatform/regressions/kt17001"), Pattern.compile("^([^\\.]+)$"), true);
            }
        }

        @TestMetadata("compiler/testData/multiplatform/regressions/kt28385")
        @TestDataPath("$PROJECT_ROOT")
        @RunWith(JUnit3RunnerWithInners.class)
        public static class Kt28385 extends AbstractMultiPlatformIntegrationTest {
            private void runTest(String testDataFilePath) throws Exception {
                KotlinTestUtils.runTest(this::doTest, this, testDataFilePath);
            }

            public void testAllFilesPresentInKt28385() throws Exception {
                KotlinTestUtils.assertAllTestsPresentByMetadata(this.getClass(), new File("compiler/testData/multiplatform/regressions/kt28385"), Pattern.compile("^([^\\.]+)$"), true);
            }
        }
    }

    @TestMetadata("compiler/testData/multiplatform/simple")
    @TestDataPath("$PROJECT_ROOT")
    @RunWith(JUnit3RunnerWithInners.class)
    public static class Simple extends AbstractMultiPlatformIntegrationTest {
        private void runTest(String testDataFilePath) throws Exception {
            KotlinTestUtils.runTest(this::doTest, this, testDataFilePath);
        }

        public void testAllFilesPresentInSimple() throws Exception {
            KotlinTestUtils.assertAllTestsPresentByMetadata(this.getClass(), new File("compiler/testData/multiplatform/simple"), Pattern.compile("^([^\\.]+)$"), true);
        }
    }

    @TestMetadata("compiler/testData/multiplatform/simpleNoImplKeywordOnTopLevelFunction")
    @TestDataPath("$PROJECT_ROOT")
    @RunWith(JUnit3RunnerWithInners.class)
    public static class SimpleNoImplKeywordOnTopLevelFunction extends AbstractMultiPlatformIntegrationTest {
        private void runTest(String testDataFilePath) throws Exception {
            KotlinTestUtils.runTest(this::doTest, this, testDataFilePath);
        }

        public void testAllFilesPresentInSimpleNoImplKeywordOnTopLevelFunction() throws Exception {
            KotlinTestUtils.assertAllTestsPresentByMetadata(this.getClass(), new File("compiler/testData/multiplatform/simpleNoImplKeywordOnTopLevelFunction"), Pattern.compile("^([^\\.]+)$"), true);
        }
    }

    @TestMetadata("compiler/testData/multiplatform/weakIncompatibilityWithoutActualModifier")
    @TestDataPath("$PROJECT_ROOT")
    @RunWith(JUnit3RunnerWithInners.class)
    public static class WeakIncompatibilityWithoutActualModifier extends AbstractMultiPlatformIntegrationTest {
        private void runTest(String testDataFilePath) throws Exception {
            KotlinTestUtils.runTest(this::doTest, this, testDataFilePath);
        }

        public void testAllFilesPresentInWeakIncompatibilityWithoutActualModifier() throws Exception {
            KotlinTestUtils.assertAllTestsPresentByMetadata(this.getClass(), new File("compiler/testData/multiplatform/weakIncompatibilityWithoutActualModifier"), Pattern.compile("^([^\\.]+)$"), true);
        }
    }
}
