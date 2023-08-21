import numpy as np

def parseParticles(staticFile, dynamicFile):
    with open(staticFile, 'r') as static:
        static_data = np.genfromtxt(static, skip_header=2)
        particle_radius = static_data[:, 0]  # Tomar la primera columna con el radio

    with open(dynamicFile, 'r') as dynamic:
        dynamic_data = np.genfromtxt(dynamic, skip_header=1)
        particle_info = dynamic_data[:, :4]  # Tomar las cuatro primeras columnas con x, y, velocidad y angulo

    particle_dict = {}
    num_particles = min(len(particle_radius), len(particle_info))
    for particle_id in range(num_particles):
        particle_dict[particle_id+1] = (particle_radius[particle_id],
                                        particle_info[particle_id][:2],
                                        particle_info[particle_id][2:4])

    return particle_dict


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
        # Leer la primera línea
        first_line = file.readline().strip().split()
        N, L, Rc, eta, iterations = map(float, first_line)
        # Leer el resto de los datos
        data = np.genfromtxt(file)
        order_parameters = data[:, 0]  # Tomar la primera columna con todos los valores de va

    return N, L, Rc, eta, iterations, order_parameters