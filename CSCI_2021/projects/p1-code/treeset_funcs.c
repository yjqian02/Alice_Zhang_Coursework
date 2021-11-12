// treeset_funcs.c: Provides a small library of functions that operate
// on binary search trees of unique strings.

#include "treeset.h"


void treeset_init(treeset_t *tree){
// Initialize the given tree to have a null root and have size 0.
    tree->root = NULL;
    tree->size = 0;
}

int treeset_insert(treeset_t *tree, char name[]){
    tsnode_t *new_node = malloc(sizeof(tsnode_t)); // initialize a new node with the name field of name[]
    new_node -> left = NULL;
    new_node -> right = NULL;
    strncpy(new_node->name, name, 128); // setting name field of new_node
    tsnode_t *ptr = tree->root;
    tsnode_t *parent = NULL; // points to the trailer value of 

    if(tree->root == NULL){ // pointer is null so you set the new node = to pointer
        tree->root = new_node;
        tree->size ++;
        return 1;
    }
    while(ptr != NULL){
        parent = ptr;
        // printf("%s\n", parent->name);
        // printf("%s\n", ptr->name);
        if(strcmp(ptr->name, name) == 0){ // duplicate found
            printf("duplicate element ignored\n");
            free(new_node);
            return 0;
        }
        // traverse left side of tree
        if(strcmp(ptr->name, name) > 0){
            ptr = ptr->left;
        }
        // traverse right side of tree
        else{
            ptr = ptr->right;
        }
        
    }
    if(parent == NULL){ // pointer is null so you set the new node = to pointer
        parent = new_node;
    }
    else if(strcmp(parent->name, name) < 0){ // insert to the right
        parent->right = new_node;
    }
    else{
        parent->left = new_node;
    }
    tree->size ++;
    return 1;
}
// Inserts given name into a binary search tree. Uses an ITERATIVE
// (loopy) approach to insertion which starts a pointer at the root of
// the tree and changes its location until the correct insertion point
// is located. Returns 1 if a new tsnode is created and 0 if a duplicate
// name is found in the tree already in which case the name is not
// added.


int treeset_find(treeset_t *tree, char name[]){
    tsnode_t *ptr = tree->root;
    while(ptr != NULL){
        //left side
        if(strcmp(name, ptr->name) < 0){
            ptr = ptr->left;
        }
        //right side
        else if(strcmp(name, ptr->name) > 0){
            ptr = ptr->right;
        }
        else{ // name is present
           return 1; 
        }
    }
    return 0; // name is not present
}
// Determines whether name is present or not in the tree using binary
// search. Returns 1 if name is present and 0 otherwise. Uses an
// ITERATIVE (loopy) approach which starts a pointer at the root of
// the tree and changes it until the search name is found or
// determined to not be in the tree.

void treeset_clear(treeset_t *tree){
    tsnode_remove_all(tree->root); // call to helper function
    tree->size = 0; // resetting size of tree
    tree->root = NULL;
}

// Eliminate all tsnodes in the tree setting its contents empty. Uses
// recursive tsnode_remove_all() function to free memory for all tsnodes.

void tsnode_remove_all(tsnode_t *cur){
    if(cur == NULL){ 
        return;
    }
    // remove down the left side
    tsnode_remove_all(cur->left);
    // remove down the right side
    tsnode_remove_all(cur->right);
    free(cur);
    
}
// Recursive helper function which visits all tsnodes in a tree and
// frees the memory associated with them. This requires a post-order
// traversal: visit left tree, visit right tree, then free the cur
// tsnode.

void treeset_print_revorder(treeset_t *tree){
    tsnode_t *ptr = tree->root;
    tsnode_print_revorder(ptr, 0);
}
// Prints the elements of the tree in reverse order at differing
// levels of indentation which shows all elements and their structure
// in the tree. Visually the tree can be rotated clockwise to see its
// structure.

void tsnode_print_revorder(tsnode_t *cur, int indent){
    if(cur == NULL){ 
        return;
    }
    if(cur->right != NULL){
        tsnode_print_revorder(cur->right, indent+1);
    }
    for(int i=0; i<indent; i++){
        printf("  ");
    }
    printf("%s\n", cur->name);
    if(cur->left != NULL){
        tsnode_print_revorder(cur->left, indent+1);
    }
    
}
// Recursive helper function which prints all elements in the tree
// rooted at cur in reverse order: traverses right subtree, prints cur
// tsnode, traverses left tree. Parameter 'indent' indicates how far to
// indent (2 spaces per indent level).

void treeset_print_preorder(treeset_t *tree){
    tsnode_write_preorder(tree->root, stdout, 0);
}
// Print all the data in the tree in pre-order with indentation
// corresponding to the depth of the tree. Makes use of
// tsnode_write_preorder() for this.

void treeset_save(treeset_t *tree, char *fname){
    tsnode_t *ptr = tree -> root;
    FILE *fp = fopen(fname, "w");
    tsnode_write_preorder(ptr, fp, 0);
    fclose(fp);
}
// Saves the tree by opening the named file, writing the tree to it in
// pre-order with tsnode_write_preorder(), then closing the file.

void tsnode_write_preorder(tsnode_t *cur, FILE *out, int depth){
    tsnode_t *ptr = cur;
    if(ptr == NULL){ 
        return;
    }
    for(int i=0; i<depth; i++){
        fwrite("  ", sizeof(char) * 2, 1, out);
    }
    fwrite(ptr->name, sizeof(char) * 128, 1, out);
    fprintf(out, "\n");
    if(ptr->left != NULL){
        tsnode_write_preorder(ptr->left, out, depth+1);
    }
    if(ptr->right != NULL){
        tsnode_write_preorder(ptr->right, out, depth+1);
    }
}
// Recursive helper function which writes/prints the tree in pre-order
// to the given open file handle. The parameter depth gives how far to
// indent tsnode data, 2 spaces per unit depth. Depth increases by 1 on
// each recursive call. The function prints the cur tsnode data,
// traverses the left tree, then traverses the right tree.

int treeset_load(treeset_t *tree, char *fname ){
    FILE *fp = fopen(fname, "r");
    if(fp == NULL){
        printf("Unable to open stock file '%s', bailing out\n", fname);
        return 0;
    }
    treeset_clear(tree);
    char c[128];
    //printf("%d", feof(fp));
    while(fscanf(fp, "%s", c) != EOF){  
        //printf("c is: %snow\n", c);
        treeset_insert(tree, c);
        // printf("printing tree \n");
        // treeset_print_preorder(tree);  
        //printf("%d", feof(fp));   
    }
    fclose(fp);
    return 1;
}
// Clears the given tree then loads new elements to it from the
// named. Repeated calls to treeset_insert() are used to add strings read
// from the file.  If the tree is stored in pre-order in the file, its
// exact structure will be restored.  Returns 1 if the tree is loaded
// successfully and 0 if opening the named file fails in which case no
// changes should be made to the tree.
