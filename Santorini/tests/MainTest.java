import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Main Testing Class.
 * Runs all Tests
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({BoardStatusTest.class, BoardStatusTest.class, PlayerTest.class, StrategyTest.class,
        RefereeTest.class, testRuleChecker.class, testWorker.class})

public class MainTest {


}

