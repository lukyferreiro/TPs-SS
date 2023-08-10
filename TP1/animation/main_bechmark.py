from utils.benchmarkParser import parseM_variation, parseMethod_variation
from utils.benchmarkPlot import plotM_variation, plotMethod_variation

def main(): 
    BASE = '../simulator/src/main/resources'

    M_VARIATION_PATH = BASE + "/M_variation"
    METHOD_VARIATION_PATH = BASE + "/method_variation"

    L = 20.0
    Rc = 1

    data_dict_m = parseM_variation(M_VARIATION_PATH)
    plotM_variation(data_dict_m)

    data_dict_method = parseMethod_variation(METHOD_VARIATION_PATH)
    plotMethod_variation(data_dict_method)


if __name__ == "__main__":
    main()
