import numpy as np

def parseParticles(staticFile, dynamicFile):
    with open(staticFile, 'r') as static:
        static_data = np.genfromtxt(static, skip_header=2)
        particle_radius = static_data[:, 0]  # Tomar la primera columna con el radio

    with open(dynamicFile, 'r') as dynamic:
        dynamic_data = np.genfromtxt(dynamic, skip_header=1)
        particle_positions = dynamic_data[:, :4]  # Tomar las cuatro primeras columnas con x, y, velocidad y angulo

    particle_dict = {}
    num_particles = min(len(particle_radius), len(particle_positions))
    for particle_id in range(num_particles):
        particle_dict[particle_id+1] = (particle_radius[particle_id],
                                        particle_positions[particle_id][:2],
                                        particle_positions[particle_id][2:4])

    return particle_dict