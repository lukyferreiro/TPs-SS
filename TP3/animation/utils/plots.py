import matplotlib.pyplot as plt
import os
import numpy as np
from utils.particlesParser import parseGasDiffusionFile

def plot_pressure_over_time(data_dict, L, N):
    fig, ax = plt.subplots()

    times = []
    p1_values = []
    p2_values = []

    for time, data in data_dict.items():
        times.append(time)
        p1_values.append(data['P1'])
        p2_values.append(data['P2'])

    ax.plot(times[:350], p1_values[:350], label='Presión recinto 1')
    ax.plot(times[:350], p2_values[:350], label='Presión recinto 2')
 
    ax.set_xlabel('Tiempo (s)')
    ax.set_ylabel('Presión ($\\frac{kg}{m \\cdot s^2}$)')
    legend = ax.legend(loc='center left', bbox_to_anchor=(1, 0.5))
    legend.set_title(f'L={round(L,3)}, N={N}')
    plt.show()

def plot_pressure_over_L(dirPath):
    pressure_values = {}

    N = 0

    for filename in os.listdir(dirPath):
        if filename.endswith(".txt"):
            file_path = os.path.join(dirPath, filename)
            particles_dict = parseGasDiffusionFile(file_path)
            
            parts = filename.split("_")
            N = int(parts[1])
            L = float(parts[3])

            pressures = [data['PT'] for data in particles_dict.values()]
            
            if (N, L) not in pressure_values:
                pressure_values[(N, L)] = []
            pressure_values[(N, L)].extend(pressures)

    pressure_averages = {}
    pressure_stddevs = {}
    for key, values in pressure_values.items():
        N, L = key
        average_pressure = np.mean(values)  #TODO ver a partir de cuando se estaciona
        stddev_pressure = np.std(values)
        pressure_averages[(N, L)] = average_pressure
        pressure_stddevs[(N, L)] = stddev_pressure

    N_L_values = list(pressure_averages.keys())
    averages = [pressure_averages[key] for key in N_L_values]
    stddevs = [pressure_stddevs[key] for key in N_L_values]

    labels = [f"L={L}" for _, L in N_L_values]

    plt.figure(figsize=(12, 6))
    plt.bar(labels, averages, yerr=stddevs, capsize=5)
    plt.ylabel('Presión ($\\frac{kg}{m \\cdot s^2}$)')
    plt.title(f"Presión promedio en función de L con N={N}")
    plt.tight_layout()
    plt.show()
