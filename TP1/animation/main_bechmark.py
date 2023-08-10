from utils.benchmarkParser import parseM_variation
from utils.benchmarkPlot import plotM_variation

def main(): 
    BASE = '../simulator/src/main/resources'

    M_VARIATION_PATH = BASE + "/M_variation"

    L = 20.0
    Rc = 1

    data_dict = parseM_variation(M_VARIATION_PATH)
    print(data_dict[600][4])
    plotM_variation(data_dict)


if __name__ == "__main__":
    main()
