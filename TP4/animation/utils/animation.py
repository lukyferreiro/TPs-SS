import matplotlib.pyplot as plt
from matplotlib.lines import Line2D

def update(frame, particles_dict, L):
    plt.clf()

    print(frame)

    R = 2.25

    line = Line2D([0, L], [0, 0], color='grey', linestyle='--', linewidth=1)
    plt.gca().add_line(line)

    for id, particle_info in particles_dict[frame].items():
        if isinstance(id, int):
            x = particle_info['x']
            y = particle_info['y']
            x = x % L
            if x + R > L:
                plt.gca().add_patch(plt.Circle((x-L, y), R, fill=True))
                plt.gca().add_patch(plt.Circle((x, y), R, fill=True))
            elif x - R < 0:
                plt.gca().add_patch(plt.Circle((x, y), R, fill=True))
                plt.gca().add_patch(plt.Circle((x+L, y), R, fill=True))
            else:
                plt.gca().add_patch(plt.Circle((x, y), R, fill=True))

    plt.xlim(0, L)
    plt.ylim(-R, R)
    plt.gca().set_aspect('equal', adjustable='datalim')
    plt.title(f'Tiempo: {frame:.2f}')
    plt.xlabel('Posición X')
    plt.ylabel('Posición Y')