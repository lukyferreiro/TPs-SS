import json
from utils.dataConfig import DataConfig
from utils.particlesParser import parse
from matplotlib.animation import FuncAnimation
from utils.animation import update
import matplotlib.pyplot as plt


def main(): 
    BASE = '../simulator/'
    TO_CONFIG_METHOD = BASE + 'src/main/resources/configMethod.json'

    with open(TO_CONFIG_METHOD, 'r') as f:
        config = json.load(f)

    c = DataConfig(config)
    TO_OUT = BASE + c.outFile

    particles_dict = parse(TO_OUT)

    animation_times = list(particles_dict.keys())[:1000]
    anim = FuncAnimation(plt.gcf(), update, frames=animation_times, repeat=False,
                         interval=1, fargs=(particles_dict, c.W, c.L, c.D)).save("omega_20_d_6.gif")

if __name__ == "__main__":
    main()
