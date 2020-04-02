import TestData.BitbucketStatusTestData
import TestData.ShellTestData
import Utils.Helper
import groovy.json.JsonOutput
import org.junit.Before
import org.junit.Test

class BitbucketStatusSuccessfulFirstBuild_CommonTests extends GroovyTestCase {

    protected bitbucketStatusSuccessful_ = new bitbucketStatusSuccessful()

    @Before
    void setUp(){
        def variables = BitbucketStatusTestData.commonVariablesForFirstBuild()
        Helper.setEnvVariables(variables, bitbucketStatusSuccessful_)
        InjectVars.injectTo(bitbucketStatusSuccessful_, 'bitbucketStatus', 'commitId', 'imageName')
        InjectVars.injectClosureTo(bitbucketStatusSuccessful_, 'sh', ShellTestData.shellClosureSSH)
    }

    @Test
    void test_BitbucketStatusFailed_StatusIsFailed(){
        def actualHttpRequestParameters = []
        bitbucketStatusSuccessful_.httpRequest = { Map map ->
            actualHttpRequestParameters << map
            BitbucketStatusTestData.httpRequestMock(map)
        }
        def data = [
                state: 'SUCCESSFUL',
                url: 'http://jenkins.k8s.iamoffice.lv/blue/organizations/jenkins/Job_Name/detail/not_master/1/pipeline/',
                key: 'Job_Name',
                name: 'Job_Name/not_master #1'
        ]
        def expectedBody = JsonOutput.toJson(data)

        bitbucketStatusSuccessful_()

        assertTrue('We should have 2 requests', actualHttpRequestParameters.size() == 2)
        assertEquals(expectedBody, actualHttpRequestParameters[1]['requestBody'])

    }

}
