/***************************************************************
* @author: Corey Koelewyn
* @student_id: V00869081
* @description: this program is designed to handle streams from text files
* it will read lines of text, compute word frequency,  and print words in
* a certain format as discribed on the SENG 265 Fall 2017 Assignment 2 handout
* NOTE: I Checked with Dana and she said we could assume that the file
* would fit into memory.
**************************************************************/
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <ctype.h>

#define START_SIZE 5//starting size of string, which we will adjust with realloc
#define TEST printf("TEST\n");
//Struct for tracking frequency and word count
typedef struct freq_node {
	int count;
	int size;
	struct freq_node * next;
} freq_node_t;

//struct containing word
typedef struct word_node {
	char *word;
	struct word_node *next;
} word_node_t;

//checks for proper allocation of memory, adapted from seng 265 slides
void *emalloc(size_t n){
	void *p;
	p = malloc(n);
	if (p == NULL) {
		fprintf(stderr, "malloc of %u bytes failed", (int)n);
		exit(1);
	}
	return p;
}
/* function for setting the flags from commandline chars
   input: 	takes the arguements and thier count, as determined at runtime
   output: 	Returns an octal representation of which flags are set and
			and points filename at the name of the input file
*/
int set_flags(int argc, char **argv){ //COMPLETE
	int i;
	int flags = 0;
	if(argc < 2){
		printf("You must provide the --inflag and a filename!\n");
		exit(1);
	}
	//taken from my assignment 1
	//set program based flags for --sort and --print-words set as "octal"
	// 001 infile passed = 1 		check with &0b001
	// 010 sorted		 = 2 		check with &0b010
	// 100 with word display = 4	check with &0b0100
	for(i = 1; i < argc; i ++){
		if(strcmp(*(argv+i), "--infile") == 0){
			flags += 1;
		}
		if(strcmp(*(argv+i), "--sort") == 0){
			flags += 2;
//			printf("sort flag set\n");
		}
		if(strcmp(*(argv+i), "--print-words")==0){
			flags += 4;
//			printf("print-words flag set\n");
		}
	}
	return flags;

}

/**** COULDN'T GET PART C TO WORK WITH DYNAMIC MEMORY
word_node_t *new_word_node(char *new_word){
	word_node_t *new_node = (word_node_t*)emalloc(sizeof(word_node_t));
	new_node->word = new_word;
	return new_node;
}
*/
//function for inserting a node into a sorted linked list of words.
//requires the word passed in, and the headof the word linked list
/*
word_node_t *word_Insert(word_node_t *head, word_node_t* new_node)
{
	if((head) != NULL) printf("old list head %s\n",(head)->word);
	new_node->next = (head);
	(head) = new_node;
	return head;
}
*/

freq_node_t *new_freq(int length){
	freq_node_t *new_node;
	new_node = (freq_node_t*)emalloc(sizeof(freq_node_t));
	new_node->count = 1;
	new_node->size = length;
	new_node->next = NULL;
	return new_node;
}

/* function 	: freq_insert
 * description	: take the head of a linked list and the length of a word, and
 *				  either inserts the length into the list, or if it already
 *				  exists, it increases the count for times it has been seen
 */
void *freq_insert(freq_node_t *head, int length){
	freq_node_t *newNode = new_freq(length);
	if(head ==NULL){
		head = newNode;
		return head;
	}
	if(head->size == length){
		head->count++;
		free(newNode);
		return head;
	}
	freq_node_t *curr;
	for(curr = head; curr->next != NULL; curr = curr->next){
		if(curr->next->size == length){
			curr->next->count++;
			free(newNode);
			return head;
		}
	}
	curr->next = newNode;
	return head;
}


void print_size(freq_node_t *head, int biggest){
	freq_node_t *curr = head;
	int i;
	for(i = 0; i <= biggest; i++){
		for(curr = head;curr != NULL; curr = curr->next){
			if(curr->size == i){
				if(!(curr->size == 0)){
					printf("Count[%02d]=%02d;\n", curr->size, curr->count);
				}
			}
		}
	}
}

void link_list_free(freq_node_t *head){
	freq_node_t *tmp;

	while(head != NULL){
		tmp = head;
		head = head->next;
		free(tmp);
	}


}

void print_unsorted_freq_list(freq_node_t *head, int biggest){
	freq_node_t *curr = head;
	int highest_count = 0;
	for(curr = head;curr != NULL; curr = curr->next){
		if(curr->count > highest_count){
			highest_count = curr->count;
		}
	}
	for(; highest_count > 0; highest_count--){
		for(curr = head;curr != NULL; curr = curr->next){
			if(curr->count == highest_count){
				if(!(curr->size == 0)){
					printf("Count[%02d]=%02d;\n", curr->size, curr->count);
				}
			}
		}
	}
}

int main(int argc, char *argv[]){
	int i,CURR_MAX,length;
	int biggest = 0;
	char ch;
	ch = '!';
	char *file_name; // pointer for the eventual place on heap for filename
	int flags = set_flags(argc, argv);
	freq_node_t  *freq_head = NULL;
	if(!(flags&0b001)){
		printf("--infile flag must be set!!\nExiting!\n");
		return(1);
	}
	for(i = 1; i < argc; i ++){ //grabs filename
		if(strcmp(*(argv+i), "--infile") == 0){
			file_name = (char*)emalloc(sizeof(argv+(i+1)));
			strcpy(file_name, (*(argv+i+1)));
		}
	}
	FILE *input_file = fopen(file_name, "r");
	if(input_file == NULL){
		printf("unable to open %s, exiting\n" , file_name);
		if(strcmp(file_name, "--sort") || strcmp(file_name, "--print-words")){
			printf("(make sure your flags are in the right order)\n");
		}
		exit(1);
	}
	while(ch != EOF){ //done processing file when EOF gets reached
		CURR_MAX = 4096; //Memory is cheap!
		char *buffer = 	(char*) emalloc(sizeof(char)*CURR_MAX);
		length = 0;

		while(1){//loop for grabbing words, putting it into a string
			if(length == CURR_MAX){
				CURR_MAX *= 2;
				buffer = (char*)realloc(buffer, CURR_MAX);
			}
				ch = getc(input_file);
				ch = tolower(ch);
				if(ch == EOF  || ch == '\n') break;
				if(!ispunct(ch) && ch != ' ' && ch != '\0'){
					*(buffer + length) = ch;
					length++;
				}
			if(ch == ' ') break;
		}
		*(buffer+length) = '\0';
		//Everything past here is spaghetti :(
		if(!(length = strlen(buffer))){
			free(buffer);
			continue;
		}
		freq_head = freq_insert(freq_head, length);
		if(strlen(buffer)>biggest) biggest = strlen(buffer);
		free(buffer);

	}

if(flags&2)	print_unsorted_freq_list(freq_head, biggest);
if(!(flags&2)) print_size(freq_head, biggest);

	if(flags&4){
		printf("--word-count not implemented");
	}




	link_list_free(freq_head);
	fclose(input_file);
	free(file_name);

	return 0;
}
