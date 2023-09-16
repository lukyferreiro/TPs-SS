import matplotlib.pyplot as plt
import numpy as np

def plot_pressure_over_time(data_dict, L):
    fig, ax = plt.subplots()

    times = []
    p1_values = []
    p2_values = []

    for time, data in data_dict.items():
        times.append(time)
        p1_values.append(data['P1'])
        p2_values.append(data['P2'])

    ax.plot(times, p1_values, label='P1')
    ax.plot(times, p2_values, label='P2')
 
    ax.set_xlabel('Tiempo (s)')
    ax.set_ylabel('Presion ($\\frac{kg}{m \\cdot s^2}$)')
    legend = ax.legend(loc='center left', bbox_to_anchor=(1, 0.5))
    legend.set_title(f'L={round(L,3)}')
    plt.show()