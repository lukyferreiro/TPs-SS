import json
from utils.dataConfig import DataConfig
from utils.particlesParser import parseGasDiffusionFile, parse
import matplotlib.pyplot as plt
from matplotlib.animation import FuncAnimation, writers
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
    #particles_dict = parse('../simulator/src/main/resources/simulation.json')

    animation_times = list(particles_dict.keys())

    anim = FuncAnimation(plt.gcf(), update, frames=animation_times, repeat=False,
                         interval=1, fargs=(particles_dict, c.side, c.L)).save("simulationMenosParticulas.gif")
    
    Writer = writers['ffmpeg']
    writer = Writer(fps=20, metadata=dict(artist='Me'), bitrate=1800)
    anim.save('animation.mp4', writer=writer)


if __name__ == "__main__":
    main()
