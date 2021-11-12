# image_viewer.py
#
# Alice Zhang <zhan6698@umn.edu> (Section 070)
#
# displays pixelated images from a matrix of colors

SCREEN_SIZE = 640

import turtle
import sys
from images import IMAGES

def draw_pixel(x, y, color, size): #draws square pixel
    turtle.penup()
    turtle.goto(x * size, (SCREEN_SIZE / size - 1 - y)* size)
    turtle.fillcolor(color)
    turtle.begin_fill()
    for side in range(4):
        turtle.forward(size)
        turtle.left(90)
    turtle.end_fill()

def draw_image(img): #draws image
    for row in range(len(img)):
        for col in range(len(img[0])):
            draw_pixel(col, row, img[row][col], SCREEN_SIZE / len(img))
    
def main(): #validates command line arg then selects and draws image
    turtle.tracer(0,0)
    turtle.hideturtle()
    turtle.penup()
    turtle.setup(width=SCREEN_SIZE, height=SCREEN_SIZE)
    turtle.setworldcoordinates(0, 0, SCREEN_SIZE, SCREEN_SIZE)
    
    if len(sys.argv) < 2:
        print('Usage: python3 image_viewer.py index between 0 and 3')
        exit()
        
    draw_image(IMAGES[int(sys.argv[1])])
    turtle.update()
    turtle.mainloop()
    
if __name__ == '__main__':
    main()
