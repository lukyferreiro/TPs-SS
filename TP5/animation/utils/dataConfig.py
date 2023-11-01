def check_int(num, str):
   if not type(num) == int or num < 0:
      raise ValueError(f"Valor de '{str}' invalido")
   
   return num

def check_float(num, str):
   if not type(num) == float and num < 0:
      raise ValueError(f"Valor de '{str}' invalido")
   
   return num
   
class DataConfig:

   def __init__(self, data):
      self.staticFile = data['staticFile']
      self.dynamicFile = data['dynamicFile']
      self.outFile = data['outFile']
      self.deltaT = check_float(data['deltaT'], 'deltaT')
      self.deltaT2 = check_float(data['deltaT2'], 'deltaT2')
      self.maxTime = check_float(data['maxTime'], 'maxTime')
      self.D = check_float(data['D'], 'D')
      self.omega = check_float(data['omega'], 'omega')

      with open(f"../simulator/{self.staticFile}", 'r') as static:   
         self.N = float(static.readline().strip())
         self.L = float(static.readline().strip())    
         self.W = float(static.readline().strip())  