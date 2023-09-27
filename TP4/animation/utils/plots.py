import matplotlib.pyplot as plt
import os
import numpy as np
import re
from utils.particlesParser import parse


# Graficar las soluciones analítica y numérica y calcular el error cuadrático medio (sumando las
# diferencias al cuadrado para todos los pasos temporales y normalizando por el número total de pasos).
def plot_oscillator(dir, dt):
    data = []

    #Solucion analitica
    A = 1
    gamma = 100
    k = 10000
    m = 70

    times = np.arange(0, 5 + dt, dt)
    exp_term = -(gamma / (2 * m))
    cos_term = ((k / m) - ((gamma ** 2) / (4 * m ** 2))) ** 0.5
    analytic_values = A * np.exp(exp_term * times) * np.cos(cos_term * times)

    data.append(analytic_values)

    ECM = []
    names = ['VerletOriginal', 'Beeman', 'GearPredictor']
    
    print(f'Parsing {dt:.6f}')

    for i, name in enumerate(names):
        folder = os.path.join(dir, "benchmark")
        files = [f for f in os.listdir(folder) if re.match(f"{name}_{dt:.6f}\.txt", f)]
        file = files[0]  
        
        positions_x = []
        data_dict = parse(os.path.join(folder, file))

        for time in data_dict.keys():
            #Soluciones numericas
            positions_x.append(data_dict[time][1]['x'])

        difference = 0
        for i in range(0, len(analytic_values)):
            difference += (analytic_values[i] - positions_x[i]) ** 2
        
        ecm = difference / len(analytic_values)
        ECM.append(ecm)
        data.append(positions_x)
        print(f"ECM {name}: {ecm}")

    names = ['Analitica', 'Verlet Original', 'Beeman', 'Gear predictor-corrector de orden 5']

    plt.figure(figsize=(12, 6))
    for i, positions_x in enumerate(data):
        plt.plot(times, positions_x, '-', label=names[i])

    plt.xlabel('Tiempo (s)')
    plt.ylabel('Posición (m)')
    plt.legend()
    plt.grid(True)
    plt.show()

    return ECM