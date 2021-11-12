#recycling.py
#
#Alice Zhang <zhan6698@umn.edu> (Section 070)
#
#Tells user if item can or cannot be recycled given a specific list of recyclable items

print('---Is it recyclable?---')
item = input('Item to recycle: ') #capture user input for item to recycle
cleanliness = input('Is the item clean? (y/n): ') #capture user input on whether item is clean or not
if (cleanliness == 'y'): #checks if item is clean
    if (item == 'glass jar'): #the following if statements check if the item is one of six recyclable items
        print('That', item, 'is recyclable')
    elif (item == 'glass bottle'): 
        print('That', item, 'is recyclable')
    elif (item == 'aluminum can'):
        print('That', item, 'is recyclable')
    elif (item == 'plastic container'):
        print('That', item, 'is recyclable')
    elif (item == 'paper'):
        print('That', item, 'is recyclable')
    elif (item == 'cardboard'):
        print('That', item, 'is recyclable')
    else:
        print('That', item, 'is not recyclable') 
else:
        print('That', item, 'is not recyclable')

#if item == glass jar or item == etc.
    
            


        
        
