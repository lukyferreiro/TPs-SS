import json
from utils.dataConfig import DataConfig
from utils.particlesParser import parseGasDiffusionFile
import matplotlib.pyplot as plt
from matplotlib.animation import FuncAnimation, writers
from utils.animation import update
from utils.plots import plot_pressure_over_time

def main(): 
    BASE = '../simulator/'
    TO_CONFIG_METHOD = BASE + 'src/main/resources/configMethod.json'

    with open(TO_CONFIG_METHOD, 'r') as f:
        config = json.load(f)

    c = DataConfig(config)
    TO_OUT = BASE + c.outFile
    TO_DYNAMIC = BASE + c.dynamicFile

    particles_dict = parseGasDiffusionFile("D:\\PC\\Downloads\\Simulación de Sistemas\\TPs-SS\\TP3\\simulator\\src\\main\\resources\\benchmark\\N_200_L_0.03_it_0.txt")
    #particles_dict = parseGasDiffusionFile("D:\PC\Downloads\Simulación de Sistemas\TPs-SS\TP3\simulator\src\main\resources\benchmark\N_200_L_0.03_it_0.txt")
    #particles_dict = parse('../simulator/src/main/resources/simulation.json')

    plot_pressure_over_time(particles_dict, c.L)

    animation_times = list(particles_dict.keys())
    anim = FuncAnimation(plt.gcf(), update, frames=animation_times, repeat=False,interval=1, fargs=(particles_dict, c.side, c.L)).save("falopa.gif")


if __name__ == "__main__":
    main()
