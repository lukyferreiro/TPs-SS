# TP5 - ...

En este TP se immplementa un algorimto para un medio granular que simula un silo 2D con vibraciones.

## Generación de partículas

Se debe ejecutar el main del archivo Generator.java. Su ejecución tomará los datos de configGenerator.json y creará los archivos static.txt y dynamic.txt.

## Simulacion 

Se debe ejecutar el main del archivo Simulation.java. Su ejecución tomará los datos de los archivos static.txt y dynamic.txt y creará el archivo out.txt (con la informacion de las particulas en cada tiempo)

## Configuración

#### configGenerator.json 

``` json
{
  "staticFile": "src/main/resources/static.txt",
  "dynamicFile": "src/main/resources/dynamic.txt",
  "N": 200,
  "L": 70.0,
  "W": 20.0,
  "max_radius": 1.15,
  "min_radius": 0.85,
  "mass": 1.0,
  "times": 1
}
```

| Variables    | Descripción                                                           |
|--------------|-----------------------------------------------------------------------|
| staticFile   | Path de donde se creará el archivo estático                           | 
| dynamicFile  | Path de donde se creará el archivo dinámico                           | 
| N            | Numero natural que representa la cantidad de partículas a generar     | 
| L            | Numero real positivo que representa el alto del area de simulación    | 
| W            | Numero real positivo que representa el ancho del area de simulación   | 
| max_radius   | Numero real positivo que representa el radio máximo de las partículas | 
| min_radius   | Numero real positivo que representa el radio máximo de las partículas | 
| times        | Numero natural que representa la cantidad de tiempos                  |
| mass         | Numero real que representa la masa de la particula                    |   
| speed        | Numero real que representa la velocidad de las particulas             |      
 
#### configMethod.json 

``` json
{
  "staticFile": "src/main/resources/static.txt",
  "dynamicFile": "src/main/resources/dynamic.txt",
  "outFile": "src/main/resources/outFile.txt",
  "outTimeFile": "src/main/resources/outTimeFile.txt",
  "deltaT": 0.001,
  "deltaT2": 0.1,
  "maxTime": 100.0,
  "D": 6.0,
  "omega": 20.0
}
```

| Variables                 | Descripción                                                                          |
|---------------------------|--------------------------------------------------------------------------------------|
| staticFile                | Path al archivo estático para tomar su información                                   | 
| dynamicFile               | Path al archivo dinámico para tomar su información                                   | 
| outFile                   | Path de donde se creará el archivo con las posiciones de las particulas              | 
| deltaT                    | Numero real positivo que representa el intervalo de tiempo de la simulacion          | 
| deltaT2                   | Numero real positivo que representa el intervalo de tiempo para guardar el sistema   | 
| maxTime                   | Numero real positivo que representa el tiempo maximo de simulacion                   | 
| D                         | Numero real positivo que representa el ancho de apertura del recinto de simulacion   | 
| omega                     | Numero real positivo que representa la frecuencia del forzado externo                | 
 