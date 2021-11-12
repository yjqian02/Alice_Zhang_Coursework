# adventure_game.py
#
# Alice Zhang <zhan6698@umn.edu> (Section 070)
#
# creates class AdventureGame that has a text-based intervface for interacting with adventurers

from dice import Dice
from adventurer import Adventurer
from monk import Monk
from wizard import Wizard
from barbarian import Barbarian

class AdventureGame:
    def __init__(self):
        self.characters = []
    def mainloop():
        user_input = 'y'
        try:  
            while user_input != 'n':
                user_input = input('AG> ')
                run_command(user_input)
        except TypeError:
            print('Wrong type of input!')
        except ValueError:
            print('Invalid input value!')
    def run_command(user_input):
        if user_input == 'roll':
            dice = input('Dice: ')
            dice = dice.roll()
            print('You rolled a: ', dice)
        elif user_input == 'new':
            self.new_character
        elif user_input == 'attack':
            char_name = input('Character name: ')
            weapon = input('Weapon: ')
            char = self.characters[char_name]
            attack, damage = char.attack(weapon)
            print('{} attacked with {}. Attack: {}, Damage: {}'.format(char_name, weapon, attack, damage))
        elif user_input == 'cast':
            wiz_name = input('Wizard name: ')
            cantrip = input('Cantrip: ')
            if isinstance(character, Wizard):
                wiz = self.characters[wiz_name]
                wiz.cast_cantrip(cantrip)
            else:
                print('Error, this wizard does not exist.')
        elif user_input == 'list':
            print('--CHARACTERS--')
            for char in self.characters:
                print(char)
    def new_character():
        print('--CHARACTER CREATOR--')
        characters = ['Barbarian', 'Wizard', 'Monk']
        char_type = input('Type of character: ')
        while char_type not in characters:
            char_type = input('Type of character: ')
        name = input('Character name: ')
        level = input('Level: ')
        armor_class = input('Armor Class: ')
        max_hp = input('Maximum hit points: ')
        ability_score = input('Ability scores: ')

        print('--WEAPON CREATOR--')
        num_weapons = 0
        weapon_name1, damage1, ability1, prof1 = input('Weapon name, damage dice, ability, proficiency: ')
        while weapon_name != '' and num_weapons <= 2:
            if num_weapons == 0:
                weapon_name2, damage2, ability2, prof2  = input('Weapon name, damage dice, ability, proficiency: ')
            else:
                weapon_name3, damage3, ability3, prof3 = input('Weapon name, damage dice, ability, proficiency: ')
            num_weapons += 1

        if char_type == 'Wizard':
           print('--CANTRIP CREATOR--')
           num_cantrips = 0
           cantrip_name1, damage1 = input('Cantrip name, damage: ')
           while cantrip_name != '' and num_cantrips <= 2:
                if num_cantrips == 0:
                   cantrip_name2, damage2 = input('Cantrip name, damage: ')
                else:
                  cantrip_name3, damage3 = input('Cantrip name, damage: ')
                num_cantrips += 1
