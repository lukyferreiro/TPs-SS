import matplotlib.pyplot as plt
from matplotlib.patches import Polygon

def update(frame, particles_dict, W, L, D):
    plt.clf()

    print(frame)

    vertices_rectangulo = [
        (0, 0),
        (0, L),
        (L, L),
        (L, 0),
        (0, (L-D)/2),
        (0, (L-D)/2 + D)
    ]

    poligono_rectangulo = Polygon(vertices_rectangulo, closed=True, color="black", edgecolor='black', facecolor='none', linewidth=1)
    plt.gca().add_patch(poligono_rectangulo)

    R = 2.25

    for id, particle_info in particles_dict[frame].items():
        if isinstance(id, int):
            x = particle_info['x']
            y = particle_info['y']
            r = particle_info['r']
            plt.gca().add_patch(plt.Circle((x, y), R, fill=True))

    plt.xlim(0, W)
    plt.ylim(-20, L)
    plt.gca().set_aspect('equal', adjustable='datalim')
    plt.title(f'Tiempo: {frame:.2f}')
    plt.xlabel('Posición X')
    plt.ylabel('Posición Y')