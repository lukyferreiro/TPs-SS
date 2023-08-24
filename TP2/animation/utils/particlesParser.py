import numpy as np
import os

def parseOffLatticeFile(offLatticeFile):
    data_dict = {}
    current_time = None

    with open(offLatticeFile, 'r') as file:
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
                velocity = float(parts[3])
                angle = float(parts[4])
                
                data_dict[current_time][particle_id] = {
                    'x': x_position,
                    'y': y_position,
                    'speed': velocity,
                    'angle': angle
                }
    
    return data_dict


def parseOrderParameterVaFile(orderParameterVaFile):
    with open(orderParameterVaFile, 'r') as file:
        first_line = file.readline().strip().split()
        N, L, Rc, eta, iterations = map(float, first_line)
        order_parameters = np.genfromtxt(file)

    return N, L, Rc, eta, iterations, order_parameters

def parse_order_parameter_over_iteration(orderParameterVariatingEtaFile):
    with open(orderParameterVariatingEtaFile, 'r') as file:
        first_line = file.readline().strip().split()
        N, L, Rc, iterations = map(float, first_line)
        data_dict = {}
        for line in file:
            elements = line.split()
            key = float(elements[0])
            data = [float(value) for value in elements[1:]]
            data_dict[key] = data

    return N, L, Rc, iterations, data_dict

def parse_order_parameter_directory(dir_path):
    all_data = {}

    for filename in os.listdir(dir_path):
        if filename.endswith('.txt'): 
            filepath = os.path.join(dir_path, filename)
            N, L, Rc, iterations, data_dict = parse_order_parameter_over_iteration(filepath)
            file_info = {'N': N, 'L': L, 'Rc': Rc, 'iterations': iterations, 'data_dict': data_dict}
            all_data[filename] = file_info

    return all_data