import TestData.BitbucketStatusTestData
import TestData.ShellTestData
import Utils.Helper
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized.class)
class BitbucketStatusFirstBuild_UndefinedBuildStatus_ValidBitBucketStatus_Tests extends GroovyTestCase {

    protected bitbucketStatus_ = new bitbucketStatus()

    @Parameterized.Parameters(name = "{0}")
    static Collection<Object[]> data() {
        BitbucketStatusTestData.suite_UndefinedBuildStatus_ValidBitbucketStatuses()
    }

    protected String buildStatus
    protected String customBitbucketStatus

	BitbucketStatusFirstBuild_UndefinedBuildStatus_ValidBitBucketStatus_Tests(List list){
        this.buildStatus = list[0]
        this.customBitbucketStatus = list[1]
    }

    @Before
    void setUp(){
        def variables = BitbucketStatusTestData.commonVariablesForFirstBuild()
        Helper.setEnvVariables(variables, bitbucketStatus_)
        InjectVars.injectTo(bitbucketStatus_, 'imageTag', 'commitId', 'imageName')
        InjectVars.injectClosureTo(bitbucketStatus_, 'sh', ShellTestData.shellClosureSSH)
    }

    @Test
    void test_BitbucketStatusFirstBuild_UndefinedBuildStatus_ValidBitBucketStatus_bitbucketStatusNotifyIsNotExecuted(){
        Helper.setBuildStatus(buildStatus, bitbucketStatus_)
        def bitbucketStatusNotifyWasExecuted = false
        bitbucketStatus_.httpRequest = { Map map -> BitbucketStatusTestData.httpRequestMock(map)}
        bitbucketStatus_.bitbucketStatusNotify = { Map map -> bitbucketStatusNotifyWasExecuted = true; return null}
        bitbucketStatus_.echo = { str -> return null}

        bitbucketStatus_ status: customBitbucketStatus

        assertFalse(bitbucketStatusNotifyWasExecuted)

    }

    @Test
    void test_BitbucketStatusFirstBuild_UndefinedBuildStatus_ValidBitBucketStatus_httpRequestIsExecuted(){
        Helper.setBuildStatus(buildStatus, bitbucketStatus_)
        def httpRequestWasExecuted = false
        bitbucketStatus_.httpRequest = { Map map -> httpRequestWasExecuted = true; BitbucketStatusTestData.httpRequestMock(map)}
        bitbucketStatus_.echo = { str -> return null}

        bitbucketStatus_ status: customBitbucketStatus

        assertTrue(httpRequestWasExecuted)

    }

    @Test
    void test_BitbucketStatusFirstBuild_UndefinedBuildStatus_ValidBitBucketStatus_echoDoNotSentMessage(){
        Helper.setBuildStatus(buildStatus, bitbucketStatus_)
        def echoIsExecuted = false
        bitbucketStatus_.httpRequest = { Map map -> BitbucketStatusTestData.httpRequestMock(map)}
        bitbucketStatus_.bitbucketStatusNotify = { Map map -> null}
        bitbucketStatus_.echo = { str -> echoIsExecuted = true; return null }
        bitbucketStatus_ status: customBitbucketStatus

        assertFalse(echoIsExecuted)

    }

}
