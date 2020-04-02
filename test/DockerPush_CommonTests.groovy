import TestData.ShellTestData
import TestData.Docker.DockerPushTestData
import Utils.Helper
import org.junit.Before
import org.junit.Test

class DockerPush_CommonTests extends GroovyTestCase {

    protected dockerPush_ = new dockerPush()

    @Before
    void setUp(){
        def variables = DockerPushTestData.commonVariables()
        Helper.setEnvVariables(variables, dockerPush_)
        InjectVars.injectTo(dockerPush_, 'imageTag', 'imageName', 'commitId')
    }

    @Test
    void test_DockerPushLatest_NoParameters_checkOrderOfCommands(){
        def actualCommands = []
        dockerPush_.sh = { command ->
            if (command instanceof Map){
                return ShellTestData.shellClosureSSH(command)
            }
            actualCommands << command; return null
        }
        def expectedShellCommands = [
                'docker push \"registry.com/bilderlings/Job_Name:master-1\"',
                'docker push \"registry.com/bilderlings/Job_Name:1111111\"',
        ]

        dockerPush_()

        assertEquals(expectedShellCommands, actualCommands)

    }

}
