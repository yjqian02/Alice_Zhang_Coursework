# dataset.py
#
# Alice Zhang <zhan6698@umn.edu> (Section 070)
#
# creates class PointDataset that represents dataset of DataPoints

import json
import os
import sys

from data_point import DataPoint

class PointDataset(DataPoint):
    def __init__(self, filename): 
        if os.path.isfile(filename): # check if the file exists
            with open(filename) as json_file:
                self.raw_data = json.load(json_file)
        else:
            print('File does not exist')
            sys.exit(1)
        with open(filename) as fin: # load JSON file into a dictionary self.raw_data
            self.raw_data = json.load(fin) # where you would clean up data -- note for me
        self.points = [] # use the dictionary to build up the self.points list
        for feature in self.raw_data['features']:
            if feature['geometry'] is not None:
                coord = feature['geometry']['coordinates']
                props = feature['properties']
                point = DataPoint(coord, props)
                self.points.append(point)
                           
    def bounding_box(self): # loop through each point in self.points and figure out the min/max
        #min_x, min_y  = self.points[0].coordinate
        #max_x, max_y = self.points[0].coordinate
        x_values = []
        y_values = []
        for i in range(len(self.points)): #each point is an object
            x_values.append(self.points[i].coordinate[0])
            y_values.append(self.points[i].coordinate[1]) 
        min_x = min(x_values)
        max_x = max(x_values)
        min_y = min(y_values)
        max_y = max(y_values)
        return [min_x, min_y, max_x, max_y]
                
    def aspect_ratio(self):
        min_x, min_y, max_x, max_y = self.bounding_box()
        return (max_x - min_x) / (max_y - min_y)
        
