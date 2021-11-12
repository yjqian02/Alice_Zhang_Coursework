# generate_data.py
#
# Alice Zhang <zhan6698@umn.edu> (Section 070)
#
# generates high-dimensional dataset of random numbers

import random

def generate_data(size, n_dim):
    if n_dim <= 0:
        return []
    else:
        lst = []
        for value in range(size):
            x = random.uniform(0, 1)
            lst.append(x)
        return lst + generate_data(size, n_dim - 1)
    
print(generate_data(3, 1))
