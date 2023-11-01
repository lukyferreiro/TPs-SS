import matplotlib.pyplot as plt
import os
import numpy as np
import re
from utils.particlesParser import parse, get_times
from scipy.stats import linregress

def times_graph_frequency():
    x1 = get_times('../src/main/resources/benchmark/omega/times_5.0.txt')
    x2 = get_times('../src/main/resources/benchmark/omega/times_10.0.txt')
    x3 = get_times('../src/main/resources/benchmark/omega/times_15.0.txt')
    x4 = get_times('../src/main/resources/benchmark/omega/times_20.0.txt')
    x5 = get_times('../src/main/resources/benchmark/omega/times_30.0.txt')
    x6 = get_times('../src/main/resources/benchmark/omega/times_50.0.txt')

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

    # Etiquetas de los ejes
    plt.xlabel('Tiempo (s)')
    plt.ylabel('Número de partículas que salieron')
    plt.legend()
    plt.show()