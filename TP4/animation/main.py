import json
from utils.dataConfig import DataConfig

def main(): 
    BASE = '../simulator/'
    TO_CONFIG_METHOD = BASE + 'src/main/resources/configMethod.json'

    with open(TO_CONFIG_METHOD, 'r') as f:
        config = json.load(f)

    c = DataConfig(config)
    TO_OUT = BASE + c.outFile


if __name__ == "__main__":
    main()
