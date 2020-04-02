import TestData.ShellTestData
import Utils.Helper
import org.junit.Test

class CommitId_CommonTests extends GroovyTestCase {

    protected commitId_ = new commitId()

    @Test
    void test_CommitIdWithoutGIT_COMMIT_CommitIdIsReturnedFromShell(){
        Helper.setEnvVariables([:], commitId_)
        InjectVars.injectClosureTo(commitId_, 'sh', ShellTestData.shellClosureSSH)

        assertEquals('1111111222222222222222222222222222222222', commitId_())

    }

    @Test
    void test_CommitIdWithGIT_COMMITOK_CommitIdIsReturnedFromENV(){
        Helper.setEnvVariables([GIT_COMMIT:"2222"], commitId_)
        InjectVars.injectClosureTo(commitId_, 'sh', ShellTestData.shellClosureSSH)
        assertEquals('2222', commitId_())

    }

    @Test
    void test_CommitIdWithGIT_COMMITNull_CommitIdIsReturnedFromShell(){
        Helper.setEnvVariables([GIT_COMMIT:null], commitId_)
        InjectVars.injectClosureTo(commitId_, 'sh', ShellTestData.shellClosureSSH)

        assertEquals('1111111222222222222222222222222222222222', commitId_())

    }

    @Test
    void test_CommitIdWithGIT_COMMITEmpty_CommitIdIsReturnedFromShell(){
        Helper.setEnvVariables([GIT_COMMIT:''], commitId_)
        InjectVars.injectClosureTo(commitId_, 'sh', ShellTestData.shellClosureSSH)

        assertEquals('1111111222222222222222222222222222222222', commitId_())

    }

    @Test
    void test_CommitIdWithGIT_COMMITWhitespace_CommitIdIsReturnedFromShell(){
        Helper.setEnvVariables([GIT_COMMIT:' '], commitId_)
        InjectVars.injectClosureTo(commitId_, 'sh', ShellTestData.shellClosureSSH)

        assertEquals('1111111222222222222222222222222222222222', commitId_())

    }

    @Test
    void test_CommitIdWithGIT_COMMITSeveralWhitespaces_CommitIdIsReturnedFromShell(){
        Helper.setEnvVariables([GIT_COMMIT:'   '], commitId_)
        InjectVars.injectClosureTo(commitId_, 'sh', ShellTestData.shellClosureSSH)

        assertEquals('1111111222222222222222222222222222222222', commitId_())

    }

}
