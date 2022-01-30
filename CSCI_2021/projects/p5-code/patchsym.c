// Template to complete the patchsym program which can retrive global
// symbol values or change them. Sections that start with a CAPITAL in
// their comments require additions and modifications to complete the
// program.

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <unistd.h>
#include <sys/mman.h>
#include <elf.h>

// macro to add a byte offset to a pointer, arguments are a pointer
// and a # of bytes (usually size_t)
#define PTR_PLUS_BYTES(ptr,off) ((void *) (((size_t) (ptr)) + ((size_t) (off))))

int DEBUG = 0;                  // controls whether to print debug messages

#define GET_MODE 1              // only get the value of a symbol
#define SET_MODE 2              // change the value of a symbol

int main(int argc, char **argv){
  // PROVIDED: command line handling of debug option
  if( argc > 1 && strcmp(argv[1], "-d")==0 ){
    DEBUG = 1;                  // check 1st arg for -d debug
    argv++;                     // shift args forward if found
    argc--;
  }

  if(argc < 4){
    printf("usage: %s [-d] <file> <symbol> <type> [newval]\n",argv[0]);
    return 0;
  }

  int mode = GET_MODE;          // default to GET_MODE
  char *new_val = NULL;
  if(argc == 5){                // if an additional arg is provided run in SET_MODE
    printf("SET mode\n");
    mode = SET_MODE;
    new_val = argv[4];
  }
  else{
    printf("GET mode\n");
  }
  char *objfile_name = argv[1];
  char *symbol_name = argv[2];
  char *symbol_kind = argv[3];
                        
  // PROVIDED: open file to get file descriptor
  int fd = open(objfile_name, O_RDWR); 

  // DETERMINE size of file and create read/write memory map of the file

  struct stat stat_buf;
  fstat(fd, &stat_buf);                                    // get stats on the open file such as size
  int size = stat_buf.st_size;                             // size for mmap()'ed memory is size of file
  char *file_bytes = mmap(NULL, size, PROT_READ | PROT_WRITE,
  MAP_SHARED, fd, 0);                                      // call mmap with given size and file descriptor read only, potentially share, offset==0

  // CREATE A POINTER to the intial bytes of the file which are an ELF64_Ehdr struct
  Elf64_Ehdr *ehdr = (Elf64_Ehdr *) file_bytes;

  // CHECK e_ident field's bytes 0 to for for the sequence {0x7f,'E','L','F'}.
  // Exit the program with code 1 if the bytes do not match

  int e_ident_matches =                                   // check the first bytes to ensure correct file format
    ehdr->e_ident[0] == 0x7F &&
    ehdr->e_ident[1] == 'E'  &&
    ehdr->e_ident[2] == 'L'  &&
    ehdr->e_ident[3] == 'F';

  if(!e_ident_matches){
    printf("ERROR: Magic bytes wrong, this is not an ELF file\n");
    exit(1);
  }

  // PROVIDED: check for a 64-bit file
  if(ehdr->e_ident[EI_CLASS] != ELFCLASS64){
    printf("ERROR: Not a 64-bit file ELF file\n");
    // UNMAP, CLOSE FD
    munmap(file_bytes, size);
    close(fd);
    return 1;
  }

  // PROVIDED: check for x86-64 architecture
  if(ehdr->e_machine != EM_X86_64){
    printf("ERROR: Not an x86-64 file\n");
    // UNMAP, CLOSE FD
    munmap(file_bytes, size);
    close(fd);
    return 1;
  }

  // DETERMINE THE OFFSET of the Section Header Array (e_shoff), the
  // number of sections (e_shnum), and the index of the Section Header
  // String table (e_shstrndx). These fields are from the ELF File
  // Header.
  
  //e_shoff points to header of table
  int section_header_offset = ehdr->e_shoff; 

  //e_shnum holds length of table
  int table_len = ehdr->e_shnum;

  //e_shstrndx holds the index of the table entry that has the section names
  int index_section_names = ehdr->e_shstrndx;

  // SET UP a pointer to the array of section headers. Use the section
  // header string table index to find its byte position in the file
  // and set up a pointer to it.
  Elf64_Shdr *section_headers = (Elf64_Shdr *) &file_bytes[section_header_offset];

  // index file_bytes to the location of the start of the section names
  char *section_names = (char *) &file_bytes[section_headers[index_section_names].sh_offset];
  // potentially introduce new variables.

  // SEARCH the Section Header Array for sections with names .symtab
  // (symbol table) .strtab (string table), and .data (initialized
  // data sections).  Note their positions in the file (sh_offset
  // field).  Also note the size in bytes (sh_size) and and the size
  // of each entry (sh_entsize) for .symtab so its number of entries
  // can be computed. Finally, note the .data section's index (i value
  // in loop) and its load address (sh_addr).

  uint64_t symbol_table_offset = 0; // you can also use unsigned long here (using long or long int will work, but is incorrect because you aren't declaring that it's unsigned, it's just interpreted as unsigned)
  uint64_t string_table_offset = 0;
  uint64_t data_section_offset = 0;

  uint64_t symbol_table_size;
  uint64_t symbol_table_entry_size;
  int symbol_table_index;

  // a better explanation of long int and the types we use here from the TAs:
    // A long int is actually the exact same thing as a long (in the same way that a short int is the the same thing as a short). 
    // If a long int is being used, you can just think of it as being a long.

    // A long/long int is defined by the C standard to be at least 4 bytes, BUT in practice on Unix-like systems it tends to be 
    // 8 bytes long, so we just consider it to be 8 bytes (just like how the C standard says that an int should be at least 2 bytes, 
    // but it's almost always 4 bytes in practice).

    // The reason why we use longs for offsets in project 5 is because they are all specified in the ELF manual to have size Elf64_Off
    // on a 64-bit system. Elf64_Off is just a uint64_t, which means it's 64 bits. This means you'll want to make your offset 
    // variables type long, or type uint64_t, or Elf64_Off. All of these are equivalent on a 64-bit Linux system. 
    // Just make sure you don't make your offset variables ints, or you'll be losing data.

  int data_index;
  uint64_t data_load_address = 0;
  
  for(int i=0; i<table_len; i++){
    //search for symbol table match
    //the addition here moves the pointer to the location of the specific section name string
    if(strcmp(section_names + section_headers[i].sh_name, ".symtab") == 0){
      symbol_table_offset = section_headers[i].sh_offset;
      symbol_table_size = section_headers[i].sh_size;
      symbol_table_entry_size = section_headers[i].sh_entsize;
      symbol_table_index = i;
    }
    //search for string table match
    else if(strcmp(section_names + section_headers[i].sh_name, ".strtab") == 0){
      string_table_offset = section_headers[i].sh_offset;
    }
    //search for data section match
    else if(strcmp(section_names + section_headers[i].sh_name, ".data") == 0){
      data_section_offset = section_headers[i].sh_offset;
      data_load_address = section_headers[i].sh_addr;
      data_index = i;
    }
  }


  // ERROR check to ensure everything was found based on things that
  // could not be found.
  if(symbol_table_offset == 0){
    printf("ERROR: Couldn't find symbol table\n");
    // UNMAP, CLOSE FD
    munmap(file_bytes, size);
    close(fd);
    return 1;
  }

  if(string_table_offset == 0){
    printf("ERROR: Couldn't find string table\n");
    // UNMAP, CLOSE FD
    munmap(file_bytes, size);
    close(fd);
    return 1;
  }

  if(data_section_offset == 0){
    printf("ERROR: Couldn't find data section\n");
    // UNMAP, CLOSE FD
    munmap(file_bytes, size);
    close(fd);
    return 1;
  }

  // PRINT info on the .data section where symbol values are stored

  printf(".data section\n");
  printf("- %hd section index\n", data_index);
  printf("- %lu bytes offset from start of file\n",data_section_offset); // lu stands for long unsigned in decimal
  printf("- 0x%lx preferred virtual address for .data\n",data_load_address); // lx prints unsigned long in hexadeciaml


  // PRINT byte information about where the symbol table was found and
  // its sizes. The number of entries in the symbol table can be
  // determined by dividing its total size in bytes by the size of
  // each entry.

  printf(".symtab section\n");
  printf("- %hd section index\n", symbol_table_index);
  printf("- %lu bytes offset from start of file\n",symbol_table_offset);
  printf("- %lu bytes total size\n",symbol_table_size);
  printf("- %lu bytes per entry\n",symbol_table_entry_size);
  //divide table size by entry size to get # entries
  uint64_t symbol_table_entries = symbol_table_size / symbol_table_entry_size;
  printf("- %lu entries\n", symbol_table_entries);

  // SET UP pointers to the Symbol Table and associated String Table
  // using offsets found earlier. Then SEARCH the symbol table for for
  // the specified symbol.

  // symbol_table points to the location of hte symbol table
  Elf64_Sym *symbol_table = (Elf64_Sym *) &file_bytes[symbol_table_offset];
  char *string_table = (char *) &file_bytes[string_table_offset];
  
  for(int i=0; i<symbol_table_entries; i++){
      // If symbol at position i matches the 'symbol_name' variable
      // defined at the start of main(), it is the symbol to work on.
    if(strcmp(string_table + symbol_table[i].st_name, symbol_name) == 0){
      // PRINT data about the found symbol.

      printf("Found Symbol '%s'\n",symbol_name);
      printf("- %d symbol index\n",i);
      printf("- 0x%lx value\n",symbol_table[i].st_value);
      printf("- %lu size\n",symbol_table[i].st_size);
      printf("- %hu section index\n",symbol_table[i].st_shndx);

      // CHECK that the symbol table field st_shndx matches the index
      // of the .data section; otherwise the symbol is not a global
      // variable and we should bail out now.
      if(symbol_table[i].st_shndx != data_index){
        printf("ERROR: '%s' in section %hd, not in .data section %hd\n", symbol_name,symbol_table[i].st_shndx, data_index);
        // UNMAP, CLOSE FD
        munmap(file_bytes, size);
        close(fd);
        return 1;
      }

      // CALCULATE the offset of the value into the .data section. The
      // 'value' field of the symbol is its preferred virtual
      // address. The .data section also has a preferred load virtual
      // address. The difference between these two is the offset into
      // the .data section of the mmap()'d file.
      uint64_t data_sym_offset = symbol_table[i].st_value - data_load_address;
      printf("- %ld offset in .data of value for symbol\n", data_sym_offset);

      // Symbol found, location in .data found, handle each kind (type
      // in C) of symbol value separately as there are different
      // conventions to change a string, an int, and so on.

      // string is the only required kind to handle
      if( strcmp(symbol_kind,"string")==0 ){
        // PRINT the current string value of the symbol in the .data section

        printf("string value: '%s'\n", &file_bytes[data_section_offset + data_sym_offset]);

        // Check if in SET_MODE in which case change the current value to a new one
        if(mode == SET_MODE){

          // CHECK that the length of the new value of the string in
          // variable 'new_val' is short enough to fit in the size of
          // the symbol.
          if(strlen(new_val) + 1 > symbol_table[i].st_size){
            // New string value is too long, print an error
            printf("ERROR: Cannot change symbol '%s': existing size too small\n",symbol_name);
            printf("Cur Size: %lu '%s'\n", symbol_table[i].st_size, (char *) &file_bytes[data_section_offset + data_sym_offset]);
            printf("New Size: %lu '%s'\n", strlen(new_val) + 1, new_val);
            // UNMAP, CLOSE FD
            munmap(file_bytes, size);
            close(fd);
            return 1;
          }
          else{
            // COPY new string into symbols space in .data as it is big enough
            strcpy(&file_bytes[data_section_offset + data_sym_offset], new_val);
            // PRINT the new string value for the symbol
            printf("New val is: '%s'\n", &file_bytes[data_section_offset + data_sym_offset]);
          }
        }
      }

      // OPTIONAL: fill in else/ifs here for other types on might want
      // to support such as int and double

      else{
        printf("ERROR: Unsupported data kind '%s'\n",symbol_kind);
        // UNMAP, CLOSE FD
        munmap(file_bytes, size);
        close(fd);
        return 1;
      }

      // succesful completion of getting or setting the symbol
      // UNMAP, CLOSE FD
      munmap(file_bytes, size);
      close(fd);
      return 0;
    } // end if statment
  } // end big for loop

  // Iterated through whole symbol tabel and did not find symbol, error out.
  printf("ERROR: Symbol '%s' not found\n",symbol_name);
  // UNMAP, CLOSE FD
  munmap(file_bytes, size);
  close(fd);
  return 1;
}
