import json
from utils.dataConfig import DataConfig
from utils.particlesParser import parseParticles, parseNeighbors
from utils.plots import plotParticles

def main(): 
    BASE = '../simulator/'
    TO_CONFIG_METHOD = BASE + 'src/main/resources/configMethod.json'

    with open(TO_CONFIG_METHOD, 'r') as f:
        config = json.load(f)

    c = DataConfig(config)
    TO_STATIC = BASE + c.staticFile
    TO_DYNAMIC = BASE + c.dynamicFile
    TO_NEIGHBOUR = BASE + c.outNeighborsFile
    TO_TIME = BASE + c.outTimeFile

    particles_dict = parseParticles(TO_STATIC, TO_DYNAMIC)
    neighbours = parseNeighbors(TO_NEIGHBOUR)
    plotParticles(particles_dict, neighbours, c.M, c.L, c.Rc, c.isPeriodic, c.method)


if __name__ == "__main__":
    main()
