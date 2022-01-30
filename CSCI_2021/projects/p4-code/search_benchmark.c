// Complete this main to benchmark the search algorithms outlined in
// the project specification

#include <string.h>
#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#include "search.h"

int main(int argc, char *argv[]){ // here argc is the number of arguments that follow the program call in the command line
  
  if (argc < 4) { // error case for if there are no args/not enough
    printf("NOT ENOUGH INPUT. INPUT SHOULD LOOK LIKE BELOW: \n");
    printf("./search_benchmark <minpow> <maxpow> <repeats> [which]\n");
    printf("which is a combination of:\n");
    printf("a : Linear Array Search\n");
    printf("l : Linked List Search\n");
    printf("b : Binary Array Search\n");
    printf("t : Binary Tree Search\n");
    printf("(default all)\n");
    return 1;
  }
  
  int min_size = atoi(argv[1]); // using atoi() function to convert command-line input strings to type int
  int max_size = atoi(argv[2]);
  int repetitions = atoi(argv[3]);
  char* algs_to_run;

  int do_linear_arr = 0;
  int do_linear_linkedlist = 0;
  int do_bs_binary_arr = 0;
  int do_binary_tree = 0;

  clock_t begin, end;

  if(argc == 4){ // run all search algs if not specified
    do_linear_arr = 1;
    do_linear_linkedlist = 1;
    do_bs_binary_arr = 1;
    do_binary_tree = 1;

    printf("LENGTH\tSEARCHES\t");
    printf("array"); // print labels
    printf("\t\t list");
    printf("\t\t binary");
    printf("\t\t tree");

  } // end if case
  else{
    algs_to_run = argv[4];
    printf("LENGTH\tSEARCHES\t");

    for(int i=0; i < strlen(algs_to_run); i++){ // determining selected algorithms
      if(algs_to_run[i] == 'a'){
        do_linear_arr = 1;
        printf("array");
      }
      else if(algs_to_run[i] == 'l'){
        do_linear_linkedlist = 1;
        printf("\t\t list");
      }
      else if(algs_to_run[i] == 'b'){
        do_bs_binary_arr = 1;
        printf("\t\t binary");
      }
      else if(algs_to_run[i] == 't'){
        do_binary_tree = 1;
        printf("\t\t tree");
      }
      else{
        printf("ERROR: THIS ISN'T A AN EXISTING SEARCH ALG TYPE\n");
      }
    } // end for loop

  } // end else case

  printf("\n"); // newline for formatting


  for(int m = min_size; m <= max_size; m++){ // conducting search algs
    int len = 1 << m; // m ^ 2 = 1 << m (if you left shift by 1, you're squaring the number)
    printf("%6d    %6d\t", len, len * repetitions * 2);

    if(do_linear_arr){
      int* linear_arr = make_evens_array(len);
      begin = clock();

      for(int i=0; i < repetitions; i++){
        for(int j=0; j < len; j++){ // need this loop to get search term, j
          linear_array_search(linear_arr, len,j);
        } // j loop
      } // i loop

      end = clock();

      double linear_arr_time = ((double) (end - begin));
      printf("%.4e", linear_arr_time);
      free(linear_arr); // cleanup array

    } // linear arr case

    if(do_linear_linkedlist){
      list_t* linkedlist = make_evens_list(len);
      begin = clock();

      for(int i=0; i < repetitions; i++){
        for(int j=0; j < len; j++){
          linkedlist_search(linkedlist, len,j);
        } // j loop
      } // i loop

      end = clock();

      double linear_linkedlist_time = ((double) (end - begin));
      printf("\t %.4e", linear_linkedlist_time);
      list_free(linkedlist); // cleanup linkekdlist

    } // linear linkedlist case

     if(do_bs_binary_arr){
      int* binary_arr = make_evens_array(len);
      begin = clock();

      for(int i=0; i < repetitions; i++){
        for(int j=0; j < len; j++){
          binary_array_search(binary_arr, len, j);
        } // j loop
      } // i loop

      end = clock();

      double binary_arr_time = ((double) (end - begin));
      printf("\t %.4e", binary_arr_time);
      free(binary_arr); // cleanup array

     } // binary arr case

    if(do_binary_tree){
      bst_t* bst = make_evens_tree(len);
      begin = clock();

      for(int i=0; i < repetitions; i++){
        for(int j=0; j < len; j++){
          binary_tree_search(bst, 0, j);
        } // j loop
      } // i loop

      end = clock();

      double binary_tree_time = ((double) (end - begin));
      printf("\t %.4e", binary_tree_time);
      bst_free(bst); // clean up tree
    } // binary tree case
    
    printf("\n");

  } // end executing searches for loop

  return 0;
}
