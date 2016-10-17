CC=gcc
CFLAGS=-I.

hellomake: hellomake.o hellofunc.o
	$(CC) -o hellomake hellomake.o hellofunc.o -I.
clean:
	rm hellofunc.o
	rm hellomake
	rm hellomake.o
