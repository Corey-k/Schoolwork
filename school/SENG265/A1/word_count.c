/***************************************************************
* @author: Corey Koelewyn
* @student_id: V00869081
* @description: this program is designed to handle streams from text files 
* it will read lines of text, compute word frequency,  and print words in 
* a certain format as discribed on the SENG 265 Fall 2017 Assignment 1 handout
**************************************************************/
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <ctype.h>

// folling definitions were taken from the assignment outline, one exception
// is the max word length where we gave it an extra loction for '/0'
#define MAX_LINE_LENGTH 100
#define MAX_WORD_LENGTH 35 
#define MAX_WORD_COUNT 750
#define MAX_FILE_SIZE 5000
//these are self set definitions
#define MAX_PRINTED_STRING_LENGTH 100
#define TEST printf("\nTEST\n");
//words struct, keep track of which words appear, and how many times they appear
typedef struct word_node_t{
	int size; // size
	int count;// how many times the word has occured
	char word[MAX_WORD_LENGTH];
} word_node;

typedef struct sum_count_node_t{
	int count;
	int size;
}sum_count_node;


// remove punctuation from words function, input is a pointer to a word,
// check first & last char of a word and replace it with '\0' if its punctuation
// removes the newline char where it exists
void remove_punct(char *rmv_punct){
	int j = 1;
	int i;
	int length = strlen(rmv_punct);
	if(ispunct(rmv_punct[0])){ 
		for(i = 0; i < length; i++){
		rmv_punct[i] = rmv_punct[i+1];
		}
		length--;
	}
	if(rmv_punct[length-j] == '\n'){
		rmv_punct[length-j] = '\0';
		j++;
	}
	if(ispunct(rmv_punct[length-j])){
		rmv_punct[length-j] = '\0';
		remove_punct(rmv_punct);
	}
	//printf("%s\n", rmv_punct);
	return;	
}//end of remove_punct definiton


// for qsort, modified from 
// https://stackoverflow.com/questions/6105513/need-help-using-qsort-with-an-array-of-structs
// accessed oct 14, 2017
int compare (const void *a, const void *b)
{
	sum_count_node *nodeA = (sum_count_node *)a;
	sum_count_node *nodeB = (sum_count_node *)b;
	return(nodeB->count - nodeA->count);
}
//modified the 
int compare_string( const void *a, const void *b )
{	
	const char *ret_a = (const char *)a;
	const char *ret_b = (const char *)b;
	return strcmp(ret_a, ret_b);
}

void str_tolower(char *string){
	//for( ; *string; *string++) *string = tolower(*string);
	//the above line works great, but i can't use it because it throws an error
	int i;
	int length = strlen(string);
	for(i = 0; i < length; i++){
		string[i] = tolower(string[i]);
	}
}

int main(int argc, char *argv[]){
	word_node word_array[MAX_WORD_COUNT];
	int index = 0; //for the word_array datastructure
	char line_from_file[MAX_LINE_LENGTH];
	char *token; //words pulled from tokenizer
	int flags = 0; //flags used for run commands
	int i, j; //counter used for loops
	int length;
	char input_file[MAX_WORD_LENGTH];
	int longest_word = 0; //tracks the longest word, to improve effeciency
	
	//Check command line inputs quit if invalid
	if(argc < 2){
		printf("You must provide a filename!\n");
		exit(1);
	}

	//set program based flags for --sort and --print-words set as "octal"
	// 001 infile passed = 1 		check with &0b001
	// 010 sorted		 = 2 		check with &0b010
	// 100 with word display = 4	check with &0b0100
	for(i = 1; i < argc; i ++){
		if(strcmp(argv[i], "--infile") == 0){
			strcpy(input_file, argv[i+1]);
			flags += 1;
//			printf("infile flag set\n");
		}
		if(strcmp(argv[i], "--sort") == 0){
			flags += 2;
//			printf("sort flag set\n");
		}
		if(strcmp(argv[i], "--print-words")==0){
			flags += 4;
//			printf("print-words flag set\n");
		}
	}
//	printf("%d\n", flags);
	if (!(flags&0b0001)) {
		printf("infile flag not set, exiting\n");
		exit(1);
	}
	//open file, quit if unsuccessful
	
	FILE *data_file = fopen(input_file, "r");
	if(data_file == NULL){
		printf("unable to open %s, exiting\n" , input_file);
		if(strcmp(input_file, "--sort") || strcmp(input_file, "--print-words")){
			printf("(make sure your flags are in the right order)\n");
		}
		exit(1);
	}
	if(sizeof(data_file) > MAX_FILE_SIZE){
		printf("File too large. Program terminated.\n");
		exit(1);
	}
//	printf("%s was opened successfully\n", input_file);
	
		
	//loop for reading file line by line
		//tokenize the line
		//strip punctuation from word
		//set whole word to lowercase
		//check to see if word is in word struct
		//push word into word struct if it is not in, increment counter if it isalnum
	while(fgets(line_from_file, MAX_LINE_LENGTH, data_file)){
		remove_punct(line_from_file);
 
//		printf("%s\n", line_from_file);
		token = strtok(line_from_file, " ");
		while(token != NULL){
			remove_punct(token);
			str_tolower(token);
			length = strlen(token);
			for(i = 0; i <= index; i++){
			//		printf("comparing %s and %s\n", word_array[i].word,token);
				if(strcmp(word_array[i].word, token) == 0){
//					printf("%s = %s\n",word_array[i].word, token);
					word_array[i].count++;
					break;
				}
			if(i == index){
				strncpy(word_array[index].word, token,length);
				word_array[index].size =  length;
				if(length > longest_word) longest_word = length;
				word_array[index].count++;
				index++;
				break;
			}
			}
		token = strtok(NULL, " ");
		}//end of tokenizer loop
	}//end of file line grabbing loop
	fclose(data_file); //close when done
				

	//i feel really good about my code until this point.... 
		int sum_count[MAX_WORD_LENGTH];
	for(i = 0; i <= index; i++){
		sum_count[word_array[i].size] += word_array[i].count;
	}

	
	if(flags == 0b0001){//makes sure that sort/word print not set
		for(i = 0;i <= longest_word; i++){
			if(sum_count[i] != 0){
				printf("Count[%02d]=%02d;\n",i, sum_count[i]);
			}
		}
		exit(0);
	}
	
	//seeing as the last 2 print options both require sort, we will do that 
	//for both cases
	
	sum_count_node sum_pt2[MAX_WORD_LENGTH];
	
	for(i = 0; i <= longest_word; i++){
		sum_pt2[i].size = i;
		sum_pt2[i].count = sum_count[i];
	}
	qsort(sum_pt2, MAX_WORD_LENGTH, sizeof(sum_count_node), compare);
	
	if(flags == 0b0011) {// sort but not word print	
		for(i = 0;i <= longest_word; i++){
			if(sum_pt2[i].count != 0){
				printf("Count[%02d]=%02d;\n",sum_pt2[i].size,sum_pt2[i].count);
			}
		}
		exit(0);
	}

	//sorting strings alphabetically by length then printing 
	for(i = 0; i<= longest_word; i++){
	int str_index = 0;
	char strings[MAX_WORD_COUNT][MAX_WORD_LENGTH];
		for(j = 0; j <= index; j++){
			if(word_array[j].size == sum_pt2[i].size){
				strcpy(strings[str_index], word_array[j].word);
				str_index++;
			}
		}
		//sorts the strings
		qsort(strings, str_index, sizeof(*strings), compare_string);
		//prints the correct output for all three flags
		if(flags != 0b0111) exit(1);
		if(sum_pt2[i].count != 0){
			printf("Count[%02d]=%02d; (words: ",sum_pt2[i].size,sum_pt2[i].count);
			printf("\"%s\"", strings[0]);
			for(j = 1; j < str_index-1 ; j++){
				printf(", \"%s\"", strings[j]);
			}
			if(str_index > 1){
			printf(" and \"%s\"", strings[str_index-1]);
			}
			printf(")\n");
		}
	}
}//			fin
	

