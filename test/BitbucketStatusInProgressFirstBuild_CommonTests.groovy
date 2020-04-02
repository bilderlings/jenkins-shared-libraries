import TestData.BitbucketStatusTestData
import TestData.ShellTestData
import Utils.Helper
import groovy.json.JsonOutput
import org.junit.Before
import org.junit.Test

class BitbucketStatusInProgressFirstBuild_CommonTests extends GroovyTestCase {

    protected bitbucketStatusInProgress_ = new bitbucketStatusInProgress()

    @Before
    void setUp(){
        def variables = BitbucketStatusTestData.commonVariablesForFirstBuild()
        Helper.setEnvVariables(variables, bitbucketStatusInProgress_)
        InjectVars.injectTo(bitbucketStatusInProgress_, 'bitbucketStatus', 'commitId', 'imageName')
        InjectVars.injectClosureTo(bitbucketStatusInProgress_, 'sh', ShellTestData.shellClosureSSH)
    }

    @Test
    void test_BitbucketStatusInProgressFirstBuild_StatusIsInProgress(){
        def actualHttpRequestParameters = []
        bitbucketStatusInProgress_.httpRequest = { Map map ->
            actualHttpRequestParameters << map
            BitbucketStatusTestData.httpRequestMock(map)
        }
        def data = [
                state: 'INPROGRESS',
                url: 'http://jenkins.k8s.iamoffice.lv/blue/organizations/jenkins/Job_Name/detail/not_master/1/pipeline/',
                key: 'Job_Name',
                name: 'Job_Name/not_master #1'
        ]
        def expectedBody = JsonOutput.toJson(data)

        bitbucketStatusInProgress_()

        assertTrue('We should have 2 requests', actualHttpRequestParameters.size() == 2)
        assertEquals(expectedBody, actualHttpRequestParameters[1]['requestBody'])

    }

}
