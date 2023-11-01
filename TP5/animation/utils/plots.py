import matplotlib.pyplot as plt
import numpy as np
from utils.particlesParser import parse_times
from scipy.stats import linregress

def plot_times_frequency(dir):

    x1 = parse_times(dir + 'times_5.0.txt')
    x2 = parse_times(dir + 'times_10.0.txt')
    x3 = parse_times(dir + 'times_15.0.txt')
    x4 = parse_times(dir + 'times_20.0.txt')
    x5 = parse_times(dir + 'times_30.0.txt')
    x6 = parse_times(dir + 'times_50.0.txt')

    colors = ['b', 'g', 'r', 'c', 'm', 'y']
    
    for x, label, color in zip([x1, x2, x3, x4, x5, x6], ['5', '10', '15', '20', '30', '50'], colors):
        counts, borders = np.histogram(x, bins=1000)

        accum_counts = np.cumsum(counts)

        x_values = borders[:-1]
        y_values = accum_counts
        slope, intercept, r_value, p_value, std_err = linregress(x_values, y_values)

        plt.step(borders[:-1], accum_counts, where='post', label=label + " Hz", color=color)

        intercept=0
        plt.plot(x_values, intercept + slope * x_values, label=f'Regresión (R²={r_value**2:.2f})', linestyle='--',  color=color)

    plt.xlabel('Tiempo (s)')
    plt.ylabel('Número de partículas que salieron')
    plt.legend(loc='center left', bbox_to_anchor=(1, 0.5))
    plt.show()