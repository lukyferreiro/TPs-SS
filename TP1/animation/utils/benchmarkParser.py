import os

def parseM_variation(dir_path):
    data_dict = {}

    for filename in os.listdir(dir_path):
        if filename.endswith(".txt"):
            filepath = os.path.join(dir_path, filename)
            with open(filepath, "r") as file:
                lines = file.readlines()

            file_data = {}
            current_m = None
            values = []

            for line in lines[1:]:
                line = line.strip()
                if line.startswith("M:"):
                    if current_m is not None:
                        file_data[current_m] = values
                        values = []  # Reiniciar la lista de valores para el próximo valor de M
                    current_m = int(line.split(":")[1].split()[0])  # Extract M value from "M:2 --"
                    values.extend(map(float, line.split("--")[1].split()))  # Agregar los valores a la lista

            if current_m is not None:
                file_data[current_m] = values

            n_value = int(filename.split("_")[1].split(".")[0])
            data_dict[n_value] = file_data

    return data_dict
