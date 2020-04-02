import TestData.BitbucketStatusTestData
import TestData.ShellTestData
import Utils.Exceptions.InvalidBitbucketStatusException
import Utils.Helper
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized.class)
class BitbucketStatus_UndefinedBuildStatus_InvalidBitbucketStatus_Tests extends GroovyTestCase {

    protected bitbucketStatus_ = new bitbucketStatus()

    @Parameterized.Parameters(name = "{0}")
    static Collection<Object[]> data() {
        BitbucketStatusTestData.suite_UndefinedBuildStatus_InvalidBitbucketStatuses()
    }

    protected String invalidBitbucketStatus
    protected String buildStatus

	BitbucketStatus_UndefinedBuildStatus_InvalidBitbucketStatus_Tests(List list){
        this.buildStatus = list[0]
        this.invalidBitbucketStatus = list[1]
    }

    @Rule
    public ExpectedException thrown = ExpectedException.none()

    @Before
    void setUp(){
        def variables = BitbucketStatusTestData.commonVariables()
        Helper.setEnvVariables(variables, bitbucketStatus_)
        InjectVars.injectTo(bitbucketStatus_, 'commitId', 'imageName')
        InjectVars.injectClosureTo(bitbucketStatus_, 'sh', ShellTestData.shellClosureSSH)
    }

    @Test
    void test_BitbucketStatus_UndefinedBuildStatus_InvalidBitbucketStatus_ErrorIsRaisedWithMessage(){
        Helper.setBuildStatus(buildStatus, bitbucketStatus_)
        bitbucketStatus_.error = { msg -> throw new InvalidBitbucketStatusException(msg.toString()) }
        thrown.expect(InvalidBitbucketStatusException.class)
        thrown.expectMessage("Undefined bitbucket status: ${invalidBitbucketStatus}".toString())

        bitbucketStatus_ status: invalidBitbucketStatus

    }

    @Test
    void test_BitbucketStatus_UndefinedBuildStatus_InvalidBitbucketStatus_bitbucketStatusNotifyIsNotExecuted(){
        Helper.setBuildStatus(buildStatus, bitbucketStatus_)
        def bitbucketStatusNotifyWasExecuted = false
        bitbucketStatus_.bitbucketStatusNotify = { Map map -> bitbucketStatusNotifyWasExecuted = true; return null}
        bitbucketStatus_.error = { msg -> throw new InvalidBitbucketStatusException(msg.toString()) }
        try {
            bitbucketStatus_ status: invalidBitbucketStatus
            fail("Expected an InvalidBitbucketStatusException to be thrown")
        }catch(InvalidBitbucketStatusException e){
            assertFalse(bitbucketStatusNotifyWasExecuted)
        }
    }

    @Test
    void test_BitbucketStatus_DefinedBuildStatus_InvalidBitbucketStatus_httpRequestIsNotExecuted(){
        Helper.setBuildStatus(buildStatus, bitbucketStatus_)
        def httpRequestWasExecuted = false
        bitbucketStatus_.httpRequest = { Map map -> httpRequestWasExecuted = true; return null}
        bitbucketStatus_.error = { msg -> throw new InvalidBitbucketStatusException(msg.toString()) }

        try {
            bitbucketStatus_ status: invalidBitbucketStatus
            fail("Expected an InvalidBitbucketStatusException to be thrown")
        }catch(InvalidBitbucketStatusException e){
            assertFalse(httpRequestWasExecuted)
        }
    }

}
