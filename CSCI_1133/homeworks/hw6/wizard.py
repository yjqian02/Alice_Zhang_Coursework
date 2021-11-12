# wizard.py
#
# Alice Zhang <zhan6698@umn.edu> (Section 070)
#
# creates class Wizard

from adventurer import Adventurer

class Wizard(Adventurer):
    def __init__(self, name, level, ac, hp, ability_scores, attacks, cantrips):
        super().__init__(name, level, ac, hp, ability_scores, attacks)
        self.cantrips = cantrips
    def __str__(self):
        return 'Wizard ' + super().__str__()
    def level_up(self):
        super().level_up()
        self.max_hp = self.hp + 4 + super().get_modifier('Constitution')
        self.hp = self.max_hp
    def cast_cantrip(self,cantrip):
        if cantrip in self.cantrips:
            damage_dice = self.cantrips[cantrip]
            damage_dice = damage_dice.roll()
            return damage_dice
        else:
            raise KeyError('KeyError: cantrip doesn\'t exist')
