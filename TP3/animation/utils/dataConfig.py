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
      self.outTimeFile = data['outTimeFile']
      self.L = check_float(data['L'], "L")

      with open(f"../simulator/{self.staticFile}", 'r') as static:
        self.N = int(static.readline().strip())
        self.side = float(static.readline().strip())
      