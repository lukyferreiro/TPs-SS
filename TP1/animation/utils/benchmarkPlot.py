import numpy as np
import matplotlib.pyplot as plt

def plotM_variation(data_dict):

    L = 20.0

    sorted_data = sorted(data_dict.items())
    
    for n, values in sorted_data:
        cant_m = len(values)
        averages = []
        errors = []
        #densities = []
        sorted_m = sorted(values.keys())
        smallest_m = sorted_m[0]  # Obtener el valor más pequeño

        # Calcular promedios y errores para el valor de N actual
        for m in range(smallest_m, cant_m+1):

            #density_per_m = (n / (L*L)) / (m*m)
            #densities.append(density_per_m)

            array_values = values[m]
            if array_values:
                avg = np.mean(array_values)
                err = np.std(array_values) / np.sqrt(len(array_values))  # Error estándar
                averages.append(avg)
                errors.append(err)

        color_palette = plt.cm.get_cmap('tab20')
        bars = plt.bar(np.arange(smallest_m, cant_m+1), averages, yerr=errors, capsize=4, tick_label=range(smallest_m, cant_m+1), color=color_palette(range(cant_m)))
        plt.xlabel('Cantidad de celdas (M)')
        plt.ylabel('Tiempo [ms]')
        plt.title(f'Tiempo promedio para N={n}')
        plt.tight_layout()
        #plt.legend(bars, [f'Densidad={round(d, 5):.5f}' for d in densities], loc='center left', bbox_to_anchor=(1, 0.5))
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

    plt.xlabel('Cantidad de particulas (N)')
    plt.ylabel('Tiempo [ms]')
    plt.title('Tiempo promedio comparando los metodos utilizados')
    plt.xticks(np.arange(len(sorted_keys)) + 0.2, sorted_keys)
    plt.legend()
    plt.tight_layout()
    plt.show()