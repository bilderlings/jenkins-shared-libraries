import TestData.ShellTestData
import TestData.Docker.DockerPushTestData
import TestData.Docker.DockerTestData
import Utils.Helper
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized.class)
class DockerPush_DefaultImageName_Tests extends GroovyTestCase {

    @Parameterized.Parameters(name = "{0}")
    static Collection<Object> data() {
        DockerTestData.defaultImageNames()
    }

    protected String imageName
    protected dockerPush_ = new dockerPush()

	DockerPush_DefaultImageName_Tests(String imageName){
        this.imageName = imageName
    }

    @Before
    void setUp(){
        def variables = DockerPushTestData.commonVariables()
        Helper.setEnvVariables(variables, dockerPush_)
        InjectVars.injectTo( dockerPush_, 'imageName', 'imageTag', 'commitId')
    }

    @Test
    void test_DockerPushLatest_checkOrderOfCommands(){
        def actualCommands = []
        dockerPush_.sh = { command ->
            if (command instanceof Map){
                return ShellTestData.shellClosureSSH(command)
            }
            actualCommands << command; return null
        }
        def expectedShellCommands = [
                'docker push \"registry.com/bilderlings/Job_Name:master-1\"',
                'docker push \"registry.com/bilderlings/Job_Name:1111111\"'
        ]

        dockerPush_ imageName: imageName

        assertEquals(expectedShellCommands, actualCommands)

    }

}
