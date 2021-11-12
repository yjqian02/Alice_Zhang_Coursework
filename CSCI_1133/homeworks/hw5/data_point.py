# data_point.py
#
# Alice Zhang <zhan6698@umn.edu> (Section 070)
#
# creates class DataPoint that respresents single point in public safety dataset

class DataPoint:
    def __init__(self, coordinate, properties):
        self.coordinate = coordinate
        self.properties = properties
    def __str__(self):
        return 'Point({:.2f}, {:.2f})'.format(
            self.coordinate[0], self.coordinate[1]
        )
