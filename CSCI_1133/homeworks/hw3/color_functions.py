# color_functions.py
#
# Alice Zhang <zhan6698@umn.edu> (Section 070)
#
# Computes color properties

def luminance(color):
    luminance = 0
    if len(color) == 3:
        luminance = 0.2126 * color[0] + 0.7152 * color[1] + 0.0722 * color[2]
    else:
        return None
    return luminance

def clamp(color):
    clamp_color = []
    for elem in color:
        if len(color) != 3:
            return None
        if elem < 0.0:
            clamp_color.append(0.0)
        elif elem > 1.0:
            clamp_color.append(1.0)
        else:
            clamp_color.append(elem)
    return clamp_color
