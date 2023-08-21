import json
from utils.dataConfig import DataConfig
from utils.particlesParser import parseOffLatticeFile
import matplotlib.pyplot as plt
from matplotlib.animation import FuncAnimation
from utils.animation import update

def main(): 
    BASE = '../simulator/'
    TO_CONFIG_METHOD = BASE + 'src/main/resources/configMethod.json'

    with open(TO_CONFIG_METHOD, 'r') as f:
        config = json.load(f)

    c = DataConfig(config)
    TO_STATIC = BASE + c.staticFile
    TO_DYNAMIC = BASE + c.dynamicFile
    TO_OFF_LATTICE = BASE + c.outOffLatticeFile
    TO_VA = BASE + c.outOrderParametersVaFile

    particles_dict = parseOffLatticeFile(TO_OFF_LATTICE)

    animation_times = len(list(particles_dict.keys()))

    FuncAnimation(plt.gcf(), update, frames=animation_times, fargs=(particles_dict, c.L), interval=1, repeat=False).save("simulation.gif")
    plt.show()

if __name__ == "__main__":
    main()
