import TestData.BitbucketStatusTestData
import TestData.ShellTestData
import Utils.Helper
import org.junit.Before
import org.junit.Test

class BitbucketStatusSuccessful_CommonTests extends GroovyTestCase {

    protected bitbucketStatusSuccessful_ = new bitbucketStatusSuccessful()

    @Before
    void setUp(){
        def variables = BitbucketStatusTestData.commonVariables()
        Helper.setEnvVariables(variables, bitbucketStatusSuccessful_)
        InjectVars.injectTo(bitbucketStatusSuccessful_, 'bitbucketStatus', 'commitId', 'imageName')
        InjectVars.injectClosureTo(bitbucketStatusSuccessful_, 'sh', ShellTestData.shellClosureSSH)
    }

    @Test
    void test_BitbucketStatusFailed_StatusIsSuccessful(){
        def actualParameters = [:]
        bitbucketStatusSuccessful_.bitbucketStatusNotify = { Map map -> actualParameters = map; return null}

        bitbucketStatusSuccessful_()

        assertEquals('SUCCESSFUL', actualParameters['buildState'])

    }

    @Test
    void test_BitbucketStatusFailed_StatusNotSuccessful_StatusIsSuccessful(){
        def actualParameters = [:]
        bitbucketStatusSuccessful_.bitbucketStatusNotify = { Map map -> actualParameters = map; return null}

        bitbucketStatusSuccessful_ status: 'NotSuccessful'

        assertEquals('SUCCESSFUL', actualParameters['buildState'])

    }

    @Test
    void test_BitbucketStatusFailed_SetRepoSlug_RepoSlugIsChanged(){
        def actualParameters = [:]
        bitbucketStatusSuccessful_.bitbucketStatusNotify = { Map map -> actualParameters = map; return null}

        bitbucketStatusSuccessful_ repoSlug: 'custom_repo'

        assertEquals('custom_repo', actualParameters['repoSlug'])

    }

}
