import matplotlib.pyplot as plt
from matplotlib.lines import Line2D
from matplotlib.patches import Circle, Rectangle

def update(frame, particles_dict, W, L, D):
    plt.clf()

    print(frame)

    rect = Rectangle((0, 0), W, L + L / 10, color='b', fill=False)
    plt.gca().add_patch(rect)

    # Dibuja una línea horizontal con espacio en el medio
    plt.gca().add_line(plt.Line2D([0, W/2 - D / 2], [L/10, L/10], color='r', linewidth=2))
    plt.gca().add_line(plt.Line2D([D / 2 + W/2, W], [L / 10, L / 10], color='r', linewidth=2))


    for id, particle_info in particles_dict[frame].items():
        if isinstance(id, int):
            x = particle_info['x']
            y = particle_info['y']
            r = particle_info['r']
            plt.gca().add_patch(plt.Circle((x, y), r, fill=True))

    plt.xlim(0, W)
    plt.ylim(0, L + L / 10)
    plt.gca().set_aspect('equal', adjustable='datalim')
    plt.title(f'Tiempo: {frame:.2f}')
    plt.xlabel('Posición X')
    plt.ylabel('Posición Y')