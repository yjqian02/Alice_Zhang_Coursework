# turtle_polygon.py
#
# Alice Zhang <zhan6698@umn.edu>
#
# draws a polygon with n vertices

import turtle

def draw_polygon(n_vertices):	#draws sides of shapes
    angle = 360 / n_vertices
    turtle.showturtle() 
    for i in range(0, n_vertices):
        turtle.forward(100)
        turtle.left(angle)
        draw_vertex()
        
def draw_vertex(): #draws vertex dots
    turtle.dot(10) #I asked and using turtle.dot() is apparently okay of turtle.circle? even though technically, it'd be more efficient to put this into the function above instead of making 2 separate functions

def main():
    user_sides = int(turtle.numinput('Number of sides', 'Enter the number of verices: '))
    draw_polygon(user_sides)

if __name__ == '__main__':
    main()
