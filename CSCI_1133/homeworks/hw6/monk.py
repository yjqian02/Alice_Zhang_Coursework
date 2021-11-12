# monk.py
#
# Alice Zhang <zhan6698@umn.edu> (Section 070)
#
# creates class Monk

from adventurer import Adventurer

class Monk(Adventurer):
    def __str__(self):
        return 'Monk ' + super().__str__()
    def level_up(self):
        super().level_up()
        self.max_hp = self.hp + 5 + self.get_modifier('Constitution')
        self.hp = self.max_hp
    def get_ki_points(self):
        if self.level == 1:
            return 0
        else:
            return self.level
