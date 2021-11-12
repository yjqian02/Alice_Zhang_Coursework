# happy_primes.py
#
# Alice Zhang <zhan6698@umn.edu>
#
# determines if a given number is both happy and prime

from happy_numbers import is_happy #importing function to check if number is happy

def is_happy_prime(n):
    n = abs(n)
    prime = True
    for i in range(2, n): 
        if n % i == 0:
            prime = False
    if prime == True and is_happy(n) == True:
            return True
    return False
