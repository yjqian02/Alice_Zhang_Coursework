# sierpinski.py
#
# Alice Zhang <zhan6698@umn.edu> (Section 070)
#
# creates a Sierpinski carpet

import turtle
import sys
import random
    
def random_color():
    return clamp([random.gauss(0.75, 0.1), random.gauss(0.15, 0.1), random.gauss(0.45, 0.05)])

def findcolors(x, y):
    x = abs(x)
    y = abs(y)
    color = clamp([
        random.gauss(0.6, 0.1),
        random.gauss(0.3, 0.15),
        random.gauss(0.5, 0.1)
    ])
    return color

def clamp(color):
    color = [0 if ch < 0 else ch for ch in color]
    color = [1 if ch > 1 else ch for ch in color]
    return color

def draw_square(ll_x, ll_y, size): # draws square
    turtle.penup()
    turtle.goto(ll_x, ll_y)
    turtle.pendown()
    turtle.bgcolor([0.1, 0.15, 0.24]) # sets background color
    turtle.pencolor(findcolors(75, size))
    turtle.pensize(5)
    
    for i in range(4): 
        turtle.forward(size)
        turtle.pencolor(findcolors(75, size))
        turtle.left(90) 
    
def sierpinski(ll_x, ll_y, size, level): # draws sierpinski carpet
    sub_len = size / 3
    if level > 0: 
        draw_square(ll_x + sub_len, ll_y + sub_len, sub_len)
        # Recursive calls
        for i in range(3):
            for j in range(3):
                if i == 1 and j == 1:
                    continue
                sierpinski(ll_x + sub_len * i, ll_y + sub_len * j, sub_len, level - 1)
    
def main():
    turtle.setup(width=600, height=600)
    turtle.setworldcoordinates(0, 0, 1, 1) # Set up Turtle with lower left 0, 0 and upper right 1, 1
    turtle.tracer(0, 0)
    turtle.hideturtle()
    turtle.penup()

    if len(sys.argv) < 1:
        print('Usage: python3 sierpinski.py index greater than 1')
        exit()
        
    sierpinski(0,0,1,int(sys.argv[1]))
    turtle.update() # put after initial sierpinski call
    turtle.exitonclick()# wait for the user to click to exit   
    
if __name__ == '__main__':
    main()
    
