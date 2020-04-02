import TestData.HelmUpgradeTestData
import Utils.Helper
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized.class)
class HelmUpgrade_CorrectArguments_Tests extends GroovyTestCase {

    def helmUpgrade_ = new helmUpgrade()
    String namespace
    Map args
    def resultCommand

    @Parameterized.Parameters(name = "{0}")
    static Collection<Map> data() {
        HelmUpgradeTestData.suite_correctNamespaceCorrectArgsCases()
    }

    HelmUpgrade_CorrectArguments_Tests(Map map){
        namespace = map.namespace
        args = map.args
        resultCommand = map.result
    }

    @Before
    void setUp(){
        Helper.setEnvVariables(HelmUpgradeTestData.commonVariables(), helmUpgrade_)
        InjectVars.injectTo(helmUpgrade_, 'imageName')
    }

    @Test
    void test_HelmUpgrade_CorrectArguments_shellIsExecuted(){

        def actualCommands = []
        helmUpgrade_.sh = {command ->
            actualCommands << command
        }
        helmUpgrade_.echo = {String msg -> }

        helmUpgrade_(namespace, args)

        assertEquals(1, actualCommands.size())
        assertEquals(resultCommand.toString(), actualCommands[0])
    }

    @Test
    void test_HelmUpgradeMap_CorrectArguments_shellIsExecuted(){

        def actualCommands = []
        helmUpgrade_.sh = {command ->
            actualCommands << command
        }
        helmUpgrade_.echo = {String msg -> }

        helmUpgrade_ namespace: namespace, set: args

        assertEquals(1, actualCommands.size())
        assertEquals(resultCommand.toString(), actualCommands[0])


    }

}
