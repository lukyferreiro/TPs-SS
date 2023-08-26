import matplotlib.pyplot as plt
import numpy as np

#----------------------------------------------------------------
#----------------------Eta Variation Plot------------------------
#---------------------------------------------------------------

def plot_va_eta_over_iterations(data_dict, N, L):

    selected_etas = [i for i in range(0, 6)]
    fig, ax = plt.subplots()

    for eta in selected_etas:
        values = data_dict.get(eta) 
        if values is not None:
            ax.plot(values, label=f'η={eta}')

    ax.set_xlabel('Iteración')
    ax.set_ylabel('Parametro de orden (va)')
    legend = ax.legend(loc='center left', bbox_to_anchor=(1, 0.5))
    legend.set_title(f' N={int(N)}, L={round(L,3)}, ρ={round(N/(L*L), 3)}')
    plt.show()

def plot_va_variating_eta(all_data):
    plt.figure()
    legend_labels = []
    colors = ['blue', 'orange', 'green', 'red', 'purple', 'brown']
    file_num = 0

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

        #plt.errorbar(x_values, y_avg_values, fmt='o', capsize=4, ecolor="black", color=colors[file_num], label=filename)
        plt.errorbar(x_values, y_avg_values, yerr=y_std_values, fmt='o', capsize=4, ecolor="black", color=colors[file_num], label=filename)
        legend_labels.append(f'N={int(N)}, L={round(L, 3)}, ρ={round(N/(L*L), 3)}')
        plt.plot(x_values, y_avg_values, colors[file_num], alpha=0.5)
        file_num += 1
    
    plt.xlabel('Amplitud de ruido (η)')
    plt.ylabel('Parametro de orden (va)')
    legend = plt.legend(legend_labels, loc='center left', bbox_to_anchor=(1, 0.5)) 
    legend.set_title(f'{int(iterations)} iteraciones')
    plt.show()

#----------------------------------------------------------------
#----------------------Density Variation Plot--------------------
#----------------------------------------------------------------


def plot_va_density_over_iterations(data_dict, eta, L):

    selected_densities = [1,3,5,7,10]
    fig, ax = plt.subplots()

    for density in selected_densities:
        values = data_dict.get(density) 
        if values is not None:
            ax.plot(values, label=f'ρ={density}, N={int(density*L*L)}')

    ax.set_xlabel('Iteración')
    ax.set_ylabel('Parametro de orden (va)')
    legend = ax.legend(loc='center left', bbox_to_anchor=(1, 0.5))
    legend.set_title(f'L={round(L,3)}, η={round(eta, 2)}')
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
        legend_labels.append(f'η={round(ETA,2)}')
        plt.plot(x_values, y_avg_values, colors[file_num], alpha=0.5)
        file_num += 1
    
    plt.xlabel('Densidad (ρ)')
    plt.ylabel('Parametro de orden (va)')
    legend = plt.legend(legend_labels, loc='center left', bbox_to_anchor=(1, 0.5)) 
    legend.set_title(f'L={round(L,2)}, {int(iterations)} iteraciones')
    plt.show()