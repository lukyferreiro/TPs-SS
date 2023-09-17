import matplotlib.pyplot as plt
import os
import numpy as np
from utils.particlesParser import parseGasDiffusionFile
from scipy.stats import linregress

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

def plot_pressure_vs_area(dirPath):
    pressure_values = {}
    areas_inv = {}
    pressure_averages = {}
    pressure_stddevs = {}
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
            areas_inv[(N, L)] = 1 / (0.09*0.09 + 0.09*L)
            pressure_averages[(N, L)] = np.mean(pressures[40:])  # A partir de 40 ya esta estacionado
            pressure_stddevs[(N, L)] = np.std(pressures[40:])

    N_L_values = list(pressure_values.keys())

    # Extrae los valores de A^-1 y P
    areas_inv_values = [areas_inv[key] for key in N_L_values]
    pressure_values = [pressure_averages[key] for key in N_L_values]
    pressure_errors = [pressure_stddevs[key] for key in N_L_values]

    # Grafica P vs A^-1 con barras de error
    plt.figure(figsize=(12, 6))
    plt.scatter(areas_inv_values, pressure_values, marker='o')
    plt.errorbar(areas_inv_values, pressure_values, yerr=pressure_errors, fmt='o', capthick=2)
    plt.ylabel('Presión ($\\frac{kg}{m \\cdot s^2}$)')
    plt.xlabel('$A^{-1}$')
    plt.title("P vs. $A^{{-1}}$ con N={}".format(N))
    
    # Realiza el ajuste manual de la línea recta
    x = np.array(areas_inv_values)
    y = np.array(pressure_values)
    
    # Calcula los parámetros del ajuste manual
    n = len(x)
    slope = (n * np.sum(x * y) - np.sum(x) * np.sum(y)) / (n * np.sum(x**2) - (np.sum(x))**2)
    intercept = (np.sum(y) - slope * np.sum(x)) / n
    
    # Calcula la constante de la ley de los gases ideales (P·A = constante)
    constant = slope

    # Calcula el coeficiente de determinación (R^2)
    y_pred = slope * x + intercept
    ssr = np.sum((y_pred - y)**2)
    sst = np.sum((y - np.mean(y))**2)
    r_squared = 1 - (ssr / sst)

    # Calcula el error estándar de la estimación (SEE)
    see = np.sqrt(np.sum((y - y_pred)**2) / (n - 2))
    
    # Grafica la línea recta de ajuste manual
    plt.plot(areas_inv_values, [slope * xi + intercept for xi in x], label=f'Ajuste lineal (P·A = {constant:.2f})', linestyle='--')
    
    print(f"Coeficiente de determinación (R^2): {r_squared:.4f}")
    print(f"Error estándar de la estimación (SEE): {see:.4f}")

    plt.legend()
    plt.show()

def calculate_MSD_and_plot(data_dict):
    times = list(data_dict.keys())
    
    MSD = []
    
    for t in times:
        if t == times[0]:
            continue  # Saltar el primer tiempo, ya que el MSD es 0 en t=0
        displacement_sq = 0
        
        # Iterar a través de las partículas
        for particle_id, particle_data in data_dict[t].items():
            initial_data = data_dict[times[0]][particle_id]
            displacement_sq += (particle_data['x'] - initial_data['x'])**2 + (particle_data['y'] - initial_data['y'])**2
        
        MSD.append(displacement_sq / len(data_dict[t]))
    
    # Realizar el ajuste lineal en el tiempo de interés (por ejemplo, los primeros tiempos)
    # El coeficiente de difusión D es la pendiente de la línea ajustada
    time_of_interest =  60    #TODO definir tiempo de interes
    fit_range = np.where(np.array(times[1:]) <= time_of_interest)
    fit_times = np.array(times[1:])[fit_range]
    fit_MSD = np.array(MSD)[fit_range]

    # Realizar el ajuste lineal
    slope, intercept = np.polyfit(fit_times, fit_MSD, 1)
    D = slope / 4  # Coeficiente de difusión

    plt.figure(figsize=(8, 6))
    plt.scatter(times, MSD, label='MSD vs Tiempo')
    plt.plot(fit_times, slope * fit_times + intercept, 'r', label='Ajuste Lineal')
    plt.xlabel('Tiempo')
    plt.ylabel('MSD')
    plt.legend()
    plt.title('Desplazamiento Cuadrático Medio (MSD) vs Tiempo')
    plt.show()

    print(f'Coeficiente de Difusión (D): {D:.4f}')