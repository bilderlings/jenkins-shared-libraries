import TestData.BitbucketStatusTestData
import TestData.ShellTestData
import Utils.Helper
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized.class)
class BitbucketStatus_NullBitbucketStatus_UndefinedBuildStatus_Tests extends GroovyTestCase {

    protected bitbucketStatus_ = new bitbucketStatus()
    @Parameterized.Parameters(name = "{0}")
    static Collection<Object> data() {
        BitbucketStatusTestData.undefinedBuildStatuses()
    }

    protected String buildStatus

	BitbucketStatus_NullBitbucketStatus_UndefinedBuildStatus_Tests(String buildStatus){
        this.buildStatus = buildStatus
    }
    @Before
    void setUp(){
        def variables = BitbucketStatusTestData.commonVariables()
        Helper.setEnvVariables(variables, bitbucketStatus_)
        InjectVars.injectTo(bitbucketStatus_, 'commitId', 'imageName')
        InjectVars.injectClosureTo(bitbucketStatus_, 'sh', ShellTestData.shellClosureSSH)
    }

    @Test
    void test_BitbucketStatus_NullBitbucketStatus_UndefinedBuildStatus_bitbucketStatusNotifyIsNotExecuted(){
        Helper.setBuildStatus(buildStatus, bitbucketStatus_)
        def bitbucketStatusNotifyWasExecuted = false
        bitbucketStatus_.bitbucketStatusNotify = { Map map -> bitbucketStatusNotifyWasExecuted = true; return null}
        bitbucketStatus_.echo = { str -> return null}

        bitbucketStatus_ status: null

        assertFalse(bitbucketStatusNotifyWasExecuted)

    }

    @Test
    void test_BitbucketStatus_NullBitbucketStatus_UndefinedBuildStatus_httpRequestIsNotExecuted(){
        Helper.setBuildStatus(buildStatus, bitbucketStatus_)
        def httpRequestWasExecuted = false
        bitbucketStatus_.httpRequest = { Map map -> httpRequestWasExecuted = true; return null}
        bitbucketStatus_.echo = { str -> return null}

        bitbucketStatus_ status: null

        assertFalse(httpRequestWasExecuted)

    }

    @Test
    void test_BitbucketStatus_NullBitbucketStatus_UndefinedBuildStatus_echoSentMessage(){
        Helper.setBuildStatus(buildStatus, bitbucketStatus_)
        def actualMessage = ""
        bitbucketStatus_.echo = { str -> actualMessage = str}
        def expectedMessage = "bitbucketStatusNotify is muted. Undefined build status: ${buildStatus}".toString()
        bitbucketStatus_ status: null

        assertEquals(expectedMessage, actualMessage)

    }

}
