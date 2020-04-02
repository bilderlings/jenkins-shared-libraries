import TestData.ShellTestData
import TestData.Docker.DockerBuildTestData
import Utils.Helper
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized.class)
class DockerBuild_CustomDockerfileCustomImageName_Tests extends GroovyTestCase {

    @Parameterized.Parameters(name = "{0}")
    static Collection<Object[]> data() {
        DockerBuildTestData.suite_CustomDockerFilePathsWithCustomImageNames()
    }

    protected String path
    protected String imagename
    protected dockerBuild_ = new dockerBuild()

	DockerBuild_CustomDockerfileCustomImageName_Tests(List list){
        this.path = list[0]
        this.imagename = list[1]
    }

    @Before
    void setUp(){
        def variables = DockerBuildTestData.commonVariables()
        Helper.setEnvVariables(variables, dockerBuild_)
        InjectVars.injectTo(dockerBuild_, 'imageTag', 'imageName', 'commitId')
    }

    @Test
    void test_DockerBuild_shellCommandDockerBuildIsExecuted(){
        def actualCommands = []
        dockerBuild_.sh = { command ->
            if (command instanceof Map){
                return ShellTestData.shellClosureSSH(command)
            }
            actualCommands << command; return null
        }
        def expectedCommands = [
                "docker build . -f \"${path}\" -t \"registry.com/bilderlings/${imagename}:master-1\" -t \"registry.com/bilderlings/${imagename}:1111111\"".toString()
        ]

        dockerBuild_ dockerfile: path, imageName: imagename

        assertEquals(expectedCommands, actualCommands)

    }

}
