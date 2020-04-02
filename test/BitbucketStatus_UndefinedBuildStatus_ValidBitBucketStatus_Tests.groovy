import TestData.BitbucketStatusTestData
import TestData.ShellTestData
import Utils.Helper
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized.class)
class BitbucketStatus_UndefinedBuildStatus_ValidBitBucketStatus_Tests extends GroovyTestCase {

    protected bitbucketStatus_ = new bitbucketStatus()

    @Parameterized.Parameters(name = "{0}")
    static Collection<Object[]> data() {
        BitbucketStatusTestData.suite_UndefinedBuildStatus_ValidBitbucketStatuses()
    }

    protected String buildStatus
    protected String customBitbucketStatus

	BitbucketStatus_UndefinedBuildStatus_ValidBitBucketStatus_Tests(List list){
        this.buildStatus = list[0]
        this.customBitbucketStatus = list[1]
    }

    @Before
    void setUp(){
        def variables = BitbucketStatusTestData.commonVariables()
        Helper.setEnvVariables(variables, bitbucketStatus_)
        InjectVars.injectTo(bitbucketStatus_, 'imageTag', 'commitId', 'imageName')
        InjectVars.injectClosureTo(bitbucketStatus_, 'sh', ShellTestData.shellClosureSSH)
    }

    @Test
    void test_BitbucketStatus_UndefinedBuildStatus_ValidBitBucketStatus_bitbucketStatusNotifyIsExecuted(){
        Helper.setBuildStatus(buildStatus, bitbucketStatus_)
        def bitbucketStatusNotifyWasExecuted = false
        bitbucketStatus_.bitbucketStatusNotify = { Map map -> bitbucketStatusNotifyWasExecuted = true; return null}
        bitbucketStatus_.echo = { str -> return null}

        bitbucketStatus_ status: customBitbucketStatus

        assertTrue(bitbucketStatusNotifyWasExecuted)

    }

    @Test
    void test_BitbucketStatusFirstBuild_UndefinedBuildStatus_ValidBitBucketStatus_httpRequestIsNotExecuted(){
        Helper.setBuildStatus(buildStatus, bitbucketStatus_)
        def httpRequestWasExecuted = false
        bitbucketStatus_.httpRequest = { Map map -> httpRequestWasExecuted = true; return null}
        bitbucketStatus_.bitbucketStatusNotify = { Map map -> null}
        bitbucketStatus_.echo = { str -> return null}

        bitbucketStatus_ status: customBitbucketStatus

        assertFalse(httpRequestWasExecuted)

    }

    @Test
    void test_BitbucketStatus_UndefinedBuildStatus_ValidBitBucketStatus_echoDoNotSentMessage(){
        Helper.setBuildStatus(buildStatus, bitbucketStatus_)
        def echoIsExecuted = false
        bitbucketStatus_.bitbucketStatusNotify = { Map map -> null}
        bitbucketStatus_.echo = { str -> echoIsExecuted = true; return null }
        bitbucketStatus_ status: customBitbucketStatus

        assertFalse(echoIsExecuted)

    }

}
