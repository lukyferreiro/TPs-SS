import numpy as np
import os

def parseGasDiffusionFile(gasDiffusionFile):
    data_dict = {}
    current_time = None

    with open(gasDiffusionFile, 'r') as file:
        for line in file:
            line = line.strip()
            if line.isdigit():
                current_time = int(line)
                data_dict[current_time] = {}
            else:
                parts = line.split()
                particle_id = int(parts[0])
                x_position = float(parts[1])
                y_position = float(parts[2])
                vx = float(parts[3])
                vy = float(parts[4])
                
                data_dict[current_time][particle_id] = {
                    'x': x_position,
                    'y': y_position,
                    'vx': vx,
                    'vy': vy
                }
    
    return data_dict