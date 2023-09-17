import json
from utils.dataConfig import DataConfig
from utils.particlesParser import parseGasDiffusionFile
import matplotlib.pyplot as plt
from matplotlib.animation import FuncAnimation
from utils.animation import update
from utils.plots import plot_pressure_over_time, plot_pressure_over_L

def main(): 
    BASE = '../simulator/'
    TO_CONFIG_METHOD = BASE + 'src/main/resources/configMethod.json'

    with open(TO_CONFIG_METHOD, 'r') as f:
        config = json.load(f)

    c = DataConfig(config)
    TO_OUT = BASE + c.outFile

    particles_dict = parseGasDiffusionFile(TO_OUT)

    print(particles_dict)

    plot_pressure_over_time(particles_dict, c.L, c.N)

    PATH = '../simulator/src/main/resources/benchmark/N_400'
    plot_pressure_over_L(PATH)

    animation_times = list(particles_dict.keys())
    #anim = FuncAnimation(plt.gcf(), update, frames=animation_times, repeat=False,interval=1, fargs=(particles_dict, c.side, c.L)).save("simulation.gif")


if __name__ == "__main__":
    main()
