import TestData.RunTestsData
import Utils.Exceptions.StageResultException
import Utils.Helper
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException

class RunTests_CommonTests extends GroovyTestCase {

    protected runTests_ = new runTests()

    @Before
    void setUp(){
        def variables = RunTestsData.commonVariables()
        Helper.setEnvVariables(variables, runTests_)
        InjectVars.injectTo(runTests_, 'successBuild')
    }

    @Test
    void test_RunTestsOneStringParameter_StringParameterWillBeAdded(){
        def jobName = 'account-tests-api/master'
        def expectedStringObjs = [[name: 'TAG1', value: 'tag1']]
        def stringProps = []
        runTests_.build = { Map args -> stringProps = args.parameters; RunTestsData.defaultSuccessBuildResult() }

        runTests_(job: jobName, parameters: [[name: 'TAG1', value: 'tag1']])

        assertEquals(expectedStringObjs, stringProps)

    }
    @Test
    void test_RunTestsTwoStringParameter_StringParameterWillBeAdded(){
        def jobName = 'account-tests-api/master'
        def expectedStringObjs = [[name: 'TAG1', value: 'tag1'], [name: 'TAG2', value: 'tag2']]
        def stringProps = []
        runTests_.build = { Map args -> stringProps = args.parameters; RunTestsData.defaultSuccessBuildResult() }

        runTests_(job: jobName, parameters: [[name: 'TAG1', value: 'tag1'], [name: 'TAG2', value: 'tag2']])

        assertEquals(expectedStringObjs, stringProps)

    }

    @Test
    void test_RunTests_EnvironmentVariableWithUrlExist(){
        def jobName = 'account-tests-api/master'
        def stringProps = []
        runTests_.build = { Map args ->  stringProps = args.parameters; RunTestsData.defaultSuccessBuildResult() }

        runTests_(job: jobName, parameters: ['TAGS': 'tag'])

        assertEquals('http://localhost:8080/job/child/1/allure/', runTests_.env['account-tests-api_TESTS_URL'])

    }

    @Test
    void test_RunTests_jobNameIsCorrect(){
        def jobName = 'account-tests-api/master'
        def buildParams = [:]
        runTests_.build = {Map params ->
            buildParams = params
            RunTestsData.defaultSuccessBuildResult()
        }

        runTests_(job: jobName, parameters: ['TAGS': 'tag'])

        assertEquals(jobName, buildParams['job'])

    }

    @Test
    void test_RunTests_propagateIsFalse(){
        def jobName = 'account-tests-api/master'
        def buildParams = [:]
        runTests_.build = {Map params ->
            buildParams = params
            RunTestsData.defaultSuccessBuildResult()
        }

        runTests_(job: jobName, parameters: ['TAGS': 'tag'])

        assertEquals(false, buildParams['propagate'])

    }

    @Test
    void test_RunTests_waitIsTrue(){
        def jobName = 'account-tests-api/master'
        def buildParams = [:]
        runTests_.build = {Map params ->
            buildParams = params
            RunTestsData.defaultSuccessBuildResult()
        }

        runTests_(job: jobName, parameters: ['TAGS': 'tag'])

        assertEquals(true, buildParams['wait'])

    }

    @Test
    void test_RunTests_parametersIsCorrect(){
        def jobName = 'account-tests-api/master'
        def expectedStringObjs = [[name: 'TAG1', value: 'tag1']]
        def buildParams = [:]
        runTests_.build = {Map params ->
            buildParams = params
            RunTestsData.defaultSuccessBuildResult()
        }

        runTests_(job: jobName, parameters: [[name:'TAG1', value: 'tag1']])

        assertEquals(expectedStringObjs, buildParams['parameters'])

    }

    @Test
    void test_RunTests_buildExecutedOneTime(){
        def jobName = 'account-tests-api/master'
        def buildIsExecuted = 0
        runTests_.build = {Map params ->
            buildIsExecuted++
            RunTestsData.defaultSuccessBuildResult()
        }

        runTests_(job: jobName, parameters: ['TAGS': 'tag'])

        assertEquals(1, buildIsExecuted)

    }

}
