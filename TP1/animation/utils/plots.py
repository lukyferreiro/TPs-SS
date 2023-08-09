import matplotlib.pyplot as plt
import numpy as np
import random

def plotParticles(particle_dict, neighbors_data, M, L, Rc, periodic, method, selectedParticleId):
    fig, ax = plt.subplots()

    # Dibujar todas las partículas con círculos azules
    for particle_id, (radius, position) in particle_dict.items():
        circle = plt.Circle(position, radius=radius, edgecolor='blue', facecolor='blue', fill=True)
        ax.add_patch(circle)

        # Agregar label con el ID
        label_offset = radius * 0.5 
        text_x = position[0] + radius + label_offset
        text_y = position[1]
        ax.text(text_x, text_y, str(particle_id), ha='left', va='center', color='black', fontsize=8)

    # Seleccionamos aleatoriamente una particula para remarcar sus vecinos
    #random_row = random.choice(neighbors_data)
    #selected_particle_id = random_row[0]
    #selected_particle_radius, selected_particle_position = particle_dict[selected_particle_id]

    # Dibujamos la partícula seleccionada en rojo
    selected_particle_radius, selected_particle_position = particle_dict[selectedParticleId]
    circle_selected = plt.Circle(selected_particle_position, radius=selected_particle_radius,
                                 edgecolor='red', facecolor='red', fill=True)
    ax.add_patch(circle_selected)

    for row in neighbors_data:
        if row[0] == selectedParticleId:
            selectedNeighbours = row[1:]

    # Dibujamos los vecinos de la partícula seleccionada en verde
    for neighbor_id in selectedNeighbours:
        if neighbor_id in particle_dict:
            neighbor_radius, neighbor_position = particle_dict[neighbor_id]
            circle_neighbor = plt.Circle(neighbor_position, radius=neighbor_radius,
                                         edgecolor='green', facecolor='green', fill=True)
            ax.add_patch(circle_neighbor)

    # Dibujamos el radio Rc con efecto de contornos periódicos
    if(periodic):
        num_repeats = int(np.ceil(L / Rc)) + 1
        for i in range(-num_repeats, num_repeats + 1):
            for j in range(-num_repeats, num_repeats + 1):
                center_offset = np.array([i * L, j * L])
                circle_orange = plt.Circle(selected_particle_position + center_offset,
                                           radius=Rc+selected_particle_radius, edgecolor='orange',
                                           facecolor='none', fill=False, linewidth=1)
                ax.add_patch(circle_orange)
    else: 
        circle_orange = plt.Circle(selected_particle_position, radius=Rc+selected_particle_radius,
                                   edgecolor='orange', facecolor='none', fill=False, linewidth=2)
        ax.add_patch(circle_orange)
    
    # Creamos la grilla de MxM
    ax.grid(True, which='both', linewidth=0.5, color='gray', linestyle='--')
    ax.set_xticks(np.linspace(0, L, M+1))
    ax.set_yticks(np.linspace(0, L, M+1))

    # Restringimos los límites del plot a L
    ax.set_xlim(0, L)
    ax.set_ylim(0, L)

    ax.set_aspect('equal')
    ax.set_xlabel('Posición X')
    ax.set_ylabel('Posición Y')
    ax.set_title(f'Partículas con vecinos usando {method}')
    ax.autoscale_view()

    plt.show()
