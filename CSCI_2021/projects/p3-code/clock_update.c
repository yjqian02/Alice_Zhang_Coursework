// clock_update.c
// provides functions for clock_main.c and clock_sim.c to update the Display according to input values

#include "clock.h"

// int set_tod_from_secs(int time_of_day_sec, tod_t *tod){
//     if(time_of_day_sec < 0 || time_of_day_sec > 86400){ // time_of_day_sec is invalid
//         return 1;
//     }
//     tod->hours = time_of_day_sec / 3600;
//     if(tod->hours > 12){ // case for pm values
//         tod->hours -= 12;
//     }
//     else if(tod->hours == 0){ // case for 12 pm
//         tod->hours = 12;
//     }
//     tod->minutes = (time_of_day_sec / 60) % 60;
//     tod->seconds = (time_of_day_sec) % 60;
//     if(time_of_day_sec < 43200){ // 43200 seconds in 12 hours
//         tod->ampm = 1;
//     }
//     else{
//         tod->ampm = 2;
//     }
//     return 0; // success
// }

// Accepts time of day in seconds as an argument and modifies the
// struct pointed at by tod to fill in its hours, minutes,
// etc. fields.  If time_of_day_sec is invalid (negative or larger
// than the number of seconds in a day) does nothing to tod and
// returns 1 to indicate an error. Otherwise returns 0 to indicate
// success. This function DOES NOT modify any global variables
//
// CONSTRAINT: Uses only integer operations. No floating point
// operations are used as the target machine does not have a FPU.
// 
// CONSTRAINT: Limit the complexity of code as much as possible. Do
// not use deeply nested conditional structures. Seek to make the code
// as short, and simple as possible. Code longer than 40 lines may be
// penalized for complexity.

// int set_display_from_tod(tod_t tod, int *display){ // you're supposed to set display from tod not tod from display
//     // if any fields of tod are invalid (negative or too large)
//     if(tod.hours < 0 || tod.hours > 12 || tod.minutes < 0 || tod.minutes > 59 || tod.seconds < 0 || tod.seconds > 59){
//         return 1;
//     }
    
//     int bit_masks[10] =     // bit_masks contains bit_masks for integers from 0-9
//     {0b0111111, 0b0110000, // 0 1
//     0b1101101, 0b1111001, // 2 3
//     0b1110010, 0b1011011, // 4 5
//     0b1011111, 0b0110001, // 6 7
//     0b1111111, 0b1111011}; // 8 9

//     *display = 0; // initializing display to zero so its value will be 00 000000 000000 000000 000000 to start with

//     int ones_hour = tod.hours % 10;
//     int tens_hour = tod.hours / 10;
//     int ones_min = tod.minutes % 10;
//     int tens_min = tod.minutes / 10;

//     if(tod.ampm == 1){ // am
//         *display = *display | (1 << 28);
//     }
//     else if(tod.ampm == 2){ // pm
//         *display = *display | (1 << 29);
//     }
//     if(tens_hour == 1){ // tens digit of hour is either 1
//         *display = *display | (bit_masks[1] << 21);
//     }
    
//     *display = *display | (bit_masks[ones_hour] << 14); // set ones place of hour
//     *display = *display | (bit_masks[tens_min] << 7); // set tens place of minutes
//     *display = *display | (bit_masks[ones_min] << 0); // set ones place of minutes

//     return 0; // success
  
// }
// Accepts a tod and alters the bits in the int pointed at by display
// to reflect how the clock should appear. If any fields of tod are
// negative or too large (e.g. bigger than 12 for hours, bigger than
// 59 for min/sec), no change is made to display and 1 is returned to
// indicate an error. Otherwise returns 0 to indicate success. This
// function DOES NOT modify any global variables
//
// May make use of an array of bit masks corresponding to the pattern
// for each digit of the clock to make the task easier.
// 
// CONSTRAINT: Limit the complexity of code as much as possible. Do
// not use deeply nested conditional structures. Seek to make the code
// as short, and simple as possible. Code longer than 85 lines may be
// penalized for complexity.

// int clock_update(){
//     tod_t tod; // initializing new tod struct
//     int set_tod = set_tod_from_secs(TIME_OF_DAY_SEC, &tod);
//     if(set_tod == 1){ // TIME_OF_DAY_SEC is in error
//         return 1;
//     }
//     set_display_from_tod(tod, &CLOCK_DISPLAY_PORT);
//     return 0; // success
// }
// Examines the TIME_OF_DAY_SEC global variable to determine hour,
// minute, and am/pm.  Sets the global variable CLOCK_DISPLAY_PORT bits
// to show the proper time.  If TIME_OF_DAY_SEC appears to be in error
// (to large/small) makes no change to CLOCK_DISPLAY_PORT and returns 1
// to indicate an error. Otherwise returns 0 to indicate success.
//
// Makes use of the set_tod_from_secs() and
// set_display_bits_from_tod() functions.
// 
// CONSTRAINT: Does not allocate any heap memory as malloc() is NOT
// available on the target microcontroller.  Uses stack and global
// memory only.