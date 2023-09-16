
import matplotlib.pyplot as plt
from matplotlib.patches import Polygon

def update(frame, particles_dict, SIDE, L):
    plt.clf()

    vertices_rectangulo = [
        (0, 0),
        (0, SIDE),
        (SIDE, SIDE),
        (SIDE, ((SIDE-L)/2)+L),
        (2*SIDE, ((SIDE-L)/2)+L),
        (2*SIDE, ((SIDE-L)/2)),
        (SIDE, ((SIDE-L)/2)),
        (SIDE, 0)
    ]

    print(frame)

    poligono_rectangulo = Polygon(vertices_rectangulo, closed=True, edgecolor='black', facecolor='none')
    plt.gca().add_patch(poligono_rectangulo)

    x_positions = []
    y_positions = []

    for id, particle_info in particles_dict[frame].items():
        x = particle_info['x']
        y = particle_info['y']
        x_positions.append(x)
        y_positions.append(y)
        circle = plt.Circle((x, y), radius=0.0015, fill=True)
        plt.gca().add_patch(circle)

    plt.xlim(0, 2 * SIDE)
    plt.ylim(0, SIDE)
    plt.title(f'Tiempo: {frame}')
    plt.xlabel('Posición X')
    plt.ylabel('Posición Y')