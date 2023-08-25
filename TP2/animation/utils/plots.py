import matplotlib.pyplot as plt
import numpy as np

def plot_va_over_iterations(data_dict, N, L):

    selected_keys = [i for i in range(0, 6)]

    # Crear una figura y ejes
    fig, ax = plt.subplots()

    # Iterar sobre las claves seleccionadas y valores del diccionario
    for key in selected_keys:
        values = data_dict.get(key)  # Obtener valores correspondientes a la clave
        if values is not None:
            ax.plot(values, label=f'η={key}')

    ax.set_title(F'Parametro de orden en funcion del tiempo\n N={int(N)}, L={round(L,3)}, ρ={round(N/(L*L), 3)}')
    ax.set_xlabel('Iteración')
    ax.set_ylabel('Parametro de orden (va)')
    ax.legend(loc='center left', bbox_to_anchor=(1, 0.5))
    plt.show()



def plot_va_variating_eta(all_data):
    plt.figure()
    legend_labels = []

    for filename, file_info in all_data.items():
        N = file_info['N']
        L = file_info['L']
        iterations = file_info['iterations']
        data_dict = file_info['data_dict']

        x_values = []
        y_avg_values = []
        y_std_values = []

        for key, data in data_dict.items():
            x_values.append(key)
            y_avg_values.append(np.mean(data))
            y_std_values.append(np.std(data))

        plt.errorbar(x_values, y_avg_values, fmt='o', capsize=4, ecolor="black", label=filename)
        #plt.errorbar(x_values, y_avg_values, yerr=y_std_values, fmt='o', capsize=4, ecolor="black", label=filename)
        legend_labels.append(f'N={int(N)}, L={round(L, 3)}, ρ={round(N/(L*L), 3)}')
    
    plt.title(f'Parametro de orden va en funcion del ruido \n {int(iterations)} iteraciones')
    plt.xlabel('Amplitud de ruido (η)')
    plt.ylabel('Parametro de orden (va)')
    plt.legend(legend_labels, loc='center left', bbox_to_anchor=(1, 0.5)) 
    plt.show()

def plot_va_variating_density(all_data):
    plt.figure()
    legend_labels = []
    colors = ['blue', 'orange', 'green', 'red', 'purple', 'brown']
    file_num = 0

    for filename, file_info in all_data.items():
        ETA = file_info['eta']
        L = file_info['L']
        iterations = file_info['iterations']
        data_dict = file_info['data_dict']

        x_values = []
        y_avg_values = []
        y_std_values = []

        for key, data in data_dict.items():
            x_values.append(key)
            y_avg_values.append(np.mean(data))
            y_std_values.append(np.std(data))

        plt.errorbar(x_values, y_avg_values, yerr=y_std_values, fmt='o', capsize=4, ecolor="black", color=colors[file_num], label=filename)
        legend_labels.append(f'eta={round(ETA,2)}')
        plt.plot(x_values, y_avg_values, colors[file_num], alpha=0.5)
        file_num += 1
    
    plt.title(f'Parametro de orden va en funcion de la densidad \n L={round(L,2)} y {int(iterations)} iteraciones')
    plt.xlabel('Densidad (ρ)')
    plt.ylabel('Parametro de orden (va)')
    plt.legend(legend_labels, loc='center left', bbox_to_anchor=(1, 0.5)) 
    plt.show()