# main.py
#
# Alice Zhang <zhan6698@umn.edu> (Section 070)
#
# Main file for the visualization
#
# 0. Main File: Complete
# 1. DataPoint Class: Complete
# 2. PointDataset Class: Complete
#   - Contructor: Complete
#   - Bounding Box: Complete
#   - Aspect Ratio: Complete
# 3. Visualizer Class Setup: Complete
#   - Contructor: Complete
#   - Turtle Setup: Complete
#   - Wait to Exit: Complete
#   - Mapping to Screen Coordinates: Complete
# 4. Visualization Context: Complete
#   - Basemap: Complete
#   - Glyphs with Color: Complete
#   - Legend: Complete
# 5. Making a Visualization: Complete
#   - Option B: Multiple Discrete Variables: Complete
# 6. More Visualization Features: Complete
#   - Custom Glyphs: Complete

import turtle
from dataset import PointDataset
from visualizer import Visualizer

def main():
    diamond = ((0, 0), (10, 10), (20,0), (10, -10)) # custom glyphs
    turtle.register_shape('diamond', diamond)
    hexagon = ((10, 10), (20, 10), (20, -10), (10, -10))
    turtle.register_shape('rectangle', hexagon)
    
    style = {
        'shape':{
            'property': 'Crash Type',
            'style': {
                'Bike': 'diamond',
                'Pedestrian': 'rectangle',
                'Pedesrian': 'rectangle',
                'Ped2': 'rectangle',
            }
        },
        'color':{
            'property': 'Signal Present?',
            'style': {
                'Yes': '#5dbcd2',
                'yes': '#5dbcd2',
                'Traffic Signal': '#5dbcd2',
                'No': '#660066',
                '': '#660066',
                'RRFB': '#660066',
                'Unknown': '#660066',
                'Stop Sign': '#cc00ff'
                }
            }
        }

    d = PointDataset('bike_crashes.geojson')
    v = Visualizer(d, style)
    v.draw()
    v.draw_legend()
    v.wait_to_exit()

if __name__== '__main__':
    main()
