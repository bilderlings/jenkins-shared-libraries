import TestData.BitbucketStatusTestData
import TestData.ShellTestData
import Utils.Helper
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized.class)
class BitbucketStatus_DefaultCallParameters_DefinedBuildStatus_Tests extends GroovyTestCase {

    protected bitbucketStatus_ = new bitbucketStatus()
    @Parameterized.Parameters(name = "{0}")
    static Collection<Object> data() {
        BitbucketStatusTestData.definedBuildStatuses()
    }

    protected String buildStatus

    BitbucketStatus_DefaultCallParameters_DefinedBuildStatus_Tests(String buildStatus){
        this.buildStatus = buildStatus
    }
    @Before
    void setUp(){
        def variables = BitbucketStatusTestData.commonVariables()
        Helper.setEnvVariables(variables, bitbucketStatus_)
        InjectVars.injectTo(bitbucketStatus_, 'imageName', 'commitId')
        InjectVars.injectClosureTo(bitbucketStatus_, 'sh', ShellTestData.shellClosureSSH)
    }

    @Test
    void test_BitbucketStatus_DefaultCallParameters_DefinedBuildStatus_buildStateIsSuccessful(){
        Helper.setBuildStatus(buildStatus, bitbucketStatus_)
        def actualParameters = [:]
        bitbucketStatus_.bitbucketStatusNotify = { Map map -> actualParameters = map; return null}

        bitbucketStatus_()

        assertEquals(BitbucketStatusTestData.buildStateMap()[buildStatus], actualParameters['buildState'])

    }

    @Test
    void test_BitbucketStatus_DefaultCallParameters_DefinedBuildStatus_commitIdIsCorrect(){
        Helper.setBuildStatus(buildStatus, bitbucketStatus_)
        def actualParameters = [:]
        bitbucketStatus_.bitbucketStatusNotify = { Map map -> actualParameters = map; return null}

        bitbucketStatus_()

        assertEquals('1111111222222222222222222222222222222222', actualParameters['commitId'])

    }

    @Test
    void test_BitbucketStatus_DefaultCallParameters_DefinedBuildStatus_repoSlugIsCorrect(){
        Helper.setBuildStatus(buildStatus, bitbucketStatus_)
        def actualParameters = [:]
        bitbucketStatus_.bitbucketStatusNotify = { Map map -> actualParameters = map; return null}

        bitbucketStatus_()

        assertEquals('Job_Name', actualParameters['repoSlug'])

    }

}
