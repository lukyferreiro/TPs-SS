import matplotlib.pyplot as plt
import numpy as np

radius = 1
density = 1/3   # La cuenta es (#de particulas)/(volumen) = (200)/(20*70)
g = 5 ** 0.5

def beverloo(x, c):
    aux = x - c * radius
    return density * g * (aux ** 1.5)


def beverloo_error(Qs, Ds, c):
    result = 0
    for q, d in zip(Qs, Ds):
        b = beverloo(d, c)
        result = result + (q - b) ** 2
    return result

def getQs(path):
    with open(path) as file:
        tiempos_str = file.readlines()

    tiempos = []
    for line in tiempos_str:
        tiempos.append(float(line))

    data = np.array(tiempos)
    return data


def plot_beverloo(path):

    Cs = [num / 100.0 for num in range(0, 200, 1)]
    Qs = getQs(path)

    beverloo_err = [beverloo_error(Qs, [3, 4, 5, 6], c) for c in Cs]

    plt.plot(Cs, beverloo_err, color='blue')

    c_min = Cs[np.argmin(beverloo_err)]
    b_min = beverloo_err[np.argmin(beverloo_err)]
    plt.scatter(c_min, b_min, color='blue')

    plt.xlabel('Parámetro libre c')
    plt.ylabel('Error')
    plt.legend()
    plt.show()

    x = [0.1 * i for i in range(20, 100)]
    y = [beverloo(x_i, c_min) for x_i in x]

    plt.plot(x, y, color='blue', label='Beverloo')
    plt.scatter([3, 4, 5, 6], Qs, color='blue', label='Resultados')
    plt.ylabel('Caudal ($\\frac{{\mathrm{partícula}}}{{\mathrm{s}}})$')
    plt.xlabel('Ancho de apertura (cm)')
    plt.legend()
    plt.show()
