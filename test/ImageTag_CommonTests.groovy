import TestData.ShellTestData
import Utils.Helper
import org.junit.Before
import org.junit.Test

class ImageTag_CommonTests extends GroovyTestCase {

    protected imageTag_ = new imageTag()

    @Before
    void setUp(){
        InjectVars.injectTo(imageTag_, 'commitId')
        InjectVars.injectClosureTo(imageTag_, 'sh', ShellTestData.shellClosureSSH)
    }

    @Test
    void test_ImageTag_ImageTagIsReturned(){

        Helper.setEnvVariables([BRANCH_NAME:'branch', BUILD_ID: '1234'], imageTag_)

        assertEquals('1111111', imageTag_())

    }

}
