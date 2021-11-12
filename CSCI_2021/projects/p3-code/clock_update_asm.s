# provies assembly version of clock_update.c functions
.file "clock_update.c"
.text
.global  set_tod_from_secs
        
## ENTRY POINT FOR REQUIRED FUNCTION
set_tod_from_secs:
## set arguments
    ## TIME_OF_DAY_SEC = %edi
    ## tod pointer = %rsi

## check that TIME_OF_DAY_SEC is in bounds
    ## if TIME_OF_DAY_SEC is negative 
    cmpl $0, %edi                       # compare 0 and time_of_day_sec
    jl .OUT_OF_BOUNDS                   # jump if out of bounds

    ## if time of day is out of bounds/greater than 86400
    cmpl $86400, %edi                   # compare 86400 and time_of_day_sec
    jg .OUT_OF_BOUNDS                   # jump if out of bounds
    
## set time_of_day_sec = time_of_day_sec / 3600
    movl %edi,%eax                      # eax = time_of_day_secs
    cqto                                # prep for division
    movl $3600,%r8d                     # put integer to divide by into %r8d
    idivl %r8d                          # eax / 3600   
    # result in eax, remainder in edi

## if tod->hours > 12 (pm values)
    cmpl $12,%eax                       # compare 12 and calculated hours (inside eax)
    jg .PM_HOURS                        # jump if 12 < calculated hours
    jmp .TWELVEPM                       # if not pm values (after 12), check if 12 pm

    ## case for pm hours
    .PM_HOURS:
        subl $12,%eax                   # eax = eax - 12
        jmp .SETHOURS

## if tod->hours = 12 pm (case for 12 pm)
    .TWELVEPM:
        cmpl $0,%eax                    # compare 0 and calculated hours (inside eax)
        je .TWELVE                      # jump if calculated hours (inside eax) = 0
        jmp .SETHOURS

    ## case for 12 pm
    .TWELVE: 
        movl $12,%eax                   # set calculated hours (inside eax) to be 12

## set tod->hours value
    .SETHOURS:
        movw %ax,0(%rsi)                # tod->hours = calculated hours (inside eax)

## set tod->minutes = (time_of_day_sec / 60) % 60
    ## set time_of_day_sec = (time_of_day_sec / 60)
    movl %edi,%eax                      # put time_of_day_seconds into %eax
    cqto                                # prep for division
    movl $60,%r8d                       # put integer to divide by into %edx
    idivl %r8d

    ## set time_of_day_sec %= 60 (modulo is same as division, except remainder is in %edx)
    cqto                                # prep for division
    movl $60,%r9d                       
    idivl %r9d                          # eax = eax / 60

    ## set tod->minutes value
    movw %dx,2(%rsi)

## set tod->seconds = time_of_day_sec % 60
    movl %edi,%eax                      # move original time_of_day_sec into %eax
    cqto                                # prep for division
    movl $60,%r8d                       # put integer to divide by into %edx
    idivl %r8d

    ## set tod->sec value
    movw %dx,4(%rsi)

## if time_of_day_sec < 43200 (case for AM)
    cmpl $43200,%edi                    # compare 43200 and value in time_of_day_sec
    jb .AM                              # jump if time_of_day_sec < 43200
    jmp .CHECK_PM

    ## case for AM
    .AM: 
        movw $1,6(%rsi)                 # tod->ampm = 1

## if time_of_day_sec >= 43200 (case for PM)
    .CHECK_PM:
        cmpl $43200,%edi               # compare 43200 and value in time_of_day_sec
        jge .PM                        # jump if time_of_day_sec >= 43200
        jmp .RETURN

    ## case for PM
    .PM: 
        movw $2,6(%rsi)                # tod->ampm = 2

## return 0 for success
    .RETURN:
        movl $0,%eax
        ret

## case for tod->hours out of bounds
    .OUT_OF_BOUNDS:
            movl $1,%eax
            ret

### Data area associated with the next function
.data

## array of 11 ints for serving as bit masks
array:  
        .int 0b0111111             # 0
        .int 0b0110000             # 1
        .int 0b1101101             # 2
        .int 0b1111001             # 3
        .int 0b1110010             # 4
        .int 0b1011011             # 5
        .int 0b1011111             # 6
        .int 0b0110001             # 7
        .int 0b1111111             # 8
        .int 0b1111011             # 9

.text
.global  set_display_from_tod

## ENTRY POINT FOR REQUIRED FUNCTION
set_display_from_tod:
## tod struct pointer = %rdi
## CLOCK_DISPLAY_PORT = %esi
## initialize state variable for display
    pushq %r12                     # need to push and then pop before returning for this register (callee save register)
    movl $0, %r12d                 # initialize display state variable to 0 

## work with ampm
    ## extract ampm
        movq %rdi, %rdx
        sarq $48, %rdx             # move ampm to low order bits
        andq $0xFF, %rdx
    
    ## checking if tod.ampm is valid
        ## if tod.ampm < 0

        cmpl $1, %edx              # compare 1 and tod.ampm
        jl .OOB
        jmp .CONTINUE_AMPM
        
        ## if tod.seconds > 59
        .CONTINUE_AMPM:
            cmpl $2, %edx          # compare 2 and tod.ampm
            jg .OOB
            jmp .CHECK_AM_SET

    ## if tod.ampm = 1 (case for am)
    .CHECK_AM_SET:
        cmpl $1, %edx
        je .AM_SET
        jmp .CHECK_PM_SET

        .AM_SET:                   # *display = *display | (1 << 28)
            movl $1, %ecx          # ecx = 1 (use 64 bit here since you're working with memory addresses)
            sall $28, %ecx         # 1 << 28                
            orl %ecx, %r12d                

    ## if tod.ampm = 2 (case for pm)
    .CHECK_PM_SET:
        cmpl $2, %edx
        je .PM_SET
        jmp .SECONDS

        .PM_SET:                   # *display = *display | (1 << 29)
            movl $1, %ecx          # ecx = 1 (use 64 bit here since you're working with memory addresses)
            sall $29, %ecx         # 1 << 29              
            orl %ecx, %r12d

## work with tod.seconds 
    ## extract seconds
    .SECONDS:
        movq %rdi, %rdx
        sarq $32, %rdx             # move seconds to low order bits
        andq $0xFFFF, %rdx

    ## checking if tod.seconds is valid
        ## if tod.seconds < 0

        cmpl $0, %edx              # compare 0 and tod.seconds
        jl .OOB
        jmp .CONTINUE_SECONDS
        
        ## if tod.seconds > 59
        .CONTINUE_SECONDS:
            cmpl $59, %edx         # compare 59 and tod.seconds
            jg .OOB
            jmp .MINUTES

## work with tod.minutes
    ## extract minutes
    .MINUTES:
        movq %rdi, %rdx 
        sarq $16, %rdx                      
        andq $0xFFFF, %rdx      

    ## check if tod.minutes are valid
        ## if tod.minutes < 0
        cmpl $0, %edx               # compare 0 and tod.minutes
        jl .OOB
        jmp .CONTINUE_MINUTES

        ## if tod.minutes > 59
        .CONTINUE_MINUTES:
            cmpl $59, %edx          # compare 59 and tod.minutes
            jg .OOB
            jmp .INITIALIZE_MINUTES

    ## initializes ones_min and tens_min
        ## ones_min = r10
        ## tens_min = r11
        ## int ones_min = tod.minutes % 10
        .INITIALIZE_MINUTES:
            movq %rdx, %rcx        # temporarily hold rdx in rcx

            movl %edx, %eax        # move tod.hours into eax for division
            cqto                   # prep for division
            movl $10, %r10d
            idivl %r10d            # modulo result/division remainder is stored in rdx
            movl %edx, %r10d

            movq %rcx, %rdx        # move rcx back into rdx
            movq $0, %rcx          # zero out rcx
    
        ## int tens_min = tod.minutes / 10
        ## remember result from tod.minues / 10 is already in rax from before
            movl %eax, %r11d       # store tens_min in %r11d
    
    ## shifting display for tens_min
        # *display = *display | (bit_masks[tens_min] << 7)
        leaq array(%rip),%rax      # rax will point to the bit masks array

        movl (%rax,%r11,4), %ecx   # rcx = bit_masks[tens_min] (r10 = tens_min)
        sall $7, %ecx              # bit_masks[tens_min] << 7 
        orl %ecx, %r12d            # remember r12d holds display state variable
        movq $0, %rcx              # zero out ecx

    ## shifting display for ones_min
        # *display = *display | (bit_masks[ones_min] << 0)
        movl (%rax,%r10,4), %ecx   # rcx = bit_masks[ones_min] (r11d = ones_min)
        sall $0, %ecx              # bit_masks[oness_min] << 0
        orl %ecx, %r12d
        movq $0, %rcx              # zero out ecx

## work with tod.hours
    ## extract hours
    movq %rdi, %rdx                # use q suffix because you're working with integers
    andq $0xFFFF, %rdx             # mask low byte (using F since binary version of hex F is 1111 and the andq zeros everything except the bytes you want, we just 0xFFFF here since 1 f is 4 bytes and we want to mask 16 bytes)

    ## check if tod.hours are valid
        ## if tod.hours < 0
        cmpl $0, %edx              # compare 0 and tod.hours
        jl .OOB                    # jump if out of bounds
        jmp .CONTINUE_HOURS

        ## if tod.hours > 12
        .CONTINUE_HOURS:
            cmpl $12, %edx         # compare 12 and tod.hours
            jg .OOB                # jump if out of bounds
            jmp .INITIALIZE_HOUR

    ## initialize ones_hour
        ## r8 = ones_hour
        ## r9 = tens_hour

        ## int ones_hour = tod.hours % 10
        .INITIALIZE_HOUR:
            movq %rdx, %rcx        # temporarily hold rdx in rcx

            movl %edx, %eax        # move tod.hours into eax for division
            cqto                   # prep for division
            movl $10, %r8d
            idivl %r8d             # result in eax, remainder in rdx (always, this is just the convention in division)
            movl %edx, %r8d        # put remainder in r8 from rdx

            movq %rcx, %rdx        # move rcx back into rdx

            movq $0, %rcx          # zero out ecx

    ## initialize tens_hour
        ## int tens_hour = tod.hours / 10
            ## remember tod.hours / 10 is already in rax from before
            movl %eax,%r9d         # store division result in %r9

    ## shifting display for ones_hour

        ## if tens digit of hour is 1 (a check related to tens_hour)   
            cmpl $1,%r9d                  
            je .CHANGE_TENS               
            jmp .SHIFT14

            .CHANGE_TENS:                # *display = *display | (bit_masks[1] << 21)
                leaq array(%rip),%rax    # rax will point to the bit masks array
                movl $1,%ecx             # rcx = 1
                movl (%rax,%rcx,4), %ecx # rcx = bit_masks[1]
                sall $21, %ecx           # bit_masks[1] << 21 
           
                orl %ecx, %r12d          # r12d holds display state variable
                movq $0, %rcx            # zero out ecx

        ## *display = *display | (bit_masks[ones_hour] << 14)
        .SHIFT14:
            leaq array(%rip),%rax        # rax will point to the bit masks array
            movl (%rax,%r8,4), %ecx      # rcx = bit_masks[ones_hour] (r8 = ones_hour)
            sall $14, %ecx               # bit_masks[ones_hour] << 14   

            orl %ecx, %r12d              # r12d now holds display state variable that is shifted
            movq $0, %rcx                # zero out ecx

    ## return 0 for success
    .RETURN2:
        movl %r12d, (%rsi)               # make rsi point to the value of the state variable r8d
        movl $0,%eax
        popq %r12                        # remember to pop %r12 from the stack 
        ret

## out of bounds case
    .OOB:
        movl $1,%eax
        popq %r12                        # remember to pop %r12 from the stack so that the return isn't whatever happens to be in the stack
        ret

.text
.global clock_update
        
## ENTRY POINT FOR REQUIRED FUNCTION
clock_update:
## create new tod struct and call set_tod_from_sec
        pushq $0                        # allocating stack memory for tod struct
        movl TIME_OF_DAY_SEC(%rip),%edi # setting first argument for set_today_from sec (pointer to TIME_OF_DAY_SEC)
        movq %rsp,%rsi                  # setting second argument for set_tod_from_sec (dereferencing tod struct pointer)
        call set_tod_from_secs          # call function set_tod_from_sec

## if time of day is in error
        cmpq $1, %rax                   # if set_tod_from_sec returns 1
        je .ERROR

## call set_display_from_tod
        movq (%rsp),%rdi
        movq %rsp, %rsi                 # setting first argument of set_display_from_tod 
        call set_display_from_tod       # call function set_display_from_tod

        popq %rdx                       # pop what's on the stack into rdx
        movl %edx, CLOCK_DISPLAY_PORT(%rip) # make clock_display_port point into edx

## deallocating stack memory            
        movq $0, %rax                   # return 0 for success                  
        ret

## if TIME_OF_DAY_SEC is out of bounds/in error
        .ERROR: 
                                        ## you don't need to do a movq $1, %rax here since the cmpq statement abvoe shows rax already contains the return value of 1
                popq %rdx               # pop again from stack to restore the stack (otherwise the return is whatever is in the stack)
                ret
