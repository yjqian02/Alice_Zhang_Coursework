#snell_vis.py
#
#Alice Zhang <zhan6698@umn.edu> (Section 070)
#
#Visualizes snell's law using turtle graphics

import turtle #importing turtle graphics package
import math #importing math module

PI = 3.14

n1 = 1.0 #index of refraction for aid
n2 = 1.5 #index of refraction for glass
ratio = n1 / n2 #radio of indices of refraction

turtle.showturtle() #causes graphics window to be displayed with turtle in the middle
angle_of_incidence = turtle.numinput("Angle of incidence", "Input angle (degrees): ") #obtain the initial angle from user

angle_1_rad = angle_of_incidence / 180 * PI #convert angle_of_incidence to radians
angle_2_rad = math.asin(ratio * math.sin(angle_of_incidence)) #finding second angle
angle_2 = angle_2_rad / PI * 180

turtle.left(angle_of_incidence)
turtle.forward(100) #guessing that you go forward 100 since it wasn't a given number
turtle.left(angle_2)
turtle.forward(100)
turtle.right(angle_2)
turtle.forward(100)
