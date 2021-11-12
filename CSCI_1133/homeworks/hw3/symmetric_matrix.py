# symmetric_matrix.py
#
# Alice Zhang <zhan6698@umn.edu> (Section 070)
#
# checks if a matrix is symmetric

def is_symmetric(matrix):
    if len(matrix) == 0 or len(matrix) == 1:
        return True
    elif len(matrix) == len(matrix[0]):
        for row in range(len(matrix)):
            for col in range(len(matrix)):
                if matrix[row][col] != matrix[col][row]:
                    return False
    else:
        return False
    return True
