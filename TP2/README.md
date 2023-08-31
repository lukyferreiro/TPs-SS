# TP2- Autómata Off-Lattice: Bandadas de agentes autopropulsados

En este TP se implementa el algoritmo de bandadas de agentes propulsores usando un autómata Off-Lattice.

## Generación de partículas

Se debe ejecutar el main del archivo Generator.java. Su ejecución tomará los datos de configGenerator.json y creará
los archivos static.txt y dynamic.txt.

## Simulacion Off-Lattice

Se debe ejecutar el main del archivo Simulation.java. Su ejecución tomará los datos de los archivos static.txt y dynamic.txt
y creará dos archivos: off_lattice.txt (con la informacion de las particulas en cada tiempo) y time.txt (con el tiempo tardado
en toda la ejecucion)

## Configuración

#### configGenerator.json 

``` json
{
  "staticFile": "src/main/resources/static.txt",
  "dynamicFile": "src/main/resources/dynamic.txt",
  "N": 50,
  "L": 100.0,
  "max_radius": 1.0,
  "min_radius": 2.0,
  "times": 1,
  "property": 1.0,
  "speed": 0.03
}
```

| Variables    | Descripción                                                           |
|--------------|-----------------------------------------------------------------------|
| staticFile   | Path de donde se creará el archivo estático                           | 
| dynamicFile  | Path de donde se creará el archivo dinámico                           | 
| N            | Numero natural que representa la cantidad de partículas a generar     | 
| L            | Numero real positivo que representa el lado del area de simulación    | 
| max_radius   | Numero real positivo que representa el radio máximo de las partículas | 
| min_radius   | Numero real positivo que representa el radio máximo de las partículas | 
| times        | Numero natural que representa la cantidad de tiempos                  |
| property     | Numero real que representa una propiedad de la particulas             |   
| speed        | Numero real que representa la velocidad de las particulas             |      
 
#### configMethod.json 

``` json
{
  "staticFile": "src/main/resources/static.txt",
  "dynamicFile": "src/main/resources/dynamic.txt",
  "outOffLatticeFile": "src/main/resources/off_lattice.txt",
  "outOrderParametersVaFile": "src/main/resources/va.txt",
  "outTimeFile": "src/main/resources/time.txt",
  "isPeriodic": true,
  "Rc": 1.0,
  "dt": 1.0,
  "eta": 1.0,
  "iterations": 3000
}
```

| Variables                 | Descripción                                                                          |
|---------------------------|--------------------------------------------------------------------------------------|
| staticFile                | Path al archivo estático para tomar su información                                   | 
| dynamicFile               | Path al archivo dinámico para tomar su información                                   | 
| outOffLatticeFile         | Path de donde se creará el archivo con las posiciones y velocidad de las particulas  | 
| outOrderParametersVaFile  | Path de donde se creará el archivo con los valores del parametro de orden            | 
| outTimeFile               | Path de donde se creará el archivo con el tiempo                                     | 
| isPeriodic                | Booleano que representa si la simulación tiene en cuenta contornos periódicos        | 
| Rc                        | Numero real positivo que representa el radio de interacción de las partículas        | 
| dt                        | Numero real positivo que representa el paso temporal                                 | 
| eta                       | Numero real positivo que representa la amplitud de ruido entre cada paso temporal    | 
| iterations                | Numero entero positivo que representa la cantidad de iteraciones a realizar          | 
 