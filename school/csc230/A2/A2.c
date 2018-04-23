/*
 *	Name		: Corey Koelewyn
 *	Student ID	: V00869081
 *	Course		: CSC 230 Fall 2017
 *	Description	: This assignment is designed for learning basic C skills.
 *	it requires the user to enter a number, and it returns the binary form of 
 *	that number's factorial.
 *	Last Modified: 9/19/2017
 */


#include <stdio.h>

#define SIZE_INT 16 // as per assignment outlines


/* 	this function gets passed the already created array of pseudo bits and 
 *	prints it out.
 */
void printBitArray(unsigned char theBits[SIZE_INT]){
	int i;
	printf("0b");
	for(i = SIZE_INT-1; i >= 0; i--){
		printf("%d" , theBits[i]);
	}
	return;
}

/* Factorial returns the factorial of a number which was passed into it
 * using recursion
 */
unsigned short factorial(unsigned short num){
	if(num == 1){ // base case of 1
		return 1;
	}
	num = num * factorial(num - 1); //calls itself recursively
	return num;
}

/* initializes and reinitializes the array so we do not have any data left from 
 * before	
 */
void arrayInitialize(unsigned char arr[SIZE_INT]){
	for(int i = 0; i < SIZE_INT; i++){
		arr[i] = 0;
	}
	return;
}
	
void toBits(unsigned short value, unsigned char inBits[SIZE_INT]){
	unsigned short factorialValue;
	factorialValue = factorial(value);
	printf("\n %d\tFactorial = %d or ",value, factorialValue);
	for(int j = 0; j < SIZE_INT; j++){
		inBits[j] = factorialValue & 1;
		factorialValue = factorialValue  >> 1;
	}	
}

int main(){
	unsigned short x;
	unsigned char theArray[SIZE_INT];
	arrayInitialize(theArray);
	char repeat = 'y';
	printf("FACTORIAL AND BIT TESTER\n");
	while(repeat == 'y' || repeat == 'Y'){
		printf("Input a positive integer -->");
		scanf("%hu", &x);
		toBits(x, theArray);
		printBitArray(theArray);
		printf("\n Do another (y/n)? ");
		scanf(" %c", &repeat);
		arrayInitialize(theArray);
		
	}
	return 0;
}