# diagonal_matrix.py
#
# Alice Zhang <zhan6698@umn.edu> (Section 070)
#
# checks if matrix is diagonal

def is_diagonal(matrix):
    if len(matrix) == 0 or len(matrix) == 1:
        return True
    elif len(matrix) == len(matrix[0]):
        for row in range(len(matrix)):
            for col in range(len(matrix)):
                if row != col and matrix[row][col] != 0:
                    return False
    else:
        return False
    return True
