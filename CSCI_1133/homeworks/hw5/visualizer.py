# visualizer.py
#
# Alice Zhang <zhan6698@umn.edu> (Section 070)
#
# creates class visualizer that visualizes PointDataset with turtle graphics

import turtle
from dataset import PointDataset

class Visualizer:
    def __init__(self, dataset, style_mappings):
        self.dataset = dataset
        self.style_mappings = style_mappings
        self.turtle_setup()

    def turtle_setup(self):
        self.t = turtle.Turtle()
        self.scr = self.t.getscreen()
        self.scr.setup(width=620 * self.dataset.aspect_ratio(), height=620)
        turtle.setworldcoordinates(-1, -1, 1, 1)
        self.t.speed(0)
        self.t.penup()
        self.scr.tracer(0)

        self.scr.bgpic('basemap.gif') # adds basemap background

    def wait_to_exit(self):
        self.scr.exitonclick()

    def map_coord_to_screen(self, coord):
        min_x, min_y, max_x, max_y = self.dataset.bounding_box()
        norm_x = (coord[0] - min_x) / (max_x - min_x)
        norm_y = (coord[1] - min_y) / (max_y - min_y)
        screen_x = 2 * norm_x - 1
        screen_y = 2 * norm_y - 1
        return [screen_x, screen_y]
    
    def draw(self):
        for i in range(len(self.dataset.points)): #each coord is a DataPoint object
            # draw shape from self.style_mappings in color from self.style_mappings
            coord = self.dataset.points[i]
            x, y = self.map_coord_to_screen(coord.coordinate)
            self.t.goto(x, y)
            self.t.color(self.style_mappings['color']['style'][coord.properties['Signal Present?']])
            shape = self.style_mappings['shape']['style'][coord.properties['Crash Type']]
            self.t.shapesize(0.30, 0.30)
            if shape == 'diamond':
                self.t.shape(shape)
            elif shape == 'rectangle':
                self.t.shape(shape)
            self.t.stamp()
            self.scr.update()
        
    def draw_legend(self):
        self.t.goto(0.70, -0.23) # create box in lower right hand corner
        self.t.fillcolor('white')
        self.t.begin_fill()
        for i in range(2): # draws white box for legend
            self.t.forward(0.3)
            self.t.right(90)
            self.t.forward(0.87)
            self.t.right(90)
        self.t.end_fill()

        self.t.color('black')
        self.t.goto(0.80, -0.32) # title
        self.t.write('LEGEND', move=False, align='Left', font=('arial', 16, 'normal'))

        self.t.goto(0.73, -0.37) # first heading: shape, crash type
        self.t.write('Shape', move=False, align='Left', font=('arial', 12, 'normal'))
        self.t.goto(0.73, -0.43)
        self.t.write('Crash Type', move=False, align='Left', font=('arial', 12, 'normal'))

        x, y = [0.73, -0.45]
        shapes = ['diamond', 'rectangle']
        crash_type = ['Bike', 'Pedestrian']

        for i in range(2):
            self.t.goto(x, y)
            self.t.shapesize(0.40, 0.40)
            self.t.shape(shapes[i])
            self.t.stamp()
            self.t.goto(x + 0.02, y - 0.04)
            self.t.write(crash_type[i], move=False, align='Left', font=('arial', 10, 'normal'))
            y -= 0.04
            
        self.t.goto(0.73, -0.59) # second heading: color, signal present?
        self.t.write('Color', move=False, align='Left', font=('arial', 12, 'normal'))
        self.t.goto(0.73, -0.65)
        self.t.write('Signal Present?', move=False, align='Left', font=('arial', 12, 'normal'))    

        colors = ['#5dbcd2', '#80391e', '#cc00ff'] # list of colors
        styles = ['Yes', 'Traffic Signal', 'No','RRFB', 'Unknown', 'Stop Sign']

        x, y = [0.75, -0.71]
        for color in colors:
            self.t.goto(x, y) 
            self.t.color(color)
            if color == '#5dbcd2':
                self.t.write('Yes', move=False, align='Left', font=('arial', 10, 'normal'))
                self.t.goto(x, y - 0.05)
                self.t.write('Traffic Signal', move=False, align='Left', font=('arial', 10, 'normal'))
                y -= 0.06
            elif color == '#80391e':
               self.t.write('No', move=False, align='Left', font=('arial', 10, 'normal'))
               self.t.goto(x, y - 0.05)
               self.t.write('RRFB', move=False, align='Left', font=('arial', 10, 'normal'))
               self.t.goto(x, y - 0.10)
               self.t.write('Unknown', move=False, align='Left', font=('arial', 10, 'normal'))
               y -= 0.12
            else:
                self.t.write('Stop Sign', move=False, align='Left', font=('arial', 10, 'normal'))
            y -= 0.03
