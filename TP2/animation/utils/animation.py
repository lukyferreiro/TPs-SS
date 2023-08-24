import math
import matplotlib.pyplot as plt

def update(frame, data_dict, L):

    x_positions = []
    y_positions = []
    x_speed = []
    y_speed = []
    angles = []

    for id, particle_info in data_dict[frame].items():
        x_positions.append(particle_info['x'])
        y_positions.append(particle_info['y'])
        x_speed.append(particle_info['speed'] * math.cos(particle_info['angle']))
        y_speed.append(particle_info['speed'] * math.sin(particle_info['angle']))
        angles.append(particle_info['angle'])

    plt.clf()

    plt.quiver(x_positions, y_positions, x_speed, y_speed, angles, cmap='autumn')

    plt.title(f'Tiempo: {frame}')
    plt.xlabel('Posición X')
    plt.ylabel('Posición Y')

    plt.xlim(0, L)  
    plt.ylim(0, L)  