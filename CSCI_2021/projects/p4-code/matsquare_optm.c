// optimized versions of matrix diagonal summing
#include "matvec.h"

int matsquare_VER1(matrix_t mat, matrix_t matsq) { // optimize cache access in memory, score 25/35
  for(int i=0; i<mat.rows; i++){
    for(int j=0; j<mat.cols;j++){
      MSET(matsq,i,j,0);                          // initialize to 0s
    }
    for(int j=0; j<mat.cols; j++){
      for(int k=0; k<mat.rows; k++){
        int mij = MGET(mat, i, j);         
        int mjk = MGET(mat, j, k);    // increment across current rows of matsq
        int cur = MGET(matsq, i, k);  // add across row
        int new = cur + mij*mjk;
        MSET(matsq, i, k, new);      // set across row
      }
    }
  }
  return 0;
}


int matsquare_VER2(matrix_t mat, matrix_t matsq) { // final version score 35/35
  for(int i=0; i<mat.rows; i++){
    // this for loop isn't a big issue in terms of speed 
    // (why? because initializing to zero is necessary in this case 
    // because of how the matrix is initialized in main, so everytime we set to zero is necessary. 
    // Plus, the time it takes to do this pales in comparison to all the arithmetic operations done later)
    for(int j=0; j<mat.cols;j++){      // initialize to 0s           
      MSET(matsq,i,j,0);                          
    }
  }

  for(int i=0; i<mat.rows; i++){
    int sum0 = 0;                     // set 4 variables to store sums to reduce loop iterations and 
    int sum1 = 0;                     // allow for parallel execution of arithmetic with multiple ALUs
    int sum2 = 0;
    int sum3 = 0;
    for(int j=0; j<mat.cols; j++){    // use loop unrolling
      
      int k = 0;
      int base = MGET(mat, i, j);     // store the base index value in a variable so you can access it quickly from cache
      for(k=0; k<mat.rows-3; k+=3){
        sum1 = MGET(matsq, i, k);     // grab existing sums to add off of from matsq
        sum2 = MGET(matsq, i, k + 1);
        sum3 = MGET(matsq, i, k + 2);

        sum1 += base * MGET(mat, j, k); 
        sum2 += base * MGET(mat, j, k + 1);
        sum3 += base * MGET(mat, j, k + 2);

        MSET(matsq, i, k, sum1);
        MSET(matsq, i, k + 1, sum2);
        MSET(matsq, i, k + 2, sum3);

      }
      
      for(; k<mat.rows; k++){       // cleanup any sums you missed
        sum0 = MGET(matsq, i, k);
        sum0 += base * MGET(mat, j, k);
        MSET(matsq, i, k, sum0);
      }
      
    }
    
  }
  return 0;
}

int matsquare_OPTM(matrix_t mat, matrix_t matsq) {
  if(mat.rows != mat.cols ||                       // must be a square matrix to square it
     mat.rows != matsq.rows || mat.cols != matsq.cols)
  {
    printf("matsquare_OPTM: dimension mismatch\n");
    return 1;
  }


  // Call to some version of optimized code
  return matsquare_VER2(mat, matsq);
}

