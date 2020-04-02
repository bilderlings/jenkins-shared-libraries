import TestData.BitbucketStatusTestData
import TestData.ShellTestData
import Utils.Helper
import groovy.json.JsonOutput
import org.junit.Before
import org.junit.Test

class BitbucketStatusFailedFirstBuild_CommonTests extends GroovyTestCase {

    protected bitbucketStatusFailed_ = new bitbucketStatusFailed()

    @Before
    void setUp(){
        def variables = BitbucketStatusTestData.commonVariablesForFirstBuild()
        Helper.setEnvVariables(variables, bitbucketStatusFailed_)
        InjectVars.injectTo(bitbucketStatusFailed_, 'bitbucketStatus', 'commitId', 'imageName')
        InjectVars.injectClosureTo(bitbucketStatusFailed_, 'sh', ShellTestData.shellClosureSSH)
    }

    @Test
    void test_BitbucketStatusFailedFirstBuild_StatusIsFailed(){
        def actualHttpRequestParameters = []
        bitbucketStatusFailed_.httpRequest = { Map map ->
            actualHttpRequestParameters << map
            return BitbucketStatusTestData.httpRequestMock(map)
        }

        def data = [
                state: 'FAILED',
                url: 'http://jenkins.k8s.iamoffice.lv/blue/organizations/jenkins/Job_Name/detail/not_master/1/pipeline/',
                key: 'Job_Name',
                name: 'Job_Name/not_master #1'
        ]
        def expectedBody = JsonOutput.toJson(data)

        bitbucketStatusFailed_()

        assertTrue('We should have 2 requests', actualHttpRequestParameters.size() == 2)
        assertEquals(expectedBody, actualHttpRequestParameters[1]['requestBody'])

    }

}
