#include <cstdio>
#include "rand.h"

int main()
{
	z_srand(42);
	for (int i = 0; i < 466888; ++i)
		printf("%c", rand31() % 5 + 'A');
	return 0;
}