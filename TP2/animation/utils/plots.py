import matplotlib.pyplot as plt
import numpy as np

def plot_va_over_iterations(data_dict):

    selected_keys = [i for i in range(0, 6)]

    # Crear una figura y ejes
    fig, ax = plt.subplots()

    # Iterar sobre las claves seleccionadas y valores del diccionario
    for key in selected_keys:
        values = data_dict.get(key)  # Obtener valores correspondientes a la clave
        if values is not None:
            ax.plot(values, label=f'η={key}')

    ax.set_title('Parametro de orden en funcion del tiempo')
    ax.set_xlabel('Iteración')
    ax.set_ylabel('Parametro de orden (va)')
    ax.legend()  
    plt.show()



def plot_va_variating_eta(data_dict):
    x_values = []
    y_avg_values = []
    y_std_values = []

    for key, data in data_dict.items():
        x_values.append(key)
        y_avg_values.append(np.mean(data))
        y_std_values.append(np.std(data))

    plt.errorbar(x_values, y_avg_values, yerr=y_std_values, fmt='o', capsize=4, ecolor="black")
    plt.title('Parametro de orden va en funcion del ruido')
    plt.xlabel('Amplitud de ruido (η)')
    plt.ylabel('Parametro de orden (va)')
    plt.show()
