# adventurer.py
#
# Alice Zhang <zhan6698@umn.edu> (Section 070)
#
# creates class Adventurer (serves as base class for every adventurer created)

from dice import Dice

class Adventurer:
    def __init__(self, name, level, ac, hp, ability_scores, attacks):
        self.name = name
        self.level = level
        self.ac = ac
        self.hp = hp
        self.max_hp = hp
        self.ability_score = ability_scores
        self.attacks = attacks
    def __str__(self):
        return 'Name: {}, Level: {}, Armor Class: {}, Current Hit Points: {}'.format(self.name, self.level, self.ac, self.hp)
    def level_up(self):
        self.level += 1
    def get_modifier(self, ability):
        if ability in self.ability_score:
            return (self.ability_score[ability] - 10) // 2
        else:
            raise KeyError('KeyError: The character does not have the ability')
    def get_proficiency_bonus(self):
        if self.level < 1 or self.level > 20:
            raise ValueError('ValueError: Level is invalid') 
        elif self.level in range(1, 5):
            return 2
        elif self.level in range(5, 9):
            return 3
        elif self.level in range(9, 13):
            return 4
        elif self.level in range(13, 17):
            return 5
        else:
            return 6
    def attack(self, weapon):
        d20 = Dice(1, 20, 0) # rolling a d20 for the attack
        attack_roll = d20.roll()
        damage_dice, ability, prof = self.attacks[weapon]
        damage_roll = damage_dice.roll() # rolling specified damage
        if attack_roll >= 20:
            damage_roll += damage_dice.roll()
        if prof == True:
            attack_roll += self.get_proficiency_bonus()
        attack_roll += self.get_modifier(ability)
        damage_roll += self.get_modifier(ability)
        return (attack_roll, damage_roll)
