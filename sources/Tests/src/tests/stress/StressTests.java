package tests.stress;

import org.junit.extensions.cpsuite.ClasspathSuite;
import org.junit.runner.RunWith;

@RunWith(ClasspathSuite.class)
@ClasspathSuite.BaseTypeFilter(StressTestBase.class)
public class StressTests
{
}
