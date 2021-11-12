# barbarian.py
#
# Alice Zhang <zhan6698@umn.edu> (Section 070)
#
# creates class Barbarian

from adventurer import Adventurer
from dice import Dice

class Barbarian(Adventurer):
    def __init__(self,name, level, ac, hp, ability_scores, attacks):
        super().__init__(name, level, ac, hp, ability_scores, attacks)
        self.rages = self.get_max_rages()
        self.raging = False
    def get_max_rages(self):
        if self.level < 1 or self.level > 20:
            print('ValueError: Level is invalid')
        elif self.level in range(1, 3):
            return 2
        elif self.level in range(3, 6):
            return 3
        elif self.level in range(6, 12):
            return 4
        elif self.level in range(12, 17):
            return 5
        elif self.level in range(17, 20):
            return 6
        else:
            return float('inf')
    def __str__(self):
        if self.raging:
            return 'Barbarian ' + super().__str__() + ' (raging)'
        else:
            return 'Barbarian ' + super().__str__() + ' (not raging)'
    def level_up(self):
        super().level_up()
        self.max_hp = self.hp + 7 + super().get_modifier('Constitution')
        self.hp = self.max_hp
    def get_rage_damage(self):
        if self.level < 1 or self.level > 20:
            raise ValueError('ValueError: Level is invalid')
        elif self.level in range(1, 9):
            return 2
        elif self.level in range(9, 16):
            return 3
        elif self.level in range(16, 21):
            return 4
    def attack(self, weapon):
        attack_roll, damage_roll = super().attack(weapon)
        if self.raging == True:
            damage_roll += self.get_rage_damage()
        return (attack_roll, damage_roll)     
    def enter_rage(self):
        if self.rages > 0:
            self.raging = True
    def exit_rage(self):
        self.rages -= 1
        self.raging = False
