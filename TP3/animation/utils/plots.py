import matplotlib.pyplot as plt
import numpy as np

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