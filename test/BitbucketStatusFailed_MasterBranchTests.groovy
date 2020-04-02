import TestData.BitbucketStatusTestData
import TestData.ShellTestData
import Utils.Helper
import org.junit.Before
import org.junit.Test

class BitbucketStatusFailed_MasterBranchTests extends GroovyTestCase {

    protected bitbucketStatusFailed_ = new bitbucketStatusFailed()

    @Before
    void setUp(){
        def variables = BitbucketStatusTestData.commonVariablesForFirstBuildMaster()
        Helper.setEnvVariables(variables, bitbucketStatusFailed_)
        InjectVars.injectTo(bitbucketStatusFailed_, 'bitbucketStatus')
    }

    @Test
    void test_BitbucketStatus_MasterBranch_OnlyEchoIsExecuted(){
        def httpRequestIsExecuted = false
        bitbucketStatusFailed_.httpRequest = { Map map ->
            httpRequestIsExecuted = true
        }
        def notifyIsExecuted = false
        bitbucketStatusFailed_.bitbucketStatusNotify = { Map map ->
            notifyIsExecuted = true
        }
        def echoMessage = ''
        bitbucketStatusFailed_.echo = { msg ->
            echoMessage = msg?.toString()
        }

        bitbucketStatusFailed_()

        assertFalse('httpRequest is not executed', httpRequestIsExecuted)
        assertFalse('bitbucketStatusNotify is not executed',  notifyIsExecuted)
        assertEquals('Bitbucket status \'FAILED\' is ignored cause \'master\' branch', echoMessage)

    }

    @Test
    void test_BitbucketStatus_MasterBranchAndNotIgnoreBranch_BitbucketStatusNotifyIsExecuted(){
        def variables = BitbucketStatusTestData.commonVariables()
        Helper.setEnvVariables(variables, bitbucketStatusFailed_)
        InjectVars.injectTo(bitbucketStatusFailed_, 'commitId', 'imageName')
        InjectVars.injectClosureTo(bitbucketStatusFailed_, 'sh', ShellTestData.shellClosureSSH)
        def httpRequestIsExecuted = false
        bitbucketStatusFailed_.httpRequest = { Map map ->
            httpRequestIsExecuted = true
        }
        def notifyIsExecuted = false
        bitbucketStatusFailed_.bitbucketStatusNotify = { Map map ->
            notifyIsExecuted = true
        }

        bitbucketStatusFailed_ ignoreMaster: false

        assertTrue('bitbucketStatusNotify is not executed',  notifyIsExecuted)

    }


}
