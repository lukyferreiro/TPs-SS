import json
from utils.dataConfig import DataConfig
from utils.particlesParser import parseParticles

def main(): 
    BASE = '../simulator/'
    TO_CONFIG_METHOD = BASE + 'src/main/resources/configMethod.json'

    with open(TO_CONFIG_METHOD, 'r') as f:
        config = json.load(f)

    c = DataConfig(config)
    TO_STATIC = BASE + c.staticFile
    TO_DYNAMIC = BASE + c.dynamicFile

    particles_dict = parseParticles(TO_STATIC, TO_DYNAMIC)
    print(particles_dict[2])


if __name__ == "__main__":
    main()
