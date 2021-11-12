# nester_min.py
#
# Alice Zhang <Zhan6698@umn.edu> (Section 070)
#
# finds minimum floating point value of some arbitarily nested floating point data

def nested_min(lst):
    if type(lst) == float:
        return lst
    elif len(lst) == 0:
        return float('inf')
    else:
        minimum = nested_min(lst[1:])
        mini = nested_min(lst[0])
        if minimum < mini:
            mini = minimum
        return mini
    
