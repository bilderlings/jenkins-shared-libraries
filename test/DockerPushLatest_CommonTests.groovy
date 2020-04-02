import TestData.ShellTestData
import TestData.Docker.DockerPushLatestTestData
import Utils.Helper
import org.junit.Before
import org.junit.Test

class DockerPushLatest_CommonTests extends GroovyTestCase {

    protected dockerPushLatest_ = new dockerPushLatest()

    @Before
    void setUp(){
        def variables = DockerPushLatestTestData.commonVariables()
        Helper.setEnvVariables(variables, dockerPushLatest_)
        InjectVars.injectTo(dockerPushLatest_, 'imageTag', 'imageName', 'commitId')
    }

    @Test
    void test_DockerPushLatest_NoParameters_checkOrderOfCommands(){
        def actualShellCommands = []
        dockerPushLatest_.sh = { command ->
            if (command instanceof Map) {
                return ShellTestData.shellClosureSSH(command)
            }
            actualShellCommands << command
        }
        def expectedShellCommands = [
                'docker tag \"registry.com/bilderlings/Job_Name:1111111\" \"registry.com/bilderlings/Job_Name:latest\"',
                'docker push \"registry.com/bilderlings/Job_Name:latest\"',
        ]

        dockerPushLatest_()

        assertEquals(expectedShellCommands, actualShellCommands)

    }

}
