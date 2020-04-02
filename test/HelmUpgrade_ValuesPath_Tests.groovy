import TestData.HelmUpgradeTestData
import Utils.Helper
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

class HelmUpgrade_ValuesPath_Tests extends GroovyTestCase {

    def helmUpgrade_ = new helmUpgrade()

    @Before
    void setUp(){
        Helper.setEnvVariables(HelmUpgradeTestData.commonVariables(), helmUpgrade_)
        InjectVars.injectTo(helmUpgrade_, 'imageName')
    }

    @Test
    void test_HelmUpgradeMap_ValuesPathNull_CustomValuesWithNamespace(){

        def actualCommands = []
        helmUpgrade_.sh = {command ->
            actualCommands << command
        }
        helmUpgrade_.echo = {String msg -> }

        helmUpgrade_ namespace: 'any', valuesPath: null

        assertEquals(1, actualCommands.size())
        assertEquals('helm upgrade -f "chart/values-any.yaml" --install --force --wait --namespace "any" "FAKE_Job_Name-any" chart/', actualCommands[0])

    }

    @Test
    void test_HelmUpgradeMap_ValuesPathEmpty_NoCustomValues(){

        def actualCommands = []
        helmUpgrade_.sh = {command ->
            actualCommands << command
        }
        helmUpgrade_.echo = {String msg -> }

        helmUpgrade_ namespace: 'any', valuesPath: ''

        assertEquals(1, actualCommands.size())
        assertEquals('helm upgrade --install --force --wait --namespace "any" "FAKE_Job_Name-any" chart/', actualCommands[0])

    }

    @Test
    void test_HelmUpgradeMap_ValuesPathWhitespace_NoCustomValues(){

        def actualCommands = []
        helmUpgrade_.sh = {command ->
            actualCommands << command
        }
        helmUpgrade_.echo = {String msg -> }

        helmUpgrade_ namespace: 'any', valuesPath: ' '

        assertEquals(1, actualCommands.size())
        assertEquals('helm upgrade --install --force --wait --namespace "any" "FAKE_Job_Name-any" chart/', actualCommands[0])

    }

    @Test
    void test_HelmUpgradeMap_ValuesPathIsSpecified_CustomValues(){

        def actualCommands = []
        helmUpgrade_.sh = {command ->
            actualCommands << command
        }
        helmUpgrade_.echo = {String msg -> }

        helmUpgrade_ namespace: 'any', valuesPath: './some-values.yaml'

        assertEquals('helm upgrade -f "./some-values.yaml" --install --force --wait --namespace "any" "FAKE_Job_Name-any" chart/', actualCommands[0])

    }

}
