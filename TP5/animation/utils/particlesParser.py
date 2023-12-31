import re
import numpy as np

def parse(file):
    data_dict = {}
    current_time = None

    with open(file, 'r') as file:
        for line in file:
            line = line.strip()
            if re.match(r'^\d+\.\d+$', line):
                current_time = float(line)
                data_dict[current_time] = {}
            else:
                parts = line.split()
                particle_id = int(parts[0])
                x = float(parts[1])
                y = float(parts[2])
                r = float(parts[3])
                
                data_dict[current_time][particle_id] = {
                    'x': x,
                    'y': y,
                    'r': r,
                }
    
    return data_dict

def parse_times(path):
    with open(path) as file:
        times_str = file.readlines()

    times = []
    for line in times_str:
        times.append(float(line))

    data = np.array(times)
    return data