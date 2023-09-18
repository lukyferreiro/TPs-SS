import re

def parseGasDiffusionFile(gasDiffusionFile):
    data_dict = {}
    current_time = None

    with open(gasDiffusionFile, 'r') as file:
        for line in file:
            line = line.strip()
            if re.match(r'^\d+\.\d+$', line):
                current_time = float(line)
                data_dict[current_time] = {}
            elif re.match(r'^P\d+=\d+\.\d+ P\d+=\d+\.\d+ P\d+=\d+\.\d+$', line):
                values = re.findall(r'P\d+=([\d.]+)', line)
                pressure1 = float(values[0])
                pressure2 = float(values[1])
                total_pressure = float(values[2])
                data_dict[current_time]['P1'] = pressure1
                data_dict[current_time]['P2'] = pressure2
                data_dict[current_time]['PT'] = total_pressure
            else:
                parts = line.split()
                particle_id = int(parts[0])
                x_position = float(parts[1])
                y_position = float(parts[2])
                
                data_dict[current_time][particle_id] = {
                    'x': x_position,
                    'y': y_position,
                }
    
    return data_dict