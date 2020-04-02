import TestData.BitbucketStatusTestData
import TestData.ShellTestData
import Utils.Helper
import groovy.json.JsonOutput
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized.class)
class BitbucketStatusFirstBuild_DefinedBuildStatus_ValidBitBucketStatus_Tests extends GroovyTestCase {

    protected bitbucketStatus_ = new bitbucketStatus()

    @Parameterized.Parameters(name = "{0}")
    static Collection<Object[]> data() {
        BitbucketStatusTestData.suite_DefinedBuildStatus_ValidBitbucketStatuses()
    }

    protected String buildStatus
    protected String validBitbucketStatus

	BitbucketStatusFirstBuild_DefinedBuildStatus_ValidBitBucketStatus_Tests(List list){
        this.buildStatus = list[0]
        this.validBitbucketStatus = list[1]
    }

    @Before
    void setUp(){
        def variables = BitbucketStatusTestData.commonVariablesForFirstBuild()
        Helper.setEnvVariables(variables, bitbucketStatus_)
        InjectVars.injectTo(bitbucketStatus_, 'imageName', 'commitId')
        InjectVars.injectClosureTo(bitbucketStatus_, 'sh', ShellTestData.shellClosureSSH)
    }

    @Test
    void test_BitbucketStatusFirstBuild_DefinedBuildStatus_ValidBitBucketStatus_requestUrlIsCorrect(){
        Helper.setBuildStatus(buildStatus, bitbucketStatus_)
        def actualHttpRequestParameters = []
        bitbucketStatus_.httpRequest = { Map map ->
            actualHttpRequestParameters << map
            return BitbucketStatusTestData.httpRequestMock(map)
        }
        def expectedUrl = 'https://api.bitbucket.org/2.0/repositories/bilderlings/Job_Name/commit/1111111222222222222222222222222222222222/statuses/build'

        bitbucketStatus_ status: validBitbucketStatus

        assertTrue('We should have 2 requests', actualHttpRequestParameters.size() == 2)
        assertEquals(expectedUrl, actualHttpRequestParameters[1]['url'])

    }

    @Test
    void test_BitbucketStatusFirstBuild_DefinedBuildStatus_ValidBitBucketStatus_authenticationIsCorrect(){
        Helper.setBuildStatus(buildStatus, bitbucketStatus_)
        def actualHttpRequestParameters = []
        bitbucketStatus_.httpRequest = { Map map ->
            actualHttpRequestParameters << map
            return BitbucketStatusTestData.httpRequestMock(map)
        }

        bitbucketStatus_ status: validBitbucketStatus

        assertTrue('We should have 2 requests', actualHttpRequestParameters.size() == 2)
        assertEquals('bitbucket-oauth-credentials', actualHttpRequestParameters[1]['authentication'])

    }

    @Test
    void test_BitbucketStatusFirstBuild_DefinedBuildStatus_ValidBitBucketStatus_httpMethodIsCorrect(){
        Helper.setBuildStatus(buildStatus, bitbucketStatus_)
        def actualHttpRequestParameters = []
        bitbucketStatus_.httpRequest = { Map map ->
            actualHttpRequestParameters << map
            return BitbucketStatusTestData.httpRequestMock(map)
        }

        bitbucketStatus_ status: validBitbucketStatus

        assertTrue('We should have 2 requests', actualHttpRequestParameters.size() == 2)
        assertEquals('POST', actualHttpRequestParameters[1]['httpMode'])

    }

    @Test
    void test_BitbucketStatusFirstBuild_DefinedBuildStatus_ValidBitBucketStatus_contentTypeIsCorrect(){
        Helper.setBuildStatus(buildStatus, bitbucketStatus_)
        def actualHttpRequestParameters = []
        bitbucketStatus_.httpRequest = { Map map ->
            actualHttpRequestParameters << map
            return BitbucketStatusTestData.httpRequestMock(map)
        }

        bitbucketStatus_ status: validBitbucketStatus

        assertTrue('We should have 2 requests', actualHttpRequestParameters.size() == 2)
        assertEquals('APPLICATION_JSON', actualHttpRequestParameters[1]['contentType'])

    }

    @Test
    void test_BitbucketStatusFirstBuild_DefinedBuildStatus_ValidBitBucketStatus_responseCodeIsCorrect(){
        Helper.setBuildStatus(buildStatus, bitbucketStatus_)
        def actualHttpRequestParameters = []
        bitbucketStatus_.httpRequest = { Map map ->
            actualHttpRequestParameters << map
            return BitbucketStatusTestData.httpRequestMock(map)
        }

        bitbucketStatus_ status: validBitbucketStatus

        assertTrue('We should have 2 requests', actualHttpRequestParameters.size() == 2)
        assertEquals('200:201', actualHttpRequestParameters[1]['validResponseCodes'])

    }

    @Test
    void test_BitbucketStatusFirstBuild_DefinedBuildStatus_ValidBitBucketStatus_requestBodyIsCorrect(){
        Helper.setBuildStatus(buildStatus, bitbucketStatus_)
        def actualHttpRequestParameters = []
        bitbucketStatus_.httpRequest = { Map map ->
            actualHttpRequestParameters << map
            return BitbucketStatusTestData.httpRequestMock(map)
        }

        def data = [
                state: "${validBitbucketStatus}".toString(),
                url: 'http://jenkins.k8s.iamoffice.lv/blue/organizations/jenkins/Job_Name/detail/not_master/1/pipeline/',
                key: 'Job_Name',
                name: 'Job_Name/not_master #1'
        ]
        def expectedBody = JsonOutput.toJson(data)

        bitbucketStatus_ status: validBitbucketStatus

        assertTrue('We should have 2 requests', actualHttpRequestParameters.size() == 2)
        assertEquals(expectedBody, actualHttpRequestParameters[1]['requestBody'])

    }

}
