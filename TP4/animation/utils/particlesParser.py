import re

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
                vx = float(parts[3])
                vy = float(parts[4])
                
                data_dict[current_time][particle_id] = {
                    'x': x,
                    'y': y,
                    'vx': vx,
                    'vy': vy,
                }
    
    return data_dict

def parsePhiValues(file):
    data_dict = {}
    current_time = None

    with open(file, 'r') as file:
        for line in file:
            line = line.strip()
            if re.match(r'^\d+$', line):
                current_time = int(line)
                data_dict[current_time] = {}
            else:
                parts = line.split()
                phiValues = []

                for part in parts:
                    phiValues.append(part)

                data_dict[current_time] = {
                    'phiValues': phiValues
                }
    
    return data_dict