import TestData.BitbucketStatusTestData
import TestData.ShellTestData
import Utils.Helper
import org.junit.Before
import org.junit.Test

class BitbucketStatusInProgress_MasterBranchTests extends GroovyTestCase {

    protected bitbucketStatusInProgress_ = new bitbucketStatusInProgress()

    @Before
    void setUp(){
        def variables = BitbucketStatusTestData.commonVariablesForFirstBuildMaster()
        Helper.setEnvVariables(variables, bitbucketStatusInProgress_)
        InjectVars.injectTo(bitbucketStatusInProgress_, 'bitbucketStatus')
    }

    @Test
    void test_BitbucketStatus_MasterBranch_OnlyEchoIsExecuted(){
        def httpRequestIsExecuted = false
        bitbucketStatusInProgress_.httpRequest = { Map map ->
            httpRequestIsExecuted = true
        }
        def notifyIsExecuted = false
        bitbucketStatusInProgress_.bitbucketStatusNotify = { Map map ->
            notifyIsExecuted = true
        }
        def echoMessage = ''
        bitbucketStatusInProgress_.echo = { msg ->
            echoMessage = msg?.toString()
        }

        bitbucketStatusInProgress_()

        assertFalse('httpRequest is executed', httpRequestIsExecuted)
        assertFalse('bitbucketStatusNotify is executed',  notifyIsExecuted)
        assertEquals('Bitbucket status \'INPROGRESS\' is ignored cause \'master\' branch', echoMessage)

    }

    @Test
    void test_BitbucketStatus_MasterBranchAndNotIgnoreBranch_BitbucketStatusNotifyIsExecuted(){
        def variables = BitbucketStatusTestData.commonVariables()
        Helper.setEnvVariables(variables, bitbucketStatusInProgress_)
        InjectVars.injectTo(bitbucketStatusInProgress_, 'commitId', 'imageName')
        InjectVars.injectClosureTo(bitbucketStatusInProgress_, 'sh', ShellTestData.shellClosureSSH)
        def httpRequestIsExecuted = false
        bitbucketStatusInProgress_.httpRequest = { Map map ->
            httpRequestIsExecuted = true
        }
        def notifyIsExecuted = false
        bitbucketStatusInProgress_.bitbucketStatusNotify = { Map map ->
            notifyIsExecuted = true
        }

        bitbucketStatusInProgress_ ignoreMaster: false

        assertTrue('bitbucketStatusNotify is not executed',  notifyIsExecuted)

    }

}
