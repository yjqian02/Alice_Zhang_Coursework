#kinematics.py
#
#Alice Zhang <zhan6698@umn.edu> (Section 070)
#
#Calculates the position of a falling object after an amount of time, specified by the user

ACCELERATION = -9.81
velocity = float(input('Input velocity (m/s): '))
time = float(input('Elapsed time (s): '))
displacement = 0.5 * ACCELERATION * time ** 2 + velocity * time
print('Displacement is ', displacement, 'm.')

