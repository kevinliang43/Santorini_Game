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
        RefereeTest.class, testRuleChecker.class, testWorker.class, ObserverTest.class, BoardTest.class,
        ConfigReaderTest.class, TranslatorTest.class, BreakerStrategyTest.class, InfiniteStrategyTest.class,
        RemoteStrategyTest.class, ProxyPlayerTest.class})

public class MainTest {


}

