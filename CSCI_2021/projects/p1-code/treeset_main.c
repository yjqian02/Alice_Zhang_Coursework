// treeset_main.c: takes in user input commands and calls corresponding treeset functions


#include "treeset.h"

int main(int argc, char *argv[]){
    int echo = 0; // controls echoing, 0: echo off, 1: echo on
    if(argc > 1 && strcmp("-echo",argv[1])==0) {
        echo = 1;
    }
    
    //print instructions for commands
    printf("Treeset Main\n");
    printf("Commands:\n");
    printf("  print:          shows contents of the tree in reverse sorted order\n");
    printf("  size:           prints the number of nodes currently in the tree\n");
    printf("  clear:          eliminates all elements from the tree\n");
    printf("  quit:           exit the program\n");
    printf("  add <name>:     inserts the given string into the tree, duplicates ignored\n");
    printf("  find <name>:    prints FOUND if the name is in the tree, NOT FOUND otherwise\n");
    printf("  preorder:       prints contents of the tree in pre-order which is how it will be saved\n");
    printf("  save <file>:    writes the contents of the tree in pre-order to the given file\n");
    printf("  load <file>:    clears the current tree and loads the one in the given file\n");

    // Create a tree variable, likely on the stack as a local variable in main()
    treeset_t tree;
    treeset_init(&tree);
    
    char cmd[128];
    int success;
    // Start a loop that terminates when the user exits or there is no more input
    printf("TS#> "); // print a prompt
    while(1){
        success = fscanf(stdin,"%s", cmd); // read a command
        if(success == EOF){ //check for end of input
            printf("\n"); // found end of input
            break; // break from loop
        }
        if(strcmp("quit", cmd) == 0){ // quit command
            if(echo){
                printf("quit\n");
            }
            break; // break from loop
        }
        else if(strcmp("print", cmd) == 0){ //print command
            if(echo){
                printf("print\n");
            }
            treeset_print_revorder(&tree);
        }
        else if(strcmp("size", cmd) == 0){ // size of tree
            if(echo){
                printf("size\n");
            }
            printf("%d nodes\n", tree.size); // print size of tree
        }
        else if(strcmp("clear", cmd) == 0){
            if(echo){
                printf("clear\n");
            }
            treeset_clear(&tree);
        }
        else if(strcmp("add", cmd) == 0){ // add command
            fscanf(stdin, "%s", cmd); // read name to add
            if(echo){
                printf("add %s\n", cmd);
            }
            treeset_insert(&tree, cmd);
        }
        else if(strcmp("find", cmd) == 0){
            fscanf(stdin, "%s", cmd); // read name to find
            if(echo){
                printf("find %s\n", cmd);
            }
            if(treeset_find(&tree, cmd) == 1){
                printf("FOUND\n");
            }
            else{
                printf("NOT FOUND\n");
            }

        }
        else if(strcmp("preorder", cmd) == 0){
            if(echo){
                printf("preorder\n");
            }
            treeset_print_preorder(&tree);
        }
        else if(strcmp("save", cmd) == 0){
            fscanf(stdin, "%s", cmd); // read in file name
            if(echo){
                printf("save %s\n", cmd);
            }
            treeset_save(&tree, cmd);
        }
        else if(strcmp("load", cmd) == 0){
            fscanf(stdin, "%s", cmd); // read in file name
            if(echo){
                printf("load %s\n", cmd);
            }
            treeset_load(&tree, cmd);
        }
        else{
            printf("%s not valid input", cmd);
        }
        printf("TS#> "); // print a prompt
    } // end main while loop
// Each time the user enters a string, read it and check for one of the built-in commands
// On identifying the command, potentially read another string if needed (commands like add and find)
// Call an appropriate treeset_XXX() function to handle the command
    treeset_clear(&tree);
    return 0;

}