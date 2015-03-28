package tests.unit;

import org.junit.extensions.cpsuite.ClasspathSuite;
import org.junit.runner.RunWith;

@RunWith(ClasspathSuite.class)
@ClasspathSuite.BaseTypeFilter(UnitTestBase.class)
public class UnitTests
{
}
