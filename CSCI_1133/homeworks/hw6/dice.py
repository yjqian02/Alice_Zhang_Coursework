# dice.py
#
# Alice Zhang <zhan6698@umn.edu> (Section 070)
#
# creates class Dice

import random

class Dice:
    def __init__(self, how_many, sides, modifier=0):
        self.how_many = how_many
        self.sides = sides
        self.modifier = modifier
    def __str__(self):
        return '{}d{}+{}'.format(self.how_many, self.sides, self.modifier)
    def roll(self):
        total = 0
        for i in range(self.how_many):
            total += random.randint(1, self.sides)
        return total + self.modifier
    def interpret(dice_string):
        new_hm, new_s, new_m = [0, 0, 0] 
        d = dice_string.find('d')
        plus = dice_string.find('+') 
        dice_string = dice_string.strip()
        
        if d != 0 and plus != -1: # the # of dice and modifier is given
            new_hm = dice_string[:d]
            new_s = dice_string[d+1:plus]
            new_m = dice_string[plus+1:]
        elif d != 0 and plus == -1: # the # of dice is given but modifier is not
            new_hm = dice_string[:d]
            new_s = dice_string[d+1:]
            new_m = 0
        elif d == 0 and plus != -1: # neither is given
            new_hm = 1
            new_s = dice_string[1:plus]
            new_m = dice_string[plus+1:]
        
        return Dice(new_hm, new_s, new_m)    
