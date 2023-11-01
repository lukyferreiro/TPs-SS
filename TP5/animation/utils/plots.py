import matplotlib.pyplot as plt
import numpy as np
from utils.particlesParser import parse_times
from scipy.stats import linregress

def plot_descarga_with_differet_frecuencies(dir):

    x1 = parse_times(dir + 'times_5.0.txt')
    x2 = parse_times(dir + 'times_10.0.txt')
    x3 = parse_times(dir + 'times_15.0.txt')
    x4 = parse_times(dir + 'times_20.0.txt')
    x5 = parse_times(dir + 'times_30.0.txt')
    x6 = parse_times(dir + 'times_50.0.txt')

    colors = ['b', 'g', 'r', 'c', 'm', 'y']
    
    for x, label, color in zip([x1, x2, x3, x4, x5, x6], ['5', '10', '15', '20', '30', '50'], colors):
        counts, borders = np.histogram(x, bins=1000)

        accum_counts = np.cumsum(counts)

        plt.step(borders[:-1], accum_counts, where='post', label=label + '($\\frac{{\mathrm{rad}}}{{\mathrm{s}}})$', color=color)

    plt.xlabel('Tiempo (s)')
    plt.ylabel('Descarga (Nro. de particulas que salen)')
    plt.legend(loc='center left', bbox_to_anchor=(1, 0.5))
    plt.show()

##############################################################################
##############################################################################
##############################################################################

""" def plot_descarga_with_different_apertura(dir):
    x1 = parse_times(dir + 'times_3.0.txt')
    x2 = parse_times(dir + 'times_4.0.txt')
    x3 = parse_times(dir + 'times_5.0.txt')
    x4 = parse_times(dir + 'times_6.0.txt')

    colors = ['b', 'g', 'r', 'c'] 
    for x, label, color in zip([x1, x2, x3, x4], ['3', '4', '5', '6'], colors):
        counts, borders = np.histogram(x, bins=700)

        accum_counts = np.cumsum(conteos)

        x_values = borders[:-1] - borbordersdes[0]
        y_values = accum_counts

        plt.step(x_values, y_values, where='post', label=label + " cm", color=color)

    plt.xlim(0, 700) 
    plt.xlabel('Tiempo (s)')
    plt.ylabel('Número de partículas que salieron')
    plt.legend(loc='center left', bbox_to_anchor=(1, 0.5))
    plt.show() """

##############################################################################
##############################################################################
##############################################################################

def getTimes(path):
    with open(path) as file:
        tiempos_str = file.readlines()

    tiempos = []
    for line in tiempos_str:
        tiempos.append(float(line))

    data = np.array(tiempos)
    return data

def plot_caudal_over_frecuency(dir):
    x1 = getTimes(dir + 'times_5.0.txt')
    x2 = getTimes(dir + 'times_10.0.txt')
    x3 = getTimes(dir + 'times_15.0.txt')
    x4 = getTimes(dir + 'times_20.0.txt')
    x5 = getTimes(dir + 'times_30.0.txt')
    x6 = getTimes(dir + 'times_50.0.txt')

    errors = []
    Qs = []
    fs = [5, 10, 15, 20, 30, 50]

    for x, label in zip([x1, x2, x3, x4, x5, x6], ['5', '10', '15', '20', '30', '50']):
        Q = (len(x))/(x[-1]-x[0])
        Qs.append(Q)

        mean = np.mean(x)

        f = []
        for i in range(len(x)):
            f.append(Q*x[i])

        S = np.sqrt(np.sum((x-f)**2)/(len(x)-2))

        Sxx = np.sum((x - mean)**2)

        error = S / np.sqrt(Sxx)

        errors.append(error)

    plt.plot(fs, Qs, marker='o', linestyle='-', color='magenta')
    plt.errorbar(fs, Qs, yerr=errors, label="w = " + label, color='purple')
    plt.xlabel('Frecuencia ($\\frac{{\mathrm{rad}}}{{\mathrm{s}}})$')
    plt.ylabel('Caudal ($\\frac{{\mathrm{partícula}}}{{\mathrm{s}}})$')
    plt.show()

##############################################################################
##############################################################################
##############################################################################

def get_times(path):
    with open(path) as file:
        tiempos_str = file.readlines()

    tiempos = []
    for line in tiempos_str:
        tiempos.append(float(line))

    data = np.array(tiempos)
    return data

def get_Qs(path):
    with open(path) as file:
        tiempos_str = file.read()

    Qs = list(map(float, tiempos_str.split('\n')))
    return Qs

def plot_caudal_over_apertura(dir):

    x1 = get_times(dir + 'times_3.0.txt')
    x2 = get_times(dir + 'times_4.0.txt')
    x3 = get_times(dir + 'times_5.0.txt')
    x4 = get_times(dir + 'times_6.0.txt')

    error_list = []

    Qs = []
    for x, label in zip([x1, x2, x3, x4], ['3', '4', '5', '6']):

        Q = (len(x)) / (x[-1] - x[0])

        x_mean = np.mean(x)

        f = []
        for i in range(len(x)):
            f.append(Q * x[i])

        S = np.sqrt(np.sum((x-f)**2)/(len(x)-2))

        Sxx = np.sum((x - x_mean)**2)

        error = S / np.sqrt(Sxx)

        error_list.append(error)
        Qs.append(Q)

    plt.plot(['3','4','5','6'], Qs, marker='o', linestyle='-', color='magenta')
    plt.errorbar(['3','4','5','6'], Qs, yerr=error_list, label="w = " + label, color='purple')
    plt.xlabel('Ancho de apertura (cm)')
    plt.ylabel('Caudal ($\\frac{{\mathrm{partícula}}}{{\mathrm{s}}})$')
    plt.show()