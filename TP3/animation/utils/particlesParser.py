import json
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
            elif re.match(r'^P\d+=\d+\.\d+ P\d+=\d+\.\d+$', line):
                values = re.findall(r'P\d+=([\d.]+)', line)
                pressure1 = float(values[0])
                pressure2 = float(values[1])
                data_dict[current_time]['P1'] = pressure1
                data_dict[current_time]['P2'] = pressure2
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

""" def parse(file):
    with open(file, 'r') as archivo_json:
        data = json.load(archivo_json)

    # Crea un diccionario para almacenar los datos procesados
    resultado = {}  
    for i, board_item in enumerate(data['board']):
        particles = board_item['particles']
        
        # Crea un diccionario para almacenar las partículas y sus posiciones
        particles_dict = {}
        
        # Itera a través de las partículas en la lista "particles"
        for j, particle in enumerate(particles):
            x = particle['x']
            y = particle['y']
            
            # Crea un diccionario con las posiciones x e y de la partícula
            particle_data = {'x': x, 'y': y}
            
            # Agrega el diccionario al diccionario de partículas con el ID como clave
            particles_dict[j] = particle_data
        
        # Agrega el diccionario de partículas al resultado con el ID de "board" como clave
        resultado[i] = particles_dict
    return resultado """


"""
def parseGasDiffusionFile(gasDiffusionFile):
    data_dict = {}
    current_time = None

    with open(gasDiffusionFile, 'r') as file:
        i = -1
        for line in file:
            line = line.strip()
            if line.isdigit():
                current_time = int(line)
                data_dict[current_time] = {}
            else:
                parts = line.split()
                #particle_id = int(parts[0])
                x_position = float(parts[0])
                y_position = float(parts[1])
                #vx = float(parts[3])
                #vy = float(parts[4])
                
                data_dict[current_time][i] = {
                    'x': x_position,
                    'y': y_position,
                    #'vx': vx,
                    #'vy': vy
                }
            i += 1
    
    return data_dict
"""