import matplotlib.pyplot as plt
from matplotlib.patches import Polygon

def update(frame, particles_dict, L):
    plt.clf()

    vertices_rectangulo = [
        (0, 0),
        (L, 0),
    ]

    print(frame)

    poligono_rectangulo = Polygon(vertices_rectangulo, closed=True, edgecolor='black', facecolor='none')
    plt.gca().add_patch(poligono_rectangulo)

    x_positions = []
    y_positions = []

    for id, particle_info in particles_dict[frame].items():
        if isinstance(id, int):
            x = particle_info['x']
            y = particle_info['y']
            x_positions.append(x)
            y_positions.append(y)
            circle = plt.Circle((x, y), radius=2.25, fill=True)
            plt.gca().add_patch(circle)

    plt.xlim(0, L)
    plt.ylim(-L, L)
    plt.title(f'Tiempo: {frame:.2f}')
    plt.xlabel('Posición X')
    plt.ylabel('Posición Y')