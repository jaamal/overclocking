#include <cstdio>

void printX(int index, int n)
{
	for (int i = n - 1; i >= 0; --i)
		printf("%c", (index & (1 << i)) ? 'B' : 'A');
	printf("C");
}

int main()
{
	const int P = 13;
	const int ALL = (1 << P);
	for (int i = 0; i < ALL; ++i)
	{
		printX(i, P);
		printf("%c", i == ALL - 1 ? 'E' : 'D');
	}
	printX(0, P);
	printX(1, P);
	for (int i = 2; i < ALL; ++i)
	{
		printX(i - 2, P);
		printX(i - 1, P);
		printX(i, P);
	}
	return 0;
}