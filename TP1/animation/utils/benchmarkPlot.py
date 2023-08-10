import numpy as np
import matplotlib.pyplot as plt

def plotM_variation(data_dict):
    for n, values in data_dict.items():
        cant_m = len(values)
        averages = []
        errors = []

        # Calcular promedios y errores para el valor de N actual
        for m in range(1, cant_m+1):
            array_values = values[m]
            if array_values:
                avg = np.mean(array_values)
                err = np.std(array_values) / np.sqrt(len(array_values))  # Error estándar
                averages.append(avg)
                errors.append(err)

        color_palette = plt.cm.get_cmap('tab20')
        # Crear gráfico de barras para el valor de N actual
        plt.bar(np.arange(1, cant_m+1), averages, yerr=errors, capsize=4, tick_label=range(1, cant_m+1), color=color_palette(range(cant_m)))
        plt.xlabel('Valores de M')
        plt.ylabel('Tiempo [ms]')
        plt.title(f'Tiempo promedio para N={n}')
        plt.tight_layout()
        plt.show()

def plotMethod_variation(data):
    # Ordenar las claves del diccionario
    sorted_keys = sorted(data.keys())

    averages_cim = []
    errors_cim = []
    averages_brute = []
    errors_brute = []

    for n in sorted_keys:
        methods = data[n]
        array_values_cim = methods['CIM']
        array_values_brute = methods['BRUTE']
        
        if array_values_cim:
            avg_cim = np.mean(array_values_cim)
            err_cim = np.std(array_values_cim) / np.sqrt(len(array_values_cim))
            averages_cim.append(avg_cim)
            errors_cim.append(err_cim)
        
        if array_values_brute:
            avg_brute = np.mean(array_values_brute)
            err_brute = np.std(array_values_brute) / np.sqrt(len(array_values_brute))
            averages_brute.append(avg_brute)
            errors_brute.append(err_brute)

    # Crear gráfico de barras para CIM
    plt.bar(np.arange(len(sorted_keys)), averages_cim, yerr=errors_cim, capsize=4, label='CIM', width=0.4)
    # Crear gráfico de barras para BRUTE
    plt.bar(np.arange(len(sorted_keys)) + 0.4, averages_brute, yerr=errors_brute, capsize=4, label='BRUTE', width=0.4)

    plt.xlabel('Valores de N')
    plt.ylabel('Tiempo [ms]')
    plt.title('Tiempo promedio comparando los metodos utilizados')
    plt.xticks(np.arange(len(sorted_keys)) + 0.2, sorted_keys)
    plt.legend()
    plt.tight_layout()
    plt.show()