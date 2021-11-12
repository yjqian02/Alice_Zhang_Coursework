# happy_numbers.py
#
# Alice Zhang <zhan6698@umn.edu>
#
# determines if a given number is happy or not using exhaustive enumeration

def is_happy(n): #checks if number is happy
    n = abs(n)
    i = 0
    happy = False
    while happy == False:
        if i <= 10000:
            if sum_squares_digits(n) == 1:
                return True
            else:
                n = sum_squares_digits(n)
                i += 1
        else:
            return False
              
def sum_squares_digits(n): #finds the sum of the square of the digits of n
    n = abs(n)
    sum_squares = 0
    while n > 0:
        sum_squares += (n % 10) ** 2 #get last digit
        n = n // 10 #get rid of last digit
    return sum_squares
