import numpy as np
import matplotlib.pyplot as plt

def plotM_variation(data_dict):
    for n, values in data_dict.items():
        averages = []
        errors = []

        # Calcular promedios y errores para el valor de N actual
        for m in range(1, 14):
            array_values = values[m]
            if array_values:
                avg = np.mean(array_values)
                err = np.std(array_values) / np.sqrt(len(array_values))  # Error estándar
                averages.append(avg)
                errors.append(err)

        color_palette = plt.cm.get_cmap('tab20')
        # Crear gráfico de barras para el valor de N actual
        plt.bar(np.arange(1, 14), averages, yerr=errors, capsize=4, tick_label=range(1, 14), color=color_palette(range(13)))
        plt.xlabel('Valores de M')
        plt.ylabel('Tiempo en ms')
        plt.title(f'Tiempo promedio para N={n}')
        plt.tight_layout()
        plt.show()