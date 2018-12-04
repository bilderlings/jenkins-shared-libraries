import TestData.CommitIdTestData
import TestData.Docker.DockerBuildTestData
import Utils.Helper
import org.junit.Before
import org.junit.Test

class CommitId_CommonTests extends GroovyTestCase {

    protected commitId_ = new commitId()

    @Before
    void setUp(){

    }

    @Test
    void test_CommitIdWithoutGIT_COMMIT_CommitIdIsReturnedFromShell(){
        Helper.setEnvVariables([:], commitId_)
        InjectVars.injectClosureTo(commitId_, 'sh', CommitIdTestData.lastCommitIdClosure)

        assertEquals('1111', commitId_())

    }

    @Test
    void test_CommitIdWithGIT_COMMITOK_CommitIdIsReturnedFromENV(){
        Helper.setEnvVariables([GIT_COMMIT:"2222"], commitId_)
        InjectVars.injectClosureTo(commitId_, 'sh', CommitIdTestData.lastCommitIdClosure)
        commitId_.echo = { }
        assertEquals('2222', commitId_())

    }

    @Test
    void test_CommitIdWithGIT_COMMITOK_CheckEchoMessage(){
        Helper.setEnvVariables([GIT_COMMIT:"2222"], commitId_)
        InjectVars.injectClosureTo(commitId_, 'sh', CommitIdTestData.lastCommitIdClosure)
        def actualMessage = ''
        commitId_.echo = { String msg -> actualMessage = msg }
        commitId_()
        assertEquals('Get git commit id from environment variable GIT_COMMIT', actualMessage)

    }

    @Test
    void test_CommitIdWithGIT_COMMITNull_CommitIdIsReturnedFromShell(){
        Helper.setEnvVariables([GIT_COMMIT:null], commitId_)
        InjectVars.injectClosureTo(commitId_, 'sh', CommitIdTestData.lastCommitIdClosure)

        assertEquals('1111', commitId_())

    }

    @Test
    void test_CommitIdWithGIT_COMMITEmpty_CommitIdIsReturnedFromShell(){
        Helper.setEnvVariables([GIT_COMMIT:''], commitId_)
        InjectVars.injectClosureTo(commitId_, 'sh', CommitIdTestData.lastCommitIdClosure)

        assertEquals('1111', commitId_())

    }

    @Test
    void test_CommitIdWithGIT_COMMITWhitespace_CommitIdIsReturnedFromShell(){
        Helper.setEnvVariables([GIT_COMMIT:' '], commitId_)
        InjectVars.injectClosureTo(commitId_, 'sh', CommitIdTestData.lastCommitIdClosure)

        assertEquals('1111', commitId_())

    }

    @Test
    void test_CommitIdWithGIT_COMMITSeveralWhitespaces_CommitIdIsReturnedFromShell(){
        Helper.setEnvVariables([GIT_COMMIT:'   '], commitId_)
        InjectVars.injectClosureTo(commitId_, 'sh', CommitIdTestData.lastCommitIdClosure)

        assertEquals('1111', commitId_())

    }

}
