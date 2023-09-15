import json
from utils.dataConfig import DataConfig
from utils.particlesParser import parseGasDiffusionFile
import matplotlib.pyplot as plt
from matplotlib.patches import Polygon
from matplotlib.animation import FuncAnimation
from utils.animation import update

def main(): 
    BASE = '../simulator/'
    TO_CONFIG_METHOD = BASE + 'src/main/resources/configMethod.json'

    with open(TO_CONFIG_METHOD, 'r') as f:
        config = json.load(f)

    c = DataConfig(config)
    TO_OUT = BASE + c.outFile
    TO_DYNAMIC = BASE + c.dynamicFile

    particles_dict = parseGasDiffusionFile(TO_OUT)

    animation_times = list(particles_dict.keys())

    ani = FuncAnimation(plt.gcf(), update, frames=animation_times, repeat=False,
                         interval=1, fargs=(particles_dict, c.side, c.L)).save("simulation.gif")

    plt.show()

if __name__ == "__main__":
    main()
