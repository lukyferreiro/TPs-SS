import json
from utils.dataConfig import DataConfig
from utils.particlesParser import parseGasDiffusionFile
import matplotlib.pyplot as plt
from matplotlib.animation import FuncAnimation
from utils.animation import update
from matplotlib.patches import Polygon

def main(): 
    BASE = '../simulator/'
    TO_CONFIG_METHOD = BASE + 'src/main/resources/configMethod.json'

    with open(TO_CONFIG_METHOD, 'r') as f:
        config = json.load(f)

    c = DataConfig(config)
    TO_OUT = BASE + c.outFile

    particles_dict = parseGasDiffusionFile(TO_OUT)

    animation_times = list(particles_dict.keys())
    
    # Definir las dimensiones del contenedor según las especificaciones
    SIDE = c.side
    L = c.L
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

    # Crear los polígonos para los rectángulos
    poligono_rectangulo = Polygon(vertices_rectangulo, closed=True, edgecolor='black', facecolor='none')

    # Crear la figura y el eje
    fig, ax = plt.subplots()

    # Agregar los polígonos al eje
    ax.add_patch(poligono_rectangulo)

    x_positions = []
    y_positions = []

    for id, particle_info in particles_dict[0].items():
        x = particle_info['x']
        y = particle_info['y']
        x_positions.append(x)
        y_positions.append(y)

        # Dibujar cada partícula como un círculo
        circle = plt.Circle((x, y), radius=0.0015, fill=True)
        ax.add_patch(circle)

    # Configurar los límites del eje
    ax.set_xlim(0, 2*SIDE)
    ax.set_ylim(0, SIDE)
    ax.set_title(f'Tiempo: {0}')
    ax.set_xlabel('Posición X')
    ax.set_ylabel('Posición Y')
    plt.show()

if __name__ == "__main__":
    main()
