package tests.integration;

import org.junit.extensions.cpsuite.ClasspathSuite;
import org.junit.runner.RunWith;

@RunWith(ClasspathSuite.class)
@ClasspathSuite.BaseTypeFilter(StorageTestBase.class)
public class StorageTests
{
}
